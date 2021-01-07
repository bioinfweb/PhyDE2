package info.bioinfweb.phyde2.document.io;
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

import javax.xml.namespace.QName;

public interface ReadWriteParameterConstants {
	public static final String NAMESPACE_URI_PREFIX = "http://bioinfweb.info/xmlns/PhyDE/ReadAlignment/";  //TODO Possibly change this namespace, when according decision is made. //$NON-NLS-1$
    public static final String PREDICATE_NAMESPACE_URI = NAMESPACE_URI_PREFIX + "Predicates/"; 
    public static final String PREDICATE_NAMESPACE_PREFIX = "ra"; 
//  public static final String DATA_TYPE_NAMESPACE_URI = NAMESPACE_URI_PREFIX + "DataTypes/"; 
//  public static final String DATA_TYPE_NAMESPACE_PREFIX = "radt";

    
    public static final QName PREDICATE_IS_SINGLE_READ = new QName(PREDICATE_NAMESPACE_URI, "isSingleRead", PREDICATE_NAMESPACE_PREFIX); 
    public static final QName PREDICATE_IS_CONSENSUS_SEQUENCE = new QName(PREDICATE_NAMESPACE_URI, "isConsensus", PREDICATE_NAMESPACE_PREFIX); 
    public static final QName PREDICATE_IS_REVERSE_COMPLEMENTED = new QName(PREDICATE_NAMESPACE_URI, "isRCed", PREDICATE_NAMESPACE_PREFIX); 
    public static final QName PREDICATE_ALIGNMENT_TYPE = new QName(PREDICATE_NAMESPACE_URI, "alignmentType", PREDICATE_NAMESPACE_PREFIX);
    public static final QName PREDICATE_HAS_CONTIG_REFERENCE = new QName(PREDICATE_NAMESPACE_URI, "hasContigReference", PREDICATE_NAMESPACE_PREFIX); 
    
}
