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
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOContentExtensionFileFilter;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class ExportAction extends AbstractFileAction{
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	
	private JFileChooser exportfileChooser = null;
	
	
	public ExportAction (MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Export..."); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		putValue(Action.SHORT_DESCRIPTION, "Export"); 
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	
	private JFileChooser getExportFileChooser() {
		if (exportfileChooser == null) {
			exportfileChooser = new JFileChooser() {
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
			//can not save in "all supported formats" option
			exportfileChooser.setAcceptAllFileFilterUsed(false);
			
	        for (String formatID : factory.getFormatIDsSet()) {
				JPhyloIOFormatInfo info = factory.getFormatInfo(formatID);
				if (info.isElementModeled(EventContentType.ALIGNMENT, false)) {
					JPhyloIOContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
					exportfileChooser.addChoosableFileFilter(filter);
				}
			}
	        exportfileChooser.setDialogTitle("Export File");
		} 
		return exportfileChooser;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		try {
			if (promptExportFileName()) {
				writeFile(getExportFileChooser().getSelectedFile(), ((JPhyloIOContentExtensionFileFilter)getExportFileChooser().getFileFilter()).getFormatID());
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage(), "Error while export file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	//show save Dialog, set File, set File format
	protected boolean promptExportFileName() {
		boolean result = (getExportFileChooser().showDialog(getMainFrame(), "Export") == JFileChooser.APPROVE_OPTION);
		if (result) {
	    	getMainFrame().getDocument().setFile(getExportFileChooser().getSelectedFile());
		}
		return result;
	}


	@Override
	public void setEnabled(Document document, MainFrame mainframe) {
		setEnabled(document != null);
	}
}
