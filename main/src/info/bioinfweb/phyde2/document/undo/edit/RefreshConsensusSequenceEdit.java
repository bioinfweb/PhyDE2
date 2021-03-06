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


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;



public class RefreshConsensusSequenceEdit extends AlignmentEdit {
	private List<String> sequenceIDs = null;
	private Map<String, char[]> tokenMap = new TreeMap<String, char[]>();


	public RefreshConsensusSequenceEdit(DefaultPhyDE2AlignmentModel alignment, List<String> sequenceIDs) {
		super(alignment);
		this.sequenceIDs = sequenceIDs;

		for (String sequenceID : sequenceIDs){
			int sequenceLength = alignment.getAlignmentModel().getSequenceLength(sequenceID);
			char [] tokens = new char [sequenceLength];
			for (int i = 0; i < tokens.length; i++) {
				tokens [i] = alignment.getAlignmentModel().getTokenAt(sequenceID, i);
			}
			tokenMap.put(sequenceID, tokens);
		}
	}



	@Override
	public void redo() throws CannotRedoException {
		for (String sequenceID : sequenceIDs){
			if(((DefaultPhyDE2AlignmentModel)getAlignment()).sequenceHasContig(sequenceID)){
				int sequenceLength = getAlignment().getAlignmentModel().getSequenceLength(sequenceID);
				SingleReadContigAlignmentModel contigModel = ((DefaultPhyDE2AlignmentModel) getAlignment()).getContigReference(sequenceID);
				getAlignment().getAlignmentModel().removeTokensAt(sequenceID, 0 , sequenceLength);
				//((DefaultPhyDE2AlignmentModel) getAlignment()).addConsensus(contigModel, sequenceID);
				
				for (int token = 0; token < contigModel.getConsensusModel().getSequenceLength(contigModel.getConsensusSequenceID()); token++) {
					((DefaultPhyDE2AlignmentModel) getAlignment()).getAlignmentModel().appendToken(sequenceID, contigModel.getConsensusModel().getTokenAt(contigModel.getConsensusSequenceID(), token), true);  
				}
			}
		}//TODO: check if proper token change events are fired here
		super.redo();
	}



	@Override
	public void undo() throws CannotUndoException {
		for (String sequenceID : sequenceIDs){
			char [] tokens = tokenMap.get(sequenceID);
			int sequenceLength = getAlignment().getAlignmentModel().getSequenceLength(sequenceID);
			getAlignment().getAlignmentModel().removeTokensAt(sequenceID, 0 , sequenceLength);
			for (int i = 0; i < tokens.length; i++) {
				getAlignment().getAlignmentModel().appendToken(sequenceID, tokens[i], true);
				//TODO Will the pherogram now be distorted, since interaction was recently moved to the models? This would have to be avoided. (Implementation of edits will anyway be refactored, though.)
			}	
		}

		super.undo();
	}



	@Override
	public String getPresentationName() {
		StringBuilder result = new StringBuilder(64);
		int counter = 0;
		result.append("Sequence refreshed from contig in ");
		Iterator<String> i = sequenceIDs.iterator();
		while (i.hasNext() && (counter < 3)) {
			result.append("\"");
			result.append(getAlignment().getAlignmentModel().sequenceNameByID(i.next()));
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
