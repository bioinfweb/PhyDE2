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

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.libralign.pherogram.model.PherogramAlignmentRelation;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;

public class ReverseComplementWithPherogramEdit extends AlignmentEdit{

	private Collection <String> sequenceIDs;
	private TreeMap<String, Integer> sequenceLengthStorage = new TreeMap<>(); 
	private AlignmentModel<Character> underlyingModel;
	private int firstColumn;
	private int lastColumn;
	
	public ReverseComplementWithPherogramEdit(PhyDE2AlignmentModel alignment, Collection<String> sequenceIDs) {
		super(alignment);
		this.sequenceIDs = sequenceIDs;
		underlyingModel = getAlignment().getAlignmentModel().getUnderlyingModel();
		firstColumn = 0;
		lastColumn = underlyingModel.getMaxSequenceLength() - 1;
		
		
		for (String sequenceID : sequenceIDs) {
			int diff = lastColumn + 1 - underlyingModel.getSequenceLength(sequenceID);
			sequenceLengthStorage.put(sequenceID, diff);
		}
	}

	private void reverseComplement() {
	   	//SelectionModel selection = getReadsArea().getSelection();  
    	for (String sequenceID : sequenceIDs) {
    		PherogramReference pherogramReference = ((SingleReadContigAlignmentModel) getAlignment()).getPherogramModel(sequenceID);
    		PherogramAreaModel pherogramModel = pherogramReference.getModel();
    		
    		int diff = sequenceLengthStorage.get(sequenceID);
    		for (int i = 0; i < diff; i++) {
    			underlyingModel.appendToken(sequenceID, '-');
			}
    		
    		AlignmentModelUtils.reverseComplement(underlyingModel, sequenceID, firstColumn, lastColumn + 1);
    		
    		if (pherogramModel != null){
    			 PherogramAlignmentRelation rightRelation = pherogramModel.editableIndexByBaseCallIndex(
    	                    pherogramModel.getRightCutPosition());
    	            int rightBorder;
    	            if (rightRelation.getCorresponding() == PherogramAlignmentRelation.OUT_OF_RANGE) {
    	                rightBorder = rightRelation.getBeforeValidIndex() + 1;
    	            }
    	            else {
    	                rightBorder = rightRelation.getAfterValidIndex();
    	            }
    	            
    	            int shift = lastColumn-rightBorder;
    	            
    	            pherogramModel.reverseComplement();
    	            pherogramModel.setFirstSeqPos(shift+1);
    		}	
    		
		}

	}
	
	@Override
	public void redo() throws CannotRedoException {
		reverseComplement();
		super.redo();
	}


	@Override
	public void undo() throws CannotUndoException {
		reverseComplement();
		for (String sequenceID : sequenceIDs){
			int originalSequenceLength = lastColumn + 1 - sequenceLengthStorage.get(sequenceID);
			underlyingModel.removeTokensAt(sequenceID, originalSequenceLength, underlyingModel.getSequenceLength(sequenceID));
		}
		
		super.undo();
	}


	@Override
	public String getPresentationName() {
		AlignmentModel<?> model = getAlignment().getAlignmentModel();
		StringBuilder result = new StringBuilder(64);
		int counter = 0;
		int dif;

		result.append("Reverse complement in sequence");
		
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