//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.02 at 11:45:08 AM CET 
//


package io.leishvl.dwc.simple;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the io.leishvl.dwc.simple package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AnyExtension_QNAME = new QName("http://rs.tdwg.org/dwc/xsd/simpledarwincore/", "anyExtension");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: io.leishvl.dwc.simple
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SimpleDarwinRecordSet }
     * 
     */
    public SimpleDarwinRecordSet createSimpleDarwinRecordSet() {
        return new SimpleDarwinRecordSet();
    }

    /**
     * Create an instance of {@link SimpleDarwinRecord }
     * 
     */
    public SimpleDarwinRecord createSimpleDarwinRecord() {
        return new SimpleDarwinRecord();
    }

    /**
     * Create an instance of {@link SimpleDarwinExtensions }
     * 
     */
    public SimpleDarwinExtensions createSimpleDarwinExtensions() {
        return new SimpleDarwinExtensions();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rs.tdwg.org/dwc/xsd/simpledarwincore/", name = "anyExtension", substitutionHeadNamespace = "http://rs.tdwg.org/dwc/terms/", substitutionHeadName = "anyClass")
    public JAXBElement<Object> createAnyExtension(Object value) {
        return new JAXBElement<Object>(_AnyExtension_QNAME, Object.class, null, value);
    }

}
