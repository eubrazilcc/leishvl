//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.04 at 10:30:07 PM CET 
//


package io.leishvl.core.tapir.geo;

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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2003/01/geo/wgs84_pos#}lat"/&gt;
 *         &lt;element ref="{http://www.w3.org/2003/01/geo/wgs84_pos#}long"/&gt;
 *         &lt;element ref="{http://www.w3.org/2003/01/geo/wgs84_pos#}alt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "lat",
    "_long",
    "alt"
})
@XmlRootElement(name = "Point")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
public class Point {

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected float lat;
    @XmlElement(name = "long")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected float _long;
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    protected Float alt;

    /**
     * Gets the value of the lat property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public float getLat() {
        return lat;
    }

    /**
     * Sets the value of the lat property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setLat(float value) {
        this.lat = value;
    }

    /**
     * Gets the value of the long property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public float getLong() {
        return _long;
    }

    /**
     * Sets the value of the long property.
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setLong(float value) {
        this._long = value;
    }

    /**
     * Gets the value of the alt property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public Float getAlt() {
        return alt;
    }

    /**
     * Sets the value of the alt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public void setAlt(Float value) {
        this.alt = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public Point withLat(float value) {
        setLat(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public Point withLong(float value) {
        setLong(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
    public Point withAlt(Float value) {
        setAlt(value);
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
