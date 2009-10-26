package com.cosmocode.image.filtering.filters;

import com.cosmocode.image.filtering.ImageFilter;

/**
 *
 * @version $Id: BrightnessFilter.java,v 1.1 2005/10/21 10:04:05 rademacher Exp $
 * @copy CosmoCode GmbH 2004; Contact: info@cosmocode.de
 */
public class BrightnessFilter implements ImageFilter {
 
	public static final String cvsID = "$Id: BrightnessFilter.java,v 1.1 2005/10/21 10:04:05 rademacher Exp $";

    private String name = "brightness" ;

	double _b = 0.0 ;

	public BrightnessFilter(double b) {
		_b = b;
	}

    public String getName() { return name ; }

	public double getBrightness() { return _b;  }
 
} // end class BrightnessFilter
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: BrightnessFilter.java,v $
// Revision 1.1  2005/10/21 10:04:05  rademacher
// 0.6.0.0
//
// 
//
