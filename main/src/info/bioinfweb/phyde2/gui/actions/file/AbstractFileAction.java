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


import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOContentExtensionFileFilter;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.objecttranslation.ObjectTranslatorFactory;
import info.bioinfweb.jphyloio.objecttranslation.implementations.ColorTranslator;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSet;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramIOConstants;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.ShiftListObjectTranslator;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.document.io.IOConstants;
import info.bioinfweb.phyde2.document.io.PhyDEDocumentDataAdapter;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.collections4.MapIterator;



@SuppressWarnings("serial")
public abstract class AbstractFileAction extends AbstractPhyDEAction {
	private static JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private static JFileChooser fileChooser = null;


	public static ObjectTranslatorFactory createTranslatorFactory() {
		ObjectTranslatorFactory result = new ObjectTranslatorFactory();
		result.addXSDTranslators(true);
		result.addJPhyloIOTranslators(true);
		result.addTranslator(new ColorTranslator(), true, IOConstants.DATA_TYPE_COLOR);
		result.addTranslator(new ShiftListObjectTranslator(), true, PherogramIOConstants.DATA_TYPE_SHIFT_LIST);
		return result;
	}
	

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
			fileChooser.addChoosableFileFilter(factory.getFormatInfo(MainFrame.DEFAULT_FORMAT).createFileFilter(TestStrategy.BOTH));

			fileChooser.setDialogTitle("Save File");
			fileChooser.setToolTipText("Save File in NeXML format.");
		}

		return fileChooser;
	}
	
	
	private boolean checkForOverlappingCharSet () {
		MapIterator<String, CharSet> it = getMainFrame().getActiveAlignment().getCharSetModel().mapIterator();
		//TODO Move to save related action
		boolean overlappingCharSet = false;
		while (it.hasNext()) {
			it.next();
			if (!it.getValue().isEmpty() && (it.getValue().last().getLastPos() > getMainFrame().getActiveAlignment().getAlignmentModel().getMaxSequenceLength())) {
				overlappingCharSet = true;
				break;
			}
		}
		
		if (overlappingCharSet) {
			//save, saveAs and Export Action will ask if the character bars has to be saved
			switch(JOptionPane.showConfirmDialog(getMainFrame() , "There are character sets that are longer than the sequences. Do you want to save the invisible bars?", "Character sets are too long!", JOptionPane.YES_NO_CANCEL_OPTION)) {
			case JOptionPane.YES_OPTION:
				return false;
			case JOptionPane.NO_OPTION:
				return true;
			case JOptionPane.CANCEL_OPTION:
				return true;
			case JOptionPane.CLOSED_OPTION:
				return true;
			}
			return false;
		}
		//	false = program do not remove overlapping bars  
		return false;
	}
	

	protected void writeFile() {
		writeFile(getMainFrame().getSelectedDocument().getFile(), Main.DEFAULT_FORMAT, new PhyDEDocumentDataAdapter(getMainFrame().getSelectedDocument()));
	}
	

	protected void writeFile(File file, String formatID, PhyDEDocumentDataAdapter documentAdapter) {
		if (factory.getFormatInfo(formatID).isElementModeled(EventContentType.CHARACTER_SET, false)) {
			int maxSeqLength  = getMainFrame().getActiveAlignment().getAlignmentModel().getMaxSequenceLength();
			
			if (checkForOverlappingCharSet()) {
				MapIterator<String, CharSet> it = getMainFrame().getActiveAlignment().getCharSetModel().mapIterator();
				//TODO Move to save related action
				while (it.hasNext())
				{
					it.next();
					if (it.getValue().last().getLastPos() > maxSeqLength){
						it.getValue().remove(maxSeqLength, it.getValue().last().getLastPos());
					}
				}
			}
		}
		try {
			ReadWriteParameterMap parameters = new ReadWriteParameterMap();
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_NAME, Main.APPLICATION_NAME);
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_VERSION, Main.getInstance().getVersion());
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_URL, Main.APPLICATION_URL);
			parameters.put(ReadWriteParameterMap.KEY_OBJECT_TRANSLATOR_FACTORY, createTranslatorFactory());
			
			factory.getWriter(formatID).writeDocument(documentAdapter, file, parameters);
			
			getMainFrame().getActiveAlignment().setChanged(false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage(), "Error while writing file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	protected boolean promptFileName() {
		boolean result = (getFileChooser().showSaveDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION);
		if (result) {
			getMainFrame().getSelectedDocument().setFile(getFileChooser().getSelectedFile());
		}
		return result;
	}
	
	
	protected boolean saveAs() {
		boolean result = promptFileName();
		if (result) {
			writeFile();
		}
		return result;
	}
	

	protected boolean save() {
		if (getMainFrame().getActiveAlignment().getFile() == null) {
			return saveAs();
		}
		else {
			writeFile();
			return true;
		}
	}
	
	   
	public int handleUnsavedChanges() {
		if (getMainFrame().getActiveAlignment().isChanged()) {
			String closingTab = getMainFrame().getActiveTabTitle();
			if (getMainFrame().getSelectedDocument().getFile() != null) {
				closingTab = getMainFrame().getSelectedDocument().getFile().getName();
			}
			
			String[] buttons = { "Yes", "Yes to all", "No", "No to all", "Cancel" }; // TODO Ticket ID: 0000268
			
			switch (JOptionPane.showOptionDialog(getMainFrame(), "There are unsaved changes in " + closingTab + ". Do you want to save the changes?", 
					"Unsaved changes", JOptionPane.ERROR_MESSAGE, 0, null, buttons, buttons[0])) {
					case 0:
						if (save()) {
							return 0;
						}
						else {
							return 2;
						}
					case 1:
						return 1;
					case 2:
						return 2;
					case 3:
						return 3;
					case 4:
						return 4;
			}
			return 2;
		}
		else {
			getMainFrame().removeTab();
			return 0;
		}
	}
}

