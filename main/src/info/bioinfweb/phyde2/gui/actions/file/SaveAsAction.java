package info.bioinfweb.phyde2.gui.actions.file;


import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import info.bioinfweb.commons.io.ContentExtensionFileFilter.TestStrategy;
import info.bioinfweb.jphyloio.factory.JPhyloIOReaderWriterFactory;
import info.bioinfweb.phyde2.Main;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class SaveAsAction extends AbstractFileAction{
	private static JPhyloIOReaderWriterFactory factory = new JPhyloIOReaderWriterFactory();
	
	public SaveAsAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Save As..."); 
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
		putValue(Action.ACCELERATOR_KEY, key);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		save();
		
		factory.getFormatInfo(Main.DEFAULT_FORMAT).createFileFilter(TestStrategy.BOTH);
	}
}
