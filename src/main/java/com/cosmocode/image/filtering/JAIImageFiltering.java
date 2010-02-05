package com.cosmocode.image.filtering;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.media.jai.RenderedOp;
import javax.media.jai.operator.BorderDescriptor;
import javax.media.jai.operator.CropDescriptor;
import javax.media.jai.operator.EncodeDescriptor;
import javax.media.jai.operator.FileStoreDescriptor;
import javax.media.jai.operator.InvertDescriptor;
import javax.media.jai.operator.RescaleDescriptor;
import javax.media.jai.operator.RotateDescriptor;
import javax.media.jai.operator.ScaleDescriptor;
import javax.media.jai.operator.StreamDescriptor;
import javax.media.jai.operator.URLDescriptor;

import com.cosmocode.image.filtering.filters.BorderImageFilter;
import com.cosmocode.image.filtering.filters.BrightnessFilter;
import com.cosmocode.image.filtering.filters.CropImageFilter;
import com.cosmocode.image.filtering.filters.InvertImageFilter;
import com.cosmocode.image.filtering.filters.RotateImageFilter;
import com.cosmocode.image.filtering.filters.ScaleImageFilter;
import com.sun.media.jai.codec.BMPEncodeParam;
import com.sun.media.jai.codec.FileCacheSeekableStream;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageEncodeParam;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.PNMEncodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;

/**
 *
 * @version $Id: JAIImageFiltering.java,v 1.7 2006/05/30 15:27:26 rademacher Exp $
 */
public class JAIImageFiltering  implements ImageFiltering {
 
	public static final String cvsID = "$Id: JAIImageFiltering.java,v 1.7 2006/05/30 15:27:26 rademacher Exp $";

    private RenderedOp ro ; 
    private Vector<ImageFilter> v = new Vector<ImageFilter>() ;
    private float quality = 1.0f ;

	public JAIImageFiltering ( InputStream in ) throws Exception {
        ro = StreamDescriptor.create( new FileCacheSeekableStream( in ), null,null ) ;
	}

    public JAIImageFiltering ( java.net.URL location ) throws Exception {
        ro = URLDescriptor.create( location, null, null );
    }

    public JAIImageFiltering( String filename ) throws Exception {
        ro = StreamDescriptor.create( new FileSeekableStream( filename ), null,null ) ;
    }

	/** returns an RenderedOp. for more details see
	 * http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/RenderedOp.html
	 */
	public static RenderedOp getRenderedOp( InputStream in ) throws Exception {
		return StreamDescriptor.create( new FileCacheSeekableStream( in ), null,null ) ;
	}
	/** returns an RenderedOp. for more details see
	 * http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/RenderedOp.html
	 */
	public static RenderedOp getRenderedOp( java.net.URL location ) throws Exception {
		return URLDescriptor.create( location, null, null );
	}
	/** returns an RenderedOp. for more details see
	 * http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/RenderedOp.html
	 */
	public static RenderedOp getRenderedOp( String filename ) throws Exception {
		return StreamDescriptor.create( new FileSeekableStream( filename ), null,null ) ;
	}

	/** returns an RenderedOp. for more details see
	 * http://java.sun.com/products/java-media/jai/forDevelopers/jai-apidocs/javax/media/jai/RenderedOp.html
	 */
	public RenderedOp getRenderedOp() {
		return ro ; 
	}

    public int getWidth() {
        if ( ro == null ) return -1 ;
        return ro.getWidth() ;
    }

    public int getHeight() {
        if ( ro == null ) return -1 ;
        return ro.getHeight() ;
    }

    public void addFilter( ImageFilter filter ) {
        v.addElement( filter ) ;
    }

    public void setQuality( float quality ) {
        this.quality = quality ;
    }

