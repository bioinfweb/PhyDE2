/*
 * PhyDE 2 - An alignment editor for phylogenetic purposes
 * Copyright (C) 2017  Ben Stöver, Jonas Bohn, Kai Müller
 * <http://bioinfweb.info/PhyDE2>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.phyde2.gui.actions.file;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.commons.collections4.set.ListOrderedSet;

import info.bioinfweb.commons.io.ContentExtensionFileFilter;
import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.commons.io.ExtensionFileFilter;
import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.phyde2.document.io.IOConstants;
import info.bioinfweb.phyde2.document.io.PhyDEAlignmentDataReader;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class OpenAction extends AbstractFileAction {
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private JFileChooser fileChooser = null;
	
	
	public OpenAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Open..."); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		loadSymbols("Open");
	}

	//TODO Fix Bug: cant open NexML files because there are some unreadable changes after writing NexML files
	public JFileChooser getOpenFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(true);
			
			//TODO Unterscheiden nach input Dateiformaten --> Nexus, FASTA, Phylip = import, ungespeichert --> Nexml = öffnen, gespeichert
			ListOrderedSet<String> validExtensions = new ListOrderedSet<String>();
			for (String formatID : factory.getFormatIDsSet()) {
				JPhyloIOFormatInfo info = factory.getFormatInfo(formatID);
				if (info.isElementModeled(EventContentType.ALIGNMENT, true)) {
					ContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
					validExtensions.addAll(filter.getExtensions());
					fileChooser.addChoosableFileFilter(filter);
				}
			}
			ExtensionFileFilter allFormatsFilter = new ExtensionFileFilter("All supported formats", false, validExtensions.asList());
			fileChooser.addChoosableFileFilter(allFormatsFilter);
			fileChooser.setFileFilter(allFormatsFilter);
		}
		return fileChooser;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (handleUnsavedChanges()) {
			try {
				if (getOpenFileChooser().showOpenDialog(getMainFrame().getFrame()) == JFileChooser.APPROVE_OPTION) {
					JPhyloIOEventReader eventReader = factory.guessReader(getOpenFileChooser().getSelectedFile(), new ReadWriteParameterMap());
					PhyDEAlignmentDataReader mainReader = new PhyDEAlignmentDataReader(eventReader);
					mainReader.readAll();
					
					// File does not contain any alignments
					if (mainReader.getAlignmentModelReader().getCompletedModels().size() == 0) {
						JOptionPane.showMessageDialog(getMainFrame().getFrame(), "The file \"" +  getOpenFileChooser().getSelectedFile().getAbsolutePath() + 
								"\" does not contain any alignments.", "Error while loading file", JOptionPane.ERROR_MESSAGE);
					}
					else {
						// File contains one alignment
						getMainFrame().getAlignmentArea().setAlignmentModel(mainReader.getAlignmentModelReader().getCompletedModels().get(0), true);
						if (eventReader.getFormatID().equals(MainFrame.DEFAULT_FORMAT) && (IOConstants.FORMAT_VERSION.equals(mainReader.getFormatVersion()))) {
							getMainFrame().getDocument().setFile(getOpenFileChooser().getSelectedFile());
							getMainFrame().getDocument().setChanged(false);


							// File contains more than one alignment --> just the first one was loaded
							if (mainReader.getAlignmentModelReader().getCompletedModels().size() > 1) {
								JOptionPane.showMessageDialog(getMainFrame().getFrame(),
										"The file contained more than one alignment. Only the first one was loaded.", "Multiple alignments found", JOptionPane.WARNING_MESSAGE);
							}
						}
						
					}
					
				}
			}
			catch (Exception ex) {
				//TODO: Throws Exception while opening a NexML file: "The symbol("-")of a standard data token definition must be of type Integer."
				ex.printStackTrace();
				JOptionPane.showMessageDialog(getMainFrame().getFrame(), ex.getMessage(), "Error while loading file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
