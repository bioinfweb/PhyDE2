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


import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOContentExtensionFileFilter;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.libralign.model.io.IOTools;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;




@SuppressWarnings("serial")
public abstract class AbstractFileAction extends AbstractPhyDEAction {
	private static JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private static JFileChooser fileChooser = null;



	public AbstractFileAction(MainFrame mainframe) {
		super(mainframe);
	}


	protected static JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser() {

				@Override
				public void approveSelection(){
					File f = getSelectedFile();
					JPhyloIOContentExtensionFileFilter filter = (JPhyloIOContentExtensionFileFilter)getFileFilter();
					if (!filter.accept(f)) {
						f = new File(f.getAbsolutePath() + "." + filter.getDefaultExtension());
						setSelectedFile(f);
					}

					if(f.exists()) {
						switch(JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION)) {
						case JOptionPane.YES_OPTION:
							super.approveSelection();
							return;
						case JOptionPane.NO_OPTION:
							return;
						case JOptionPane.CLOSED_OPTION:
							return;
						case JOptionPane.CANCEL_OPTION:
							cancelSelection();
							return;
						}
					}
					super.approveSelection();
				}
			};
			fileChooser.setAcceptAllFileFilterUsed(false);

			JPhyloIOFormatInfo info = factory.getFormatInfo(MainFrame.DEFAULT_FORMAT);
			if (info.isElementModeled(EventContentType.ALIGNMENT, true)) {
				JPhyloIOContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
				fileChooser.addChoosableFileFilter(filter);
			}

			fileChooser.setDialogTitle("Save File");
			fileChooser.setToolTipText("Save File in NeXML format.");
		}

		return fileChooser;
	}


	protected void writeFile() {
		writeFile(getMainFrame().getFile(), Main.DEFAULT_FORMAT);
	}


	protected void writeFile(File file, String formatID) {
		try {
			ReadWriteParameterMap parameters = new ReadWriteParameterMap();
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_NAME, MainFrame.APPLICATION_NAME);
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_VERSION, MainFrame.APPLICATION_VERSION);
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_URL, MainFrame.APPLICATION_URL);
			
			IOTools.writeSingleAlignment(getMainFrame().getAlignmentArea().getAlignmentModel(), null, file, formatID, parameters);
			// Note that files containing multiple alignments or additional trees or OTU lists would be overwritten with a single alignment file here. 
			// This problem is not handles here, to keep this example simple
			getMainFrame().setChanged(false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(getMainFrame().getFrame(), ex.getMessage(), "Error while writing file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	protected boolean promptFileName() {

		boolean result = (getFileChooser().showSaveDialog(getMainFrame().getFrame()) == JFileChooser.APPROVE_OPTION);
		if (result) {
			getMainFrame().setFile(getFileChooser().getSelectedFile());
			//JPhyloIOContentExtensionFileFilter filter = (JPhyloIOContentExtensionFileFilter)getFileChooser().getFileFilter();
		}
		return result;
	}
	
	
	   protected boolean save() {
		   boolean result = promptFileName();
		   if (result) {
			   writeFile();
		   }
		   
		   return result;
	   }
	
	   
	public boolean handleUnsavedChanges() {
		if (getMainFrame().isChanged()) {
            switch (JOptionPane.showConfirmDialog(getMainFrame().getFrame(), "There are unsaved changes. Do you want to save the changes?", 
            		"Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION)) {
	            case JOptionPane.YES_OPTION:
	            	writeFile();
	            	return false;
	            case JOptionPane.NO_OPTION:
	            	return true;
            }
    		return false;
		}
		else {
			return true;
		}
	}
}

