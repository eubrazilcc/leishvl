//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.04 at 10:30:07 PM CET 
//


package io.leishvl.core.tapir.vcard;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2001/vcard-rdf/3.0#}FN"/&gt;
 *         &lt;element ref="{http://www.w3.org/2001/vcard-rdf/3.0#}TITLE" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/2001/vcard-rdf/3.0#}TEL" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/2001/vcard-rdf/3.0#}EMAIL"/&gt;
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
@XmlType(name = "", propOrder = {
    "fn",
    "title",
    "tel",
    "email"
})
@XmlRootElement(name = "VCARD")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
public class VCARD {

    @XmlElement(name = "FN", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected SimpleElement fn;
    @XmlElement(name = "TITLE")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected SimpleElement title;
    @XmlElement(name = "TEL")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected TEL tel;
    @XmlElement(name = "EMAIL", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected EMAIL email;
    @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected String lang;

    /**
     * Gets the value of the fn property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleElement }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public SimpleElement getFN() {
        return fn;
    }

    /**
     * Sets the value of the fn property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleElement }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setFN(SimpleElement value) {
        this.fn = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link SimpleElement }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public SimpleElement getTITLE() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link SimpleElement }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setTITLE(SimpleElement value) {
        this.title = value;
    }

    /**
     * Gets the value of the tel property.
     * 
     * @return
     *     possible object is
     *     {@link TEL }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public TEL getTEL() {
        return tel;
    }

    /**
     * Sets the value of the tel property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEL }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setTEL(TEL value) {
        this.tel = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link EMAIL }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public EMAIL getEMAIL() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link EMAIL }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setEMAIL(EMAIL value) {
        this.email = value;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
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
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setLang(String value) {
        this.lang = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public VCARD withFN(SimpleElement value) {
        setFN(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public VCARD withTITLE(SimpleElement value) {
        setTITLE(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public VCARD withTEL(TEL value) {
        setTEL(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public VCARD withEMAIL(EMAIL value) {
        setEMAIL(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public VCARD withLang(String value) {
        setLang(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
