package com.huahui.datasphere.mdm.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;


/**
 * Элементы безопасности
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="users" type="{http://security.mdm.unidata.com/}UserDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="userProperties" type="{http://security.mdm.unidata.com/}UserPropertyDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="roles" type="{http://security.mdm.unidata.com/}RoleDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="roleProperties" type="{http://security.mdm.unidata.com/}RolePropertyDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="labels" type="{http://security.mdm.unidata.com/}LabelDef" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@JsonPropertyOrder({"users","userProperties","roles","roleProperties","labels"})
@JacksonXmlRootElement(localName = "security")
public class Security {

    protected List<UserDef> users;
    protected List<UserPropertyDef> userProperties;
    protected List<RoleDef> roles;
    protected List<RolePropertyDef> roleProperties;
    protected List<LabelDef> labels;

    /**
     * Gets the value of the users property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the users property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserDef }
     * 
     * 
     */
    public List<UserDef> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return this.users;
    }

    /**
     * Gets the value of the userProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserPropertyDef }
     * 
     * 
     */
    public List<UserPropertyDef> getUserProperties() {
        if (userProperties == null) {
            userProperties = new ArrayList<>();
        }
        return this.userProperties;
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
     * {@link RoleDef }
     * 
     * 
     */
    public List<RoleDef> getRoles() {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return this.roles;
    }

    /**
     * Gets the value of the roleProperties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roleProperties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoleProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RolePropertyDef }
     * 
     * 
     */
    public List<RolePropertyDef> getRoleProperties() {
        if (roleProperties == null) {
            roleProperties = new ArrayList<>();
        }
        return this.roleProperties;
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

    public Security withUsers(UserDef... values) {
        if (values!= null) {
            for (UserDef value: values) {
                getUsers().add(value);
            }
        }
        return this;
    }

    public Security withUsers(Collection<UserDef> values) {
        if (values!= null) {
            getUsers().addAll(values);
        }
        return this;
    }

    public Security withUserProperties(UserPropertyDef... values) {
        if (values!= null) {
            for (UserPropertyDef value: values) {
                getUserProperties().add(value);
            }
        }
        return this;
    }

    public Security withUserProperties(Collection<UserPropertyDef> values) {
        if (values!= null) {
            getUserProperties().addAll(values);
        }
        return this;
    }

    public Security withRoles(RoleDef... values) {
        if (values!= null) {
            for (RoleDef value: values) {
                getRoles().add(value);
            }
        }
        return this;
    }

    public Security withRoles(Collection<RoleDef> values) {
        if (values!= null) {
            getRoles().addAll(values);
        }
        return this;
    }

    public Security withRoleProperties(RolePropertyDef... values) {
        if (values!= null) {
            for (RolePropertyDef value: values) {
                getRoleProperties().add(value);
            }
        }
        return this;
    }

    public Security withRoleProperties(Collection<RolePropertyDef> values) {
        if (values!= null) {
            getRoleProperties().addAll(values);
        }
        return this;
    }

    public Security withLabels(LabelDef... values) {
        if (values!= null) {
            for (LabelDef value: values) {
                getLabels().add(value);
            }
        }
        return this;
    }

    public Security withLabels(Collection<LabelDef> values) {
        if (values!= null) {
            getLabels().addAll(values);
        }
        return this;
    }

}
