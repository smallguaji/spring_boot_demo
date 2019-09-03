package com.example.demo;

import java.io.Serializable;

public class aaa implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7512919261842883523L;
	
	
	private Integer c_id;
	private String a;
	private String b;
	
	public Integer getCid() {return c_id;}
	public void setCid(Integer c_id) {this.c_id = c_id;}
	
	public String getA() {return a;}
	public void setA(String a) {this.a = a;}
	
	public String getB() {return b;}
	public void setB(String b) {this.b = b;}
}
