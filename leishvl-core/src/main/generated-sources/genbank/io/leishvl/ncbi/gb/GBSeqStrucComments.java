//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.02 at 11:45:03 AM CET 
//


package io.leishvl.ncbi.gb;

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
    "gbStrucComment"
})
@XmlRootElement(name = "GBSeq_struc-comments")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
public class GBSeqStrucComments {

    @XmlElement(name = "GBStrucComment")
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    protected List<GBStrucComment> gbStrucComment;

    /**
     * Gets the value of the gbStrucComment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gbStrucComment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGBStrucComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GBStrucComment }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public List<GBStrucComment> getGBStrucComment() {
        if (gbStrucComment == null) {
            gbStrucComment = new ArrayList<GBStrucComment>();
        }
        return this.gbStrucComment;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBSeqStrucComments withGBStrucComment(GBStrucComment... values) {
        if (values!= null) {
            for (GBStrucComment value: values) {
                getGBStrucComment().add(value);
            }
        }
        return this;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public GBSeqStrucComments withGBStrucComment(Collection<GBStrucComment> values) {
        if (values!= null) {
            getGBStrucComment().addAll(values);
        }
        return this;
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
    }

    @Override
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-02T11:45:03+01:00", comments = "JAXB RI v2.2.11")
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
