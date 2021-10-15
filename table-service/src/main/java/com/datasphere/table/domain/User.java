package com.datasphere.table.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import io.swagger.annotations.ApiModelProperty;

@Table(name = "sys_user")

public class User implements Serializable {
	
	@Id
	@Column(name = "userId", nullable = false, columnDefinition = "Long")
	@NotNull(message = "用户Id不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "用户Id", example = "1234567890")
	
    private Long userId; 
	
	@Column(name = "roleId", nullable = false, columnDefinition = "Long")
	@NotNull(message = "角色Id不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "角色Id", example = "1234567890")
	
    private String roleId;
	
	@Column(name = "userName", nullable = false, columnDefinition = "Long")
	@NotNull(message = "用户名称不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "用户名称", example = "1234567890")
    private String userName; 
	
	@Column(name = "password", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "密码不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "密码", example = "12345678-abcd-90ab-cdef-1234567890ab")
    private String password;
	
	@Column(name = "email", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "邮箱", example = "12345678-abcd-90ab-cdef-1234567890ab")
    private String email;
    
	@Column(name = "mobile", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "手机号", example = "12345678-abcd-90ab-cdef-1234567890ab")
    private String mobile;
	
	@Column(name = "roleType", nullable = false, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "角色类型", example = "12345678-abcd-90ab-cdef-1234567890ab")
    private String roleType;
    
	@Column(name = "loginIp", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "登录IP地址", example = "192.168.0.1")
    private String loginIp;
	
	@Column(name = "loginDate", nullable = false, columnDefinition = "TimeStamp")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "登录时间", example = "")
    private String loginDate;
    
	@Column(name = "userState", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "用户状态不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "用户状态", example = "12345678-abcd-90ab-cdef-1234567890ab")
    private String userState;
	
	@Id
	@Column(name = "deptId", nullable = false, columnDefinition = "Long")
	@NotNull(message = "部门Id不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "部门Id", example = "1234567890")
    private Long deptId;
	
	@Column(name = "deptName", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "部门名称不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "部门名称", example = "1234567890")
    private String deptName;
    
	@Column(name = "roleName", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "角色名称不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "角色名称", example = "1234567890")
    private String roleName;
    
	@Column(name = "roleTypeGroup", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "角色类型组", example = "1234567890")
    
    private String roleTypeGroup;//角色类型组

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        if (!StringUtils.isEmpty(userId)) {
            this.userId = new Long(userId);
        }
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

   

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

   


    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Long getDeptId() {
		return deptId;
	}


	
	public void setDeptId(String deptId) {
        if (!StringUtils.isEmpty(deptId)) {
            this.deptId = new Long(deptId);
        }
    }

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getRoleTypeGroup() {
		return roleTypeGroup;
	}

	public void setRoleTypeGroup(String roleTypeGroup) {
		this.roleTypeGroup = roleTypeGroup;
	}

	

  

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
