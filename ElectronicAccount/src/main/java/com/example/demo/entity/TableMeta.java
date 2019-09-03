package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

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
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createtime;
	private int level;
	
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
}
