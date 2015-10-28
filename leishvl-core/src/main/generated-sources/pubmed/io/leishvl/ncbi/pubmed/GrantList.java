//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 03:53:29 PM CET 
//


package io.leishvl.ncbi.pubmed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    "grant"
})
@XmlRootElement(name = "GrantList")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
public class GrantList {

    @XmlAttribute(name = "CompleteYN")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected String completeYN;
    @XmlElement(name = "Grant", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected List<Grant> grant;

    /**
     * Gets the value of the completeYN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public String getCompleteYN() {
        if (completeYN == null) {
            return "Y";
        } else {
            return completeYN;
        }
    }

    /**
     * Sets the value of the completeYN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public void setCompleteYN(String value) {
        this.completeYN = value;
    }

    /**
     * Gets the value of the grant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the grant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGrant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Grant }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public List<Grant> getGrant() {
        if (grant == null) {
            grant = new ArrayList<Grant>();
        }
        return this.grant;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public GrantList withCompleteYN(String value) {
        setCompleteYN(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public GrantList withGrant(Grant... values) {
        if (values!= null) {
            for (Grant value: values) {
                getGrant().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public GrantList withGrant(Collection<Grant> values) {
        if (values!= null) {
            getGrant().addAll(values);
        }
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
