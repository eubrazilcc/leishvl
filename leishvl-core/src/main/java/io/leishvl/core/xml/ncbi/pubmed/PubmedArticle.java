//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.30 at 11:28:41 PM CEST 
//


package io.leishvl.core.xml.ncbi.pubmed;

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
    "medlineCitation",
    "pubmedData"
})
@XmlRootElement(name = "PubmedArticle")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
public class PubmedArticle {

    @XmlElement(name = "MedlineCitation", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    protected MedlineCitation medlineCitation;
    @XmlElement(name = "PubmedData")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    protected PubmedData pubmedData;

    /**
     * Gets the value of the medlineCitation property.
     * 
     * @return
     *     possible object is
     *     {@link MedlineCitation }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public MedlineCitation getMedlineCitation() {
        return medlineCitation;
    }

    /**
     * Sets the value of the medlineCitation property.
     * 
     * @param value
     *     allowed object is
     *     {@link MedlineCitation }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public void setMedlineCitation(MedlineCitation value) {
        this.medlineCitation = value;
    }

    /**
     * Gets the value of the pubmedData property.
     * 
     * @return
     *     possible object is
     *     {@link PubmedData }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public PubmedData getPubmedData() {
        return pubmedData;
    }

    /**
     * Sets the value of the pubmedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link PubmedData }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public void setPubmedData(PubmedData value) {
        this.pubmedData = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public PubmedArticle withMedlineCitation(MedlineCitation value) {
        setMedlineCitation(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public PubmedArticle withPubmedData(PubmedData value) {
        setPubmedData(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public boolean equals(java.lang.Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
