package com.cosmocode.image.filtering.filters;

import com.cosmocode.image.filtering.ImageFilter;

/**
 *
 * @version $Id: RotateImageFilter.java,v 1.2 2005/01/07 11:54:21 rademacher Exp $
 * @copy CosmoCode GmbH 2002; Contact: info@cosmocode.de
 */
public class RotateImageFilter implements ImageFilter {
 
	public static final String cvsID = "$Id: RotateImageFilter.java,v 1.2 2005/01/07 11:54:21 rademacher Exp $";

    private String name = "rotate" ;
    private float radians ;

    public String getName() { return name ; }
 
	public RotateImageFilter ( float radians ) {
        this.radians = radians ;
	}
	public RotateImageFilter ( int degrees ) {
        this.radians = (float)((float)degrees * ( Math.PI / 180.0) );
	}

    public float getRadians() { return radians; } 
 
} // end class RotateImageFilter
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: RotateImageFilter.java,v $
// Revision 1.2  2005/01/07 11:54:21  rademacher
// 0.3.1.0
//
// Revision 1.1.1.1  2004/02/19 14:37:31  rademacher
//
//
// 
//
