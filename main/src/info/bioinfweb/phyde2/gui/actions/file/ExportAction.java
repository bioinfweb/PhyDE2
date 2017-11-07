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
import java.io.File;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOContentExtensionFileFilter;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class ExportAction extends AbstractFileAction{
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	
	private JFileChooser fileChooser = null;
	
	
	public ExportAction (MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Export..."); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	
	private JFileChooser getExportFileChooser() {
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
			
	        for (String formatID : factory.getFormatIDsSet()) {
				JPhyloIOFormatInfo info = factory.getFormatInfo(formatID);
				if (info.isElementModeled(EventContentType.ALIGNMENT, true)) {
					JPhyloIOContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
					fileChooser.addChoosableFileFilter(filter);
				}
			}
	        fileChooser.setDialogTitle("Export File");

		} 
	
		return fileChooser;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			if (exportFileName()) {
				JPhyloIOEventReader eventReader = factory.guessReader(getExportFileChooser().getSelectedFile(), new ReadWriteParameterMap());
				writeFile(getMainFrame().getFile(), eventReader.getFormatID());
			}
		}
		catch (Exception ex){
			JOptionPane.showMessageDialog(getMainFrame().getFrame(), ex.getMessage(), "Error while export file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	//show save Dialog, set File, set File format
	protected boolean exportFileName() {
		boolean result = (getExportFileChooser().showDialog(getMainFrame().getFrame(), "Export") == JFileChooser.APPROVE_OPTION);
		
		if (result) {
	    	getMainFrame().setFile(getExportFileChooser().getSelectedFile());
		}
		
		return result;
	}
}
