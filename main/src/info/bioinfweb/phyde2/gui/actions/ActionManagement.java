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
package info.bioinfweb.phyde2.gui.actions;


import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.undo.UndoableEdit;

import info.bioinfweb.commons.swing.AbstractUndoActionManagement;
import info.bioinfweb.commons.swing.AccessibleUndoManager;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.edit.AddSequenceAction;
import info.bioinfweb.phyde2.gui.actions.edit.DeleteSequenceAction;
import info.bioinfweb.phyde2.gui.actions.edit.RemoveGapsAction;
import info.bioinfweb.phyde2.gui.actions.file.NewAction;
import info.bioinfweb.phyde2.gui.actions.file.OpenAction;
import info.bioinfweb.phyde2.gui.actions.file.SaveAction;
import info.bioinfweb.phyde2.gui.actions.file.SaveAsAction;



public class ActionManagement extends AbstractUndoActionManagement {
	private MainFrame mainFrame = null;
//	private Vector<Action> popupActions = new Vector<Action>();
//	private JPopupMenu popupMenu = new JPopupMenu();
	
	public ActionManagement(MainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
		fillMap();
	}
	
	protected void fillMap() {
		put("file.new", new NewAction(mainFrame));
		put("file.openAction", new OpenAction(mainFrame));
		put("file.saveAction", new SaveAction(mainFrame));
		put("file.saveAsAction", new SaveAsAction(mainFrame));
		
		put("edit.addSequence", new AddSequenceAction(mainFrame));
		put("edit.deleteSequence", new DeleteSequenceAction(mainFrame));
		put("edit.removegaps", new RemoveGapsAction(mainFrame));
	}
	
	@Override
	protected AccessibleUndoManager getUndoManager() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	protected JMenu getUndoMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	protected JMenu getRedoMenu() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	protected Action createUndoAction(UndoableEdit edit) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	protected Action createRedoAction(UndoableEdit edit) {
		// TODO Auto-generated method stub
		return null;
	}
}
