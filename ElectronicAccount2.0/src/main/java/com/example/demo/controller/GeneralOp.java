package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.Result;
import com.example.demo.pojo.TableMeta;
import com.example.demo.pojo.User;
import com.example.demo.service.GeneralService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GeneralOp {
	
	@Autowired
	private GeneralService generalservice;
	
	
	/**
	 *用户登录函数 
	 *@Param user 登录用户的对象
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
	public Result<Object> login(@RequestBody User user) {
		Result<Object> re = generalservice.findUserByOfficenum(user);
		return re;
	}
	
	/**
	 * 用户登录后自动搜索该用户自己创建的表格
	 * @param user
	 * @return result，包含该用户创建的所有表格字段信息
	 */
	@RequestMapping(value = "/autosearch", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
	public Result<Object> autoSearchAvailableTable(User user) {
		//重复登录步骤，因无前端界面暂时这样调用，后期需删除这行代码
		Result<Object> re = generalservice.findUserByOfficenum(user);
		
		Result<Object> re1 = new Result<Object>();
		if(re.getSuccess() == true) {
			user = (User) re.getContent();
			re1 = generalservice.searchOwnedTableList(user);
		}
		return re1;
	}
	
	/**
	 * 用户根据筛选条件搜索符合条件的台账
	 * @param json info
	 * 	userid       目前处于登录状态的用户id
	 *  userlevel    登录用户的职位
	 * 	creatorname  表格创建人（模糊查询）
	 * 	tablename    表格名（模糊查询）
	 * 	startdate    起始日期 （范围查询）
	 *  enddate      截止日期 （范围查询）
	 * @return result，包含该搜索到的所有表格字段信息
	 *  
	 */
	@RequestMapping(value = "/search", produces="text/html;charset=UTF-8")
	public Result<Object> SearchTableList(@RequestBody JSONObject info) {
		Result<Object> result =new Result<Object>();
		
		Map<String, Object> map = (Map<String, Object>)info;
		result = generalservice.searchTableList(map);
		
		return result;
	}
	
	
	/**
	 * 创建新的台账
	 * @param user
	 * @return
	 */
	//@SuppressWarnings("unchecked")
	@RequestMapping(value = "/create_new_table", produces="text/html;charset=UTF-8")
	//后期需修改参数类型为TableMeta
	//@RequestBody JSONObject obj
	public Result<Object> createNewTable() {
		//解析从前端传进来的数据
		//User user = JSONObject.toJavaObject((JSONObject)obj.get("user"), User.class);
		//TableMeta tablemeta = JSONObject.toJavaObject((JSONObject)obj.get("tablemeta"), TableMeta.class);
		//Map<String, Integer> columns = (Map<String, Integer>) obj.get("columns");
		
		Result<Object> re1 = new Result<Object>();
		
		//手动创建数据用于测试，后期删除
		User user = new User();
		user.setUserid("1234567890");
		user.setUsername("徐可");
		user.setLevel(3);
		TableMeta tablemeta = new TableMeta();
		tablemeta.generateUUID();
		tablemeta.setCreatorid(user.getUserid());
		tablemeta.setCreatorname(user.getUsername());
		tablemeta.setCreatetime(new Date());
		tablemeta.setLevel(0);
		tablemeta.setTablename("我太难了");
		Map<String, Integer> columns = new HashMap<String, Integer>();
		columns.put("col1", 1);
		columns.put("col2", 4);
		columns.put("col3", 2);
		columns.put("col4", 3);
			
		re1 = generalservice.createNewTable(tablemeta, columns);
		return re1;
	}
	
	@RequestMapping(value = "/getpermission", produces="text/html;charset=UTF-8")
	public Result<Object> getPermissionLevel(User user) throws ParseException {
		//重复登录步骤，因无前端界面暂时这样调用，后期需删除这行代码
		Result<Object> re = generalservice.findUserByOfficenum(user);
		
		Result<Object> result = new Result<Object>();
		
		if(re.getSuccess() == true) {
			user = (User) re.getContent();
			
			TableMeta tablemeta = new TableMeta();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tablemeta.setTableid("tdb764766-4f0a-46a3-99ea-0ba81ab55886");
			tablemeta.setCreatorid("1234567890");
			tablemeta.setCreatorname("徐可");
			tablemeta.setCreatetime(sdf.parse("2019-08-21 06:15:22"));
			tablemeta.setLevel(2);
			tablemeta.setPermissionlevel(0);
			tablemeta.setTablename("我好穷");
			
			result = generalservice.getPermissionLevel(user, tablemeta);
		}
		return result;
	}
	
	@RequestMapping(value = "/historicaloperation", method = RequestMethod.POST, produces="text/html;charset=UTF-8")
	public Result<Object> searchHistoricalOperatedTableList(User user) {
		//重复登录步骤，因无前端界面暂时这样调用，后期需删除这行代码
		Result<Object> re = generalservice.findUserByOfficenum(user);
		
		Result<Object> re1 = new Result<Object>();
		if(re.getSuccess() == true) {
			user = (User) re.getContent();
			re1 = generalservice.searchHistoricalOperatedTableList(user);
		}
		return re1;
	}
}
