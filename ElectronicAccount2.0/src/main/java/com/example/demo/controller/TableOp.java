package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
	public Result<Object> getTableDetail(@RequestBody JSONObject obj) {
		//将权限信息从前端传进来的数据包中解析出来
		Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
		Result<Object> result = basicservice.getTableDetail(permission);
		return result;
	}

	/***
	 * 批量向台账中添加记录，该功能需要用户有这个台账的1权限
	 * 
	 * @param
	 * @return result
	 * optype指明了这条数据的操作类型： 0-添加    1-修改已有记录  2-删除该记录
	 * c_id为该条记录在数据表中的索引，若为新记录，则c_id为空
	 * index指向原始记录，用于记录的修改，若为新增记录，则index为空
	 * 这本功能中，所有数据的optype应都为0,index和c_id都为null
	 */
	@SuppressWarnings("unchecked")
	@RoleAuthorization(level = 1)
	@RequestMapping(value = "/addrecords", produces = "text/html;charset=UTF-8")
	public Result<Object> addRecords(@RequestBody JSONObject obj) {
		Result<Object> result = new Result<Object>();
		
		//将传进来的参数拆分成权限信息、元数据（包含了该台账各字段的名字以及属性）和数据（即每条记录具体的值）
		Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
		Map<String, Integer> metadata = (Map<String, Integer>) obj.get("metadata");
		List<JSONObject> data = (List<JSONObject>) obj.get("data");
		
		//测试数据，后期需删除
		permission.setUserid("0987654321");
		permission.setTableid("tdb764766-4f0a-46a3-99ea-0ba81ab55886");
		permission.setLevel(1);
		metadata.put("a", 1);
		metadata.put("b", 4);
		metadata.put("c", 3);
		metadata.put("d", 2);
		data.get(0).put("c_id", null);
		data.get(0).put("a", "xxx");
		data.get(0).put("b", "1990-08-11 13:03:47");
		data.get(0).put("c", 0.5);
		data.get(0).put("d", 1);
		data.get(0).put("index", null);
		data.get(0).put("optype", 0);
		data.get(1).put("c_id", null);
		data.get(1).put("a", "yyy");
		data.get(1).put("b", "1992-04-21 14:23:13");
		data.get(1).put("c", 0.6);
		data.get(1).put("d", 2);
		data.get(1).put("index", null);
		data.get(1).put("optype", 0);
		JSONObject jo = new JSONObject();
		jo.put("c_id", null);
		jo.put("a", "zzz");
		jo.put("b", "1995-07-03 16:01:25");
		jo.put("c", 0.7);
		jo.put("d", 3);
		jo.put("index", null);
		jo.put("optype", 0);
		data.add(jo);

		result = advanceservice.addRecords(permission, metadata, data);
		
		return result;
	}

	/***
	 * 批量修改台账中的记录(包括添加、修改、删除)，该功能需要用户有这个台账的2权限
	 * 
	 * @param
	 * @result result
	 * optype指明了这条数据的操作类型： 0-添加    1-修改已有记录  2-删除该记录
	 */
	//@RoleAuthorization(level = 2)
	@RequestMapping(value = "/modifyrecords", produces = "text/html;charset=UTF-8")
	//@RequestBody JSONObject obj
	public Result<Object> modifyRecords() {
		//将传进来的参数拆分成权限信息、元数据（包含了该台账各字段的名字以及属性）和数据（即每条记录具体的值）
		//Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
		//Map<String, Integer> metadata = (Map<String, Integer>) obj.get("metadata");
		//List<JSONObject> data = (List<JSONObject>) obj.get("data");
		
		Result<Object> result = new Result<Object>();
		
		//测试数据，后期需删除
		Permission permission = new Permission("1234567890", "tdb764766-4f0a-46a3-99ea-0ba81ab55886", 3);
		Map<String, Integer> metadata = new HashMap<String, Integer>();
		metadata.put("a", 1);
		metadata.put("b", 4);
		metadata.put("c", 3);
		metadata.put("d", 2);
		List<JSONObject> data = new ArrayList<JSONObject>();
		JSONObject jo1 = new JSONObject();  //添加一条新的记录
		jo1.put("c_id", null);
		jo1.put("a", "xxx");
		jo1.put("b", "1990-08-11 13:03:47");
		jo1.put("c", 0.5);
		jo1.put("d", 1);
		jo1.put("index", null);
		jo1.put("optype", 0);
		JSONObject jo2 = new JSONObject();  //修改第一条记录
		jo2.put("c_id", 1);
		jo2.put("a", "yyy");
		jo2.put("b", "1992-04-21 14:23:13");
		jo2.put("c", 0.6);
		jo2.put("d", 2);
		jo2.put("index", 1);
		jo2.put("optype", 1);
		JSONObject jo3 = new JSONObject(); //删除第二条记录
 		jo3.put("c_id", 2);
		jo3.put("a", "zzz");
		jo3.put("b", "1995-07-03 16:01:25");
		jo3.put("c", 0.7);
		jo3.put("d", 3);
		jo3.put("index", null);
		jo3.put("optype", 2);
		data.add(jo1); data.add(jo2); data.add(jo3);
		
		result = adminservice.modifyRecords(permission, metadata, data);
		
		return result;
	}

	/***
	 * 修改台账结构，该功能需要用户有这个台账的2权限
	 * 
	 * @param
	 * @result result
	 */
	@RoleAuthorization(level = 2)
	@RequestMapping(value = "/updatetable", produces = "text/html;charset=UTF-8")
	public Result<Object> updateTableStructure() {
		Result<Object> result = new Result<Object>();
		return result;
	}

	/***
	 * 删除台账，该功能需要用户有这个台账的2权限
	 * 
	 * @param
	 * @result result
	 */
	@RoleAuthorization(level = 2)
	@RequestMapping(value = "/deletetable", produces = "text/html;charset=UTF-8")
	public Result<Object> deleteTable(@RequestBody JSONObject obj) {
		Permission permission = JSONObject.toJavaObject((JSONObject)obj.get("permission"), Permission.class);
		Result<Object> result = adminservice.deleteTable(permission);
		return result;
	}
}
