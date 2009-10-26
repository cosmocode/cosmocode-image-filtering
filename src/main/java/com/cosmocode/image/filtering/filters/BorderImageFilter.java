package com.cosmocode.image.filtering.filters;

import com.cosmocode.image.filtering.ImageFilter;

/**
 *
 * @version $Id: BorderImageFilter.java,v 1.1 2004/02/20 08:36:29 rademacher Exp $
 * @copy CosmoCode GmbH 2002; Contact: info@cosmocode.de
 */
public class BorderImageFilter implements ImageFilter {
 
	public static final String cvsID = "$Id: BorderImageFilter.java,v 1.1 2004/02/20 08:36:29 rademacher Exp $";

    private String name = "border" ;
    private int width ;

    public String getName() { return name ; }
 
	public BorderImageFilter ( int width ) {
        this.width = width ;
	}

    public int getWidth() { return width; }

} // end class BorderImageFilter
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: BorderImageFilter.java,v $
// Revision 1.1  2004/02/20 08:36:29  rademacher
// 0.0.0.1
//
// 
//
