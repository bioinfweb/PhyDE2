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


import java.util.List;

import info.bioinfweb.libralign.alignmentarea.AlignmentArea;
import info.bioinfweb.libralign.dataarea.DataAreaFactory;
import info.bioinfweb.libralign.dataarea.ModelBasedDataArea;
import info.bioinfweb.libralign.dataarea.implementations.pherogram.PherogramArea;
import info.bioinfweb.libralign.model.data.DataModel;
import info.bioinfweb.phyde2.document.PherogramReference;



public class PhyDE2DataAreaFactory implements DataAreaFactory {
	private AlignmentArea alignmentArea;
	
	
	public PhyDE2DataAreaFactory(AlignmentArea alignmentArea) {
		super();
		this.alignmentArea = alignmentArea;
	}


	@Override
	public void createDataAreas(DataModel<?> model, String sequenceID, List<DataAreaResult> result) {
		if (model instanceof PherogramReference) {
			PherogramArea pherogramArea = new PherogramArea(alignmentArea, (PherogramReference)model, MainFrame.getInstance().getPherogramFormats());
			pherogramArea.addMouseListener(new PherogramMouseListener(pherogramArea));
			result.add(new DataAreaResult(pherogramArea));
		}
	}
	
	
	@Override
	public boolean removeDataArea(ModelBasedDataArea<?, ?> dataArea) {
		return true;
		//TODO SequenceIndexArea, CharSetArea and ConsensusSequenceArea should not be removed, if they are linked to the model.
		//     - A third option in LibrAlign to leave areas unchanged (associated with their model) might make sense, but it might also not be necessary, since the TOP and BOTTOM areas would only be passed here, when a model is changed.
		//     - The area being in another alignment area (as the case here) is another use case that should work with LibrAlign. (Adding them there is beyond the scope of this factory but it should allow to have the respective data models in here.)
	}
}
