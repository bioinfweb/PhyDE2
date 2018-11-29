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
package info.bioinfweb.phyde2.gui.actions.edit;


import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.AddColumnsToCharSetEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;



@SuppressWarnings("serial")
public class AddColumnsToCharSetAction extends AbstractPhyDEAction implements Action {
	public AddColumnsToCharSetAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Add columns to selected character set"); 
		putValue(Action.SHORT_DESCRIPTION, "Add columns to selected char. set"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		loadSymbols("AddBar");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		String id = getSelectedCharSetID();
		
		if (id == null) {
			JOptionPane.showMessageDialog(getMainFrame(), "Please select the Char-Set where you want to add a column.", "Char-Set not found.", JOptionPane.ERROR_MESSAGE);
		}
		
		getMainFrame().getActiveAlignment().executeEdit(new AddColumnsToCharSetEdit(getMainFrame().getActiveAlignment(), id, getMainFrame().getActiveAlignmentArea().getSelection().getFirstColumn(), getMainFrame().getActiveAlignmentArea().getSelection().getLastColumn()));
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled(getSelectedCharSetID() != null);
	}
}
