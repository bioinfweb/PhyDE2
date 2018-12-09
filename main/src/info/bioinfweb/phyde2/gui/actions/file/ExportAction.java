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
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.dialogs.ExportDialog;


@SuppressWarnings("serial")
public class ExportAction extends AbstractFileAction {
	

	public ExportAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Export..."); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
		putValue(Action.SHORT_DESCRIPTION, "Export"); 
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		ExportDialog dialog = new ExportDialog(getMainFrame());
		dialog.setName("");
		if (dialog.execute()) {
			try {
				writeFile(dialog.getSelectedFile(), dialog.getFormatID());
			}
			catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage(), "Error while export file", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}


	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled(document != null);
	}
}
