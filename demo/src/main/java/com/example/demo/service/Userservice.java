package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.Usermapper;
import com.example.demo.model.Result;
import com.example.demo.model.User;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class Userservice {
	
	@Autowired 
	private Usermapper usermapper;

	public Result regist(User user) {
		Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            User existUser = usermapper.findUserByName(user.getUserName());
            if(existUser != null){
                //如果用户名已存在
                result.setMsg("用户名已存在");

            }else{
                usermapper.regist(user);
                result.setMsg("注册成功");
                result.setSuccess(true);
                result.setDetail(user);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
	}
	
	/**
     * 登录
     * @param user 用户名和密码
     * @return Result
     */
    public Result login(User user) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            String userName= usermapper.login(user);
            if(userName == null){
                result.setMsg("用户名或密码错误");
            }else{
                result.setMsg("登录成功");
                result.setSuccess(true);
                user.setUserName(userName);
                result.setDetail(user);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
