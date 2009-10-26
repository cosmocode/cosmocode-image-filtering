package com.cosmocode.image.filtering.filters;

import com.cosmocode.image.filtering.ImageFilter;

/**
 *
 * @version $Id: InvertImageFilter.java,v 1.1 2004/02/20 08:36:29 rademacher Exp $
 * @copy CosmoCode GmbH 2002; Contact: info@cosmocode.de
 */
public class InvertImageFilter implements ImageFilter {
 
	public static final String cvsID = "$Id: InvertImageFilter.java,v 1.1 2004/02/20 08:36:29 rademacher Exp $";

    private String name = "invert" ;

    public String getName() { return name ; }
 
	public InvertImageFilter ( ) { }
 
} // end class InvertImageFilter
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: InvertImageFilter.java,v $
// Revision 1.1  2004/02/20 08:36:29  rademacher
// 0.0.0.1
//
// 
//
