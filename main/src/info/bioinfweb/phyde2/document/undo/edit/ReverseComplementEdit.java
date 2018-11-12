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

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.undo.DocumentEdit;



public class ReverseComplementEdit extends DocumentEdit {
	private int firstColumn;
	private int lastColumn;
	private Collection<String> sequenceIDs;
	
	
	public ReverseComplementEdit(Document document, int firstColumn, int lastColumn, Collection<String> sequenceIDs) {
		super(document);
		this.firstColumn = firstColumn;
		this.lastColumn = lastColumn;
		this.sequenceIDs = sequenceIDs;
	}


	private void reverseComplement() {
    	//SelectionModel selection = getReadsArea().getSelection();
    	AlignmentModel<?> model = getDocument().getAlignmentModel();
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

			AlignmentModelUtils.reverseComplement(model, sequenceID, firstColumn, lastColumn);
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
		return "Reverse complement between column " + firstColumn + " and " + lastColumn + " in " + sequenceIDs.size() + " sequences.";
	}
}
