package com.cosmocode.image.filtering.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cosmocode.image.filtering.ImageFiltering;
import com.cosmocode.image.filtering.JAIImageFiltering;
import com.cosmocode.image.filtering.filters.BorderImageFilter;
import com.cosmocode.image.filtering.filters.CropImageFilter;
import com.cosmocode.image.filtering.filters.InvertImageFilter;
import com.cosmocode.image.filtering.filters.RotateImageFilter;
import com.cosmocode.image.filtering.filters.ScaleImageFilter;

/**
 *
 * Parameter, die dem servlet uebergeben werden koennen:
 * <table>
 * 	<tr>
 * 		<td><b>Parameter</b></td>
 * 		<td><b>Wert</b></td>
 * 		<td><b>Beschreibung</b></td>
 * 	</tr>
 * 	<tr>
 * 		<td>url</td>
 * 		<td>beliebig</td>
 * 		<td>Dokument, welches nach dem filtern aufgerufen werden soll. Dabei handelt es sich in den meisten Faellen um das
 * 		gleiche Dokument, welches das Servlet im form-Tag aufruft.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>filein</td>
 * 		<td>file</td>
 * 		<td>Datei, welches gefiltert werden soll</td>
 * 	</tr>
 * 	<tr>
 * 		<td>fileout</td>
 * 		<td>file | empty</d>
 * 		<td>Wenn keine Datei angegeben wird, wird <i>filein</i> ueberschrieben.</td>
 * 	</tr>
 * 	<tr>
 * 		<td>outformat</td>
 * 		<td>jpeg | bmp | fpx | png | pnm | tiff | empty</d>
 * 		<td>wenn keins angegeben ist, dann wird das eingangsformat benutzt</td>
 * 	</tr>
 * </table>
 * <br>
 * Filter-Parameter, die dem servlet uebergeben werden koennen:
 * <table>
 * 	<tr>
 * 		<td><b>Parameter</b></td>
 * 		<td><b>Wert</b></td>
 * 		<td><b>Beschreibung</b></td>
 * 		<td><b>Beispiel</b></td>
 * 	</tr>
 * 	<tr>
 * 		<td>filter_quality</td>
 * 		<td>0.0 - 1.0</td>
 * 		<td>setzt die qualitaet des bildes (hat nur effekt bei jpeg)</td>
 * 		<td>filter_quality=0.5</td>
 * 	</tr>
 * 	<tr>
 * 		<td>filter_dimension</td>
 * 		<td>width*height</td>
 * 		<td>setzt neue breite und hoehe fuer das bild</td>
 * 		<td>filter_dimension=150*233 | filter_dimension=150* (ratio beibehalten) | filter_dimension=*150 (ratio beibehalten)</td>
 * 	</tr>
 * 	<tr>
 * 		<td>filter_scale</td>
 * 		<td>percent (0.0-1.0)</td>
 * 		<td>scaliert das Bild</td>
 * 		<td>filter_scale=0.1</td>
 * 	</tr>
 * 	<tr>
 * 		<td>filter_rotate</td>
 * 		<td>angle (0.0-1.0)</td>
 * 		<td>rotiert das Bild</td>
 * 		<td>filter_rotate=0.5</td>
 * 	</tr>
 * 	<tr>
 * 		<td>filter_border</td>
 * 		<td>border (int-value)</td>
 * 		<td>malt einen rahmen um das bild</td>
 * 		<td>filter_border=10</td>
 * 	</tr>
 * 	<tr>
 * 		<td>filter_crop</td>
 * 		<td>x,y,w,h (float-value)</td>
 * 		<td>schneidet ein teil des bildes aus</td>
 * 		<td>filter_crop=0.0,10.0,100.0,100.0</td>
 * 	</tr>
 * 	<tr>
 * 		<td>filter_invert</td>
 * 		<td>0 | 1</td>
 * 		<td>invertiert das bild</td>
 * 		<td>filter_invert=on</td>
 * 	</tr>
 * </table>
 * 
 * @version $Id: FilterServlet.java,v 1.3 2005/01/05 12:03:19 rademacher Exp $
 * @copy CosmoCode GmbH 2004; Contact: info@cosmocode.de
 */
