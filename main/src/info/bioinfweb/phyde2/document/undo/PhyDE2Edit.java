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
package info.bioinfweb.phyde2.document.undo;


import info.bioinfweb.commons.swing.AbstractDocumentEdit;
import info.bioinfweb.phyde2.gui.MainFrame;

import javax.swing.undo.UndoableEdit;



public abstract class PhyDE2Edit extends AbstractDocumentEdit {
	public PhyDE2Edit() {
		super();
	}

	
	@Override
	protected void registerDocumentChange() {
		MainFrame.getInstance().getActionManagement().refreshActionStatus();  //TODO Replace this call by DocumentChangeEvent processing in the future.
	}
}