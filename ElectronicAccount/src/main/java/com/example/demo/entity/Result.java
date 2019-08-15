package com.example.demo.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class Result<T> implements Serializable{
	
	/**
	 * 自动生成的id，听说是用来保持版本的兼容性，即在版本升级时反序列化仍保持对象的唯一性（看不懂什么意思）
	 */
	private static final long serialVersionUID = 5360952641982943661L;
	
	@JSONField(ordinal = 0)
	private boolean success;
	@JSONField(ordinal = 1)
	private String msg;
	@JSONField(ordinal = 2)
	private T content;
	
	public Result(){
		this.success = false;
        this.content = null;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
}