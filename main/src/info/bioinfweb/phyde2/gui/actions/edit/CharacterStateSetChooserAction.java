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


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import info.bioinfweb.commons.bio.CharacterStateSetType;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.libralign.model.tokenset.TokenSet;
import info.bioinfweb.phyde2.document.AlignmentType;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;
import info.bioinfweb.phyde2.gui.dialogs.CharacterStateSetChooserDialog;




public class CharacterStateSetChooserAction extends AbstractPhyDEAction implements Action{
	
	public CharacterStateSetChooserAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Change character tokens"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "Change character tokens");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		getMainFrame().getActiveAlignment().getEditRecorder().endEdit("User edits");
		CharacterStateSetChooserDialog dialog = CharacterStateSetChooserDialog.getInstance(MainFrame.getInstance());
		TokenSet<?> oldTokenSet = MainFrame.getInstance().getActiveAlignmentArea().getAlignmentModel().getTokenSet();
		dialog.setChangedType(oldTokenSet.getType());
		
		
		if (dialog.execute()){

			TokenSet<Character> tokenSet;
			switch (dialog.getChangedType()) {
			case DNA:
				tokenSet = CharacterTokenSet.newDNAInstance(true);
				break;
			case RNA:
				tokenSet = CharacterTokenSet.newRNAInstance(true);
				break;
			case NUCLEOTIDE:
				tokenSet = CharacterTokenSet.newNucleotideInstance(true);
				break;
			default: 
				tokenSet = CharacterTokenSet.newAminoAcidInstance(true);  
				break;
			}

			HashSet<Character> conflictingTokens = new HashSet<>();

			
			AlignmentModel<Character> currentAlignment = MainFrame.getInstance().getActiveAlignment().getAlignmentModel();
			PhyDE2AlignmentModel activeAlignment = MainFrame.getInstance().getActiveAlignment();
			
			
			
			for (Iterator<String> it = currentAlignment.sequenceIDIterator(); it.hasNext();) {
				String sequenceID = it.next();
				int sequencelength = currentAlignment.getSequenceLength(sequenceID);
				for (int x = 0; x < sequencelength; x++) {
					if (!tokenSet.contains(currentAlignment.getTokenAt(sequenceID, x))) {
						conflictingTokens.add(currentAlignment.getTokenAt(sequenceID, x));
					}
				}
			}
			
			boolean replaceU = false;
			boolean replaceT = false;
			
			if (oldTokenSet.getType().isNucleotide() && tokenSet.getType().equals(CharacterStateSetType.DNA) && conflictingTokens.contains('U')) {
				replaceU = true;
			}
			else if (oldTokenSet.getType().isNucleotide() && tokenSet.getType().equals(CharacterStateSetType.RNA) && conflictingTokens.contains('T')) {
				replaceT = true;
			}
			
			
			if (!conflictingTokens.isEmpty()) {
				String[] options = new String[3];
				if (replaceU) {
				options[0] = "Replace and convert";
				options[1] = "Do not convert U to T";
				options[2] = "Cancel";
				}
				else if (replaceT) {
					options[0] = "Replace and convert";
					options[1] = "Do not convert T to U";
					options[2] = "Cancel";
				} 
				else {
					options[0] = "Replace with ?";
					options[1] = "";
					options[2] = "Cancel";
				}
				
				int option = JOptionPane.showOptionDialog(null, "The following tokens are not in the selected character token set but are present in the alignment:\n " + conflictingTokens + "\n" + "Replace them with a questionmark?",
		                "Choose an option",
		                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
				if (option == 1) {
					replaceU = false;
					replaceT = false;
				}
				if (option == 0 || option == 1) {
					for (Iterator<String> it = currentAlignment.sequenceIDIterator(); it.hasNext();) { 
						String sequenceID = it.next();
						int sequencelength = currentAlignment.getSequenceLength(sequenceID);
						for (int x = 0; x < sequencelength; x++) {
							if ((conflictingTokens.contains(currentAlignment.getTokenAt(sequenceID, x)))) {
								if (currentAlignment.getTokenAt(sequenceID, x).equals('U') && replaceU) {
									currentAlignment.setTokenAt(sequenceID, x, 'T');
								}
								else if (currentAlignment.getTokenAt(sequenceID, x).equals('T') && replaceT){
									currentAlignment.setTokenAt(sequenceID, x, 'U');
								}
								else {
									currentAlignment.setTokenAt(sequenceID, x, '?');
								}
							}
						}
					}
					createNewAlignmentModel(currentAlignment, tokenSet, activeAlignment);
					//currentAlignment.setTokenSet(tokenSet); // TODO: This class does not support changing the token set during runtime
				}
			}
			else {
				//currentAlignment.setTokenSet(tokenSet);
			}
		}
		getMainFrame().getActiveAlignment().getEditRecorder().startEdit();
	}

	//TODO: replace conflicting tokens here so that the new Alignment gets the right tokens
	private void createNewAlignmentModel(AlignmentModel<Character> currentAlignment, TokenSet<Character> tokenSet, PhyDE2AlignmentModel activeAlignment) {
		AlignmentModel<Character> newAlignment = null;
		PackedAlignmentModel<Character> newModel = new PackedAlignmentModel<Character>(tokenSet);
		
		if (activeAlignment.getType().equals(AlignmentType.DEFAULT)) {
			DefaultPhyDE2AlignmentModel model = new DefaultPhyDE2AlignmentModel (getMainFrame().getSelectedDocument(), newModel, new CharSetDataModel(newModel)); //TODO: save the model so it can be set later
			newAlignment = model.getAlignmentModel();
		}
		else if (activeAlignment.getType().equals(AlignmentType.SINGLE_READ_CONTIG)){
			SingleReadContigAlignmentModel model = new SingleReadContigAlignmentModel(getMainFrame().getSelectedDocument(), newModel, new CharSetDataModel(newModel), null); //TODO: save the model so it can be set later
			newAlignment = model.getAlignmentModel();
		}
		
		for (Iterator<String> it = currentAlignment.sequenceIDIterator(); it.hasNext();) {
			String sequenceID = it.next();
			newAlignment.addSequence("seq: " + sequenceID.toString(), sequenceID); //TODO: Sequence name can't be set which causes errors
			System.out.println("Sequence name: " + newAlignment.sequenceNameByID(sequenceID));
			
			int sequencelength = currentAlignment.getSequenceLength(sequenceID);
			for (int x = 0; x < sequencelength; x++) {
				newAlignment.appendToken(sequenceID, currentAlignment.getTokenAt(sequenceID, x), true);
				
			}//TODO: Need to think of a way to save storage (like removing the old sequence) that works with the iterator
			//currentAlignment.removeSequence(sequenceID);
		}
		//getMainFrame().getActiveAlignmentArea().setAlignmentModel(newAlignment); //TODO: Doesn't work yet, revisit later after undo revisions. Use the right alignment model 
	}
	
	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((document != null) && (mainframe.getActiveAlignmentArea() != null));
	}

}
