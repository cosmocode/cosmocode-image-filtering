package com.cosmocode.image.filtering;

/**
 *
 * @version $Id: ImageFiltering.java,v 1.2 2004/02/23 11:57:41 rademacher Exp $
 */
public interface ImageFiltering  {
 
	public static final String cvsID = "$Id: ImageFiltering.java,v 1.2 2004/02/23 11:57:41 rademacher Exp $";

    public int getWidth() ;
    public int getHeight() ;
    public void addFilter( ImageFilter filter ) ;
    public void proceedFilters() ;
    public boolean writeToFile( String filename ) ;

    /** nur die stream-functions unterstueten
     * compression
     */
    public boolean writeToStream( java.io.OutputStream out ) ;

    /** format can be
     * png
     * pnm
     * tiff
     * bmp
     * jpeg
     */
    public boolean writeToStream( java.io.OutputStream out, String format ) ;

    /** hat nur Effekt bei JPEG
     */
    public void setQuality( float quality );
 
} // end class ImageFiltering
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: ImageFiltering.java,v $
// Revision 1.2  2004/02/23 11:57:41  rademacher
// 0.1.0.0
//
// Revision 1.1.1.1  2004/02/19 14:37:31  rademacher
//
//
// 
//
