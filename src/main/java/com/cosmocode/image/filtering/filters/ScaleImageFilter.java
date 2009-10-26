package com.cosmocode.image.filtering.filters;

import com.cosmocode.image.filtering.ImageFilter;

/**
 *
 * @version $Id: ScaleImageFilter.java,v 1.1.1.1 2004/02/19 14:37:31 rademacher Exp $
 * @copy CosmoCode GmbH 2002; Contact: info@cosmocode.de
 */
public class ScaleImageFilter implements ImageFilter {
 
	public static final String cvsID = "$Id: ScaleImageFilter.java,v 1.1.1.1 2004/02/19 14:37:31 rademacher Exp $";

    private String name = "scale" ;
    private float xscale, yscale ;

    public String getName() { return name ; }
 
	public ScaleImageFilter ( float percent ) {
        xscale = percent ;
        yscale = percent ;
	}

	public ScaleImageFilter ( float xscale, float yscale ) {
        this.xscale=xscale;
        this.yscale=yscale;
    }

    public float getXScale() { return xscale ; }
    public float getYScale() { return yscale ; }

 
} // end class ScaleImageFilter
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: ScaleImageFilter.java,v $
// Revision 1.1.1.1  2004/02/19 14:37:31  rademacher
//
//
// 
//
