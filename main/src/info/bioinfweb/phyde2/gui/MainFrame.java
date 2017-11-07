package info.bioinfweb.phyde2.gui;


import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.AlignmentModelChangeListener;
import info.bioinfweb.libralign.model.events.SequenceChangeEvent;
import info.bioinfweb.libralign.model.events.SequenceRenamedEvent;
import info.bioinfweb.libralign.model.events.TokenChangeEvent;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.phyde2.gui.actions.ActionManagement;
import info.bioinfweb.phyde2.gui.actions.file.SaveAction;
import info.bioinfweb.tic.SwingComponentFactory;


//TODO: change class header to ... extends Main for remove getInstance method

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final String APPLICATION_NAME = "PhyDE 2";
	public static final String APPLICATION_VERSION = "0.0.0";
	public static final String APPLICATION_URL = "http://r.bioinfweb.info/PhyDE2";
	
	private JPanel jContentPane = null;
	//private JFrame frame;
	
	private ActionManagement actionManagement = new ActionManagement(this);
	private JMenuBar mainMenu = null;
	private JMenu fileMenu = null;
	private JMenu editMenu = null;
	private JMenu helpMenu = null;
	
	// Create actions:
	private SaveAction saveAction = new SaveAction(this);
	
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;
	
	private static MainFrame firstInstance = null;
	
	public AlignmentArea alignmentArea;
	
	private File file = null;
	private boolean changed = false;
	
	
	/**
	 * Create the application.
	 */
	public MainFrame() {
		super();
		initialize();
	}
	
	
	public static MainFrame getInstance() {
		if (firstInstance == null) {
			firstInstance = new MainFrame();
		}
		return firstInstance;
	}
	
	
	public JFrame getFrame() {
		return this;
	}


	public AlignmentArea getAlignmentArea() {
		return alignmentArea;
	}


	public File getFile() {
		return file;
	}


	public void setFile(File file) {
		this.file = file;
		refreshWindowTitle();
	}


	public boolean isChanged() {
		return changed;
	}


	public void setChanged(boolean changed) {
		this.changed = changed;
		refreshWindowTitle();
	}

	
	public ActionManagement getActionManagement() {
		return actionManagement;
	}
	
	
	private void refreshWindowTitle() {
		StringBuilder title = new StringBuilder();
		title.append(APPLICATION_NAME);
		title.append(" - ");
		if (isChanged()) {
			title.append("*");
		}
		if (getFile() != null) {
			title.append(getFile().getAbsolutePath());
		}
		else {
			title.append("Unsaved");
		}
		setTitle(title.toString());
	}

	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		setBounds(100, 100, 450, 300);
		setExtendedState(JFrame.NORMAL);
		setContentPane(getJContentPane());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(saveAction.handleUnsavedChanges()) {
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				}
				else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);  // Workaround since AboutDialog hinders application termination for a currently unknown reason.
			}
		});
		refreshWindowTitle();
		
		// Create LibrAlign component instance:
		alignmentArea = new AlignmentArea();
		alignmentArea.setAlignmentModel(new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)), false);
		
		// Register changes listener to know when to ask for saving changes:
		alignmentArea.getAlignmentModel().getChangeListeners().add(new AlignmentModelChangeListener() {
			@Override
			public <T> void afterTokenChange(TokenChangeEvent<T> e) {
				setChanged(true);
			}
			
			@Override
			public <T> void afterSequenceRenamed(SequenceRenamedEvent<T> e) {
				setChanged(true);
			}
			
			@Override
			public <T> void afterSequenceChange(SequenceChangeEvent<T> e) {
				setChanged(true);
			}
			
			@Override
			public <T, U> void afterProviderChanged(AlignmentModel<T> previous, AlignmentModel<U> current) {}
		});
		
		setJMenuBar(getMainMenu());
		
		// Create instance specific to Swing:
		JComponent swingAlignmentArea = SwingComponentFactory.getInstance().getSwingComponent(alignmentArea);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(swingAlignmentArea, BorderLayout.CENTER);
	}
	
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
		}
		return jContentPane;
	}
	
		
	private JMenuBar getMainMenu() {
		if (mainMenu == null) {
			mainMenu = new JMenuBar();
			mainMenu.add(getFileMenu());
			mainMenu.add(getEditMenu());
			mainMenu.add(getHelpMenu());
		}
		return mainMenu;
	}
	
	
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getActionManagement().get("edit.addSequence"));
			editMenu.add(getActionManagement().get("edit.deleteSequence"));
			editMenu.add(getActionManagement().get("edit.removeGaps"));
		}
		return editMenu;
	}
	
	
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getActionManagement().get("file.new"));
			fileMenu.add(getActionManagement().get("file.open"));
			fileMenu.add(getActionManagement().get("file.save"));
			fileMenu.add(getActionManagement().get("file.saveAs"));
			fileMenu.add(getActionManagement().get("file.export"));
		}
		return fileMenu;
	}
	
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getActionManagement().get("help.about"));
			}
		return helpMenu;
	}

}