    public void proceedFilters() {
        for (int i=0;i<v.size();i++) {
            ImageFilter filter = (ImageFilter)v.elementAt(i) ;
            if ( filter instanceof ScaleImageFilter ) {
                ro = ScaleDescriptor.create(
                    ro,
                    new Float( ((ScaleImageFilter)filter).getXScale() ),
                    new Float( ((ScaleImageFilter)filter).getYScale()),
                    new Float(0),new Float(0),null,null);
            } else
            if ( filter instanceof RotateImageFilter ) {
                ro = RotateDescriptor.create(
                    ro,
                    new Float( ro.getWidth()/2 ) ,
                    new Float( ro.getHeight()/2 ) ,
                    new Float( ((RotateImageFilter)filter).getRadians()),
                    null,null,null );
            } else
            if ( filter instanceof BorderImageFilter ) {
                ro = BorderDescriptor.create(
                    ro,
                    new Integer( ((BorderImageFilter)filter).getWidth() ),
                    new Integer(((BorderImageFilter)filter).getWidth()),
                    new Integer(((BorderImageFilter)filter).getWidth()),
                    new Integer(((BorderImageFilter)filter).getWidth()),
                    null, null );
            } else
            if ( filter instanceof CropImageFilter ) {
                ro = CropDescriptor.create(
                    ro,
                    new Float(((CropImageFilter)filter).getX() ),
                    new Float(((CropImageFilter)filter).getY()),
                    new Float(((CropImageFilter)filter).getWidth()),
                    new Float(((CropImageFilter)filter).getHeight()),
                    null) ;
            } else
            if ( filter instanceof InvertImageFilter ) {
                ro = InvertDescriptor.create(
                    ro,null );
            } else
            if ( filter instanceof BrightnessFilter ) {
				double[] c =  { ((BrightnessFilter)filter).getBrightness() } ;
				double[] o =  { 0.0f } ;
				ro = RescaleDescriptor.create(
					ro,
					c,
					o,
					null );

			}
        }
    }

    public boolean writeToFile( String filename ) {
        if ( ro == null ) return false;
        FileStoreDescriptor.create( ro , filename , null ,null,null,null);
        return true ;
    }

    public boolean writeToStream( OutputStream out ) {
        return writeToStream( out, null );
    }

    /** format can be
     * png
     * pnm
     * tiff
     * bmp
     * jpeg
     */
    public boolean writeToStream( java.io.OutputStream out, String format ) {
        if ( ro == null ) return false ;
        if ( format == null ) format="";
        ImageEncodeParam iep = null ;

        if ( format.toLowerCase().equals("png") ) {
            iep = null ;
        } else 
        if ( format.toLowerCase().equals("pnm") ) {
            iep = new PNMEncodeParam() ;
        } else 
        if ( format.toLowerCase().equals("tiff") ) {
            iep = new TIFFEncodeParam() ;
        } else 
        if ( format.toLowerCase().equals("bmp") ) {
            iep = new BMPEncodeParam() ;
        } else 
        if ( format.toLowerCase().equals("jpeg") ) {
            iep = new JPEGEncodeParam( ) ;
            ((JPEGEncodeParam)iep).setQuality( quality );
        } else format = null ;

        EncodeDescriptor.create( ro, out, format , iep, null );
        return true ;
    }
 
} // end class JAIImageFiltering
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: JAIImageFiltering.java,v $
// Revision 1.7  2006/05/30 15:27:26  rademacher
// 0.7.0.0
//
// Revision 1.6  2005/10/21 10:04:05  rademacher
// 0.6.0.0
//
// Revision 1.5  2005/05/13 12:32:45  rademacher
// 0.5.0.0
//
// Revision 1.4  2005/01/07 11:54:21  rademacher
// 0.3.1.0
//
// Revision 1.3  2004/02/23 11:57:41  rademacher
// 0.1.0.0
//
// Revision 1.2  2004/02/20 08:36:29  rademacher
// 0.0.0.1
//
// Revision 1.1.1.1  2004/02/19 14:37:31  rademacher
//
//
// 
//
