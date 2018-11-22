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


import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.AddAlignmentEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import javax.swing.Action;
import javax.swing.JOptionPane;



public abstract class AbstractAddAlignmentAction extends AbstractPhyDEAction implements Action {
	public AbstractAddAlignmentAction(MainFrame mainframe) {
		super(mainframe);
	}

	
	protected void addAlignment(String message, PhyDE2AlignmentModel model) {
		String label = JOptionPane.showInputDialog(getMainFrame(), message);
		if (label != null) {
			model.getAlignmentModel().setID(getMainFrame().getNewDocument().generateUniqueID());
			model.getAlignmentModel().setLabel(label);
			getMainFrame().getDocument().executeEdit(new AddAlignmentEdit(getMainFrame().getNewDocument(), model));  //TODO Use getNewDocument().executeEdit() as soon as undo manager has been moved.
		}
	}
}
