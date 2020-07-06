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
package info.bioinfweb.phyde2.document.io;


import info.bioinfweb.commons.bio.CharacterStateSetType;
import info.bioinfweb.libralign.model.factory.CharacterStateSetChooser;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.dialogs.CharacterStateSetChooserDialog;



public class PhyDE2CharacterStateSetChooser implements CharacterStateSetChooser{
	@Override
	public CharacterStateSetType chooseCharacterStateSet() {
		CharacterStateSetChooserDialog dialog = CharacterStateSetChooserDialog.getInstance(MainFrame.getInstance());
		if (dialog.execute()){
			return dialog.getChangedType();
		}
		else {
			return CharacterStateSetType.UNKNOWN;  //TODO: handle case that user aborted the imput
		}
	}


	public PhyDE2CharacterStateSetChooser() {
		super();
		
	}
}
	
	
	

