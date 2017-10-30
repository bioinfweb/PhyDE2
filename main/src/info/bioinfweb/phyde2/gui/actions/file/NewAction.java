package info.bioinfweb.phyde2.gui.actions.file;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import info.bioinfweb.libralign.model.AlignmentModel;
import info.bioinfweb.libralign.model.implementations.PackedAlignmentModel;
import info.bioinfweb.libralign.model.tokenset.CharacterTokenSet;
import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public class NewAction extends AbstractFileAction {
	public NewAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "New"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	
	public static AlignmentModel<Character> createAlignmentModel() {
		return new PackedAlignmentModel<Character>(CharacterTokenSet.newNucleotideInstance(true));
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (handleUnsavedChanges()) {
			getMainFrame().getAlignmentArea().setAlignmentModel(createAlignmentModel(), true);
			getMainFrame().setFile(null);
			getMainFrame().setFormat(MainFrame.DEFAULT_FORMAT);
			getMainFrame().setChanged(false);
		}
	}
}
