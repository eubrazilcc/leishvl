//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.30 at 11:28:43 PM CEST 
//


package io.leishvl.core.xml.tdwg.tapir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * A complex type for multiple logical operators.
 * 
 * <p>Java class for multiLOPType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="multiLOPType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence maxOccurs="unbounded" minOccurs="2"&gt;
 *         &lt;element ref="{http://rs.tdwg.org/tapir/1.0}booleanOP"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "multiLOPType", propOrder = {
    "booleanOP"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
public class MultiLOPType {

    @XmlElementRef(name = "booleanOP", namespace = "http://rs.tdwg.org/tapir/1.0", type = JAXBElement.class)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
    protected List<JAXBElement<?>> booleanOP;

    /**
     * Gets the value of the booleanOP property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the booleanOP property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBooleanOP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link BinaryCOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link BinaryCOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link Not }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link BinaryCOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link MultiCOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link BinaryCOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnaryCOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link BinaryCOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link MultiLOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link MultiLOPType }{@code >}
     * {@link JAXBElement }{@code <}{@link BinaryCOPType }{@code >}
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
    public List<JAXBElement<?>> getBooleanOP() {
        if (booleanOP == null) {
            booleanOP = new ArrayList<JAXBElement<?>>();
        }
        return this.booleanOP;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
    public MultiLOPType withBooleanOP(JAXBElement<?> ... values) {
        if (values!= null) {
            for (JAXBElement<?> value: values) {
                getBooleanOP().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
    public MultiLOPType withBooleanOP(Collection<JAXBElement<?>> values) {
        if (values!= null) {
            getBooleanOP().addAll(values);
        }
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-07-30T11:28:43+02:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
