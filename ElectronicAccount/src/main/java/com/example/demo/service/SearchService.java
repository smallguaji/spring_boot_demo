package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.SearchDAO;
import com.example.demo.entity.Result;
import com.example.demo.entity.User;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SearchService {

	@Autowired
	private SearchDAO searchdao;
	
	/**
	 * 登录服务
	 * @param user
	 * @return result
	 */
	public Result findUserByOfficenum(User user) {
		Result re = new Result();
        try {
        	User result = searchdao.findUserByOfficenum(user);
        	if(result == null) {
        		re.setMsg("无此用户");
        	} else {
        		re.setMsg("登录成功");
        		re.setSuccess(true);
        		re.setContent(result);
        	}
        } catch (Exception e) {
        	re.setMsg("系统故障");
        	re.setContent(e.getMessage());
            e.printStackTrace();
        }
		return re;
	}
	
	/**
	 * 查询用户创建的所有表格
	 * @param  User
	 * @return result
	 */
	public Result autoSearchOwnedTable(User user) {
		Result re = new Result();
		try {
        	List<JSONObject> result = searchdao.autoSearchOwnedTable(user);
        	re.setSuccess(true);
        	if(result == null) {
        		re.setMsg("该用户没有创建过表格");
        	} else {
        		re.setMsg("查找成功");
        		/*这里可做List<JSONObject>的类型转换*/

        		re.setContent(result);
        	}
        } catch (Exception e) {
        	re.setMsg("系统故障");
        	re.setContent(e.getMessage());
            e.printStackTrace();
        }
		return re;
	}
}
