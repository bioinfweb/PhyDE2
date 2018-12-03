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
package info.bioinfweb.phyde2.gui.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;

import info.bioinfweb.libralign.pherogram.PherogramFormats.QualityOutputType;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class DisplayQualityScoresAction  extends AbstractPhyDEAction implements Action{

	public DisplayQualityScoresAction(MainFrame mainframe) {
		super(mainframe);
		putValue(Action.NAME, "Select displayed quality scores...");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 int selection = JOptionPane.showOptionDialog(getMainFrame(), "Select quality scores to be displayed.", "Quality scores", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
				 			QualityOutputType.values(), getMainFrame().getPherogramFormats().getQualityOutputType());
		 
		 if (selection != JOptionPane.CLOSED_OPTION) {
			 getMainFrame().getPherogramFormats().setQualityOutputType(QualityOutputType.values()[selection]);
		 }
		
	}

	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		setEnabled(mainframe.getActiveAlignment() instanceof SingleReadContigAlignmentModel);
		
	}

}
