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
package info.bioinfweb.phyde2.gui.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class ShowHideBasecalllinesAction extends AbstractPhyDEAction implements Action{

	public ShowHideBasecalllinesAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Show/hide basecall lines");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getMainFrame().getPherogramFormats().toggleShowBaseCallLines();
	}

	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled(mainframe.getActiveAlignment() instanceof SingleReadContigAlignmentModel);

		
	}

}
