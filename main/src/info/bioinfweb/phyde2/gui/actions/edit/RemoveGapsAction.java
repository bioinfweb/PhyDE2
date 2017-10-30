package info.bioinfweb.phyde2.gui.actions.edit;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;




@SuppressWarnings("serial")
public class RemoveGapsAction extends AbstractPhyDEAction implements Action {
	public RemoveGapsAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Remove gaps"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('R', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		SelectionModel selection = getMainFrame().getAlignmentArea().getSelection();
		int indexFirstColumn = selection.getFirstColumn();

		for (int row = selection.getFirstRow(); row <= selection.getLastRow(); row++) {
			String id = getMainFrame().getAlignmentArea().getSequenceOrder().idByIndex(row);
			int indexLastColumn = Math.min(selection.getLastColumn(), getMainFrame().getAlignmentArea().getAlignmentModel().getSequenceLength(id) - 1);

			int columnPosition = indexFirstColumn;
			for (int i = indexFirstColumn; i <= indexLastColumn; i++) {				
				if (getMainFrame().getAlignmentArea().getAlignmentModel().getTokenAt(id, columnPosition).equals('-')) {
					getMainFrame().getAlignmentArea().getAlignmentModel().removeTokenAt(id, columnPosition);
				} 
				else {
					columnPosition++;
				}
			}				
		}
	}
}
