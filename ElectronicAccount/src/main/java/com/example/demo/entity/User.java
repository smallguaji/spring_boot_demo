package com.example.demo.entity;

import java.io.Serializable;

/**
 * 用户信息模型
 * 用于用户进入系统后保存用户信息
 */
public class User implements Serializable{
	
	/**
	 * 自动生成的id，听说是用来保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性（看不懂什么意思）
	 */
	private static final long serialVersionUID = -6568358157873734656L;
	
	private String userid;
	private String username;
	private String officenum;
	private String headofficenum;
	private String department;
	private String team;
	private String email;
	private int level;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getOfficenum() {
		return officenum;
	}
	public void setOfficenum(String officenum) {
		this.officenum = officenum;
	}
	
	public String getHeadofficenum() {
		return headofficenum;
	}
	public void setHeadofficenum(String headofficenum) {
		this.headofficenum = headofficenum;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
