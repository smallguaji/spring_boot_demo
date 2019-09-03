package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Result;
import com.example.demo.entity.User;
import com.example.demo.globalfunction.RandomUUID;
import com.example.demo.service.SearchService;


@RestController
public class SearchOp {
	
	@Autowired
	private SearchService searchservice;
	
	/**
	 *用户登录函数 
	 *@Param user 登录用户的对象
	 */
	@RequestMapping(value = "/login", produces="text/html;charset=UTF-8")
	public Result login(User user) {
		Result re = searchservice.findUserByOfficenum(user);
		return re;
	}
	
	/**
	 * 用户登录后自动搜索该用户自己创建的表格
	 * @param user
	 * @return result，包含该用户创建的所有表格字段信息
	 */
	@RequestMapping(value = "/autosearch", produces="text/html;charset=UTF-8")
	public Result autoSearchAvailableTable(User user) {
		String SN = "t" + RandomUUID.randomString(15);
		//重复登录步骤，因无前端界面暂时这样调用，后期需修改
		Result re = searchservice.findUserByOfficenum(user);
		
		Result re1 = new Result();
		if(re.getSuccess() == true) {
			user = (User) re.getContent();
			re1 = searchservice.autoSearchOwnedTable(user);
		}
		return re1;
	}
}
