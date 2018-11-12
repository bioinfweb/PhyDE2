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
import java.util.ArrayList;
import java.util.Collection;

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.undo.edit.AddCharSetEdit;
import info.bioinfweb.phyde2.document.undo.edit.ReverseComplementEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import javax.swing.Action;



public class ReverseComplementAction extends AbstractPhyDEAction implements Action {
	public ReverseComplementAction(MainFrame mainframe) {
		super(mainframe);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		SelectionModel model = getMainFrame().getAlignmentArea().getSelection();
		
//		Collection<String> sequenceIDs = new ArrayList<>();
//		for (int i = model.getFirstRow(); i <= model.getLastRow(); i++) {
//			
//		}
//		
//		getMainFrame().getDocument().executeEdit(new ReverseComplementEdit(getMainFrame().getDocument(), model.getFirstColumn(), model.getLastColumn(), );
	}

	
	@Override
	public void setEnabled(Document document, MainFrame mainframe) {
		setEnabled((document != null) && !mainframe.getAlignmentArea().getSelection().isEmpty() && 
				mainframe.getAlignmentArea().getAlignmentModel().getTokenSet().getType().isNucleotide());
	}
}
