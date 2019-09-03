package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.annotation.RoleAuthorization;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Result;
import com.example.demo.service.AdminService;
import com.example.demo.service.AdvancedService;
import com.example.demo.service.BasicService;

@RestController
public class TableOp {

	@Autowired
	private BasicService basicservice;
	@Autowired
	private AdvancedService advanceservice;
	@Autowired
	private AdminService adminservice;

	/**
	 * 获取某个台账的内容，该用户需要有这个台账的0权限
	 * 
	 * @param permission
	 * @return result
	 */
	@RoleAuthorization(level = 0)
	@RequestMapping(value = "/table_detail", produces = "text/html;charset=UTF-8")
	public Result getTableDetail(@RequestBody JSONObject obj) {
		//将权限信息从前端传进来的数据包中解析出来
		Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
		Result result = basicservice.getTableDetail(permission);
		return result;
	}

	/***
	 * 批量向台账中添加记录，该功能需要用户有这个台账的1权限
	 * 
	 * @param
	 * @return result
	 */
	@SuppressWarnings("unchecked")
	@RoleAuthorization(level = 1)
	@RequestMapping(value = "/addrecords", produces = "text/html;charset=UTF-8")
	public Result addRecords(@RequestBody JSONObject obj) {
		Result result = new Result();
		//将传进来的参数拆分成权限信息、元数据（包含了该台账各字段的名字以及属性）和数据（即每条记录具体的值）
		Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
		
		//修改permission中的数据用作测试，后期删去这部分代码
		permission.setUserid("0987654321");
		permission.setTableid("tdb764766-4f0a-46a3-99ea-0ba81ab55886");
		permission.setLevel(1);
		
		Map<String, Integer> metadata = (Map<String, Integer>) obj.get("metadata");
		
		//前端数据出错，后端修正，后期需删除这两行代码
		metadata.put("a", 1);
		metadata.put("b", 1);
		
		List<JSONObject> data = (List<JSONObject>) obj.get("data");
		
		result = advanceservice.addRecords(permission, metadata, data);
		
		return result;
	}

	/***
	 * 批量修改台账中的记录，该功能需要用户有这个台账的2权限
	 * 
	 * @param
	 * @result result
	 */
	@RoleAuthorization(level = 2)
	@RequestMapping(value = "/modifyrecords", produces = "text/html;charset=UTF-8")
	public Result modifyRecords() {
		Result result = new Result();
		return result;
	}

	/***
	 * 批量修改台账中的记录，该功能需要用户有这个台账的2权限
	 * 
	 * @param
	 * @result result
	 */
	@RoleAuthorization(level = 2)
	@RequestMapping(value = "/deleteaddrecords", produces = "text/html;charset=UTF-8")
	public Result deleteRecords() {
		Result result = new Result();
		return result;
	}

	/***
	 * 批量修改台账中的记录，该功能需要用户有这个台账的2权限
	 * 
	 * @param
	 * @result result
	 */
	@RoleAuthorization(level = 2)
	@RequestMapping(value = "/updatetable", produces = "text/html;charset=UTF-8")
	public Result updateTableStructure() {
		Result result = new Result();
		return result;
	}

	/***
	 * 批量修改台账中的记录，该功能需要用户有这个台账的2权限
	 * 
	 * @param
	 * @result result
	 */
	@RoleAuthorization(level = 2)
	@RequestMapping(value = "/deletetable", produces = "text/html;charset=UTF-8")
	public Result deleteTable(@RequestBody JSONObject obj) {
		Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
		Result result = adminservice.deleteTable(permission);
		return result;
	}
}
