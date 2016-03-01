package lstevens.xslt;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;


public class XsltTransform {

    public static void main(String[] args){
        System.out.println("Hey.");
    }

    /**
     * @param XSLpath Path to XSL file to run.
     * @param XMLIn Path to input XML file.
     * @param XMLOut Path to output XML file (use dummy for result-doc driven).
     * @throws IOException if an I/O error occurs.
     * @throws TransformerException if there is a problem with the XSLT.
     */
    public void transform(String XSLpath, String XMLIn, String XMLOut)
        throws IOException, TransformerException {

        StreamSource xmlInput = new StreamSource(
            new File(XMLIn));
        StreamSource xsl = new StreamSource(
            new File(XSLpath));
        StreamResult xmlOutput = new StreamResult(
            new File(XMLOut));
        Transformer transformer = TransformerFactory.newInstance(
            ).newTransformer(xsl);
        // TODO: accept an optional parameters hashmap and apply them if present
        transformer.setParameter("output_prefix", "test_");
        transformer.transform(xmlInput, xmlOutput);
    }
}

