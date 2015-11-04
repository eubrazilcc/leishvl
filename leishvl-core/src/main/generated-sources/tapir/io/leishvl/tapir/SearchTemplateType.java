//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.02 at 11:45:06 AM CET 
//


package io.leishvl.tapir;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * TAPIR can use a template for configuring search requests. 
 * 				The Template is accessed, as an external document, by its URI
 * 
 * <p>Java class for searchTemplateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="searchTemplateType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://rs.tdwg.org/tapir/1.0}extResourceDocumentationType"&gt;
 *       &lt;sequence&gt;
 *         &lt;group ref="{http://rs.tdwg.org/tapir/1.0}searchTemplateGroup"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "searchTemplateType", propOrder = {
    "externalOutputModel",
    "outputModel",
    "filter",
    "orderBy"
})
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
public class SearchTemplateType
    extends ExtResourceDocumentationType
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    protected ExternalResourceType externalOutputModel;
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    protected OutputModelType outputModel;
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    protected Filter filter;
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    protected io.leishvl.tapir.RequestType.Search.OrderBy orderBy;

    /**
     * Gets the value of the externalOutputModel property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalResourceType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public ExternalResourceType getExternalOutputModel() {
        return externalOutputModel;
    }

    /**
     * Sets the value of the externalOutputModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalResourceType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public void setExternalOutputModel(ExternalResourceType value) {
        this.externalOutputModel = value;
    }

    /**
     * Gets the value of the outputModel property.
     * 
     * @return
     *     possible object is
     *     {@link OutputModelType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public OutputModelType getOutputModel() {
        return outputModel;
    }

    /**
     * Sets the value of the outputModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link OutputModelType }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public void setOutputModel(OutputModelType value) {
        this.outputModel = value;
    }

    /**
     * Gets the value of the filter property.
     * 
     * @return
     *     possible object is
     *     {@link Filter }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public Filter getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Filter }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public void setFilter(Filter value) {
        this.filter = value;
    }

    /**
     * Gets the value of the orderBy property.
     * 
     * @return
     *     possible object is
     *     {@link io.leishvl.tapir.RequestType.Search.OrderBy }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public io.leishvl.tapir.RequestType.Search.OrderBy getOrderBy() {
        return orderBy;
    }

    /**
     * Sets the value of the orderBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link io.leishvl.tapir.RequestType.Search.OrderBy }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public void setOrderBy(io.leishvl.tapir.RequestType.Search.OrderBy value) {
        this.orderBy = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public SearchTemplateType withExternalOutputModel(ExternalResourceType value) {
        setExternalOutputModel(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public SearchTemplateType withOutputModel(OutputModelType value) {
        setOutputModel(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public SearchTemplateType withFilter(Filter value) {
        setFilter(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public SearchTemplateType withOrderBy(io.leishvl.tapir.RequestType.Search.OrderBy value) {
        setOrderBy(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public SearchTemplateType withLabel(String value) {
        setLabel(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public SearchTemplateType withDocumentation(String value) {
        setDocumentation(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:06+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}