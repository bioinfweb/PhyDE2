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

import info.bioinfweb.phyde2.document.Document;



public class AddColumnsToCharSetEdit extends AddRemoveCharSetColumns {
	public AddColumnsToCharSetEdit(Document document, String id, int FirstColumn, int LastColumn) {
		super(document, id, FirstColumn, LastColumn);
	}

	
	@Override
	public void redo() throws CannotRedoException {
		addColumnsToCharSet();
		super.redo();
	}

	
	@Override
	public void undo() throws CannotUndoException {
		removeColumnsFromCharSet();
		restoreColumns();
		super.undo();
	}

	
	@Override
	public String getPresentationName() {
		return "Add columns to character set \"" + getDocument().getCharSetModel().get(getID()) + "\"";
	}
}
