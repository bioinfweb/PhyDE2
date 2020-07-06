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


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import info.bioinfweb.commons.bio.CharacterStateSetType;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;
import info.bioinfweb.phyde2.gui.dialogs.CharacterStateSetChooserDialog;



public class CharacterStateSetChooserAction extends AbstractPhyDEAction implements Action{
	
	public CharacterStateSetChooserAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Change character tokens"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "Change character tokens");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		CharacterStateSetChooserDialog dialog = CharacterStateSetChooserDialog.getInstance(MainFrame.getInstance());
		dialog.setChangedType(MainFrame.getInstance().getActiveAlignmentArea().getAlignmentModel().getTokenSet().getType());
		if (dialog.execute()){
			// MainFrame.getInstance().getActiveAlignmentArea().getAlignmentModel().getTokenSet().setType(dialog.getChangedType());
			System.out.println(MainFrame.getInstance().getActiveAlignmentArea().getAlignmentModel().getTokenSet().getType()); //TODO: set new tokenSet instance and handle possible conflicts with current content
		}
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((document != null) && (mainframe.getActiveAlignmentArea() != null));
	}

}
