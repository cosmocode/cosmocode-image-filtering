package com.cosmocode.image.filtering;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.cosmocode.image.filtering.filters.BorderImageFilter;
import com.cosmocode.image.filtering.filters.BrightnessFilter;
import com.cosmocode.image.filtering.filters.CropImageFilter;
import com.cosmocode.image.filtering.filters.InvertImageFilter;
import com.cosmocode.image.filtering.filters.RotateImageFilter;
import com.cosmocode.image.filtering.filters.ScaleImageFilter;

/**
 *
 * Filter-Parameter, die dem servlet uebergeben werden koennen:
 * <table>
 * 	<tr>
 * 		<td><b>Parameter</b></td>
 * 		<td><b>Wert</b></td>
 * 		<td><b>Beschreibung</b></td>
 * 		<td><b>Beispiel</b></td>
 * 	</tr>
 * 	<tr>
 * 		<td>quality</td>
 * 		<td>0.0 - 1.0</td>
 * 		<td>setzt die qualitaet des bildes (hat nur effekt bei jpeg)</td>
 * 		<td>quality=0.5</td>
 * 	</tr>
 * 	<tr>
 * 		<td>dimension</td>
 * 		<td>widthxheight</td>
 * 		<td>setzt neue breite und hoehe fuer das bild</td>
 * 		<td>filter_dimension=150x233 | filter_dimension=150x (ratio beibehalten) | filter_dimension=x150 (ratio beibehalten)</td>
 * 	</tr>
 * 	<tr>
 * 		<td>scale</td>
 * 		<td>percent (0.0-1.0)</td>
 * 		<td>scaliert das Bild</td>
 * 		<td>scale=0.1</td>
 * 	</tr>
 * 	<tr>
 * 		<td>rotate</td>
 * 		<td>radians (0.0-1.0) | degrees (0-360)</td>
 * 		<td>rotiert das Bild</td>
 * 		<td>rotate=0.5 | rotate=45</td>
 * 	</tr>
 * 	<tr>
 * 		<td>border</td>
 * 		<td>border (int-value)</td>
 * 		<td>malt einen rahmen um das bild</td>
 * 		<td>border=10</td>
 * 	</tr>
 * 	<tr>
 * 		<td>crop</td>
 * 		<td>x,y,w,h (float-value)</td>
 * 		<td>schneidet ein teil des bildes aus</td>
 * 		<td>crop=0.0x10.0x100.0x100.0</td>
 * 	</tr>
 * 	<tr>
 * 		<td>invert</td>
 * 		<td>0 | 1</td>
 * 		<td>invertiert das bild</td>
 * 		<td>invert=on</td>
 * 	</tr>
 * 	<tr>
 * 		<td>bbox</td>
 * 		<td>widthxheight</td>
 * 		<td>passt das bild in eine bounding-box an</td>
 * 		<td>bbox=200x100</td>
 * 	</tr>
 * 	<tr>
 * 		<td>mbox</td>
 * 		<td>widthxheight</td>
 * 		<td>passt das bild nur in eine bounding-box an, wenn das bild zu gross ist</td>
 * 		<td>bbox=200x100</td>
 * 	</tr>
 * 	<tr>
 * 		<td>brightness</td>
 * 		<td>brightness</td>
 * 		<td>hellt ein bild auf...</td>
 * 		<td>brightness=1.5</td>
 * 	</tr>
 * </table>
 *
 * @version $Id: ImageTool.java,v 1.7 2005/10/24 08:50:53 rademacher Exp $
 * @copy CosmoCode GmbH 2004; Contact: info@cosmocode.de
 */
public class ImageTool  {
 
	public static final String cvsID = "$Id: ImageTool.java,v 1.7 2005/10/24 08:50:53 rademacher Exp $";

	public static final String FILTER_BBOX = "bbox" ;
	public static final String FILTER_MBOX = "mbox" ;
	public static final String FILTER_QUALITY = "quality" ;
	public static final String FILTER_DIMENSION = "dimension" ;
	public static final String FILTER_SCALE = "scale" ;
	public static final String FILTER_ROTATE = "rotate" ;
	public static final String FILTER_BORDER = "border" ;
	public static final String FILTER_CROP = "crop" ;
	public static final String FILTER_INVERT = "invert" ;
	public static final String FILTER_OUTPUT = "out" ;
	public static final String FILTER_BRIGHTNESS = "brightness" ;

	static Logger logger = Logger.getLogger( ImageTool.class ) ;

	private String _filters ;

	/**
	 * @param filters filter-parameter. z.b. : rotate=0.3,dimension=150x
	 */
	public ImageTool ( String filters ) {
		_filters = filters;
	}

