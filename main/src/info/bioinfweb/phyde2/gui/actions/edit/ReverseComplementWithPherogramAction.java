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
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.libralign.pherogram.model.PherogramAlignmentRelation;
import info.bioinfweb.libralign.pherogram.provider.PherogramReference;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.ReverseComplementEdit;
import info.bioinfweb.phyde2.document.undo.edit.ReverseComplementWithPherogramEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

import javax.swing.Action;

public class ReverseComplementWithPherogramAction extends AbstractPhyDEAction implements Action{
	private Collection <String> sequenceIDs = new ArrayList<>();;
	private TreeMap<String, Integer> sequenceLengthStorage = new TreeMap<>(); 
	private AlignmentModel<Character> underlyingModel = null;
	private int firstColumn = 0;
	private int lastColumn = 0;
	
	
	public ReverseComplementWithPherogramAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Reverse complement with Pherogram"); 
		putValue(Action.SHORT_DESCRIPTION, "Reverse complement with pherogram");
	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		SelectionModel selection = getMainFrame().getActiveAlignmentArea().getSelection();
		
		for (int i = selection.getFirstRow(); i <= selection.getLastRow(); i++) {
			sequenceIDs.add(getMainFrame().getActiveAlignmentArea().getSequenceOrder().idByIndex(i));
		}
		
		underlyingModel = getMainFrame().getActiveAlignment().getAlignmentModel();
		firstColumn = 0;
		lastColumn = underlyingModel.getMaxSequenceLength() - 1;
		
		
		for (String sequenceID : sequenceIDs) {
			int diff = lastColumn + 1 - underlyingModel.getSequenceLength(sequenceID);
			sequenceLengthStorage.put(sequenceID, diff);
		}
		
		//getMainFrame().getActiveAlignment().executeEdit(new ReverseComplementWithPherogramEdit(getMainFrame().getActiveAlignment(), sequenceIDs));
		

	
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
		reverseComplement();
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit(getPresentationName());
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel model, MainFrame mainframe) {
		setEnabled((model != null) && (model instanceof SingleReadContigAlignmentModel) && 
				mainframe.getActiveAlignmentArea().getAlignmentModel().getTokenSet().getType().isNucleotide());
		
	}
	
	
	private void reverseComplement() {
	   	//SelectionModel selection = getReadsArea().getSelection();  
		for (String sequenceID : sequenceIDs) {
    		PherogramReference pherogramReference = ((SingleReadContigAlignmentModel) getMainFrame().getActiveAlignment()).getPherogramReference(sequenceID);
    		
    		int diff = sequenceLengthStorage.get(sequenceID);
    		for (int i = 0; i < diff; i++) {
    			underlyingModel.appendToken(sequenceID, '-', true);
					//TODO Will the pherogram now be distorted, since interaction was recently moved to the models? This would have to be avoided. (Implementation of edits will anyway be refactored, though.)
    		}
    		
    		AlignmentModelUtils.reverseComplement(underlyingModel, sequenceID, firstColumn, lastColumn + 1);
    		
    		if (pherogramReference != null){
    			 PherogramAlignmentRelation rightRelation = pherogramReference.editableIndexByBaseCallIndex(
    					 pherogramReference.getRightCutPosition());
    	            int rightBorder;
    	            if (rightRelation.getCorresponding() == PherogramAlignmentRelation.OUT_OF_RANGE) {
    	                rightBorder = rightRelation.getBeforeValidIndex() + 1;
    	            }
    	            else {
    	                rightBorder = rightRelation.getAfterValidIndex();
    	            }
    	            
    	            int shift = lastColumn-rightBorder;
    	            if (shift < -1) {
    	            	shift = shift + 1;
    	            }
    	            
    	            pherogramReference.reverseComplement(sequenceIDs);
    	            pherogramReference.setFirstSeqPos(shift+1);
    		}	
    		
		}

	}
	
	
	private String getPresentationName() {
		AlignmentModel<?> model = getMainFrame().getActiveAlignment().getAlignmentModel();
		StringBuilder result = new StringBuilder(64);
		int counter = 0;
		int dif;

		result.append("Reverse complement in sequence");
		
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
