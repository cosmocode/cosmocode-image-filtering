package com.cosmocode.image.filtering;

import java.io.FileOutputStream;

import com.cosmocode.image.filtering.filters.CropImageFilter;
import com.cosmocode.image.filtering.filters.InvertImageFilter;
import com.cosmocode.image.filtering.filters.RotateImageFilter;
import com.cosmocode.image.filtering.filters.ScaleImageFilter;

/**
 *
 * @version $Id: FilterTool.java,v 1.4 2004/02/23 12:22:09 rademacher Exp $
 * @copy CosmoCode GmbH 2002; Contact: info@cosmocode.de
 */
public class FilterTool  {
 
	public static final String cvsID = "$Id: FilterTool.java,v 1.4 2004/02/23 12:22:09 rademacher Exp $";

	public static void main( String [] args ) {
        if ( args.length < 2 ) {
                System.out.println( "\nusage: FilterTool [-q qulity ] [-f outformat] <in-file> <out-file> [filters...]");
                System.out.println( "\tquality  : 0.0 (low) - 1.0 (high) -> nur fuer JPEG");
                System.out.println( "\toutformat: png, pnm, tiff, bmp, jpeg");
                System.out.println( "\tfilters  :\n\t\trotate:angle\n\t\tscale:x,y\n\t\tcrop:x,y,w,h\n\t\tinvert\n");
                System.exit(0);
        }

        try {
            float quality = -1.0f;

            String outformat = null ;
            
            int i=0 ;
            boolean stop = false;
            while ( ! stop ) {
                if ( args[i].equals("-q") ) {
                    quality = Float.parseFloat( args[i+1] );
                    i+=2 ;
                } else 
                if ( args[i].equals("-f") ) {
                    outformat = args[i+1] ;
                    i+=2;
                } else stop=true ;
            }

            String infile = args[i++] ;
            String outfile = args[i++] ;

            System.out.println ( "\nproceeding: " + infile + " => " + outfile + (outformat!=null?" / "+outformat:"") + "\n" );

            ImageFiltering filtering = new JAIImageFiltering( infile ) ;
            
            if ( i < args.length ) {
                for (int j=i;j<args.length;j++) {
                        if ( args[j].startsWith("rotate") ) filtering.addFilter( 
                                new RotateImageFilter( Float.parseFloat( args[j].substring(args[j].indexOf(":")+1 ) ) )  ) ;
                            else
                        if ( args[j].startsWith("scale") ) filtering.addFilter( 
                                new ScaleImageFilter( Float.parseFloat( args[j].substring(args[j].indexOf(":")+1,
                                  args[j].indexOf(","))), Float.parseFloat(args[j].substring(args[j].indexOf(",")+1) ) ) ) ;
                            else
                        if ( args[j].startsWith("crop") ) {
                            String s=args[j] ;
                            s=s.substring( s.indexOf(":")+1 );
                            float x = Float.parseFloat(s.substring( 0, s.indexOf(",") ));
                            s=s.substring( s.indexOf(",")+1 );
                            float y = Float.parseFloat(s.substring( 0, s.indexOf(",") ));
                            s=s.substring( s.indexOf(",")+1 );
                            float w = Float.parseFloat(s.substring( 0, s.indexOf(",") ));
                            s=s.substring( s.indexOf(",")+1 );
                            float h = Float.parseFloat(s);
                            filtering.addFilter( new CropImageFilter( x,y,w,h) );
                        } else 
                        if ( args[j].equals("invert") ) filtering.addFilter( new InvertImageFilter() );
                }
                filtering.proceedFilters () ;
            }

            if ( quality >= 0.0f ) filtering.setQuality ( quality ) ;

            filtering.writeToStream ( new FileOutputStream ( outfile)  , outformat  ) ;
        } catch ( Exception e ) {
            e.printStackTrace() ;
        }
    }
 
} // end class FilterTool
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: FilterTool.java,v $
// Revision 1.4  2004/02/23 12:22:09  rademacher
// 0.1.0.1
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
