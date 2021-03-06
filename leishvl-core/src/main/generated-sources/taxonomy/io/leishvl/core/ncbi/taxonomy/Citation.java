//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.04 at 10:30:06 PM CET 
//


package io.leishvl.core.ncbi.taxonomy;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "citId",
    "citKey",
    "citUrl",
    "citText",
    "citPubmedId",
    "citMedlineId"
})
@XmlRootElement(name = "Citation")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
public class Citation {

    @XmlElement(name = "CitId", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    protected String citId;
    @XmlElement(name = "CitKey", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    protected String citKey;
    @XmlElement(name = "CitUrl")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    protected String citUrl;
    @XmlElement(name = "CitText")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    protected String citText;
    @XmlElement(name = "CitPubmedId")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    protected String citPubmedId;
    @XmlElement(name = "CitMedlineId")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    protected String citMedlineId;

    /**
     * Gets the value of the citId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public String getCitId() {
        return citId;
    }

    /**
     * Sets the value of the citId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public void setCitId(String value) {
        this.citId = value;
    }

    /**
     * Gets the value of the citKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public String getCitKey() {
        return citKey;
    }

    /**
     * Sets the value of the citKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public void setCitKey(String value) {
        this.citKey = value;
    }

    /**
     * Gets the value of the citUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public String getCitUrl() {
        return citUrl;
    }

    /**
     * Sets the value of the citUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public void setCitUrl(String value) {
        this.citUrl = value;
    }

    /**
     * Gets the value of the citText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public String getCitText() {
        return citText;
    }

    /**
     * Sets the value of the citText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public void setCitText(String value) {
        this.citText = value;
    }

    /**
     * Gets the value of the citPubmedId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public String getCitPubmedId() {
        return citPubmedId;
    }

    /**
     * Sets the value of the citPubmedId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public void setCitPubmedId(String value) {
        this.citPubmedId = value;
    }

    /**
     * Gets the value of the citMedlineId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public String getCitMedlineId() {
        return citMedlineId;
    }

    /**
     * Sets the value of the citMedlineId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public void setCitMedlineId(String value) {
        this.citMedlineId = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public Citation withCitId(String value) {
        setCitId(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public Citation withCitKey(String value) {
        setCitKey(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public Citation withCitUrl(String value) {
        setCitUrl(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public Citation withCitText(String value) {
        setCitText(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public Citation withCitPubmedId(String value) {
        setCitPubmedId(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public Citation withCitMedlineId(String value) {
        setCitMedlineId(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:06+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
