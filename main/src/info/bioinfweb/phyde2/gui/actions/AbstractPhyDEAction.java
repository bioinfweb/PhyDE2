package info.bioinfweb.phyde2.gui.actions;


import javax.swing.AbstractAction;

import info.bioinfweb.phyde2.gui.MainFrame;



@SuppressWarnings("serial")
public abstract class AbstractPhyDEAction extends AbstractAction {
	private MainFrame mainframe;


	public AbstractPhyDEAction(MainFrame mainframe) {
		super();
		this.mainframe = mainframe;
	}	
	
	
	protected MainFrame getMainFrame() {
		return mainframe;
	}
}