	public void process( InputStream in, OutputStream out ) throws Exception {
		ImageFiltering _filtering = new JAIImageFiltering ( in ) ;

		String _output = "jpeg" ;

		StringTokenizer st = new StringTokenizer( _filters, "," );
		while ( st.hasMoreTokens() ) {
			String _tok = st.nextToken() ;
			String _filter = _tok.substring( 0, _tok.indexOf("=") ).toLowerCase() ; 
			String _format = _tok.substring( _tok.indexOf("=") +1 ) ;

			if ( _filter.equals( FILTER_QUALITY ) ) {
				_filtering.setQuality( Float.parseFloat( _format ) );
			} else
			if ( _filter.equals( FILTER_DIMENSION ) ) {
				float newW = -1.0f ;
				float newH = -1.0f ;
				String sw = _format.substring(0,_format.indexOf("x")) ;
				String sh = _format.substring(_format.indexOf("x")+1) ; 
				if ( ! sw.equals("") ) newW = (float)Integer.parseInt(sw)/(float)_filtering.getWidth(); 
				if ( ! sh.equals("") ) newH = (float)Integer.parseInt(sh)/(float)_filtering.getHeight(); 
				if ( newW<0.0 ) newW = newH ;
					else
				if ( newH<0.0 ) newH = newW ;

				_filtering.addFilter( new ScaleImageFilter( newW, newH ) ) ;
			} else
			if ( _filter.equals( FILTER_SCALE ) ) {
				_filtering.addFilter( new ScaleImageFilter( Float.parseFloat( _format ) ) ) ;
			} else
			if ( _filter.equals( FILTER_ROTATE ) ) {
				if ( _format.indexOf(".")>=0 ) _filtering.addFilter( new RotateImageFilter( Float.parseFloat( _format ) ) ) ;
					else _filtering.addFilter( new RotateImageFilter( Integer.parseInt( _format ) ) ) ;
			} else
			if ( _filter.equals( FILTER_BORDER ) ) {
				_filtering.addFilter( new BorderImageFilter( Integer.parseInt( _format ) ) );
			} else
			if ( _filter.equals( FILTER_CROP ) ) {
				StringTokenizer st2 = new StringTokenizer( _format, "x" );
				String sx=st2.nextToken () ;
				String sy=st2.nextToken () ;
				String sw=st2.nextToken () ;
				String sh=st2.nextToken () ;

				float x = Float.parseFloat( sx ) ;
				float y = Float.parseFloat( sy ) ;
				float w = Float.parseFloat( sw ) ;
				float h = Float.parseFloat( sh ) ;

				if ( x > _filtering.getWidth() ) x = _filtering.getWidth()-1 ;
					else 
				if ( x < 0 ) x = 0 ;
				if ( y > _filtering.getHeight() ) y = _filtering.getHeight()-1 ;
					else 
				if ( y < 0 ) y = 0 ;

				if ( x+w > _filtering.getWidth() ) w=_filtering.getWidth()-x ;
				if ( y+h > _filtering.getHeight() ) h= _filtering.getHeight()-y ;
				
				_filtering.addFilter( new CropImageFilter( x,y,w,h ) ) ;
			} else
			if ( _filter.equals( FILTER_INVERT ) ) {
				_filtering.addFilter( new InvertImageFilter( ) );
			} else
			if ( _filter.equals( FILTER_BBOX ) || _filter.equals( FILTER_MBOX ) ) {

				boolean _do = true ;
				String sw = _format.substring(0,_format.indexOf("x")) ;
				String sh = _format.substring(_format.indexOf("x")+1) ; 

				int w = Integer.parseInt(sw) ;
				int h = Integer.parseInt(sh) ;
				
				if ( _filter.equals( FILTER_MBOX ) ) {
					_do = ( _filtering.getWidth()>w || _filtering.getHeight()>h ) ;
				}
				
				if ( _do ) {
					float newH = (float)h/(float)_filtering.getHeight(); 
					float newW = (float)w/(float)_filtering.getWidth(); 
					
					if ( newW > newH ) newW=-1.0f;
						else newH=-1.0f;

					if ( newW<0.0 ) newW = newH ;
						else
					if ( newH<0.0 ) newH = newW ;

					_filtering.addFilter( new ScaleImageFilter( newW, newH ) ) ;
				}
			} else
			if ( _filter.equals( FILTER_OUTPUT ) ) {
				_output = _format ;
			} else
			if ( _filter.equals( FILTER_BRIGHTNESS ) ) {
				double b = Double.parseDouble( _format );
				_filtering.addFilter( new BrightnessFilter( b ) ) ;
			}
		}

		_filtering.proceedFilters() ;
		_filtering.writeToStream ( out, _output ) ;
	}
 
 
} // end class ImageTool
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: ImageTool.java,v $
// Revision 1.7  2005/10/24 08:50:53  rademacher
// 0.6.1.0
//
// Revision 1.6  2005/10/21 10:04:05  rademacher
// 0.6.0.0
//
// Revision 1.5  2005/05/26 11:25:11  rademacher
// 0.5.30
//
// Revision 1.4  2005/05/24 09:17:39  rademacher
// 0.5.2.0
//
// Revision 1.3  2005/05/11 11:55:59  rademacher
// 0.4.0.0
//
// Revision 1.2  2005/01/07 11:54:21  rademacher
// 0.3.1.0
//
// Revision 1.1  2005/01/05 12:03:19  rademacher
// 0.3.0.0
//
// 
//
