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

import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.RefreshConsensusSequenceEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class RefreshConsensusSequenceAction extends AbstractPhyDEAction implements Action{
	private DefaultPhyDE2AlignmentModel model = null;
	private String sequenceID = null;
	public RefreshConsensusSequenceAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Refresh consensus sequence"); 
		//TODO: herausfinden, wie man an die SequenceID von der Sequenz kommt, die gerade
		//"angeklickt" ist -> Diese SequenceID dann hier verwenden.
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model = (DefaultPhyDE2AlignmentModel) getMainFrame().getActiveAlignment();
		getMainFrame().getActiveAlignment().executeEdit(new RefreshConsensusSequenceEdit(model, sequenceID));
		
	}

	@Override
	public void setEnabled(PhyDE2AlignmentModel model, MainFrame mainframe) {
		//setEnabled( ((DefaultPhyDE2AlignmentModel) model).sequenceHasContig(sequenceID));
		setEnabled(false);
	}

}
