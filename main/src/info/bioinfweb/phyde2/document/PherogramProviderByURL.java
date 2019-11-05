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
package info.bioinfweb.phyde2.document;


import info.bioinfweb.libralign.pherogram.provider.BioJavaPherogramProvider;
import info.bioinfweb.libralign.pherogram.provider.PherogramProvider;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import org.biojava.bio.chromatogram.Chromatogram;
import org.biojava.bio.chromatogram.ChromatogramFactory;
import org.biojava.bio.chromatogram.UnsupportedChromatogramFormatException;



public class PherogramProviderByURL {
	private static PherogramProviderByURL firstInstance = null;
	private Map<String, PherogramProvider> pherogramProviderMap = new TreeMap<String, PherogramProvider>();	

	
	private PherogramProviderByURL() {
		super();
	}
	
	
	public static PherogramProviderByURL getInstance() {
		if (firstInstance == null) {
			firstInstance = new PherogramProviderByURL();
		}
		return firstInstance;
	}
	
	
	public PherogramProvider getPherogramProvider(URL url) throws UnsupportedChromatogramFormatException, IOException {
		PherogramProvider pherogramProvider = null;
		
		if (pherogramProviderMap.get(url.toString()) == null){
			
				Chromatogram chromatogram = ChromatogramFactory.create(url.openStream());
				pherogramProvider = new BioJavaPherogramProvider(chromatogram);
				pherogramProviderMap.put(url.toString(), pherogramProvider);		
		
		}
		else {
			pherogramProvider = pherogramProviderMap.get(url.toString());
		}
		
		return pherogramProvider;
	}
}
