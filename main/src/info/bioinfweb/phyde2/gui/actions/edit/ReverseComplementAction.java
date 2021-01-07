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


import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.ReverseComplementEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.Action;



@SuppressWarnings("serial")
public class ReverseComplementAction extends AbstractPhyDEAction implements Action {
	private int firstColumn;
	private int lastColumn;
	private AlignmentModel<Character> underlyingModel;
	private Collection<String> sequenceIDs = new ArrayList<>();  
	private TreeMap<String, Integer> sequenceLengthStorage = new TreeMap<>(); 
	
	
	public ReverseComplementAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Reverse Complement"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_M);
		putValue(Action.SHORT_DESCRIPTION, "Reverse Complement");
		//loadSymbols("Remove");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();

		
		for (int i = selection.getFirstRow(); i <= selection.getLastRow(); i++) {
			sequenceIDs.add(getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(i));
		}
		
		
		firstColumn = selection.getFirstColumn();
		lastColumn = selection.getLastColumn();
		underlyingModel = getMainFrame().getActiveAlignment().getAlignmentModel();
		for (String sequenceID : sequenceIDs) {
			int diff = lastColumn - underlyingModel.getSequenceLength(sequenceID);
			sequenceLengthStorage.put(sequenceID, diff);
		}
		
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
		reverseComplement();
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit(getPresentationName());
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
//		getMainFrame().getActiveAlignment().executeEdit(new ReverseComplementEdit(getMainFrame().getActiveAlignment(), 
//				selection.getFirstColumn(), selection.getLastColumn(), sequenceIDs));
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel model, MainFrame mainframe) {
		setEnabled((model != null) && (model instanceof DefaultPhyDE2AlignmentModel) && !mainframe.getActiveAlignmentArea().getSelection().isEmpty() && 
				mainframe.getActiveAlignmentArea().getAlignmentModel().getTokenSet().getType().isNucleotide());
	}
	
	
	private void reverseComplement() { 
    	for (String sequenceID : sequenceIDs) {
    		int diff = sequenceLengthStorage.get(sequenceID);
    		for (int i = 0; i <= diff; i++) {
    			underlyingModel.appendToken(sequenceID, '-', true);
    			//TODO: Check if <= is right here.
			}
    		AlignmentModelUtils.reverseComplement(underlyingModel, sequenceID, firstColumn, lastColumn + 1);
		
		}

	}
	
	private String getPresentationName() {
		AlignmentModel<?> model = getMainFrame().getActiveAlignment().getAlignmentModel();
		StringBuilder result = new StringBuilder(64);
		int counter = 0;
		int dif;

		result.append("Reverse complement ");
		if ((firstColumn-lastColumn) < 0){
			result.append("between column ");
			result.append(firstColumn + 1);
			result.append(" and ");
		}
		else {
			result.append("in column ");
		}
		result.append(lastColumn + 1);
		
		result.append(" in sequence");
		if (sequenceIDs.size() > 1) {
			result.append("s");
		}
		result.append(" ");
		
		
		Iterator<String> i = sequenceIDs.iterator();
		while (i.hasNext() && (counter < 3)) {
			result.append("\"");
			result.append(model.sequenceNameByID(i.next()));
			result.append("\"");
			if (i.hasNext() && (counter < 2)){
				result.append(", ");
			}
			counter++;
		}
		
		dif = sequenceIDs.size() - 3;
		
		if (dif > 0){
			result.append(" and ");
			result.append(dif);
			result.append(" more sequence(s)");
		}
		
		result.append(".");
		
		return result.toString();
	}
}
