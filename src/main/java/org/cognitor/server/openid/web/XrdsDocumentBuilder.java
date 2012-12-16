package org.cognitor.server.openid.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * User: patrick
 * Date: 27.11.12
 */
public class XrdsDocumentBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(XrdsDocumentBuilder.class);
    private Document document;

    private static final String XRDS_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<xrds:XRDS xmlns:xrds=\"xri://$xrds\">"
            + "<XRD xmlns=\"xri://$xrd*($v*2.0)\">"
            + "</XRD>"
            + "</xrds:XRDS>";


    public XrdsDocumentBuilder() {
        document = this.createXrdsTemplate();
    }

    public String getDocumentAsString() {
        try {
            return transformToString(document);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Error while creating xrds document.", exception);
        }
    }

    private String transformToString(Document document) {
        StringWriter writer = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException exception) {
            LOGGER.error("Unable to transform xrds document to string.", exception);
            return "";
        }
    }

    private Document createXrdsTemplate() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(XRDS_TEMPLATE.getBytes());
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (ParserConfigurationException exception) {
            String message = "For some reason the default parser configuration does not work anymore.";
            LOGGER.error(message, exception);
            throw new IllegalStateException(message, exception);
        } catch (Exception exception) {
            String message = "An error happened while trying to parse the Xrds document template.";
            LOGGER.error(message, exception);
            throw new IllegalArgumentException(message, exception);
        }
    }

    public XrdsDocumentBuilder addSignOnSerice(String uri) {
        return addService("http://specs.openid.net/auth/2.0/signon", uri);
    }

    public XrdsDocumentBuilder addServerService(String uri) {
        return addService("http://specs.openid.net/auth/2.0/server", uri);
    }

    private XrdsDocumentBuilder addService(String type, String uri) {
        if (uri == null || uri.isEmpty()) {
            throw new IllegalArgumentException("Uri must have a value.");
        }
        Element serviceElement = createServiceElement();
        Element typeElement = createElement("Type", type);
        serviceElement.appendChild(typeElement);
        Element uriElement = createElement("URI", uri);
        serviceElement.appendChild(uriElement);
        Node xrdElement = document.getElementsByTagName("XRD").item(0);
        xrdElement.appendChild(serviceElement);
        return this;
    }

    private Element createServiceElement() {
        Element serviceElement = document.createElement("Service");
        serviceElement.setAttribute("priority", "0");
        return serviceElement;
    }

    private Element createElement(String type, String content) {
        Element element = document.createElement(type);
        element.setTextContent(content);
        return element;
    }
}
