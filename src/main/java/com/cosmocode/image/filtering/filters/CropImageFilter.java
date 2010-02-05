package com.cosmocode.image.filtering.filters;

import com.cosmocode.image.filtering.ImageFilter;

/**
 *
 * @version $Id: CropImageFilter.java,v 1.1 2004/02/20 08:36:29 rademacher Exp $
 */
public class CropImageFilter implements ImageFilter  {
 
	public static final String cvsID = "$Id: CropImageFilter.java,v 1.1 2004/02/20 08:36:29 rademacher Exp $";

    private String name = "crop" ;
    private float x,y,width, height ;

    public String getName() { return name ; }
 
	public CropImageFilter ( float x, float y, float width, float height ) {
        this.x = x;
        this.y = y;
        this.width = width ;
        this.height = height ;
	}

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
 
} // end class CropImageFilter
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: CropImageFilter.java,v $
// Revision 1.1  2004/02/20 08:36:29  rademacher
// 0.0.0.1
//
// 
//
