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
package info.bioinfweb.phyde2.gui.actions.file;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class SaveAction extends AbstractFileAction {
	
	public SaveAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Save"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		putValue(Action.SHORT_DESCRIPTION, "Save"); 
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		loadSymbols("Save");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		save();
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((document != null) && (document.isChanged()));
	}
}
