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
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;


import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
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
			sequenceIDs.add(iterator.next());
			}
        getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
		
        for (String sequenceID2 : sequenceIDs){
			if(((DefaultPhyDE2AlignmentModel)getMainFrame().getActiveAlignment()).sequenceHasContig(sequenceID2)){
				int sequenceLength = model.getAlignmentModel().getSequenceLength(sequenceID2);
				SingleReadContigAlignmentModel contigModel = ((DefaultPhyDE2AlignmentModel) model).getContigReference(sequenceID2);
				model.getAlignmentModel().removeTokensAt(sequenceID2, 0 , sequenceLength);
				//((DefaultPhyDE2AlignmentModel) getAlignment()).addConsensus(contigModel, sequenceID);
				
				for (int token = 0; token < contigModel.getConsensusModel().getSequenceLength(contigModel.getConsensusSequenceID()); token++) {
					((DefaultPhyDE2AlignmentModel)model).getAlignmentModel().appendToken(sequenceID2, contigModel.getConsensusModel().getTokenAt(contigModel.getConsensusSequenceID(), token), true);  
				}
			}
		}//TODO: check if proper token change events are fired here	
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit(getPresentationName(sequenceIDs));	
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
					enabled = ((model != null) && (model.getAlignmentModel().sequenceIDIterator().hasNext()!= false) && hasContig);
				}
			}
		}
		setEnabled(enabled);
	}
	
	
	private String getPresentationName(ArrayList<String> sequenceIDs) {
		StringBuilder result = new StringBuilder(64);
		int counter = 0;
		result.append("Sequence refreshed from contig in ");
		Iterator<String> i = sequenceIDs.iterator();
		while (i.hasNext() && (counter < 3)) {
			result.append("\"");
			result.append(getMainFrame().getActiveAlignment().getAlignmentModel().sequenceNameByID(i.next()));
			result.append("\"");
			if (i.hasNext() && (counter < 2)){
				result.append(", ");
			}
			counter++;
		}
		
		int dif = sequenceIDs.size() - 3;
		
		if (dif > 0){
			result.append(" and ");
			result.append(dif);
			result.append(" more sequence(s)");
		}
		result.append(".");
		return result.toString();
	}
}
