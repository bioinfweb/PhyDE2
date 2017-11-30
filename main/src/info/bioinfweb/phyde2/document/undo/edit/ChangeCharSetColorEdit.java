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

import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.phyde2.document.Document;

public class ChangeCharSetColorEdit extends AbstractCharSetEdit {
	private CharSetDataModel model;
	private Color newcolor;
	private Color oldcolor;
	
	
	public ChangeCharSetColorEdit(Document document, CharSetDataModel model, String id, Color color) {
		super(document, id);
		this.model = model;
		this.newcolor = color;
	}


	@Override
	public void redo() throws CannotRedoException {
		oldcolor = model.get(getID()).getColor();
		model.get(getID()).setColor(newcolor);
		super.redo();
	}


	@Override
	public void undo() throws CannotUndoException {
		model.get(getID()).setColor(oldcolor);
		super.undo();
	}
	
	
	@Override
	public String getPresentationName() {
		return "Change color of character set \"" + getDocument().getCharSetModel().get(getID()).getName() + "\"";
	}
}
