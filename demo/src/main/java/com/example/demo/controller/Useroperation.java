package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.Userservice;

@RestController
@RequestMapping("/user")
public class Useroperation {
	
	@Autowired
	private Userservice userservice;
	
	/**
	 * 注册
	 * @param user 封装好的用户数据，json格式
	 * @return userservice返回的注册结果数据
	 */
	@RequestMapping(value = "/regist")
	public ModelAndView regist(User user) {
		ModelAndView model = new ModelAndView();
		Result result = userservice.regist(user);
		boolean is_regist = result.getSuccess();
		if (is_regist == true) {
			model.addObject("name", user.getUserName());
        	model.addObject("status", "registed");
			model.setViewName("home");
		}
		else 
			model.setViewName("register");
		return model;
	}
	
	
	/**
	    * 登录
     * @param user 参数封装
     * @return Result
     */
	@RequestMapping(value = "/login")
    public ModelAndView login(User user){
		ModelAndView model = new ModelAndView();
        Result result = userservice.login(user);
        boolean is_login = result.getSuccess();
        if (is_login == true) {
        	model.addObject("name", user.getUserName());
        	model.addObject("status", "logined");
        	model.setViewName("home"); 
        }
        else 
        	model.setViewName("login");
        return model;
    }
}
