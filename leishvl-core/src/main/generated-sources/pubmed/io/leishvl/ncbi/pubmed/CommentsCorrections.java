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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "refSource",
    "pmid",
    "note"
})
@XmlRootElement(name = "CommentsCorrections")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
public class CommentsCorrections {

    @XmlAttribute(name = "RefType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected String refType;
    @XmlElement(name = "RefSource", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected String refSource;
    @XmlElement(name = "PMID")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected PMID pmid;
    @XmlElement(name = "Note")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected String note;

    /**
     * Gets the value of the refType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public String getRefType() {
        return refType;
    }

    /**
     * Sets the value of the refType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public void setRefType(String value) {
        this.refType = value;
    }

    /**
     * Gets the value of the refSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public String getRefSource() {
        return refSource;
    }

    /**
     * Sets the value of the refSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public void setRefSource(String value) {
        this.refSource = value;
    }

    /**
     * Gets the value of the pmid property.
     * 
     * @return
     *     possible object is
     *     {@link PMID }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public PMID getPMID() {
        return pmid;
    }

    /**
     * Sets the value of the pmid property.
     * 
     * @param value
     *     allowed object is
     *     {@link PMID }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public void setPMID(PMID value) {
        this.pmid = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public void setNote(String value) {
        this.note = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public CommentsCorrections withRefType(String value) {
        setRefType(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public CommentsCorrections withRefSource(String value) {
        setRefSource(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public CommentsCorrections withPMID(PMID value) {
        setPMID(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public CommentsCorrections withNote(String value) {
        setNote(value);
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
