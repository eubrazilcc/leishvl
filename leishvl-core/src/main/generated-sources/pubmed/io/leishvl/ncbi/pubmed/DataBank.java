//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 03:53:29 PM CET 
//


package io.leishvl.ncbi.pubmed;

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
    "dataBankName",
    "accessionNumberList"
})
@XmlRootElement(name = "DataBank")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
public class DataBank {

    @XmlElement(name = "DataBankName", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected String dataBankName;
    @XmlElement(name = "AccessionNumberList")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected AccessionNumberList accessionNumberList;

    /**
     * Gets the value of the dataBankName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public String getDataBankName() {
        return dataBankName;
    }

    /**
     * Sets the value of the dataBankName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public void setDataBankName(String value) {
        this.dataBankName = value;
    }

    /**
     * Gets the value of the accessionNumberList property.
     * 
     * @return
     *     possible object is
     *     {@link AccessionNumberList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public AccessionNumberList getAccessionNumberList() {
        return accessionNumberList;
    }

    /**
     * Sets the value of the accessionNumberList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessionNumberList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public void setAccessionNumberList(AccessionNumberList value) {
        this.accessionNumberList = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public DataBank withDataBankName(String value) {
        setDataBankName(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public DataBank withAccessionNumberList(AccessionNumberList value) {
        setAccessionNumberList(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(java.lang.Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
