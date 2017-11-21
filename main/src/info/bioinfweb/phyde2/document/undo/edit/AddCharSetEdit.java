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


import java.awt.Color;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import info.bioinfweb.libralign.dataarea.implementations.charset.CharSet;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.undo.DocumentEdit;



public class AddCharSetEdit extends DocumentEdit {
	private String id;
	private CharSet charSet;
	
	
	public AddCharSetEdit(Document document, String id, String name, Color color) {
		super(document);
		this.id = id;
		charSet = new CharSet(name, color);
	}
	

	@Override
	public void redo() throws CannotRedoException {
		getDocument().getCharSetModel().put(id, charSet);
		super.redo();
	}

	
	@Override
	public void undo() throws CannotUndoException {
		getDocument().getCharSetModel().remove(id);
		super.undo();
	}


	@Override
	public String getPresentationName() {
		return "Add character set \"" + charSet.getName() + "\"";
	}
}
