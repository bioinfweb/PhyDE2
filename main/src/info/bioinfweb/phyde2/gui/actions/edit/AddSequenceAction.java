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


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;

import info.bioinfweb.libralign.pherogram.provider.PherogramProvider;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PherogramProviderByURL;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.AddSequenceEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;
import info.bioinfweb.phyde2.gui.dialogs.AddSequenceDialog;
import info.bioinfweb.phyde2.gui.dialogs.AddSingleReadDialog;



@SuppressWarnings("serial")
public class AddSequenceAction extends AbstractPhyDEAction implements Action {
	private URL url;
	private PherogramProvider pherogramProvider;
	private PherogramReference pherogramReference = null;
	private SingleReadContigAlignmentModel contigReference;
	private String sequenceID = null;
	private String seqName = null;
	
	public AddSequenceAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Add sequence"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "Add sequence");
		loadSymbols("Add");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (getMainFrame().getActiveAlignment() instanceof DefaultPhyDE2AlignmentModel) {
			DefaultPhyDE2AlignmentModel model = (DefaultPhyDE2AlignmentModel) getMainFrame().getActiveAlignment();
			AddSequenceDialog dialog = new AddSequenceDialog(getMainFrame());
			if (dialog.execute()){
				contigReference = dialog.getSelectedConitgModel();
				seqName = dialog.getSequenceName();
				try {
					doEdit(seqName);
					//model.executeEdit(new AddSequenceEdit(model, dialog.getSequenceName(), null, contigReference)); //when gone there is an error with UnsupportedChromatogramFormatException
				} 
				catch (UnsupportedChromatogramFormatException | IOException ex) {
					throw new InternalError(ex);  // This should not happen since no pherogram is loaded.
				}
			}
		}
		
		else if (getMainFrame().getActiveAlignment() instanceof SingleReadContigAlignmentModel) {
			AddSingleReadDialog dialog = new AddSingleReadDialog(getMainFrame());
			if (dialog.execute()) {
				if ((dialog.getSelectedURL() != null) && (!"".equals(dialog.getSelectedURL()))) {
					try {
						url = new URL(dialog.getSelectedURL());
						pherogramProvider = PherogramProviderByURL.getInstance().getPherogramProvider(url);
						seqName = dialog.getSequenceName();
						doEdit(seqName);
					
//						getMainFrame().getActiveAlignment().executeEdit(new AddSequenceEdit(
//								getMainFrame().getActiveAlignment(), dialog.getSequenceName(), url, null)); //when gone there is an error with UnsupportedChromatogramFormatException
					}
					catch (IOException ex) {
						JOptionPane.showMessageDialog(getMainFrame(), "The following error occured when trying to load the pherogram from \"" + dialog.getSelectedURL() 
								+ "\": \"" + ex.getLocalizedMessage() + "\".", "Error when loading pherogram", JOptionPane.ERROR_MESSAGE);
					}
					catch (UnsupportedChromatogramFormatException ex) {
						JOptionPane.showMessageDialog(getMainFrame(), "The format of the pherogram to be loaded from \"" + dialog.getSelectedURL() + "\" is not known.", 
								"Unsupported pherogram format", JOptionPane.ERROR_MESSAGE);
					}
				}
				
				else {
					try {
						seqName = dialog.getSequenceName();
						doEdit(seqName);
						//getMainFrame().getActiveAlignment().executeEdit(new AddSequenceEdit(getMainFrame().getActiveAlignment(), dialog.getSequenceName(), null, null));
					} 
					catch (UnsupportedChromatogramFormatException | IOException ex) {
						throw new InternalError(ex);  // This should not happen since no pherogram is loaded.
					}
				}
			}
		}
	}

	private void doEdit(String sequenceName) throws UnsupportedChromatogramFormatException, IOException {
		PhyDE2AlignmentModel model = getMainFrame().getActiveAlignment();
		model.getEditRecorder().startEdit();
		int seqNumber = model.getAlignmentModel().getSequenceCount();
		if (sequenceID == null) {
			sequenceID = model.getAlignmentModel().addSequence(sequenceName);
			if (pherogramProvider != null) {  // New sequence does not have an attached pherogram.
				pherogramReference = new PherogramReference(model.getAlignmentModel(), pherogramProvider, url, sequenceID);  // PherogramReference cannot be created before, since the sequenceID is not known. The provider cannot be created here, since that might throw an exception that cannot be caught.
			}
		}
		else {
			model.getAlignmentModel().addSequence(sequenceName);
		}
		if ((model instanceof SingleReadContigAlignmentModel) && (pherogramReference != null)) {
			for (int j = 0; j < pherogramReference.getPherogramProvider().getSequenceLength(); j++) {
				model.getAlignmentModel().appendToken(sequenceID, pherogramReference.getPherogramProvider().getBaseCall(j), true);
						//TODO Will the pherogram now be distorted, since interaction was recently moved to the models? This would have to be avoided. (Implementation of edits will anyway be refactored, though.)
			}
			((SingleReadContigAlignmentModel) model).addPherogram(sequenceID, pherogramReference);
		}
		
		else if ((model instanceof DefaultPhyDE2AlignmentModel) && (contigReference != null)) {
			((DefaultPhyDE2AlignmentModel) model).addConsensus(contigReference, sequenceID);
		}
		model.getEditRecorder().endEdit(getPresentationName());
	}
	
	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((document != null) && (mainframe.getActiveAlignmentArea() != null));
	}
	
	
	private String getPresentationName() {
		StringBuilder result = new StringBuilder();
		result.append("Sequence ");
		result.append(seqName);
		result.append(" added to alignment ");
		result.append(getMainFrame().getActiveAlignment());
		result.append(".");
		
		return result.toString();
	}
}
