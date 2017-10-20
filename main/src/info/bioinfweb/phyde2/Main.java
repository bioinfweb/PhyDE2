package info.bioinfweb.phyde2;



import java.awt.EventQueue;

import javax.swing.JFrame;

import info.bioinfweb.jphyloio.formats.JPhyloIOFormatIDs;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.phyde2.gui.MainFrame;



public class Main extends javax.swing.JFrame{
	public static final String APPLICATION_NAME = "LibrAlign Swing Alignment Editor Demo";
	public static final String APPLICATION_VERSION = "0.0.0";
	public static final String APPLICATION_URL = "http://r.bioinfweb.info/LibrAlignSwingDemoApp";
	public static final String DEFAULT_FORMAT = JPhyloIOFormatIDs.NEXML_FORMAT_ID;	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					//show frame
					window.frame.setVisible(true);
					window.alignmentArea.assignSizeToAll();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
}
