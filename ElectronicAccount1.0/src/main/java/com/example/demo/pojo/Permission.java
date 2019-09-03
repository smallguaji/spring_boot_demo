package com.example.demo.pojo;

import java.io.Serializable;

public class Permission implements Serializable {

	private static final long serialVersionUID = -5025470309939195027L;
	
	private String userid;
	private String tableid;
	private int level;
	
	public Permission(String userid, String tableid, int level) {
		this.userid = userid;
		this.tableid = tableid;
		this.level = level;
	}

	public String getUserid() { return userid;}
	public void setUserid(String userid) { this.userid = userid;}
	
	public String getTableid() { return tableid;}
	public void setTableid(String tableid) {this.tableid = tableid;}
	
	public int getLevel() { return level;}
	public void setLevel(int level) { this.level = level;}
	
}
