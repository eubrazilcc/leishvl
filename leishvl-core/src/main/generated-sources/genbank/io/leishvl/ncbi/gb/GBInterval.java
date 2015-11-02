//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.02 at 11:45:03 AM CET 
//


package io.leishvl.ncbi.gb;

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
    "gbIntervalFrom",
    "gbIntervalTo",
    "gbIntervalPoint",
    "gbIntervalIscomp",
    "gbIntervalInterbp",
    "gbIntervalAccession"
})
@XmlRootElement(name = "GBInterval")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
public class GBInterval {

    @XmlElement(name = "GBInterval_from")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    protected String gbIntervalFrom;
    @XmlElement(name = "GBInterval_to")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    protected String gbIntervalTo;
    @XmlElement(name = "GBInterval_point")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    protected String gbIntervalPoint;
    @XmlElement(name = "GBInterval_iscomp")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    protected GBIntervalIscomp gbIntervalIscomp;
    @XmlElement(name = "GBInterval_interbp")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    protected GBIntervalInterbp gbIntervalInterbp;
    @XmlElement(name = "GBInterval_accession", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    protected String gbIntervalAccession;

    /**
     * Gets the value of the gbIntervalFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public String getGBIntervalFrom() {
        return gbIntervalFrom;
    }

    /**
     * Sets the value of the gbIntervalFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public void setGBIntervalFrom(String value) {
        this.gbIntervalFrom = value;
    }

    /**
     * Gets the value of the gbIntervalTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public String getGBIntervalTo() {
        return gbIntervalTo;
    }

    /**
     * Sets the value of the gbIntervalTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public void setGBIntervalTo(String value) {
        this.gbIntervalTo = value;
    }

    /**
     * Gets the value of the gbIntervalPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public String getGBIntervalPoint() {
        return gbIntervalPoint;
    }

    /**
     * Sets the value of the gbIntervalPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public void setGBIntervalPoint(String value) {
        this.gbIntervalPoint = value;
    }

    /**
     * Gets the value of the gbIntervalIscomp property.
     * 
     * @return
     *     possible object is
     *     {@link GBIntervalIscomp }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBIntervalIscomp getGBIntervalIscomp() {
        return gbIntervalIscomp;
    }

    /**
     * Sets the value of the gbIntervalIscomp property.
     * 
     * @param value
     *     allowed object is
     *     {@link GBIntervalIscomp }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public void setGBIntervalIscomp(GBIntervalIscomp value) {
        this.gbIntervalIscomp = value;
    }

    /**
     * Gets the value of the gbIntervalInterbp property.
     * 
     * @return
     *     possible object is
     *     {@link GBIntervalInterbp }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBIntervalInterbp getGBIntervalInterbp() {
        return gbIntervalInterbp;
    }

    /**
     * Sets the value of the gbIntervalInterbp property.
     * 
     * @param value
     *     allowed object is
     *     {@link GBIntervalInterbp }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public void setGBIntervalInterbp(GBIntervalInterbp value) {
        this.gbIntervalInterbp = value;
    }

    /**
     * Gets the value of the gbIntervalAccession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public String getGBIntervalAccession() {
        return gbIntervalAccession;
    }

    /**
     * Sets the value of the gbIntervalAccession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public void setGBIntervalAccession(String value) {
        this.gbIntervalAccession = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBInterval withGBIntervalFrom(String value) {
        setGBIntervalFrom(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBInterval withGBIntervalTo(String value) {
        setGBIntervalTo(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBInterval withGBIntervalPoint(String value) {
        setGBIntervalPoint(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBInterval withGBIntervalIscomp(GBIntervalIscomp value) {
        setGBIntervalIscomp(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBInterval withGBIntervalInterbp(GBIntervalInterbp value) {
        setGBIntervalInterbp(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBInterval withGBIntervalAccession(String value) {
        setGBIntervalAccession(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
