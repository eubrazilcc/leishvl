//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 03:53:28 PM CET 
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
    "gbInterval"
})
@XmlRootElement(name = "GBAltSeqItem_interval")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
public class GBAltSeqItemInterval {

    @XmlElement(name = "GBInterval", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    protected GBInterval gbInterval;

    /**
     * Gets the value of the gbInterval property.
     * 
     * @return
     *     possible object is
     *     {@link GBInterval }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public GBInterval getGBInterval() {
        return gbInterval;
    }

    /**
     * Sets the value of the gbInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link GBInterval }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public void setGBInterval(GBInterval value) {
        this.gbInterval = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public GBAltSeqItemInterval withGBInterval(GBInterval value) {
        setGBInterval(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
