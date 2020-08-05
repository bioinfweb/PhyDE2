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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.dataarea.implementations.consensus.ConsensusSequenceModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;

public class GenerateConsensusSequenceEdit extends AlignmentEdit {
	public GenerateConsensusSequenceEdit(SingleReadContigAlignmentModel contigSequences)  {
		super(contigSequences);
		//backup
	}				
			

	@Override
	public void redo() throws CannotRedoException {
		int maxSequenceLength = getAlignment().getAlignmentModel().getMaxSequenceLength();
		SingleReadContigAlignmentModel consensusSequenceAlignmnetModel = ((SingleReadContigAlignmentModel)getAlignment());
		ConsensusSequenceModel consensusSequenceDataModel = consensusSequenceAlignmnetModel.getConsensusDataModel();
		int consensusSeqLength = consensusSequenceAlignmnetModel.getConsensusModel().getSequenceLength(consensusSequenceAlignmnetModel.getConsensusSequenceID());	
		
		if (consensusSequenceDataModel != null) {
			for(int column = 0; column < maxSequenceLength; column ++) {
				if (column < consensusSeqLength) {
					consensusSequenceAlignmnetModel.getConsensusModel().setTokenAt(consensusSequenceAlignmnetModel.getConsensusSequenceID(), column,
							consensusSequenceAlignmnetModel.getConsensusModel().getTokenSet().tokenByRepresentation(consensusSequenceDataModel.getConsensusToken(column)));
				}
				if ((consensusSeqLength == 0) || (column >= consensusSeqLength)) {
					consensusSequenceAlignmnetModel.getConsensusModel().appendToken(consensusSequenceAlignmnetModel.getConsensusSequenceID(), 
							consensusSequenceAlignmnetModel.getConsensusModel().getTokenSet().tokenByRepresentation(consensusSequenceDataModel.getConsensusToken(column)), true);		
				}
			}
			if ((consensusSeqLength >= maxSequenceLength)) {
				for (int column = maxSequenceLength; column < consensusSequenceAlignmnetModel.getConsensusModel().getSequenceLength(consensusSequenceAlignmnetModel.getConsensusSequenceID()); column++ ) {
					consensusSequenceAlignmnetModel.getConsensusModel().removeTokenAt(consensusSequenceAlignmnetModel.getConsensusSequenceID(), column);
					column--;
				}
			}
		}
	}


	@Override
	public void undo() throws CannotUndoException {
		// TODO Auto-generated method stub
		super.undo();
	}


	@Override
	public String getPresentationName() {
		StringBuilder result = new StringBuilder();
		result.append("Consesnsus Sequence generated ");
		return result.toString();
	}

}
