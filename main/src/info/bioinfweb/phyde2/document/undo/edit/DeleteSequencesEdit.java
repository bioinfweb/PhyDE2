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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.AlignmentEdit;



public class DeleteSequencesEdit extends AlignmentEdit {
	
	
	
	public DeleteSequencesEdit(PhyDE2AlignmentModel model, Collection<String> sequenceIDs) {
		super(model);

	}


	@Override
	public void redo() throws CannotRedoException {
		//TODO Implement
		throw new InternalError("not implemented");
		//super.redo();
	}


	@Override
	public void undo() throws CannotUndoException {
		//TODO Implement
		throw new InternalError("not implemented");
		//super.undo();
	}


	@Override
	public String getPresentationName() {
		StringBuilder result = new StringBuilder();
//		result.append("Sequence");
//		int counter = 0;
//		int diff;
//		for (int i = 0; i < edits.size(); i++) {
//			if (counter < 3){
//				result.append("\"");
//				result.append(edits.get(i).getSequenceName());
//				result.append("\"");
//			}
//			if (counter < 2){
//				result.append(", ");
//			}
//			counter++;
//		}
//		
//		
//		diff = edits.size() - 3;
//		
//		if (diff > 0){
//			result.append(" and ");
//			result.append(diff);
//			result.append(" more sequence(s)");
//		}
//		
//		result.append("have been deleted in alignment");
//		result.append(getAlignment());
//		result.append(".");
		
		
		return result.toString();
	}
}
