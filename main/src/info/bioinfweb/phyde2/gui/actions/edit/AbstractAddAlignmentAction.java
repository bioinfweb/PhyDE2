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
package info.bioinfweb.phyde2.gui.actions.edit;


import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.AddAlignmentEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import javax.swing.Action;
import javax.swing.JOptionPane;



public abstract class AbstractAddAlignmentAction extends AbstractPhyDEAction implements Action {
	public AbstractAddAlignmentAction(MainFrame mainframe) {
		super(mainframe);
	}

	
	protected void addAlignment(PhyDE2AlignmentModel model, String alternativeLabel, String message) {
		String label = JOptionPane.showInputDialog(getMainFrame(), message);
		if (label != null) {
			if (label.length() == 0) {
				label = alternativeLabel;
	//			for (i = 0; i < tabbedPane.getComponentCount(); i += 1) {
	//				if (tabbedPane.getTitleAt(i).contains(tabTitle)) {
	//					l += 1;
	//				}
	//			}
	//			tabTitle = tabTitle + l;
	//			}
			}
			model.getAlignmentModel().setID(getMainFrame().getSelectedDocument().generateUniqueID());
			model.getAlignmentModel().setLabel(label);
			getMainFrame().getSelectedDocument().executeEdit(new AddAlignmentEdit(getMainFrame().getSelectedDocument(), model));  //TODO Use getNewDocument().executeEdit() as soon as undo manager has been moved
			getMainFrame().showAlignment(model);
		}
	}
	

	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled(mainframe.getSelectedDocument() != null);
	}
}
