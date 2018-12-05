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

import java.util.Map;
import java.util.TreeMap;



public class SingleReadContigAlignmentModel extends PhyDE2AlignmentModel {
	private Map<String, PherogramAreaModel> pherogramModelMap = new TreeMap <String, PherogramAreaModel>();
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
	
	
	public void addPherogram(String sequenceID, PherogramAreaModel model) {
		if ((sequenceID != null) && (model != null)) {
			pherogramModelMap.put(sequenceID, model);
			fireAfterPherogramAddedOrDeleted(ListChangeType.INSERTION, model, sequenceID);
		}
		else {
			throw new NullPointerException("sequenceID and model must not be null.");
		}
	}
	

	public void createPherogramSequence(String sequenceID){  //TODO Move to AddSequenceEdit and use underlyingModel
		PherogramAreaModel model = getPherogramModel(sequenceID);
		if (model != null){
			for (int j = 0; j < model.getPherogramProvider().getSequenceLength(); j++) {
				getAlignmentModel().appendToken(sequenceID, model.getPherogramProvider().getBaseCall(j));
			}
			
		}
	}
	
	
	protected void fireAfterPherogramAddedOrDeleted(ListChangeType listChangeType, PherogramAreaModel pherogramModel, String sequenceID) {
		PherogramChangeEvent e = new PherogramChangeEvent(this, listChangeType, pherogramModel, sequenceID);
		for (PhyDE2AlignmentModelListener listener : listeners){
			listener.afterPherogramAddedOrDeleted(e);
		}
	}
	
	public PherogramAreaModel getPherogramModel(String sequenceID) {
		return pherogramModelMap.get(sequenceID);
	}
	
	public void removePherogramModel(String sequenceID){
		PherogramAreaModel pherogramModel = pherogramModelMap.remove(sequenceID);
		fireAfterPherogramAddedOrDeleted(ListChangeType.DELETION, pherogramModel, sequenceID);
	
	}


	public AlignmentModel<Character> getConsensusModel() {
		return consensusModel;
	}
}
