package com.cosmocode.image.filtering;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @version $Id: SVGImageTool.java,v 1.2 2005/05/18 09:50:36 rademacher Exp $
 * @copy CosmoCode GmbH 2004; Contact: info@cosmocode.de
 */
public class SVGImageTool  {
 
	public static final String cvsID = "$Id: SVGImageTool.java,v 1.2 2005/05/18 09:50:36 rademacher Exp $";

	String _filters = "";
	
	/**
	 * filter-syntax ist: <id-name>[.attribute]=wert
	 * @param filters filter-parameter. z.b. : formel=peter,formel.x=10
	 */
	public SVGImageTool ( String filters ) {
		_filters = filters ;
	}
 
	public void process( InputStream in, OutputStream out ) throws Exception {

		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
		Document document = factory.createDocument(null, in);
		Element rootdoc = document.getDocumentElement() ;

		NodeList nl = rootdoc.getChildNodes() ;
		for (int i=0;i<nl.getLength();i++) {
			Node node = nl.item(i) ;
			NamedNodeMap attributes = node.getAttributes();
			for (int j=0;j<attributes.getLength();j++) {
				Node n = attributes.item(j);

				StringTokenizer st = new StringTokenizer(_filters, ",");
				while ( st.hasMoreTokens() ) {
					String tok = st.nextToken() ;

					String val = tok.substring( tok.indexOf("=")+1 ) ;
					val = java.net.URLDecoder.decode(val,"utf-8") ;
					tok = tok.substring( 0, tok.indexOf("=") );

					int p = tok.indexOf(".");
					if ( p>=0 ) {
						String attr = tok.substring(p+1);
						tok = tok.substring(0,p);
						if ( n.getNodeName().equals("id") && n.getNodeValue().equals( tok ) )
							((Element)node).setAttribute(attr,val);
					} else {
						if ( n.getNodeName().equals("id") && n.getNodeValue().equals( tok ) )
							node.getFirstChild().setNodeValue( val ) ;
					}
				}

			}
		}

		JPEGTranscoder t = new JPEGTranscoder();
		TranscoderInput input = new TranscoderInput( document );
		TranscoderOutput output = new TranscoderOutput( out ) ;

		t.transcode(input, output );
	}
 
} // end class SVGImageTool
 
 
 
//////////////////////////////////////////////////////////////////////////////////
//
// $Log: SVGImageTool.java,v $
// Revision 1.2  2005/05/18 09:50:36  rademacher
// 0.5.1.0
//
// Revision 1.1  2005/05/13 12:32:45  rademacher
// 0.5.0.0
//
// 
//
