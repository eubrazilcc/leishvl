//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.04 at 10:30:05 PM CET 
//


package io.leishvl.core.ncbi.pubmed;

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
    "history",
    "publicationStatus",
    "articleIdList",
    "objectList"
})
@XmlRootElement(name = "PubmedData")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
public class PubmedData {

    @XmlElement(name = "History")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    protected History history;
    @XmlElement(name = "PublicationStatus", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    protected String publicationStatus;
    @XmlElement(name = "ArticleIdList", required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    protected ArticleIdList articleIdList;
    @XmlElement(name = "ObjectList")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    protected ObjectList objectList;

    /**
     * Gets the value of the history property.
     * 
     * @return
     *     possible object is
     *     {@link History }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public History getHistory() {
        return history;
    }

    /**
     * Sets the value of the history property.
     * 
     * @param value
     *     allowed object is
     *     {@link History }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public void setHistory(History value) {
        this.history = value;
    }

    /**
     * Gets the value of the publicationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public String getPublicationStatus() {
        return publicationStatus;
    }

    /**
     * Sets the value of the publicationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public void setPublicationStatus(String value) {
        this.publicationStatus = value;
    }

    /**
     * Gets the value of the articleIdList property.
     * 
     * @return
     *     possible object is
     *     {@link ArticleIdList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public ArticleIdList getArticleIdList() {
        return articleIdList;
    }

    /**
     * Sets the value of the articleIdList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArticleIdList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public void setArticleIdList(ArticleIdList value) {
        this.articleIdList = value;
    }

    /**
     * Gets the value of the objectList property.
     * 
     * @return
     *     possible object is
     *     {@link ObjectList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public ObjectList getObjectList() {
        return objectList;
    }

    /**
     * Sets the value of the objectList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectList }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public void setObjectList(ObjectList value) {
        this.objectList = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public PubmedData withHistory(History value) {
        setHistory(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public PubmedData withPublicationStatus(String value) {
        setPublicationStatus(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public PubmedData withArticleIdList(ArticleIdList value) {
        setArticleIdList(value);
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public PubmedData withObjectList(ObjectList value) {
        setObjectList(value);
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(java.lang.Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:05+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
