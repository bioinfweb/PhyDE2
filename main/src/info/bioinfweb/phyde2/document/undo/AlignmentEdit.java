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

import info.bioinfweb.commons.swing.AbstractDocumentEdit;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;



public abstract class AlignmentEdit extends AbstractDocumentEdit implements UndoableEdit {
	private PhyDE2AlignmentModel document;

	
	public AlignmentEdit(PhyDE2AlignmentModel document) {
		super();
		this.document = document;
	}

	
	public PhyDE2AlignmentModel getAlignment() {
		return document;
	}


	@Override
	protected void registerDocumentChange() {
		MainFrame.getInstance().getActionManagement().refreshActionStatus();  //TODO Replace this call by DocumentChangeEvent processing in the future.
	}
}
