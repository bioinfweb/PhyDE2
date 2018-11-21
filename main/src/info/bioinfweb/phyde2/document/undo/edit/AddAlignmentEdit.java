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



import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



public class AddAlignmentEdit extends AbstractAddDeleteAlignmentEdit  {

	public AddAlignmentEdit(Document document, PhyDE2AlignmentModel model) {
		super(document, model);
	}

	
	@Override
	public void redo() throws CannotRedoException {
		addAlignmentModel();
		super.redo();
	}


	@Override
	public void undo() throws CannotUndoException {
		deleteAlignment();
		super.undo();
	}


	@Override
	public String getPresentationName() {
		return "Alignment added.";
	}

	
	


	

}
