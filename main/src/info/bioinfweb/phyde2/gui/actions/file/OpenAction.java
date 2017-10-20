package info.bioinfweb.phyde2.gui.actions.file;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

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
import info.bioinfweb.jphyloio.events.type.EventContentType;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.jphyloio.formatinfo.JPhyloIOFormatInfo;
import info.bioinfweb.libralign.model.factory.BioPolymerCharAlignmentModelFactory;
import info.bioinfweb.libralign.model.io.AlignmentDataReader;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class OpenAction extends AbstractFileAction {
	private JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private JFileChooser fileChooser = null;
	
	
	public OpenAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Open..."); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	
	public JFileChooser getOpenFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setAcceptAllFileFilterUsed(true);
			
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

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (handleUnsavedChanges()) {
			try {
				if (getOpenFileChooser().showOpenDialog(getEditor().getFrame()) == JFileChooser.APPROVE_OPTION) {
					JPhyloIOEventReader eventReader = factory.guessReader(getOpenFileChooser().getSelectedFile(), new ReadWriteParameterMap());
					AlignmentDataReader mainReader = new AlignmentDataReader(eventReader, new BioPolymerCharAlignmentModelFactory());
					mainReader.readAll();

					// File does not contain any alignments
					if (mainReader.getAlignmentModelReader().getCompletedModels().size() == 0) {
						JOptionPane.showMessageDialog(getEditor().getFrame(), "The file \"" +  getOpenFileChooser().getSelectedFile().getAbsolutePath() + 
								"\" does not contain any alignments.", "Error while loading file", JOptionPane.ERROR_MESSAGE);
					}
					else {
						// File contains one alignment
						getEditor().getAlignmentArea().setAlignmentModel(mainReader.getAlignmentModelReader().getCompletedModels().get(0), true);
						getEditor().setFile(getOpenFileChooser().getSelectedFile());
						getEditor().setFormat(eventReader.getFormatID());
						getEditor().setChanged(false);

						// File contains more than one alignment --> just the first one was loaded
						if (mainReader.getAlignmentModelReader().getCompletedModels().size() > 1) {
							JOptionPane.showMessageDialog(getEditor().getFrame(),
									"The file contained more than one alignment. Only the first one was loaded.", "Multiple alignments found", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(getEditor().getFrame(), ex.getMessage(), "Error while loading file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
