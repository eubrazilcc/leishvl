//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.30 at 11:28:41 PM CEST 
//


package io.leishvl.core.xml.ncbi.gb;

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
    "gbAltSeqDataName",
    "gbAltSeqDataItems"
})
@XmlRootElement(name = "GBAltSeqData")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
public class GBAltSeqData {

    @XmlElement(name = "GBAltSeqData_name", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    protected String gbAltSeqDataName;
    @XmlElement(name = "GBAltSeqData_items")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    protected GBAltSeqDataItems gbAltSeqDataItems;

    /**
     * Gets the value of the gbAltSeqDataName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public String getGBAltSeqDataName() {
        return gbAltSeqDataName;
    }

    /**
     * Sets the value of the gbAltSeqDataName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public void setGBAltSeqDataName(String value) {
        this.gbAltSeqDataName = value;
    }

    /**
     * Gets the value of the gbAltSeqDataItems property.
     * 
     * @return
     *     possible object is
     *     {@link GBAltSeqDataItems }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public GBAltSeqDataItems getGBAltSeqDataItems() {
        return gbAltSeqDataItems;
    }

    /**
     * Sets the value of the gbAltSeqDataItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link GBAltSeqDataItems }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public void setGBAltSeqDataItems(GBAltSeqDataItems value) {
        this.gbAltSeqDataItems = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public GBAltSeqData withGBAltSeqDataName(String value) {
        setGBAltSeqDataName(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public GBAltSeqData withGBAltSeqDataItems(GBAltSeqDataItems value) {
        setGBAltSeqDataItems(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
