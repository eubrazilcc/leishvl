//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 03:53:32 PM CET 
//


package io.leishvl.tapir.dc.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 
 *             This is the default type for all of the DC elements.
 *             It permits text content only with optional
 *             xml:lang attribute.
 *             Text is allowed because mixed="true", but sub-elements
 *             are disallowed because minOccurs="0" and maxOccurs="0" 
 *             are on the xs:any tag.
 * 
 *     	    This complexType allows for restriction or extension permitting
 *             child elements.
 *     	
 * 
 * <p>Java class for SimpleLiteral complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SimpleLiteral"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;any processContents='lax' maxOccurs="0" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SimpleLiteral", propOrder = {
    "content"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
public class SimpleLiteral {

    @XmlMixed
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    protected List<String> content;
    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    protected String lang;

    /**
     * 
     *             This is the default type for all of the DC elements.
     *             It permits text content only with optional
     *             xml:lang attribute.
     *             Text is allowed because mixed="true", but sub-elements
     *             are disallowed because minOccurs="0" and maxOccurs="0" 
     *             are on the xs:any tag.
     * 
     *     	    This complexType allows for restriction or extension permitting
     *             child elements.
     *     	Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public List<String> getContent() {
        if (content == null) {
            content = new ArrayList<String>();
        }
        return this.content;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public void setLang(String value) {
        this.lang = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public SimpleLiteral withContent(String... values) {
        if (values!= null) {
            for (String value: values) {
                getContent().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public SimpleLiteral withContent(Collection<String> values) {
        if (values!= null) {
            getContent().addAll(values);
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public SimpleLiteral withLang(String value) {
        setLang(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:32+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
