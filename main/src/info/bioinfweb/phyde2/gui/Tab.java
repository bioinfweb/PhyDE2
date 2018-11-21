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

import javax.swing.JPanel;

import info.bioinfweb.commons.events.GenericEventObject;
import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionListener;
import info.bioinfweb.libralign.alignmentarea.selection.SelectionModel;
import info.bioinfweb.libralign.dataarea.implementations.charset.CharSetArea;
import info.bioinfweb.libralign.dataarea.implementations.sequenceindex.SequenceIndexArea;
import info.bioinfweb.libralign.multiplealignments.MultipleAlignmentsContainer;
import info.bioinfweb.phyde2.document.Document;
import info.bioinfweb.phyde2.gui.actions.ActionManagement;
import info.bioinfweb.tic.SwingComponentFactory;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import java.awt.Component;


@SuppressWarnings("serial")
public class Tab extends JPanel {
	private Document document = null;
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
	
	
	public Document getDocument() {
		return document;
	}
	
	
	public Tab(Document document) {
		super();
		this.document = document;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JComponent alignmentsContainer = SwingComponentFactory.getInstance().getSwingComponent(getAlignmentsContainer());
		add(alignmentsContainer);	
	}
	
	
	private MultipleAlignmentsContainer getAlignmentsContainer() {
		if (container == null) { //das muss weg, oder?
			// Create main container instance (TIC component):
			container = new MultipleAlignmentsContainer();
					
			// out head and main AlignmentArea in container:
			sequenceIndexAlignmentArea = new AlignmentArea(container);
			characterSetAlignmentArea = new AlignmentArea(container);
			mainArea = new AlignmentArea(container);
			charSetArea = new CharSetArea(characterSetAlignmentArea.getContentArea(), mainArea, getDocument().getCharSetModel());
			charSetArea.getSelectionListeners().add(new SelectionListener<GenericEventObject<CharSetArea>>() {
				@Override
				public void selectionChanged(GenericEventObject<CharSetArea> event) {
				MainFrame.getInstance().getActionManagement().refreshActionStatus();
				}
			});
					
			// Prepare heading areas:
			sequenceIndexAlignmentArea.getDataAreas().getTopAreas().add(new SequenceIndexArea(sequenceIndexAlignmentArea.getContentArea(), mainArea));
			sequenceIndexAlignmentArea.setAllowVerticalScrolling(false);
			characterSetAlignmentArea.getDataAreas().getTopAreas().add(charSetArea);
					
			container.getAlignmentAreas().add(sequenceIndexAlignmentArea);
			container.getAlignmentAreas().add(characterSetAlignmentArea);
			//container.getAlignmentAreas().add(mainArea);  //TODO Why have sequence index and character set areas no width if the main area is added here already? 
					
			// Prepare main area:
			mainArea.setAlignmentModel(getDocument().getAlignmentModel(), false);  //TODO The underlying model should not be passed here anymore, as soon as the problem of displying its contents is solved.
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
