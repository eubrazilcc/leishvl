//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.04 at 10:30:07 PM CET 
//


package io.leishvl.core.tapir;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for contactRoleType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="contactRoleType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="data administrator"/&gt;
 *     &lt;enumeration value="system administrator"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "contactRoleType")
@XmlEnum
@Generated(value = "com.sun.tools.xjc.Driver", date = "2015-11-04T10:30:07+01:00", comments = "JAXB RI v2.2.11")
public enum ContactRoleType {

    @XmlEnumValue("data administrator")
    DATA_ADMINISTRATOR("data administrator"),
    @XmlEnumValue("system administrator")
    SYSTEM_ADMINISTRATOR("system administrator");
    private final String value;

    ContactRoleType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ContactRoleType fromValue(String v) {
        for (ContactRoleType c: ContactRoleType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}