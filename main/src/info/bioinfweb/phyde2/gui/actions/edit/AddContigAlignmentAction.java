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
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;



public class AddContigAlignmentAction extends AbstractAddAlignmentAction implements Action {
	public AddContigAlignmentAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Add contig alignment"); 
		putValue(Action.SHORT_DESCRIPTION, "Add contig alignment");
		//loadSymbols("Delete");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		addAlignment("Enter a label for the new contig alignment:", new SingleReadContigAlignmentModel());
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {}  //TODO Possibly check if a file is selected in the future. (Do that in superclass.)
}