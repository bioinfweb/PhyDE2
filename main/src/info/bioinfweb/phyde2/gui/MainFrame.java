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
import info.bioinfweb.libralign.alignmentarea.tokenpainter.NucleotideTokenPainter;
import info.bioinfweb.libralign.dataarea.implementations.sequenceindex.SequenceIndexArea;
import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.AlignmentModelChangeListener;
import info.bioinfweb.libralign.model.events.SequenceChangeEvent;
import info.bioinfweb.libralign.model.events.SequenceRenamedEvent;
import info.bioinfweb.libralign.model.events.TokenChangeEvent;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.libralign.multiplealignments.MultipleAlignmentsContainer;
import info.bioinfweb.phyde2.gui.actions.ActionManagement;
import info.bioinfweb.phyde2.gui.actions.file.SaveAction;
import info.bioinfweb.tic.SwingComponentFactory;


//TODO: change class header to ... extends Main for remove getInstance method

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	public static final String APPLICATION_NAME = "PhyDE 2";
	public static final String APPLICATION_VERSION = "0.0.0";
	public static final String APPLICATION_URL = "http://bioinfweb.info/PhyDE2";
	
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
	
	AlignmentArea mainArea = null;
	public MultipleAlignmentsContainer container = null;
	
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


	public MultipleAlignmentsContainer getMSAContainer() {
		return container;
	}
	
	
	public AlignmentArea getAlignmentArea() {
		return mainArea;
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

//	protected MultipleAlignmentsContainer createContainer() {
//		// Create main container instance (TIC component):
//		MultipleAlignmentsContainer container = new MultipleAlignmentsContainer();
//		
//		// Create head and main AlignmentArea:
//		AlignmentArea headArea = new AlignmentArea(container);
//		AlignmentArea mainArea = new AlignmentArea(container);
//		
//		// Prepare heading area:
//		headArea.getDataAreas().getTopAreas().add(new SequenceIndexArea(headArea.getContentArea(), mainArea));
//		container.getAlignmentAreas().add(headArea);
//		container.getAlignmentAreas().add(mainArea);
//		
//		// Prepare main area:
//		mainArea.setAlignmentModel(new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)), false);
//		mainArea.getPaintSettings().getTokenPainterList().set(0, new NucleotideTokenPainter());  // Define how sequences shall be painted
//		container.getAlignmentAreas().add(mainArea);
//		
//		return container;
//	}
	
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
		
		// Create main container instance (TIC component):
		container = new MultipleAlignmentsContainer();
		
		// Create head and main AlignmentArea:
		AlignmentArea headArea = new AlignmentArea(container);
		mainArea = new AlignmentArea(container);
		
		// Prepare heading area:
		headArea.getDataAreas().getTopAreas().add(new SequenceIndexArea(headArea.getContentArea(), mainArea));
		container.getAlignmentAreas().add(headArea);
		container.getAlignmentAreas().add(mainArea);
		
		// Prepare main area:
		mainArea.setAlignmentModel(new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true)), false);
		mainArea.getPaintSettings().getTokenPainterList().set(0, new NucleotideTokenPainter());  // Define how sequences shall be painted
		container.getAlignmentAreas().add(mainArea);
		
		// Create Swing-specific component from TIC component:
		JComponent swingContainer = SwingComponentFactory.getInstance().getSwingComponent(container);
		
		// Register changes listener to know when to ask for saving changes:
		mainArea.getAlignmentModel().getChangeListeners().add(new AlignmentModelChangeListener() {
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
		
		
		// Create Swing-specific component from TIC component:
		swingContainer = SwingComponentFactory.getInstance().getSwingComponent(container);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		// Add Swing component to GUI:
		getContentPane().add(swingContainer, BorderLayout.CENTER);
		
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
			helpMenu.add(getActionManagement().get("help.index"));
			helpMenu.add(getActionManagement().get("help.contents"));
			helpMenu.add(getActionManagement().get("help.twitter"));
			//helpMenu.setIcon(new ImageIcon(Object.class.getResource("/resources/symbols/Help16.png")));
			}
		return helpMenu;
	}

}
