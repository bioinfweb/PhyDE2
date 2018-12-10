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
package info.bioinfweb.phyde2.document;


import info.bioinfweb.commons.collections.ListChangeType;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.events.SequenceChangeEvent;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;



public class SingleReadContigAlignmentModel extends PhyDE2AlignmentModel {
	private Map<String, PherogramReference> pherogramModelMap = new TreeMap<String, PherogramReference>();
	private AlignmentModel<Character> consensusModel;
	
	
	public SingleReadContigAlignmentModel() {
		this(new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)));		
	}
	
	
	public SingleReadContigAlignmentModel(AlignmentModel<Character> consensusModel) {
		super();
		this.consensusModel = consensusModel;
		//for testing:
		
		String id = consensusModel.addSequence("Consensus");
//		for (int i = 0; i < 10; i++) {
//			consensusModel.appendToken(id, 'A');
//			consensusModel.appendToken(id, 'T');
//			consensusModel.appendToken(id, 'G');
//			consensusModel.appendToken(id, 'C');	
//		}

	}
	
	
	public void addPherogram(String sequenceID, PherogramReference reference) {
		if ((sequenceID != null) && (reference != null)) {
			pherogramModelMap.put(sequenceID, reference);
			fireAfterPherogramAddedOrDeleted(ListChangeType.INSERTION, reference, sequenceID);
		}
		else {
			throw new NullPointerException("sequenceID and model must not be null.");
		}
	}
	
	
	protected void fireAfterPherogramAddedOrDeleted(ListChangeType listChangeType, PherogramReference pherogramReference, 
			String sequenceID) {
		
		PherogramChangeEvent e = new PherogramChangeEvent(this, listChangeType, pherogramReference, sequenceID);
		for (PhyDE2AlignmentModelListener listener : listeners){
			listener.afterPherogramAddedOrDeleted(e);
		}
	}
	
	public PherogramReference getPherogramModel(String sequenceID) {
		return pherogramModelMap.get(sequenceID);
	}
	
	public void removePherogramModel(String sequenceID){
		PherogramReference pherogramReference = pherogramModelMap.remove(sequenceID);
		fireAfterPherogramAddedOrDeleted(ListChangeType.DELETION, pherogramReference, sequenceID);
	
	}


	public AlignmentModel<Character> getConsensusModel() {
		return consensusModel;
	}
}
