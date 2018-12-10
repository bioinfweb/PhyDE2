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
package info.bioinfweb.phyde2.gui;


import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.phyde2.document.PherogramChangeEvent;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelChangeEvent;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModelListener;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;



public class ContigTab extends Tab {
	private AlignmentArea consensusSequenceArea = null;
			
	
	public ContigTab(SingleReadContigAlignmentModel contigAlignment) {
		super(contigAlignment);
		consensusSequenceArea = new AlignmentArea(getAlignmentsContainer());
		consensusSequenceArea.setAlignmentModel(contigAlignment.getConsensusModel(), false);
		getAlignmentsContainer().getAlignmentAreas().add(consensusSequenceArea);
		consensusSequenceArea.setAllowVerticalScrolling(false);
		contigAlignment.addAlignmentListener(new PhyDE2AlignmentModelListener() {
			
			@Override
			public void afterPherogramAddedOrDeleted(PherogramChangeEvent e) {
				PherogramAreaModel pherogramModel = e.getPherogramReference().getModel();
				String sequenceID = e.getSequenceID();
				AlignmentArea alignmentArea = MainFrame.getInstance().getActiveAlignmentArea();

				switch (e.getListChangeType()) {
				case INSERTION: 	
					if (pherogramModel != null){
						PherogramArea pherogramDataArea = new PherogramArea(alignmentArea.getContentArea(), pherogramModel, MainFrame.getInstance().getPherogramFormats());
						pherogramDataArea.addMouseListener(new PherogramMouseListener(pherogramDataArea));
						alignmentArea.getDataAreas().getSequenceAreas(sequenceID).add(pherogramDataArea);
					}
				
					break;
					
				case DELETION:
					if (pherogramModel != null){
						//muss hier vorher auch der Listener removed werden?
						alignmentArea.getDataAreas().getSequenceAreas(sequenceID).clear();  //TODO Is this or should this be done in LibrAlign?
					}
					break;
				
				default:
					break;
				
			}
			
				
			}
			
			@Override
			public void afterFileNameChanged(PhyDE2AlignmentModelChangeEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterChangedFlagSet(PhyDE2AlignmentModelChangeEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	@Override
	public SingleReadContigAlignmentModel getDocument() {
		return (SingleReadContigAlignmentModel)super.getDocument();
	}
}
