package info.bioinfweb.phyde2.document.undo.edit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import info.bioinfweb.libralign.model.adapters.StringAdapter;
import info.bioinfweb.libralign.model.implementations.ArrayListAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.libralign.model.utils.AlignmentModelUtils;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;

public class ExtendGeneratedConsensusSequenceEditTest {
	
	@Test
		
	public void test_redo () {
		SingleReadContigAlignmentModel model1 = new SingleReadContigAlignmentModel(new Document());
		ExtendGeneratedConsensusSequenceEdit consensusSequence = new ExtendGeneratedConsensusSequenceEdit(model1);
		StringAdapter adapter = new StringAdapter(model1.getConsensusModel(), true);
	
		
		String sequenceID1 = model1.getAlignmentModel().addSequence("seq1");
		model1.getAlignmentModel().appendTokens(sequenceID1, AlignmentModelUtils.charSequenceToTokenList("AT-CG", model1.getAlignmentModel().getTokenSet()), true);		
		String sequenceID2 = model1.getAlignmentModel().addSequence("seq2");
		model1.getAlignmentModel().appendTokens(sequenceID2, AlignmentModelUtils.charSequenceToTokenList("AT-CG", model1.getAlignmentModel().getTokenSet()), true);
		String sequenceID3 = model1.getAlignmentModel().addSequence("seq3");
		model1.getAlignmentModel().appendTokens(sequenceID3, AlignmentModelUtils.charSequenceToTokenList("AT-CC", model1.getAlignmentModel().getTokenSet()), true);
		
		
		model1.getConsensusModel().appendTokens(model1.getConsensusSequenceID(),AlignmentModelUtils.charSequenceToTokenList("AA-TT", model1.getAlignmentModel().getTokenSet()), true);
		assertTrue(adapter.getSequence(model1.getConsensusSequenceID()).equals("AA-TT"));
		
		String sequenceID4 = model1.getAlignmentModel().addSequence("seq4");
		model1.getAlignmentModel().appendTokens(sequenceID4, AlignmentModelUtils.charSequenceToTokenList("ATCCG", model1.getAlignmentModel().getTokenSet()), true);		
		
		String sequenceID5 = model1.getAlignmentModel().addSequence("seq5");
		model1.getAlignmentModel().appendTokens(sequenceID5, AlignmentModelUtils.charSequenceToTokenList("AT-CGCC", model1.getAlignmentModel().getTokenSet()), true);		
		
		
		consensusSequence.redo();
		assertTrue(adapter.getSequence(model1.getConsensusSequenceID()).equals("AACTTCC"));
	}
}
