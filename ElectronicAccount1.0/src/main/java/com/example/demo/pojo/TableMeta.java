package com.example.demo.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 表格元数据模型
 * @author 小呱唧
 */
public class TableMeta implements Serializable {
	
	/**
	 * 自动生成的id，听说是用来保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性（看不懂什么意思）
	 */
	private static final long serialVersionUID = 6755032456470891019L;
	
	private String tableid;
	private String tablename;
	private String creatorid;
	private String creatorname;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	private int level = -1;
	private int permissionlevel = -1;
	
	public void generateUUID() { this.tableid = "t" + UUID.randomUUID().toString(); }
	
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
	public String getCreatorid() {
		return creatorid;
	}
	public void setCreatorid(String creatorid) {
		this.creatorid = creatorid;
	}
	
	public String getCreatorname() {
		return creatorname;
	}
	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getPermissionlevel() {
		return permissionlevel;
	}
	public void setPermissionlevel(int permissionlevel) {
		this.permissionlevel = permissionlevel;
	}
}
