package sugar.spring.framework.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class XmlUtils {
    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private static DocumentBuilder builder;

    static {
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document readXml(InputStream inputStream) throws IOException, SAXException {
        return builder.parse(inputStream);
    }

    public XmlUtils() {
    }
}
