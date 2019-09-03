package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Result;
import com.example.demo.service.CreateService;

@RestController
public class CreateOp {

	@Autowired
	private CreateService createservice;
	
	@RequestMapping(value = "/create_new_table", produces="text/html;charset=UTF-8")
	public Result createNewTable() {
		Result re = createservice.createNewTable();
		return re;
	}
}
