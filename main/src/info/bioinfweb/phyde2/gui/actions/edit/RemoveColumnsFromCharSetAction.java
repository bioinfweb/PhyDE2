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

import javax.swing.Action;
import javax.swing.JOptionPane;

import info.bioinfweb.phyde2.document.undo.edit.RemoveColumnsFromCharSetEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

@SuppressWarnings("serial")
public class RemoveColumnsFromCharSetAction extends AbstractPhyDEAction implements Action {

	public RemoveColumnsFromCharSetAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Remove columns from character set"); 
		putValue(Action.SHORT_DESCRIPTION, "Remove columns from char. set");
		loadSymbols("DeleteBar");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String id = getSelectedCharSetID();
		
		if (id == null) {
			JOptionPane.showMessageDialog(getMainFrame(), "Please select the character set area where you want to remove bar's.","Char-Set not found.", JOptionPane.ERROR_MESSAGE);
		}
		
		getMainFrame().getDocument().executeEdit(new RemoveColumnsFromCharSetEdit(getMainFrame().getDocument(), id, getMainFrame().getAlignmentArea().getSelection().getFirstColumn(), getMainFrame().getAlignmentArea().getSelection().getLastColumn()));
	}

}