public class FilterServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1736328756120697692L;

    public static final String cvsID = "$Id: FilterServlet.java,v 1.3 2005/01/05 12:03:19 rademacher Exp $";

	static Logger logger = Logger.getLogger( FilterServlet.class ) ;

	static final String TMP_DIR = "/tmp/";

	static final String[] errorStr = {
			"FILE_EXISTS",
			"NULL_REQUEST",
			"NO_TMP",
			"MAXPOSSIZE",
			"READONLY",
			"WRONG_POST_TYPE",
			"FILE_TOO_BIG",
			"WRONG_DATA",
			"NO_BOUNDARY",
			"MALFORMED_LINE",
			"CONTENT_CORRUPT"
	};

	public void doGet( HttpServletRequest request, HttpServletResponse resp ) throws ServletException {
		try {
			doPost( request, resp ) ;
		} catch ( Exception e) {
			throw new ServletException ( e ) ;
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setStatus( HttpServletResponse.SC_OK );
		res.setContentType("text/html");
		
		PrintWriter out = res.getWriter();

		out.println("<HTML><BODY>");

		String url = req.getParameter("url");
		String filein = req.getParameter("filein") ;
		String fileout = req.getParameter("fileout") ;
		String outformat = req.getParameter("outformat") ;

		if ( outformat==null || outformat.equals("") ) 
			outformat = (fileout!=null && !fileout.equals("") ? 
						fileout.substring( fileout.lastIndexOf(".")+1 )
						: filein.substring( filein.lastIndexOf(".")+1 ) ) ;

		String filter_quality = req.getParameter("filter_quality") ;
		String filter_dimension = req.getParameter("filter_dimension") ;
		String filter_scale = req.getParameter("filter_scale") ;
		String filter_rotate = req.getParameter("filter_rotate") ;
		String filter_border = req.getParameter("filter_border") ;
		String filter_crop = req.getParameter("filter_crop");
		String filter_invert = req.getParameter("filter_invert") ;

		try {
			ImageFiltering filtering = null ;
			if ( filein.startsWith("http") || filein.startsWith("ftp") ) 
				filtering = new JAIImageFiltering( new java.net.URL( filein) ) ;
			else
				filtering = new JAIImageFiltering( filein ) ;

			// Filtern 
			if ( filter_quality != null &&  !filter_quality.equals("") ) {
				filtering.setQuality( Float.parseFloat( filter_quality) );
				System.err.println( "*** quality: " + filter_quality );
			}
			if ( filter_border != null &&  !filter_border.equals("") ) {
				filtering.addFilter( new BorderImageFilter( Integer.parseInt( filter_border ) ) );
				System.err.println( "*** border: " + filter_border );
			}
			if ( filter_invert != null &&  !filter_invert.equals("") ) {
				filtering.addFilter( new InvertImageFilter( ) );
				System.err.println( "*** invert: " + filter_invert );
			}
			if ( filter_rotate != null &&  !filter_rotate.equals("") ) {
				filtering.addFilter( new RotateImageFilter( Float.parseFloat( filter_rotate ) ) ) ;
				System.err.println( "*** rotate: " + filter_rotate ) ;
			}
			if ( filter_crop != null &&  !filter_crop.equals("") ) {
				StringTokenizer st = new StringTokenizer(filter_crop,",") ;
				float x = Float.parseFloat( st.nextToken() ) ; 
				float y = Float.parseFloat( st.nextToken() ) ; 
				float w = Float.parseFloat( st.nextToken() ) ; 
				float h = Float.parseFloat( st.nextToken() ) ; 
				filtering.addFilter( new CropImageFilter( x,y,w,h) ) ;
				System.err.println( "*** rotate: " + filter_rotate ) ;
			}

			if ( filter_scale != null &&  !filter_scale.equals("") ) {
				filtering.addFilter( new ScaleImageFilter( Float.parseFloat( filter_scale ) ) ) ;
				System.err.println( "*** scaling per: " + filter_scale + "%" ) ;
			} else
			if ( filter_dimension != null &&  !filter_dimension.equals("") ) {
				float newW = -1.0f ;
				float newH = -1.0f ;
				String sw = filter_dimension.substring(0,filter_dimension.indexOf("*")) ;
				String sh = filter_dimension.substring(filter_dimension.indexOf("*")+1) ; 
				if ( ! sw.equals("") ) newW = (float)Integer.parseInt(sw)/(float)filtering.getWidth(); 
				if ( ! sh.equals("") ) newH = (float)Integer.parseInt(sh)/(float)filtering.getHeight(); 
				if ( newW<0.0 ) newW = newH ;
					else
				if ( newH<0.0 ) newH = newW ;

				filtering.addFilter( new ScaleImageFilter( newW, newH ) ) ;
				System.err.println( "*** scaling to: " + newW + "/" + newH ) ;
			}

			filtering.proceedFilters() ;

			FileOutputStream fout = new FileOutputStream( fileout ) ;
			filtering.writeToStream( fout, outformat );
			fout.close() ;

			out.println("<script> document.location.href = \""+url+"\"; </script>");

		} catch (Exception e) {

			e.printStackTrace() ;
			suckContent( req ) ;
			url = res.encodeURL(url + "?error=" + getErrorNr( e.toString() ) );
			out.println("<script> document.location.href = \""+url+"\"; </script>");

		} finally {
			
		}

		out.println("</BODY></HTML>");
	}

	// -- private -- 

	private void suckContent( HttpServletRequest req ) {
		try {
			InputStream in = req.getInputStream() ;
			byte [] buf = new byte[ 100000 ] ; // ~ 100 k
			while ( 0 < in.read( buf, 0, buf.length ) ) {
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	private int getErrorNr(String err) {
		for (int i=0;i<errorStr.length;i++) {
			if (err.indexOf(errorStr[i])>=0) return i;
		}

		return -1;
	}
 
} // end class FilterServlet
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: FilterServlet.java,v $
// Revision 1.3  2005/01/05 12:03:19  rademacher
// 0.3.0.0
//
// Revision 1.2  2004/10/01 13:10:32  rademacher
// 0.2.1.0
//
// Revision 1.1  2004/10/01 09:33:57  rademacher
// 0.2.0.1
//
// 
//
