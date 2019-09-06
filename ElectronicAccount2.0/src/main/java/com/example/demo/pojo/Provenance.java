package com.example.demo.pojo;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Provenance {
	private String tableid;
	private Integer tableindex;
	private JSONObject originalcontent;
	private JSONObject newcontent;
	/*0-添加新内容， 1-修改已有内容， 2-删除以有后内容， 3-修改表结构， 4-删除已有表，5-创建新表*/
	private int operationtype;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date time;
	private String userid;
	
	public String getTableid() {return tableid;}
	public void setTableid(String tableid) {this.tableid = tableid;}
	
	public Integer getTableindex() {return tableindex;}
	public void setTableindex(Integer tableindex) {this.tableindex = tableindex;}
	
	public JSONObject getOriginalcontent() {return originalcontent;}
	public void setOriginalcontent(JSONObject originalcontent) {this.originalcontent = originalcontent;}
	
	public JSONObject getNewcontent() {return newcontent;}
	public void setNewcontent(JSONObject newcontent) {this.newcontent = newcontent;}
	
	public int getOperationtype() {return operationtype;}
	public void setOperationtype(int operationtype) {this.operationtype = operationtype;}
	
	public Date getTime() {return time;}
	public void setTime(Date time) {this.time = time;}
	
	public String getUserid() {return userid;}
	public void setUserid(String userid) {this.userid = userid;}
	
	public Provenance(String tableid, Date date, String userid) {
		this.tableid = tableid;
		this.time = date;
		this.userid = userid;
	} 
	
	public static Provenance pronenanceAdd(String tableid, int tableindex, JSONObject newcontent, Date date, String userid) {
		Provenance pro = new Provenance(tableid, date, userid);
		pro.setTableindex(tableindex);
		pro.setNewcontent(newcontent);
		pro.setOperationtype(0);
		return pro;
	}
	
	public static Provenance pronenanceEditRecord(String tableid, int tableindex, JSONObject originalcontent, JSONObject newcontent, Date date, String userid) {
		Provenance pro = new Provenance(tableid, date, userid);
		pro.setTableindex(tableindex);
		pro.setOriginalcontent(originalcontent);
		pro.setNewcontent(newcontent);
		pro.setOperationtype(1);
		return pro;
	}
	
	public static Provenance pronenanceDeleteRecord(String tableid, int tableindex, JSONObject originalcontent, Date date, String userid) {
		Provenance pro = new Provenance(tableid, date, userid);
		pro.setTableindex(tableindex);
		pro.setOriginalcontent(originalcontent);
		pro.setOperationtype(2);
		return pro;
	}
	
	public static Provenance provenanceCreate(String tableid, Date date, String userid) {
		Provenance pro = new Provenance(tableid, date, userid);
		pro.setOperationtype(5);
		return pro;
	}
	
	public static Provenance provenanceDelete(String tableid, Date date, String userid) {
		Provenance pro = new Provenance(tableid, date, userid);
		pro.setOperationtype(4);
		return pro;
	}
}
