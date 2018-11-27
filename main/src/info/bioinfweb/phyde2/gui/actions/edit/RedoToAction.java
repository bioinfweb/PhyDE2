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
package info.bioinfweb.phyde2.gui.actions.edit;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.undo.UndoableEdit;

import info.bioinfweb.phyde2.gui.MainFrame;

@SuppressWarnings("serial")
public class RedoToAction extends UndoRedoToAction{
	public RedoToAction(MainFrame mainframe, UndoableEdit edit) {
		super(mainframe, edit);
		putValue(Action.NAME, edit.getRedoPresentationName()); 
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		getMainFrame().getActiveAlignment().getUndoManager().redoTo(edit);
	}

}
