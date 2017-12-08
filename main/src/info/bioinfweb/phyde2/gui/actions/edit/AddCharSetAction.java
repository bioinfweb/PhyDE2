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

import info.bioinfweb.commons.IntegerIDManager;
import info.bioinfweb.commons.graphics.UniqueColorLister;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.undo.edit.AddCharSetEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;
import info.bioinfweb.phyde2.gui.dialogs.NewCharSetDialog;


@SuppressWarnings("serial")
public class AddCharSetAction extends AbstractPhyDEAction implements Action {
	private IntegerIDManager idManager = new IntegerIDManager();
	private UniqueColorLister colorLister = new UniqueColorLister();  //TODO This class should ideally be reset after a new document is created or opened. 
	private NewCharSetDialog dialog = new NewCharSetDialog(getMainFrame());
	

	public AddCharSetAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Add character set"); 
		putValue(Action.SHORT_DESCRIPTION, "Add char. set");
		loadSymbols("AddCH");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		dialog.setSelectedColor(colorLister.generateNext());
		dialog.setName("");
		if (dialog.execute()) {
			getMainFrame().getDocument().executeEdit(new AddCharSetEdit(getMainFrame().getDocument(), "cs" + idManager.createNewID(), dialog.getName(), dialog.getSelectedColor()));
		}
	}


	@Override
	public void setEnabled(Document document, MainFrame mainframe) {
		setEnabled((document != null) && (mainframe.getCharSetArea() != null));
	}
}