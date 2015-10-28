//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 03:53:29 PM CET 
//


package io.leishvl.ncbi.taxonomy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    "taxon"
})
@XmlRootElement(name = "TaxaSet")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
public class TaxaSet {

    @XmlElement(name = "Taxon")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    protected List<Taxon> taxon;

    /**
     * Gets the value of the taxon property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taxon property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaxon().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Taxon }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public List<Taxon> getTaxon() {
        if (taxon == null) {
            taxon = new ArrayList<Taxon>();
        }
        return this.taxon;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public TaxaSet withTaxon(Taxon... values) {
        if (values!= null) {
            for (Taxon value: values) {
                getTaxon().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public TaxaSet withTaxon(Collection<Taxon> values) {
        if (values!= null) {
            getTaxon().addAll(values);
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
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:29+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
