//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.02 at 11:45:04 AM CET 
//


package io.leishvl.ncbi.taxonomy;

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
    "mgcId",
    "mgcName"
})
@XmlRootElement(name = "MitoGeneticCode")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
public class MitoGeneticCode {

    @XmlElement(name = "MGCId", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    protected String mgcId;
    @XmlElement(name = "MGCName", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    protected String mgcName;

    /**
     * Gets the value of the mgcId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public String getMGCId() {
        return mgcId;
    }

    /**
     * Sets the value of the mgcId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public void setMGCId(String value) {
        this.mgcId = value;
    }

    /**
     * Gets the value of the mgcName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public String getMGCName() {
        return mgcName;
    }

    /**
     * Sets the value of the mgcName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public void setMGCName(String value) {
        this.mgcName = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public MitoGeneticCode withMGCId(String value) {
        setMGCId(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public MitoGeneticCode withMGCName(String value) {
        setMGCName(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:04+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
