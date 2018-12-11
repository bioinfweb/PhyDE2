/*
 * PhyDE 2 - An alignment editor for phylogenetic purposes
 * Copyright (C) 2017  Ben Stöver, Jonas Bohn, Kai Müller
 * <http://bioinfweb.info/PhyDE2>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package info.bioinfweb.phyde2.gui.actions.edit;


import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.biojava.bio.chromatogram.Chromatogram;
import org.biojava.bio.chromatogram.ChromatogramFactory;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;

import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.libralign.pherogram.provider.BioJavaPherogramProvider;
import info.bioinfweb.libralign.pherogram.provider.PherogramProvider;
import info.bioinfweb.phyde2.document.DefaultPhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.PherogramProviderByURL;
import info.bioinfweb.phyde2.document.PherogramReference;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.document.undo.edit.AddSequenceEdit;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;
import info.bioinfweb.phyde2.gui.dialogs.AddSingleReadDialog;




@SuppressWarnings("serial")
public class AddSequenceAction extends AbstractPhyDEAction implements Action {
	
	public AddSequenceAction(MainFrame mainFrame) {
		super(mainFrame);
		putValue(Action.NAME, "Add sequence"); 
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "Add sequence");
		loadSymbols("Add");
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (getMainFrame().getActiveAlignment() instanceof DefaultPhyDE2AlignmentModel) {
			String name = JOptionPane.showInputDialog(getMainFrame(), "New sequence name");
			if (name != null) {
				//getMainFrame().getActiveAlignment().getAlignmentModel().addSequence(name);		
				getMainFrame().getActiveAlignment().executeEdit(new AddSequenceEdit(getMainFrame().getActiveAlignment(), name, null));
			}
		}
		
		else if (getMainFrame().getActiveAlignment() instanceof SingleReadContigAlignmentModel){
			AddSingleReadDialog dialog = new AddSingleReadDialog(getMainFrame());
			if (dialog.execute()) {
				if ((dialog.getSelectedURL() != null) && (!"".equals(dialog.getSelectedURL()))) {
					try {
						
						URL url = new URL(dialog.getSelectedURL());
						PherogramAreaModel pherogramModel = new PherogramAreaModel(PherogramProviderByURL.getInstance().getPherogramProvider(url));
						getMainFrame().getActiveAlignment().executeEdit(new AddSequenceEdit(getMainFrame().getActiveAlignment(), dialog.getSequenceName(), 
								new PherogramReference(pherogramModel, url)));
					} 
					catch (MalformedURLException e1) {
						JOptionPane.showMessageDialog(getMainFrame(), "Problems with URL!");
						e1.printStackTrace();
					} 
					catch (UnsupportedChromatogramFormatException e1) {
						
						e1.printStackTrace();
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(getMainFrame(), "Problems with URL!");
						e1.printStackTrace();
						//TODO: maybe more information for the user?
					}
				}
				
				else{
					getMainFrame().getActiveAlignment().executeEdit(new AddSequenceEdit(getMainFrame().getActiveAlignment(), dialog.getSequenceName(), null));
				}
			}
		}
	}


	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled((document != null) && (mainframe.getActiveAlignmentArea() != null));
	}
	
	
}
