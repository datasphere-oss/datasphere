package com.huahui.datasphere.mdm.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


/**
 * Пользователь
 * 
 * <p>Java class for UserDef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserDef"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="login" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="createdBy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="updatedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="admin" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="external" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="locale" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="passwords" type="{http://security.mdm.unidata.com/}UserPasswordDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="propertiesValues" type="{http://security.mdm.unidata.com/}PropertyValueDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="roles" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="labels" type="{http://security.mdm.unidata.com/}LabelDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="apis" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="emailNotification" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
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
@JsonPropertyOrder({"login","email","firstName","lastName","notes","createdBy","updatedBy","active","admin","source",
    "external","locale","passwords","propertiesValues","roles","labels","apis","emailNotification"})
public class UserDef {

    protected String login;
    protected String email;
    protected String firstName;
    protected String lastName;
    protected String notes;
    protected String createdBy;
    protected String updatedBy;
    protected boolean active;
    protected boolean admin;
    protected String source;
    protected boolean external;
    protected String locale;
    protected List<UserPasswordDef> passwords;
    protected List<PropertyValueDef> propertiesValues;
    protected List<String> roles;
    protected List<LabelDef> labels;
    protected List<String> apis;
    protected boolean emailNotification;
    @JacksonXmlProperty(isAttribute = true)
    protected XMLGregorianCalendar createdAt;
    @JacksonXmlProperty(isAttribute = true)
    protected XMLGregorianCalendar updatedAt;

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogin(String value) {
        this.login = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
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
     * Gets the value of the admin property.
     * 
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets the value of the admin property.
     * 
     */
    public void setAdmin(boolean value) {
        this.admin = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the external property.
     * 
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * Sets the value of the external property.
     * 
     */
    public void setExternal(boolean value) {
        this.external = value;
    }

    /**
     * Gets the value of the locale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the value of the locale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocale(String value) {
        this.locale = value;
    }

    /**
     * Gets the value of the passwords property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the passwords property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPasswords().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserPasswordDef }
     * 
     * 
     */
    public List<UserPasswordDef> getPasswords() {
        if (passwords == null) {
            passwords = new ArrayList<>();
        }
        return this.passwords;
    }

    /**
     * Gets the value of the propertiesValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertiesValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertiesValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyValueDef }
     * 
     * 
     */
    public List<PropertyValueDef> getPropertiesValues() {
        if (propertiesValues == null) {
            propertiesValues = new ArrayList<>();
        }
        return this.propertiesValues;
    }

    /**
     * Gets the value of the roles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return this.roles;
    }

    /**
     * Gets the value of the labels property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the labels property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLabels().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LabelDef }
     * 
     * 
     */
    public List<LabelDef> getLabels() {
        if (labels == null) {
            labels = new ArrayList<>();
        }
        return this.labels;
    }

    /**
     * Gets the value of the apis property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apis property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApis().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getApis() {
        if (apis == null) {
            apis = new ArrayList<>();
        }
        return this.apis;
    }

    /**
     * Gets the value of the emailNotification property.
     * 
     */
    public boolean isEmailNotification() {
        return emailNotification;
    }

    /**
     * Sets the value of the emailNotification property.
     * 
     */
    public void setEmailNotification(boolean value) {
        this.emailNotification = value;
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

    public UserDef withLogin(String value) {
        setLogin(value);
        return this;
    }

    public UserDef withEmail(String value) {
        setEmail(value);
        return this;
    }

    public UserDef withFirstName(String value) {
        setFirstName(value);
        return this;
    }

    public UserDef withLastName(String value) {
        setLastName(value);
        return this;
    }

    public UserDef withNotes(String value) {
        setNotes(value);
        return this;
    }

    public UserDef withCreatedBy(String value) {
        setCreatedBy(value);
        return this;
    }

    public UserDef withUpdatedBy(String value) {
        setUpdatedBy(value);
        return this;
    }

    public UserDef withActive(boolean value) {
        setActive(value);
        return this;
    }

    public UserDef withAdmin(boolean value) {
        setAdmin(value);
        return this;
    }

    public UserDef withSource(String value) {
        setSource(value);
        return this;
    }

    public UserDef withExternal(boolean value) {
        setExternal(value);
        return this;
    }

    public UserDef withLocale(String value) {
        setLocale(value);
        return this;
    }

    public UserDef withPasswords(UserPasswordDef... values) {
        if (values!= null) {
            for (UserPasswordDef value: values) {
                getPasswords().add(value);
            }
        }
        return this;
    }

    public UserDef withPasswords(Collection<UserPasswordDef> values) {
        if (values!= null) {
            getPasswords().addAll(values);
        }
        return this;
    }

    public UserDef withPropertiesValues(PropertyValueDef... values) {
        if (values!= null) {
            for (PropertyValueDef value: values) {
                getPropertiesValues().add(value);
            }
        }
        return this;
    }

    public UserDef withPropertiesValues(Collection<PropertyValueDef> values) {
        if (values!= null) {
            getPropertiesValues().addAll(values);
        }
        return this;
    }

    public UserDef withRoles(String... values) {
        if (values!= null) {
            for (String value: values) {
                getRoles().add(value);
            }
        }
        return this;
    }

    public UserDef withRoles(Collection<String> values) {
        if (values!= null) {
            getRoles().addAll(values);
        }
        return this;
    }

    public UserDef withLabels(LabelDef... values) {
        if (values!= null) {
            for (LabelDef value: values) {
                getLabels().add(value);
            }
        }
        return this;
    }

    public UserDef withLabels(Collection<LabelDef> values) {
        if (values!= null) {
            getLabels().addAll(values);
        }
        return this;
    }

    public UserDef withApis(String... values) {
        if (values!= null) {
            for (String value: values) {
                getApis().add(value);
            }
        }
        return this;
    }

    public UserDef withApis(Collection<String> values) {
        if (values!= null) {
            getApis().addAll(values);
        }
        return this;
    }

    public UserDef withEmailNotification(boolean value) {
        setEmailNotification(value);
        return this;
    }

    public UserDef withCreatedAt(XMLGregorianCalendar value) {
        setCreatedAt(value);
        return this;
    }

    public UserDef withUpdatedAt(XMLGregorianCalendar value) {
        setUpdatedAt(value);
        return this;
    }

}
