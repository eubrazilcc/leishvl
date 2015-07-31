//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.30 at 11:28:41 PM CEST 
//


package io.leishvl.core.xml.ncbi.pubmed;

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
    "chemical"
})
@XmlRootElement(name = "ChemicalList")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
public class ChemicalList {

    @XmlElement(name = "Chemical", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    protected List<Chemical> chemical;

    /**
     * Gets the value of the chemical property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chemical property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChemical().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Chemical }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public List<Chemical> getChemical() {
        if (chemical == null) {
            chemical = new ArrayList<Chemical>();
        }
        return this.chemical;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public ChemicalList withChemical(Chemical... values) {
        if (values!= null) {
            for (Chemical value: values) {
                getChemical().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:41+02:00", comments = "JAXB RI v2.2.11")
    public ChemicalList withChemical(Collection<Chemical> values) {
        if (values!= null) {
            getChemical().addAll(values);
        }
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
