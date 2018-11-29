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
package info.bioinfweb.phyde2.gui.actions.file;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.biojava.bio.chromatogram.Chromatogram;
import org.biojava.bio.chromatogram.ChromatogramFactory;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;

import info.bioinfweb.libralign.pherogram.model.PherogramAreaModel;
import info.bioinfweb.libralign.pherogram.provider.BioJavaPherogramProvider;
import info.bioinfweb.libralign.pherogram.provider.PherogramProvider;
import info.bioinfweb.phyde2.document.PhyDE2AlignmentModel;
import info.bioinfweb.phyde2.document.SingleReadContigAlignmentModel;
import info.bioinfweb.phyde2.gui.MainFrame;
import info.bioinfweb.phyde2.gui.actions.AbstractPhyDEAction;

public class OpenPherogramAction extends AbstractPhyDEAction  {
	private JFileChooser fileChooser = null;
	
	public OpenPherogramAction(MainFrame mainframe) {
		super(mainframe);
		// TODO Auto-generated constructor stub
	}
	
	
	public JFileChooser getOpenFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "SCF-Files", "scf"); // what was the alternate file format?
			    fileChooser.setFileFilter(filter);
		}
		return fileChooser;
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//brauche ich hier die JPhyloIO Klassen? Ich hab ja theoretisch nur die beiden Dateiformate scf und ?.
		//meine Idee: wenn eine Pherogrammdatei geöffnet wird --> neues contig alignment, denn nur da können sie 
		//dargestellt werden. Also müssen hier der Provider etc hin. Welche Überprüfungen sind zusätzlich notwendig?
		//Außerdem: Es gibt ja (später?) den Fall, dass die Pherogramdaten als Referenz in einem Contig sind... Das
		//heißt, da wird nicht manuell eine Pherogramdatei geladen, sondern in der Datei muss erkannt werden, wie viele
		//Referenz Pherogram-Dateien es gibt?
		SingleReadContigAlignmentModel contig = new SingleReadContigAlignmentModel();
		Chromatogram chromatogram;
		try {
			chromatogram = ChromatogramFactory.create(new File ("data/Pherogram.scf"));
			PherogramProvider provider = new BioJavaPherogramProvider(chromatogram);
			PherogramAreaModel pherogramModel = new PherogramAreaModel(provider);
			
			getMainFrame().getNewDocument().addAlignmentModel(contig);
			String id = contig.addSingleRead("name", pherogramModel);
			
			  
			//jetzt hat das Model die Daten bekommen und muss entscheiden, wies damit weiter geht?
			// => nein, das muss die gui..
		} catch (UnsupportedChromatogramFormatException | IOException e) {
			
			e.printStackTrace();
		} // TODO replace this with chosen file.

		
		
	}

	@Override
	public void setEnabled(PhyDE2AlignmentModel document, MainFrame mainframe) {
		// TODO Auto-generated method stub
		
	}

}
