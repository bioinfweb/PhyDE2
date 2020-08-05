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


//import java.awt.List;
import java.awt.event.ActionEvent;
//import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;

//import info.bioinfweb.libralign.actions.AlignmentActionProvider;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.alignmentarea.DataAreaLists;
import info.bioinfweb.libralign.alignmentarea.order.SequenceOrder;
//import info.bioinfweb.libralign.alignmentarea.DataAreaLists;
//import info.bioinfweb.libralign.alignmentarea.label.AlignmentLabelArea;
//import info.bioinfweb.libralign.alignmentarea.order.SequenceOrder;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.dataarea.DataArea;
import info.bioinfweb.libralign.dataelement.DataList;
import info.bioinfweb.libralign.dataelement.DataLists;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.data.DataModel;
import info.bioinfweb.libralign.model.implementations.swingundo.SwingUndoAlignmentModel;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
//import info.bioinfweb.libralign.multiplealignments.MultipleAlignmentsContainer;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentModelEdit;
import info.bioinfweb.phyde2.document.undo.edit.RefreshConsensusSequenceEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class RefreshAllConsensusSequenceAction extends AbstractPhyDEAction implements Action {
	public RefreshAllConsensusSequenceAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Refresh all consensus sequences"); 
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Iterator<String> iterator = getMainFrame().getActiveAlignment().getAlignmentModel().sequenceIDIterator();		
		ArrayList<String> sequenceIDs = new ArrayList<>();
		DefaultPhyDE2AlignmentModel model = (DefaultPhyDE2AlignmentModel) getMainFrame().getActiveAlignment();
		
		while (iterator.hasNext()) {
			String sequenceID = iterator.next(); 
			if (model.sequenceHasContig(sequenceID) && !AlignmentModelUtils.sequencesEqual(model.getAlignmentModel(), sequenceID, model.getContig(sequenceID).getConsensusModel(), model.getContig(sequenceID).getConsensusSequenceID())) {
				sequenceIDs.add(sequenceID);
			}
		}
		getMainFrame().getActiveAlignment().executeEdit(new RefreshConsensusSequenceEdit(model, sequenceIDs));
	}
	
	
	@Override
	public void setEnabled(PhyDE2AlignmentModel model, MainFrame mainframe) {
		boolean enabled = false;
		if (mainframe.getActiveAlignmentArea() != null) {
			if (model instanceof DefaultPhyDE2AlignmentModel) {
				Iterator<String> iterator = mainframe.getActiveAlignmentArea().getAlignmentModel().sequenceIDIterator();
				boolean hasContig = false;
				if (model.getAlignmentModel().getSequenceCount() > 0) {
					
					while (iterator.hasNext()) {
						if (((DefaultPhyDE2AlignmentModel) model).sequenceHasContig(iterator.next().toString())) {
							hasContig = true;
							break;
						}
					} 

				enabled = ((model != null) && (model.getAlignmentModel().sequenceIDIterator().hasNext() != false) && hasContig);
				}
			}
		}
		setEnabled(enabled);
	}
}
