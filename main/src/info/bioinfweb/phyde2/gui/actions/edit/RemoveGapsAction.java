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

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;




@SuppressWarnings("serial")
public class RemoveGapsAction extends AbstractPhyDEAction implements Action {
	public RemoveGapsAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Remove gaps"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		//loadSymbols("Delete");
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		SelectionModel selection = getMainFrame().getAlignmentArea().getSelection();
		int indexFirstColumn = selection.getFirstColumn();

		for (int row = selection.getFirstRow(); row <= selection.getLastRow(); row++) {
			String id = getMainFrame().getAlignmentArea().getSequenceOrder().idByIndex(row);
			int indexLastColumn = Math.min(selection.getLastColumn(), getMainFrame().getAlignmentArea().getAlignmentModel().getSequenceLength(id) - 1);

			int columnPosition = indexFirstColumn;
			for (int i = indexFirstColumn; i <= indexLastColumn; i++) {				
				if (getMainFrame().getAlignmentArea().getAlignmentModel().getTokenAt(id, columnPosition).equals('-')) {
					getMainFrame().getAlignmentArea().getAlignmentModel().removeTokenAt(id, columnPosition);
				} 
				else {
					columnPosition++;
				}
			}				
		}
	}
}
