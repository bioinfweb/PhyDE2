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
package info.bioinfweb.phyde2.document.undo.edit;


import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



public class AddSequenceEdit extends AlignmentEdit {
	private String sequenceName;
	private String sequenceID = null;
	private PherogramReference pherogramReference;
	private SingleReadContigAlignmentModel contigReference;
	
	
	public AddSequenceEdit(PhyDE2AlignmentModel alignmentModel, String sequenceName, 
			PherogramReference pherogramReference, SingleReadContigAlignmentModel contigReference) {
		
		super(alignmentModel);
	
		this.sequenceName = sequenceName;
		this.pherogramReference = pherogramReference;
		this.contigReference = contigReference;
	}


	@Override
	public void redo() throws CannotRedoException {
		if (sequenceID == null) {
			sequenceID = getAlignment().getAlignmentModel().getUnderlyingModel().addSequence(sequenceName);
		}
		else {
			getAlignment().getAlignmentModel().getUnderlyingModel().addSequence(sequenceName, sequenceID);
		}
		
		if (getAlignment() instanceof SingleReadContigAlignmentModel && pherogramReference != null) {
			
			for (int j = 0; j < pherogramReference.getModel().getPherogramProvider().getSequenceLength(); j++) {
				getAlignment().getAlignmentModel().getUnderlyingModel().appendToken(sequenceID, pherogramReference.getModel().getPherogramProvider().getBaseCall(j));
			}
			((SingleReadContigAlignmentModel) getAlignment()).addPherogram(sequenceID, pherogramReference);
		}
		
		else if (getAlignment() instanceof DefaultPhyDE2AlignmentModel && contigReference != null){
			((DefaultPhyDE2AlignmentModel) getAlignment()).addConsensus(contigReference, sequenceID);
		}
		
		super.redo();
	}


	@Override
	public void undo() throws CannotUndoException {
		getAlignment().getAlignmentModel().getUnderlyingModel().removeSequence(sequenceID);
		if (getAlignment() instanceof SingleReadContigAlignmentModel) {
			((SingleReadContigAlignmentModel) getAlignment()).removePherogramModel(sequenceID);	
		}
		
		if (getAlignment() instanceof DefaultPhyDE2AlignmentModel){
			((DefaultPhyDE2AlignmentModel)getAlignment()).removeConsensusReference(sequenceID);
		}
	
		super.undo();
	}


	@Override
	public String getPresentationName() {
		StringBuilder result = new StringBuilder();
		result.append("Sequence ");
		result.append(sequenceName);
		result.append(" added in alignment ");
		result.append(getAlignment());
		result.append(".");
		
		return result.toString();
	}
}
