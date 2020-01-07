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


import java.util.Map;
import java.util.TreeMap;

import info.bioinfweb.commons.collections.ListChangeType;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;



public class SingleReadContigAlignmentModel extends PhyDE2AlignmentModel {
	private Map<String, PherogramReference> pherogramModelMap = new TreeMap<String, PherogramReference>();
	private AlignmentModel<Character> consensusModel;
	private String consensusSequenceID;
	
	
	public SingleReadContigAlignmentModel(Document owner) {
		this(owner, new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)));		
	}
	
	
	private SingleReadContigAlignmentModel(Document owner, AlignmentModel<Character> singleReadModel) {
		this(owner, singleReadModel, new CharSetDataModel(singleReadModel), 
				new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)));		
	}
	
	
	public SingleReadContigAlignmentModel(Document owner, AlignmentModel<Character> singleReadModel, CharSetDataModel charSetModel, AlignmentModel<Character> consensusModel) {
		super(owner, singleReadModel, charSetModel);
		if (consensusModel == null) {
			this.consensusModel = new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true));
		}
		else {
			this.consensusModel = consensusModel;
		}
		consensusSequenceID = this.consensusModel.addSequence("Consensus");
	}
	
	
	@Override
	public AlignmentType getType() {
		return AlignmentType.SINGLE_READ_CONTIG;
	}


	public String getConsensusSequenceID() {
		return consensusSequenceID;
	}


	public void addPherogram(String sequenceID, PherogramReference reference) {
		if ((sequenceID != null) && (reference != null)) {
			pherogramModelMap.put(sequenceID, reference);  //TODO Add as data model to the underlying alignment model and not to a custom map.
			fireAfterPherogramAddedOrDeleted(ListChangeType.INSERTION, reference, sequenceID);
		}
		else {
			throw new NullPointerException("sequenceID and model must not be null.");
		}
	}
	
	
	//TODO Can this event be removed, now that AlignmentModel listener contains an event for adding data models?
	protected void fireAfterPherogramAddedOrDeleted(ListChangeType listChangeType, PherogramReference pherogramReference, String sequenceID) {
		PherogramReferenceChangeEvent e = new PherogramReferenceChangeEvent(this, listChangeType, sequenceID, pherogramReference);
		for (PhyDE2AlignmentModelListener listener : listeners){
			listener.afterPherogramAddedOrDeleted(e);
		}
	}

	
	public PherogramReference getPherogramReference(String sequenceID) {
		return pherogramModelMap.get(sequenceID);
	}
	
	
	public PherogramReference removePherogramModel(String sequenceID){
		PherogramReference pherogramReference = pherogramModelMap.remove(sequenceID);
		if (pherogramReference != null){
			fireAfterPherogramAddedOrDeleted(ListChangeType.DELETION, pherogramReference, sequenceID);
		}
		return pherogramReference;
	
	}


	public AlignmentModel<Character> getConsensusModel() {
		return consensusModel;
	}
}
