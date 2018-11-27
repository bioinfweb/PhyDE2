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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;



public class ReverseComplementEdit extends AlignmentEdit {
	private int firstColumn;
	private int lastColumn;
	private Collection<String> sequenceIDs;  
	 
	
	public ReverseComplementEdit(PhyDE2AlignmentModel document, int firstColumn, int lastColumn, Collection<String> sequenceIDs) {
		super(document);
		this.firstColumn = firstColumn;
		this.lastColumn = lastColumn;
		this.sequenceIDs = sequenceIDs;
	}

	
	private void reverseComplement() {
    	//SelectionModel selection = getReadsArea().getSelection();
    	AlignmentModel<Character> model = getAlignment().getAlignmentModel().getUnderlyingModel();  // Underlying model used to avoid creation of edits by SwingUndoAlignmentModel.
    	for (String sequenceID : sequenceIDs) {
//			PherogramArea area = getPherogramArea(sequenceID);
//			PherogramAreaModel pherogramAlignmentModel = area.getModel();
//
//            PherogramAlignmentRelation rightRelation = pherogramAlignmentModel.editableIndexByBaseCallIndex(
//                    pherogramAlignmentModel.getRightCutPosition());
//            int rightBorder;
//            if (rightRelation.getCorresponding() == PherogramAlignmentRelation.OUT_OF_RANGE) {
//                rightBorder = rightRelation.getBeforeValidIndex() + 1;
//            }
//            else {
//                rightBorder = rightRelation.getAfterValidIndex();
//            }
    		 
    			 if (lastColumn > model.getSequenceLength(sequenceID)){
    				int diff = lastColumn-model.getSequenceLength(sequenceID);
    				for (int i = 0; i <= diff; i++) {
    					model.appendToken(sequenceID, '-');
					}	
    			 }
    			 AlignmentModelUtils.reverseComplement(model, sequenceID, firstColumn, lastColumn + 1);
			//pherogramAlignmentModel.reverseComplement();
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
