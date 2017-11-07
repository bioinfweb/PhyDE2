package info.bioinfweb.phyde2.gui.actions.file;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class SaveAction extends AbstractFileAction{
	
	public SaveAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Save"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (getMainFrame().getFile() == null) {
			save();
		}
		else {
			writeFile();
		}
	}
}
