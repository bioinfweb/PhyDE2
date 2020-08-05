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
package info.bioinfweb.phyde2.document.undo.edit;


import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;



public class ReverseComplementEdit extends AlignmentEdit {
	private int firstColumn;
	private int lastColumn;
	private AlignmentModel<Character> underlyingModel;
	private Collection<String> sequenceIDs;  
	private TreeMap<String, Integer> sequenceLengthStorage = new TreeMap<>(); 
	 
	
	public ReverseComplementEdit(PhyDE2AlignmentModel model, int firstColumn, int lastColumn, Collection<String> sequenceIDs) {
		super(model);
		this.firstColumn = firstColumn;
		this.lastColumn = lastColumn;
		this.sequenceIDs = sequenceIDs;
		underlyingModel = getAlignment().getAlignmentModel(); // Underlying model used to avoid creation of edits by SwingUndoAlignmentModel.
		for (String sequenceID : sequenceIDs) {
			int diff = lastColumn - underlyingModel.getSequenceLength(sequenceID);
			sequenceLengthStorage.put(sequenceID, diff);
		}
	}

	
	private void reverseComplement() { 
    	for (String sequenceID : sequenceIDs) {
    		int diff = sequenceLengthStorage.get(sequenceID);
    		for (int i = 0; i <= diff; i++) {
    			underlyingModel.appendToken(sequenceID, '-', true);
    			//TODO: Check if <= is right here.
			}
    		AlignmentModelUtils.reverseComplement(underlyingModel, sequenceID, firstColumn, lastColumn + 1);
		
		}

	}
	
	
	@Override
	public void redo() throws CannotRedoException {
		reverseComplement();
		super.redo();	
	}


	@Override
	public void undo() throws CannotUndoException {
		for (String sequenceID : sequenceIDs) {
			AlignmentModelUtils.reverseComplement(underlyingModel, sequenceID, firstColumn, lastColumn + 1);
			int originalSequenceLength = underlyingModel.getSequenceLength(sequenceID)-sequenceLengthStorage.get(sequenceID);
			underlyingModel.removeTokensAt(sequenceID, originalSequenceLength-1, underlyingModel.getSequenceLength(sequenceID));
		}
		super.undo();
	}


	@Override
	public String getPresentationName() {
		AlignmentModel<?> model = getAlignment().getAlignmentModel();
		StringBuilder result = new StringBuilder(64);
		int counter = 0;
		int dif;

		result.append("Reverse complement ");
		if ((firstColumn-lastColumn) < 0){
			result.append("between column ");
			result.append(firstColumn + 1);
			result.append(" and ");
		}
		else {
			result.append("in column ");
		}
		result.append(lastColumn + 1);
		
		result.append(" in sequence");
		if (sequenceIDs.size() > 1) {
			result.append("s");
		}
		result.append(" ");
		
		
		Iterator<String> i = sequenceIDs.iterator();
		while (i.hasNext() && (counter < 3)) {
			result.append("\"");
			result.append(model.sequenceNameByID(i.next()));
			result.append("\"");
			if (i.hasNext() && (counter < 2)){
				result.append(", ");
			}
			counter++;
		}
		
		dif = sequenceIDs.size() - 3;
		
		if (dif > 0){
			result.append(" and ");
			result.append(dif);
			result.append(" more sequence(s)");
		}
		
		result.append(".");
		
		return result.toString();
	}
}
