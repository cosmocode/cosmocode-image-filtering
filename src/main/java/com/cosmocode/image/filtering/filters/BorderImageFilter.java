package com.cosmocode.image.filtering.filters;

import com.cosmocode.image.filtering.ImageFilter;

/**
 *
 * @version $Id: BorderImageFilter.java,v 1.1 2004/02/20 08:36:29 rademacher Exp $
 */
public class BorderImageFilter implements ImageFilter {
 
    private String name = "border";
    
    private int width ;

    public BorderImageFilter(int width) {
        this.width = width ;
    }

    public String getName() {
        return name;
    }
    
    public int getWidth() {
        return width;
    }

}
