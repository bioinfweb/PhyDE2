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
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.dataarea.implementations.consensus.ConsensusSequenceModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.DataModelLists;
import info.bioinfweb.libralign.model.data.DataModel;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.libralign.pherogram.provider.PherogramReference;



public class SingleReadContigAlignmentModel extends PhyDE2AlignmentModel {
	private AlignmentModel<Character> consensusAlignmentModel;
	private String consensusSequenceID;
	private ConsensusSequenceModel consensusDataModel; 

	
	
	public SingleReadContigAlignmentModel(Document owner) {
		this(owner, new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)));		
	}


	private SingleReadContigAlignmentModel(Document owner, AlignmentModel<Character> singleReadModel) {
		this(owner, singleReadModel, new CharSetDataModel(singleReadModel), 
				new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)));		
				
	}
	
	public SingleReadContigAlignmentModel(Document owner, AlignmentModel<Character> singleReadModel, CharSetDataModel charSetModel, AlignmentModel<Character> consensusAlignmentModel) {
		super(owner, singleReadModel, charSetModel);
		if (consensusAlignmentModel == null) {
			this.consensusAlignmentModel = new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true));
		}
		else {
			this.consensusAlignmentModel = consensusAlignmentModel;
		}
		consensusSequenceID = this.consensusAlignmentModel.addSequence("Consensus");
		
		this.consensusDataModel = new ConsensusSequenceModel(singleReadModel);
	}
	
	
	@Override
	public AlignmentType getType() {
		return AlignmentType.SINGLE_READ_CONTIG;
	}


	public String getConsensusSequenceID() {
		return consensusSequenceID;
	}


	public void addPherogram(String sequenceID, PherogramReference reference) {
		if (sequenceID == null) {
			throw new IllegalArgumentException("sequenceID must not be null.");
		}
		else if (reference == null) {
			throw new IllegalArgumentException("reference must not be null.");
		}
		else {
			getAlignmentModel().getDataModels().getSequenceList(sequenceID).add(reference);
			fireAfterPherogramAddedOrDeleted(ListChangeType.INSERTION, reference, sequenceID);
		}
	}
	
	
	//TODO Can this event be removed, now that AlignmentModel listener contains an event for adding data models?
	protected void fireAfterPherogramAddedOrDeleted(ListChangeType listChangeType, PherogramReference pherogramReference, String sequenceID) {
		PherogramReferenceChangeEvent e = new PherogramReferenceChangeEvent(this, listChangeType, sequenceID, pherogramReference);
		for (PhyDE2AlignmentModelListener listener : listeners) {
			listener.afterPherogramAddedOrDeleted(e);
		}
	}

	
	public PherogramReference getPherogramReference(String sequenceID) {
		return getAlignmentModel().getDataModels().getSequenceList(sequenceID).getFirstOfType(PherogramReference.class);
	}
	
	
	public PherogramReference removePherogramModel(String sequenceID){
		PherogramReference pherogramReference = getAlignmentModel().getDataModels().getSequenceList(sequenceID).removeFirstOfType(PherogramReference.class);
		if (pherogramReference != null){
			fireAfterPherogramAddedOrDeleted(ListChangeType.DELETION, pherogramReference, sequenceID);
		}
		return pherogramReference;
	}


	public AlignmentModel<Character> getConsensusModel() {
		return consensusAlignmentModel;
	}
	

	public ConsensusSequenceModel getConsensusDataModel() {
		return consensusDataModel;
	}
}
