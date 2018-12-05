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
package info.bioinfweb.phyde2.document.undo;


import javax.swing.undo.UndoableEdit;

import info.bioinfweb.libralign.model.implementations.swingundo.SwingEditFactory;
import info.bioinfweb.libralign.model.implementations.swingundo.edits.LibrAlignSwingAlignmentEdit;
import info.bioinfweb.libralign.model.implementations.swingundo.edits.sequence.SwingAddSequenceEdit;
import info.bioinfweb.libralign.model.implementations.swingundo.edits.sequence.SwingConcreteAddSequenceEdit;
import info.bioinfweb.libralign.model.implementations.swingundo.edits.sequence.SwingRemoveSequenceEdit;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.AddSequenceEdit;



public class AlignmentModelEditFactory implements SwingEditFactory<Character> {
	private PhyDE2AlignmentModel document;
	
	
	public AlignmentModelEditFactory(PhyDE2AlignmentModel document) {
		super();
		this.document = document;
	}
	
	
	@Override
	public UndoableEdit createEdit(LibrAlignSwingAlignmentEdit<Character> edit) {
		return new AlignmentModelEdit(document, edit);
	}
	
	
	@Override
	public SwingAddSequenceEdit createAddSequenceEdit(SwingConcreteAddSequenceEdit<Character> edit) {
		throw new InternalError("Adding sequence must be done in the underlying model.");
	}
}
