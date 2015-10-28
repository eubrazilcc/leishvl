//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 03:53:28 PM CET 
//


package io.leishvl.ncbi.esearch;

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
    "countOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR",
    "errorList",
    "warningList"
})
@XmlRootElement(name = "eSearchResult")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
public class ESearchResult {

    @XmlElements({
        @XmlElement(name = "Count", required = true, type = Count.class),
        @XmlElement(name = "RetMax", required = true, type = RetMax.class),
        @XmlElement(name = "RetStart", required = true, type = RetStart.class),
        @XmlElement(name = "QueryKey", required = true, type = QueryKey.class),
        @XmlElement(name = "WebEnv", required = true, type = WebEnv.class),
        @XmlElement(name = "IdList", required = true, type = IdList.class),
        @XmlElement(name = "TranslationSet", required = true, type = TranslationSet.class),
        @XmlElement(name = "TranslationStack", required = true, type = TranslationStack.class),
        @XmlElement(name = "QueryTranslation", required = true, type = QueryTranslation.class),
        @XmlElement(name = "ERROR", required = true, type = ERROR.class)
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    protected List<Object> countOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR;
    @XmlElement(name = "ErrorList")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    protected ErrorList errorList;
    @XmlElement(name = "WarningList")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    protected WarningList warningList;

    /**
     * Gets the value of the countOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the countOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Count }
     * {@link RetMax }
     * {@link RetStart }
     * {@link QueryKey }
     * {@link WebEnv }
     * {@link IdList }
     * {@link TranslationSet }
     * {@link TranslationStack }
     * {@link QueryTranslation }
     * {@link ERROR }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public List<Object> getCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR() {
        if (countOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR == null) {
            countOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR = new ArrayList<Object>();
        }
        return this.countOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR;
    }

    /**
     * Gets the value of the errorList property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public ErrorList getErrorList() {
        return errorList;
    }

    /**
     * Sets the value of the errorList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public void setErrorList(ErrorList value) {
        this.errorList = value;
    }

    /**
     * Gets the value of the warningList property.
     * 
     * @return
     *     possible object is
     *     {@link WarningList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public WarningList getWarningList() {
        return warningList;
    }

    /**
     * Sets the value of the warningList property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarningList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public void setWarningList(WarningList value) {
        this.warningList = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public ESearchResult withCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR(Object... values) {
        if (values!= null) {
            for (Object value: values) {
                getCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public ESearchResult withCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR(Collection<Object> values) {
        if (values!= null) {
            getCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR().addAll(values);
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public ESearchResult withErrorList(ErrorList value) {
        setErrorList(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public ESearchResult withWarningList(WarningList value) {
        setWarningList(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-10-28T03:53:28+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
