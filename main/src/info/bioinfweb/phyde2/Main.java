
package info.bioinfweb.phyde2;


import java.awt.EventQueue;

import info.bioinfweb.commons.ProgramMainClass;
import info.bioinfweb.commons.appversion.ApplicationType;
import info.bioinfweb.commons.appversion.ApplicationVersion;
import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.phyde2.gui.MainFrame;



public class Main extends ProgramMainClass {
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;
	public static final String APPLICATION_NAME = "PhyDE 2"; 
	
	private static Main firstInstance = null;
	
	//TODO Error report, problem with import of and create URL
	//private SwingErrorReporter errorReporter;
	//public static final String PHYDE2_URL = "http://phyde2.bioinfweb.info/";
	//public static final String ERROR_URL = PHYDE2_URL + "errorreport/ApplicationReport.jsp";
	
	
	/**
	 * Launch the application.
	 */
	public Main(){
		super(new ApplicationVersion(0, 0, 0, 0, ApplicationType.ALPHA));
		//errorReporter = new SwingErrorReporter(ERROR_URL, getVersion());
	}
	
	
	public static Main getInstance() {
		if (firstInstance == null) {
			firstInstance = new Main();
		}
		return firstInstance;
	}
	
	
	private void startApplication() {
		//Thread.setDefaultUncaughtExceptionHandler(getErrorReporter());
		//System.setProperty("apple.laf.useScreenMenuBar", "true");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					//show frame
					window.setVisible(true);
					//window.swingContainer.assignSize();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//customizeSpashScreen();
		//cmdProcessor.process(new CommandLineReader(args));
	}
	
	
	public static void main(String[] args) {
		getInstance().startApplication();	
	}
}
