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

import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class NewAction extends AbstractFileAction {
	public NewAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "New"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		loadSymbols("NewDocument");
	}
	
	
	public static AlignmentModel<Character> createAlignmentModel() {
		return new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true));
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (handleUnsavedChanges()) {
			getMainFrame().getAlignmentArea().setAlignmentModel(createAlignmentModel(), true);
			getMainFrame().setFile(null);
			//getMainFrame().setFormat(MainFrame.DEFAULT_FORMAT);
			getMainFrame().setChanged(false);
		}
	}
}
