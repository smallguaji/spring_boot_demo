package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CreateDAO;
import com.example.demo.entity.Result;

/***
 * @author 小呱唧
 *创建新表的服务逻辑
 */

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CreateService {

	@Autowired
	private CreateDAO createdao;
	
	/**
	 * 创建新表格的逻辑
	 * 
	 * @return result
	 */
	public Result createNewTable() {
		Result re = new Result();
		return re;
	}
}
