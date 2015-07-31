//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.30 at 11:28:42 PM CEST 
//


package io.leishvl.core.xml.ncbi.taxonomy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
    "modId",
    "modType",
    "modName",
    "modGBhidden",
    "rModIdOrRTaxId"
})
@XmlRootElement(name = "Modifier")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
public class Modifier {

    @XmlElement(name = "ModId", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    protected String modId;
    @XmlElement(name = "ModType", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    protected String modType;
    @XmlElement(name = "ModName", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    protected String modName;
    @XmlElement(name = "ModGBhidden", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    protected String modGBhidden;
    @XmlElements({
        @XmlElement(name = "RModId", type = RModId.class),
        @XmlElement(name = "RTaxId", type = RTaxId.class)
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    protected List<Object> rModIdOrRTaxId;

    /**
     * Gets the value of the modId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public String getModId() {
        return modId;
    }

    /**
     * Sets the value of the modId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public void setModId(String value) {
        this.modId = value;
    }

    /**
     * Gets the value of the modType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public String getModType() {
        return modType;
    }

    /**
     * Sets the value of the modType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public void setModType(String value) {
        this.modType = value;
    }

    /**
     * Gets the value of the modName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public String getModName() {
        return modName;
    }

    /**
     * Sets the value of the modName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public void setModName(String value) {
        this.modName = value;
    }

    /**
     * Gets the value of the modGBhidden property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public String getModGBhidden() {
        return modGBhidden;
    }

    /**
     * Sets the value of the modGBhidden property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public void setModGBhidden(String value) {
        this.modGBhidden = value;
    }

    /**
     * Gets the value of the rModIdOrRTaxId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rModIdOrRTaxId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRModIdOrRTaxId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RModId }
     * {@link RTaxId }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public List<Object> getRModIdOrRTaxId() {
        if (rModIdOrRTaxId == null) {
            rModIdOrRTaxId = new ArrayList<Object>();
        }
        return this.rModIdOrRTaxId;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public Modifier withModId(String value) {
        setModId(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public Modifier withModType(String value) {
        setModType(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public Modifier withModName(String value) {
        setModName(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public Modifier withModGBhidden(String value) {
        setModGBhidden(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public Modifier withRModIdOrRTaxId(Object... values) {
        if (values!= null) {
            for (Object value: values) {
                getRModIdOrRTaxId().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public Modifier withRModIdOrRTaxId(Collection<Object> values) {
        if (values!= null) {
            getRModIdOrRTaxId().addAll(values);
        }
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:42+02:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}