package com.huahui.datasphere.mdm.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


/**
 * Роль
 * 
 * <p>Java class for RoleDef complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoleDef"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="rType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="createdBy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="updatedBy" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="propertiesValues" type="{http://security.mdm.unidata.com/}PropertyValueDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="roleLabels" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="labels" type="{http://security.mdm.unidata.com/}LabelDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="rightsToResources" type="{http://security.mdm.unidata.com/}RightToResourceDef" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@JsonPropertyOrder({"name","rType","displayName","description","createdBy",
    "updatedBy","propertiesValues","roleLabels","labels","rightsToResources"})
public class RoleDef {

    protected String name;
    protected String rType;
    protected String displayName;
    protected String description;
    protected String createdBy;
    protected String updatedBy;
    protected List<PropertyValueDef> propertiesValues;
    protected List<String> roleLabels;
    protected List<LabelDef> labels;
    protected List<RightToResourceDef> rightsToResources;
    @JacksonXmlProperty(isAttribute = true)
    protected XMLGregorianCalendar createdAt;
    @JacksonXmlProperty(isAttribute = true)
    protected XMLGregorianCalendar updatedAt;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the rType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRType() {
        return rType;
    }

    /**
     * Sets the value of the rType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRType(String value) {
        this.rType = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
     * Gets the value of the roleLabels property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roleLabels property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoleLabels().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRoleLabels() {
        if (roleLabels == null) {
            roleLabels = new ArrayList<>();
        }
        return this.roleLabels;
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
     * Gets the value of the rightsToResources property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rightsToResources property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRightsToResources().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RightToResourceDef }
     * 
     * 
     */
    public List<RightToResourceDef> getRightsToResources() {
        if (rightsToResources == null) {
            rightsToResources = new ArrayList<>();
        }
        return this.rightsToResources;
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

    public RoleDef withName(String value) {
        setName(value);
        return this;
    }

    public RoleDef withRType(String value) {
        setRType(value);
        return this;
    }

    public RoleDef withDisplayName(String value) {
        setDisplayName(value);
        return this;
    }

    public RoleDef withDescription(String value) {
        setDescription(value);
        return this;
    }

    public RoleDef withCreatedBy(String value) {
        setCreatedBy(value);
        return this;
    }

    public RoleDef withUpdatedBy(String value) {
        setUpdatedBy(value);
        return this;
    }

    public RoleDef withPropertiesValues(PropertyValueDef... values) {
        if (values!= null) {
            for (PropertyValueDef value: values) {
                getPropertiesValues().add(value);
            }
        }
        return this;
    }

    public RoleDef withPropertiesValues(Collection<PropertyValueDef> values) {
        if (values!= null) {
            getPropertiesValues().addAll(values);
        }
        return this;
    }

    public RoleDef withRoleLabels(String... values) {
        if (values!= null) {
            for (String value: values) {
                getRoleLabels().add(value);
            }
        }
        return this;
    }

    public RoleDef withRoleLabels(Collection<String> values) {
        if (values!= null) {
            getRoleLabels().addAll(values);
        }
        return this;
    }

    public RoleDef withLabels(LabelDef... values) {
        if (values!= null) {
            for (LabelDef value: values) {
                getLabels().add(value);
            }
        }
        return this;
    }

    public RoleDef withLabels(Collection<LabelDef> values) {
        if (values!= null) {
            getLabels().addAll(values);
        }
        return this;
    }

    public RoleDef withRightsToResources(RightToResourceDef... values) {
        if (values!= null) {
            for (RightToResourceDef value: values) {
                getRightsToResources().add(value);
            }
        }
        return this;
    }

    public RoleDef withRightsToResources(Collection<RightToResourceDef> values) {
        if (values!= null) {
            getRightsToResources().addAll(values);
        }
        return this;
    }

    public RoleDef withCreatedAt(XMLGregorianCalendar value) {
        setCreatedAt(value);
        return this;
    }

    public RoleDef withUpdatedAt(XMLGregorianCalendar value) {
        setUpdatedAt(value);
        return this;
    }

}
