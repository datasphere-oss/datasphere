package com.huahui.datasphere.mdm.security;

import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


/**
 * Пароль пользователя
 * 
 * <p>Java class for UserPasswordDef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserPasswordDef"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="passwordText" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="createdBy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="updatedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="createdAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *       &lt;attribute name="updatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@JsonPropertyOrder({"passwordText","createdBy","updatedBy","active"})
public class UserPasswordDef {

    protected String passwordText;
    protected String createdBy;
    protected String updatedBy;
    protected boolean active;
    @JacksonXmlProperty(isAttribute = true)
    protected XMLGregorianCalendar createdAt;
    @JacksonXmlProperty(isAttribute = true)
    protected XMLGregorianCalendar updatedAt;

    /**
     * Gets the value of the passwordText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordText() {
        return passwordText;
    }

    /**
     * Sets the value of the passwordText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordText(String value) {
        this.passwordText = value;
    }

    /**
     * Gets the value of the createdBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the value of the createdBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    /**
     * Gets the value of the updatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the value of the updatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdatedBy(String value) {
        this.updatedBy = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the createdAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the value of the createdAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedAt(XMLGregorianCalendar value) {
        this.createdAt = value;
    }

    /**
     * Gets the value of the updatedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the value of the updatedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdatedAt(XMLGregorianCalendar value) {
        this.updatedAt = value;
    }

    public UserPasswordDef withPasswordText(String value) {
        setPasswordText(value);
        return this;
    }

    public UserPasswordDef withCreatedBy(String value) {
        setCreatedBy(value);
        return this;
    }

    public UserPasswordDef withUpdatedBy(String value) {
        setUpdatedBy(value);
        return this;
    }

    public UserPasswordDef withActive(boolean value) {
        setActive(value);
        return this;
    }

    public UserPasswordDef withCreatedAt(XMLGregorianCalendar value) {
        setCreatedAt(value);
        return this;
    }

    public UserPasswordDef withUpdatedAt(XMLGregorianCalendar value) {
        setUpdatedAt(value);
        return this;
    }

}
