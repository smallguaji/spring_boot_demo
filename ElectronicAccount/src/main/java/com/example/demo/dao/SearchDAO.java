package com.example.demo.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.User;

@Mapper
@Repository
public interface SearchDAO{
	
	User findUserByOfficenum(User user);
	
	List<JSONObject> autoSearchOwnedTable(User user);
}
