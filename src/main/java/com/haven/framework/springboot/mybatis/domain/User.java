package com.haven.framework.springboot.mybatis.domain;

import java.io.Serializable;
import java.util.Date;

import com.haven.framework.springboot.mybatis.service.annotation.Column;
import com.haven.framework.springboot.mybatis.service.annotation.PrimaryKey;
import com.haven.framework.springboot.mybatis.service.annotation.QueryColumns;
import com.haven.framework.springboot.mybatis.service.annotation.Table;

@Table("tb_User")
@QueryColumns("id, user_name, gender, email, registry_time")
public class User implements Serializable {
	private static final long serialVersionUID = 5173042511543041319L;

	@PrimaryKey("id")
	private Long id;
	
	@Column("user_name")
	private String userName;
	
	@Column("password")
	private String password;
	
	@Column("gender")
	private Boolean gender;
	
	@Column("email")
	private String email;
	
	@Column("registry_time")
	private Date registryTime;
	
	public User() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getRegistryTime() {
		return registryTime;
	}

	public void setRegistryTime(Date registryTime) {
		this.registryTime = registryTime;
	}
	
}
