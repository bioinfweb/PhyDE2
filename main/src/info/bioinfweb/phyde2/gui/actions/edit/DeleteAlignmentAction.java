/*
 * PhyDE 2 - An alignment editor for phylogenetic purposes
 * Copyright (C) 2017  Ben St�ver, Jonas Bohn, Kai M�ller
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
import info.bioinfweb.phyde2.document.undo.edit.DeleteAlignmentEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;



public class DeleteAlignmentAction extends AbstractPhyDEAction implements Action  {
	public DeleteAlignmentAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Delete alignment"); 
		putValue(Action.SHORT_DESCRIPTION, "Delete alignment");
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		PhyDE2AlignmentModel model = getMainFrame().getSelectedAlignment ();
		getMainFrame().getActiveAlignment().executeEdit(new DeleteAlignmentEdit(getMainFrame().getNewDocument(), model));
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled ((document != null) && (getMainFrame().getSelectedAlignment () != null ));
	}
}
