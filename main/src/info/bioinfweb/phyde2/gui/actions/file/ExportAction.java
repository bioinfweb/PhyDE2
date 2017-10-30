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

import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOContentExtensionFileFilter;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class ExportAction extends AbstractFileAction{
	private static JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private static JFileChooser fileChooser = null;
	
	public ExportAction (MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Export..."); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (exportFileName()) {
			export();
		}
		else {
			JOptionPane.showMessageDialog(getMainFrame().getFrame(), "Warning", "Error while exporting file", JOptionPane.WARNING_MESSAGE);
		}
		
		for (String formatID : factory.getFormatIDsSet()) {
			JPhyloIOFormatInfo info = factory.getFormatInfo(formatID);
			if (info.isElementModeled(EventContentType.ALIGNMENT, true)) {
				JPhyloIOContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
				fileChooser.addChoosableFileFilter(filter);
			}
		}
		
	}
	
	//show save Dialog, set File, set File format
	protected boolean exportFileName() {
		boolean result = (getFileChooser().showSaveDialog(getMainFrame().getFrame()) == JFileChooser.APPROVE_OPTION);
		if (result) {
	    	getMainFrame().setFile(getFileChooser().getSelectedFile());
	    	//setFormat beim speichern nur auf NeXML und keinen FileFilter
		    getMainFrame().setFormat(((JPhyloIOContentExtensionFileFilter)getFileChooser().getFileFilter()).getFormatID());
			getMainFrame().setChanged(true);
		}
		return result;
	}

}
