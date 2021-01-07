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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Action;


import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.ExtendGeneratedConsensusSequenceEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class ExtendGeneratedConsensusSequenceAction extends AbstractPhyDEAction implements Action   {
	public ExtendGeneratedConsensusSequenceAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Extend generated consensus sequence"); 
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		Iterator<String> idIterator = getMainFrame().getActiveAlignment().getAlignmentModel().sequenceIDIterator();		
		ArrayList <String> sequenceIDs = new ArrayList<>();
		AlignmentModel model = getMainFrame().getActiveAlignment().getAlignmentModel();
		String sequenceID= new String();
		SingleReadContigAlignmentModel contigSequences = (SingleReadContigAlignmentModel) getMainFrame().getActiveAlignment();	
		
		while (idIterator.hasNext()) {
			sequenceIDs.add(idIterator.next());
		}	
		getMainFrame().getActiveAlignment().executeEdit(new ExtendGeneratedConsensusSequenceEdit(contigSequences));	
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
	}
	

	@Override
	public void setEnabled(PhyDE2AlignmentModel consensus, MainFrame mainframe) {
		boolean enabled = false;
		if (mainframe.getActiveAlignmentArea() != null) {
			if (consensus instanceof SingleReadContigAlignmentModel) {
				if (((SingleReadContigAlignmentModel) consensus).getConsensusModel().getSequenceLength(((SingleReadContigAlignmentModel) consensus).getConsensusSequenceID()) != 0) {
				Iterator<String> Iterator = mainframe.getActiveAlignmentArea().getAlignmentModel().sequenceIDIterator();
				if (((SingleReadContigAlignmentModel) consensus).getAlignmentModel().getSequenceCount() > 0) {
				while (Iterator.hasNext()) {
					Iterator.next();	
					}
				}

				enabled = (consensus != null) && (consensus.getAlignmentModel().sequenceIDIterator().hasNext() != false);
				}
			}
		}
		setEnabled(enabled);
	}
}

