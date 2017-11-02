package info.bioinfweb.phyde2.gui.actions.file;


import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import info.bioinfweb.jphyloio.ReadWriteParameterMap;
import info.bioinfweb.jphyloio.factory.JPhyloIOContentExtensionFileFilter;
import info.bioinfweb.libralign.model.io.IOTools;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;




@SuppressWarnings("serial")
public abstract class AbstractFileAction extends AbstractPhyDEAction {
//	private static JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	private static JFileChooser fileChooser = null;
	
	
	
	public AbstractFileAction(MainFrame mainframe) {
		super(mainframe);
	}
	
	
	protected static JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser() {
			    @Override
			    public void approveSelection(){
			        File f = getSelectedFile();
			        JPhyloIOContentExtensionFileFilter filter = (JPhyloIOContentExtensionFileFilter)getFileFilter();
				        if (!filter.accept(f)) {
				        	f = new File(f.getAbsolutePath() + "." + filter.getDefaultExtension());
				        	setSelectedFile(f);
				        }
			        if(f.exists()) {
			            switch(JOptionPane.showConfirmDialog(this, "The file already exists. Do you want to overwrite?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION)) {
			                case JOptionPane.YES_OPTION:
			                    super.approveSelection();
			                    return;
			                case JOptionPane.NO_OPTION:
			                    return;
			                case JOptionPane.CLOSED_OPTION:
			                    return;
			                case JOptionPane.CANCEL_OPTION:
			                    cancelSelection();
			                    return;
			            }
			        }
			        super.approveSelection();
			    }
			};
		} 
			fileChooser.setAcceptAllFileFilterUsed(false);
			System.out.println(fileChooser.getAcceptAllFileFilter());
	
		return fileChooser;
	}
	
	
	protected void writeFile() {
		writeFile(getMainFrame().getFile(), Main.DEFAULT_FORMAT);
	}
	
	
	protected void writeFile(File file, String formatID) {
		try {
			ReadWriteParameterMap parameters = new ReadWriteParameterMap();
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_NAME, MainFrame.APPLICATION_NAME);
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_VERSION, MainFrame.APPLICATION_VERSION);
			parameters.put(ReadWriteParameterMap.KEY_APPLICATION_URL, MainFrame.APPLICATION_URL);
			
			IOTools.writeSingleAlignment(getMainFrame().getAlignmentArea().getAlignmentModel(), null, file, formatID, parameters);
			// Note that files containing multiple alignments or additional trees or OTU lists would be overwritten with a single alignment file here. 
			// This problem is not handles here, to keep this example simple
		}
		catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(getMainFrame().getFrame(), ex.getMessage(), "Error while writing file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	protected void export() {
			writeFile(getMainFrame().getFile(), getMainFrame().getFormat());
	}
	
	
	protected void save() {
			writeFile();
			getMainFrame().setChanged(false);
	}
	
	
	public boolean handleUnsavedChanges() {
		if (getMainFrame().isChanged()) {
            switch (JOptionPane.showConfirmDialog(getMainFrame().getFrame(), "There are unsaved changes. Do you want to save the changes?", 
            		"Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION)) {
	            case JOptionPane.YES_OPTION:
	            	save();
	            	return false;
	            case JOptionPane.NO_OPTION:
	            	return true;
            }
    		return false;
		}
		else {
			return true;
		}
	}
}

