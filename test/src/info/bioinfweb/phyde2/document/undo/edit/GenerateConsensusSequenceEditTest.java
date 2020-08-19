package info.bioinfweb.phyde2.document.undo.edit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.dataarea.implementations.consensus.ConsensusSequenceModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.adapters.StringAdapter;
import info.bioinfweb.libralign.model.implementations.ArrayListAlignmentModel;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;

public class GenerateConsensusSequenceEditTest {

	@Test
	
	public void test_redo () {
		SingleReadContigAlignmentModel model1 = new SingleReadContigAlignmentModel(new Document());
		GenerateConsensusSequenceEdit consensusSequence = new GenerateConsensusSequenceEdit(model1);
		StringAdapter adapter = new StringAdapter(model1.getConsensusModel(), true);
	
		
		String sequenceID1 = model1.getAlignmentModel().addSequence("seq1");
		model1.getAlignmentModel().appendTokens(sequenceID1, AlignmentModelUtils.charSequenceToTokenList("ATCA", model1.getAlignmentModel().getTokenSet()), true);		
		String sequenceID2 = model1.getAlignmentModel().addSequence("seq2");
		model1.getAlignmentModel().appendTokens(sequenceID2, AlignmentModelUtils.charSequenceToTokenList("ATTA", model1.getAlignmentModel().getTokenSet()), true);
		String sequenceID3 = model1.getAlignmentModel().addSequence("seq3");
		model1.getAlignmentModel().appendTokens(sequenceID3, AlignmentModelUtils.charSequenceToTokenList("ATTA", model1.getAlignmentModel().getTokenSet()), true);
		
		consensusSequence.redo();
		assertTrue(adapter.getSequence(model1.getConsensusSequenceID()).equals("ATTA"));
				
		model1.getAlignmentModel().setTokenAt(sequenceID2, 2, 'A');
		model1.getAlignmentModel().setTokenAt(sequenceID3, 2, 'A');
		consensusSequence.redo();
		assertTrue(adapter.getSequence(model1.getConsensusSequenceID()).equals("ATAA")); 
		
		model1.getAlignmentModel().appendTokens(sequenceID1, AlignmentModelUtils.charSequenceToTokenList("GCG", model1.getAlignmentModel().getTokenSet()), true);
		consensusSequence.redo();
		assertTrue(adapter.getSequence(model1.getConsensusSequenceID()).equals("ATAAGCG"));
		
		model1.getAlignmentModel().appendTokens(sequenceID2, AlignmentModelUtils.charSequenceToTokenList("GCGTTAA", model1.getAlignmentModel().getTokenSet()), true);
		consensusSequence.redo();
		assertTrue(adapter.getSequence(model1.getConsensusSequenceID()).equals("ATAAGCGTTAA"));
		
		model1.getAlignmentModel().removeTokensAt(sequenceID2,7,9);
		model1.getAlignmentModel().setTokenAt(sequenceID1, 4, 'C');
		model1.getAlignmentModel().setTokenAt(sequenceID2, 4, 'C');
		consensusSequence.redo();
		assertTrue(adapter.getSequence(model1.getConsensusSequenceID()).equals("ATAACCGAA"));
	}
}
