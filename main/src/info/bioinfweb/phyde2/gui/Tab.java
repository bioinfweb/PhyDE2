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
package info.bioinfweb.phyde2.gui;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import info.bioinfweb.commons.events.GenericEventObject;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionListener;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetArea;
import info.bioinfweb.libralign.dataarea.implementations.sequenceindex.SequenceIndexArea;
import info.bioinfweb.libralign.multiplealignments.MultipleAlignmentsContainer;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.tic.SwingComponentFactory;


@SuppressWarnings("serial")
public class Tab extends JPanel {
	private PhyDE2AlignmentModel alignmentModel = null;
	private AlignmentArea mainArea = null;
	private MultipleAlignmentsContainer container = null;
	private AlignmentArea sequenceIndexAlignmentArea = null;
	private AlignmentArea characterSetAlignmentArea = null;
	private CharSetArea charSetArea = null;
	
	
	public CharSetArea getCharSetArea() {
		return charSetArea;
	}
	
	
	public AlignmentArea getAlignmentArea() {
		return mainArea;
	}
	
	
	public PhyDE2AlignmentModel getAlignmentModel() {
		return alignmentModel;
	}
	
	
	public Tab(PhyDE2AlignmentModel alignmentModel) {
		super();
		this.alignmentModel = alignmentModel;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JComponent alignmentsContainer = SwingComponentFactory.getInstance().getSwingComponent(getAlignmentsContainer());
		add(alignmentsContainer);	
	}
	
	
	protected MultipleAlignmentsContainer getAlignmentsContainer() {
		if (container == null) { 
			// Create main container instance (TIC component):
			container = new MultipleAlignmentsContainer(MainFrame.getInstance().getEditSettings());
					
			// out head and main AlignmentArea in container:
			sequenceIndexAlignmentArea = new AlignmentArea(container);
			characterSetAlignmentArea = new AlignmentArea(container);
			mainArea = new AlignmentArea(container);
			charSetArea = new CharSetArea(characterSetAlignmentArea, getAlignmentModel().getCharSetModel());
			charSetArea.getSelectionListeners().add(new SelectionListener<GenericEventObject<CharSetArea>>() {
				@Override
				public void selectionChanged(GenericEventObject<CharSetArea> event) {
				MainFrame.getInstance().getActionManagement().refreshActionStatus();
				}
			});
					
			// Prepare heading areas:
			sequenceIndexAlignmentArea.getDataAreas().getTopList().add(new SequenceIndexArea(sequenceIndexAlignmentArea));
			sequenceIndexAlignmentArea.setAllowVerticalScrolling(false);
			characterSetAlignmentArea.getDataAreas().getTopList().add(charSetArea);
					
			container.getAlignmentAreas().add(sequenceIndexAlignmentArea);
			container.getAlignmentAreas().add(characterSetAlignmentArea);
			//container.getAlignmentAreas().add(mainArea);  //TODO Why have sequence index and character set areas no width if the main area is added here already? 
					
			// Prepare main area:
			mainArea.setAlignmentModel(getAlignmentModel().getAlignmentModel());  //TODO The underlying model should not be passed here anymore, as soon as the problem of displaying its contents is solved.
			mainArea.getSelection().addSelectionListener(new SelectionListener<GenericEventObject<SelectionModel>>() {
				@Override
				public void selectionChanged(GenericEventObject<SelectionModel> event) {
				MainFrame.getInstance().getActionManagement().refreshActionStatus();
				}
			});
					
			container.getAlignmentAreas().add(mainArea);
		}
		return container;
	}
}
