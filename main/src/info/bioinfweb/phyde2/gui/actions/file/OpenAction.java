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
package info.bioinfweb.phyde2.gui.actions.file;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.commons.collections4.set.ListOrderedSet;

import info.bioinfweb.commons.io.ContentExtensionFileFilter;
import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.commons.io.ExtensionFileFilter;
import info.bioinfweb.jphyloio.JPhyloIOEventReader;
import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.ReadWriteParameterNames;
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetDataModel;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.io.DataElementKey;
import info.bioinfweb.phyde2.document.AlignmentType;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.io.AlignmentTypeDataModel;
import info.bioinfweb.phyde2.document.io.ConsensusSequenceDataModel;
import info.bioinfweb.phyde2.document.io.ContigReferenceDataModel;
import info.bioinfweb.phyde2.document.io.IOConstants;
import info.bioinfweb.phyde2.document.io.PhyDEAlignmentDataReader;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class OpenAction extends AbstractFileAction {
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private JFileChooser fileChooser = null;
	
	
	public OpenAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Open..."); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		putValue(Action.SHORT_DESCRIPTION, "Open"); 
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		loadSymbols("Open");
	}

	//TODO Fix Bug: cant open NexML files because there are some unreadable changes after writing NexML files
	public JFileChooser getOpenFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(true);
			
			//TODO Unterscheiden nach input Dateiformaten --> Nexus, FASTA, Phylip = import, ungespeichert --> Nexml = öffnen, gespeichert
			ListOrderedSet<String> validExtensions = new ListOrderedSet<String>();
			for (String formatID : factory.getFormatIDsSet()) {
				JPhyloIOFormatInfo info = factory.getFormatInfo(formatID);
				if (info.isElementModeled(EventContentType.ALIGNMENT, true)) {
					ContentExtensionFileFilter filter = info.createFileFilter(TestStrategy.BOTH);
					validExtensions.addAll(filter.getExtensions());
					fileChooser.addChoosableFileFilter(filter);
				}
			}
			ExtensionFileFilter allFormatsFilter = new ExtensionFileFilter("All supported formats", false, validExtensions.asList());
			fileChooser.addChoosableFileFilter(allFormatsFilter);
			fileChooser.setFileFilter(allFormatsFilter);
		}
		return fileChooser;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		String message = new String();
		//	File reading:
		try {
			if (getOpenFileChooser().showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) {
				ReadWriteParameterMap parameters = new ReadWriteParameterMap();
				parameters.put(ReadWriteParameterNames.KEY_OBJECT_TRANSLATOR_FACTORY, createTranslatorFactory());
				JPhyloIOEventReader eventReader = factory.guessReader(getOpenFileChooser().getSelectedFile(), parameters);
				PhyDEAlignmentDataReader mainReader = new PhyDEAlignmentDataReader(eventReader);
				
				mainReader.readAll();
				
				if (mainReader.getAlignmentModelReader().getCompletedModels().size() == 0) {  // File does not contain any alignments.
					JOptionPane.showMessageDialog(getMainFrame(), "The file \"" +  getOpenFileChooser().getSelectedFile().getAbsolutePath() + 
							"\" does not contain any alignments.", "Error while loading file", JOptionPane.ERROR_MESSAGE);
				}
				else {  // File contains at least one alignment.
					//	create new Document:
					Document document = new Document();
					getMainFrame().getDocumentList().add(document); // Workaround - Listener of FileContentTreeView has to be updated to add all alignments already belonging to the document to the tree
					for (AlignmentModel<?> alignmentModel : mainReader.getAlignmentModelReader().getCompletedModels()) {
						Collection<CharSetDataModel> charSetModels = mainReader.getCharSetReader().getCompletedElements().get(new DataElementKey(alignmentModel.getID()));
						CharSetDataModel charSetModel;
						PhyDE2AlignmentModel newAlignment = null;
	
						if (charSetModels.isEmpty()) {
							charSetModel = new CharSetDataModel(alignmentModel);
						}
						else {
							charSetModel = charSetModels.iterator().next();
						}
						if (charSetModels.size() > 1) {
							message = "The file contained more than one set of character sets for the loaded alignment. Only the first one was loaded.";
						}
						
						Collection<AlignmentTypeDataModel> alignmentTypeModels = 
								mainReader.getAlignmentTypeReader().getCompletedElements().get(new DataElementKey(alignmentModel.getID()));

						AlignmentType alignmentType = AlignmentType.DEFAULT;
						if (!alignmentTypeModels.isEmpty()) {
							alignmentType = AlignmentType.valueOf(alignmentTypeModels.iterator().next().getAlignmentType());
						}
						
						switch (alignmentType) {
						case SINGLE_READ_CONTIG:
							newAlignment = new SingleReadContigAlignmentModel(document, (AlignmentModel<Character>)alignmentModel, charSetModel, null);
							Iterator<String> ids =  newAlignment.getAlignmentModel().sequenceIDIterator();
							while (ids.hasNext()) {
								String id = ids.next();
								Collection<PherogramReference> pherogramReferences = 
										mainReader.getPherogramEventReader().getCompletedElements().get(new DataElementKey(newAlignment.getAlignmentModel().getID(), id));
								Iterator<PherogramReference> references = pherogramReferences.iterator();
								while (references.hasNext()) {
									((SingleReadContigAlignmentModel) newAlignment).addPherogram(id, references.next());
								}
							}	
							
							Iterator<String> iDs =  ((SingleReadContigAlignmentModel)newAlignment).getAlignmentModel().sequenceIDIterator();
							while (iDs.hasNext()) {	
								String consensusID = iDs.next();								
								Collection<ConsensusSequenceDataModel> consensusSequences =
										mainReader.getConsensusSequenceReader().getCompletedElements().get(new DataElementKey(newAlignment.getAlignmentModel().getID(), consensusID));

								ConsensusSequenceDataModel consensusSequenceID;								
								if (!consensusSequences.isEmpty()) {
									consensusSequenceID = consensusSequences.iterator().next();
									for (int tokenIndex = 0; tokenIndex < consensusSequenceID.getAlignmentModel().getMaxSequenceLength(); tokenIndex++) {
										((SingleReadContigAlignmentModel)newAlignment).getConsensusModel().appendToken(((SingleReadContigAlignmentModel)newAlignment).getConsensusSequenceID(),
												newAlignment.getAlignmentModel().getTokenAt(consensusID, tokenIndex), true);   
									}
									newAlignment.getAlignmentModel().removeSequence(consensusSequenceID.getAlignmentModel().getID());	
								}
								if (consensusSequences.size() > 1) {
									message = "The file contained more than one consensus sequences. Only the first one was loaded.";
								}
							}
							break;
											
							case DEFAULT:
								newAlignment = new DefaultPhyDE2AlignmentModel(document, (AlignmentModel<Character>)alignmentModel, charSetModel);
								Iterator<String> idS =  newAlignment.getAlignmentModel().sequenceIDIterator();
								while (idS.hasNext()) {
									String id = idS.next();
									
									Collection<ContigReferenceDataModel> contigReferences =
											mainReader.getContigReferenceReader().getCompletedElements().get(new DataElementKey(newAlignment.getAlignmentModel().getID(), id));
						
									if (!contigReferences.isEmpty()) {
										ContigReferenceDataModel contigReference = contigReferences.iterator().next();
//										((DefaultPhyDE2AlignmentModel)newAlignment).addContigReference(contigReference.getContigReference(), id);
										}
									if (contigReferences.size() > 1) {
										message = "The file contained more than one contig references for the loaded sequence. Only the first one was loaded.";
									}
								}
							break;
							
							default:
								// TODO: String builder for message that contains all the alignments that did not belong to one of the two categories
								break;
						}
						
						if (newAlignment != null) {
							if (newAlignment.getAlignmentModel().getLabel() == null) {
								newAlignment.getAlignmentModel().setLabel("newAlignment");
							}
							document.addAlignmentModel(newAlignment);
							getMainFrame().showAlignment(newAlignment);
							getMainFrame().getActiveAlignment().setChanged(false);
						}
					}
						
					if (eventReader.getFormatID().equals(MainFrame.DEFAULT_FORMAT) && (IOConstants.FORMAT_VERSION.equals(mainReader.getFormatVersion()))) {
						getMainFrame().getDocumentList().get(getMainFrame().getDocumentList().size()-1).setFile(getOpenFileChooser().getSelectedFile());
					}
					if (eventReader.getFormatID().equals(MainFrame.SINGLE_READ_FORMAT) && (IOConstants.FORMAT_VERSION.equals(mainReader.getFormatVersion()))) {
						getMainFrame().getDocumentList().get(getMainFrame().getDocumentList().size()-1).setFile(getOpenFileChooser().getSelectedFile());
					}
					
					if (message.length() > 0) {
						JOptionPane.showMessageDialog(getMainFrame(),
								message, "Multiple data sets found", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		}
		catch (Exception ex) {
			//TODO: Throws Exception while opening a NexML file: "The symbol("-")of a standard data token definition must be of type Integer."
			ex.printStackTrace();
			JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage(), "Error while loading file", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {}
}
