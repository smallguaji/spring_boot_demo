package com.example.demo.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.GeneralOpDAO;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Provenance;
import com.example.demo.pojo.Result;
import com.example.demo.pojo.TableMeta;
import com.example.demo.pojo.User;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class GeneralService {

	@Autowired
	private GeneralOpDAO generaldao;
	
	/**
	 * 登录服务
	 * @param user
	 * @return result
	 */
	public Result<Object> findUserByOfficenum(User user) {
		Result<Object> re = new Result<Object>();
        try {
        	//检索数据库中是否有该用户
        	User result = generaldao.findUserByOfficenum(user);
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
	public Result<Object> searchOwnedTableList(User user) {
		Result<Object> re = new Result<Object>();
		try {
			//根据用户信息检索该用户创建的台账
        	List<Map<String, Object>> result = generaldao.searchOwnedTableList(user);
        	if(result.isEmpty()) {
        		re.setMsg("该用户没有创建过表格");
        	} else {
        		re.setSuccess(true);
        		List<JSONObject> transformedlist = new ArrayList<JSONObject>();
            	Iterator<Map<String, Object>> iter = result.iterator();
            	while(iter.hasNext()) {
            		transformedlist.add(JSONObject.parseObject(JSON.toJSONString(iter.next())));
            	}
        		re.setMsg("查找成功");
        		re.setContent(transformedlist);
        	}
        } catch (Exception e) {
        	re.setMsg("系统故障");
        	re.setContent(e.getMessage());
            e.printStackTrace();
        }
		return re;
	}
	
	/**
	 * 根据用户的查询条件获取用户有权限的表信息
	 * @param  json 用户用于查询的条件
	 * @return result
	 */
	public Result<Object> searchTableList(Map<String, Object> info) {
		Result<Object> re = new Result<Object>();
		try {
			//根据用户的信息以及用户输入的查询条件检索数据库中是否有符合条件并且该用户有权限查看的台账
			List<Map<String, Object>> result = generaldao.searchTableList(info);
        	if(result.isEmpty()) {
        		re.setMsg("没有符合条件的台账");
        	} else {
        		re.setSuccess(true);
        		List<String> transformedlist = new ArrayList<String>();
            	Iterator<Map<String, Object>> iter = result.iterator();
            	while(iter.hasNext()) {
            		transformedlist.add(JSON.toJSONString(iter.next()));
            	}
        		re.setMsg("查找成功");
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
	 * 创建新表格的逻辑
	 * 
	 * @return result
	 */
	public Result<Object> createNewTable(TableMeta tablemeta, Map<String, Integer> columns) {
		Result<Object> re = new Result<Object>();
		
		try {
			//创建一个admin权限
			Permission admin = new Permission(tablemeta.getCreatorid(), tablemeta.getTableid(), 2);
			List<Permission> perlist = new ArrayList<Permission>();
			perlist.add(admin);
			//创建一个操作历史
			Provenance create = Provenance.provenanceCreate(tablemeta.getTableid(), tablemeta.getCreatetime(), tablemeta.getCreatorid());
			//处理columns参数，使其变为<string, String>类型, 其中key为台账中每个字段的名字，value为该字段的类型
			Map<String, String> converted_columns = new TreeMap<String, String>();
			for (Map.Entry<String, Integer> entry : columns.entrySet()) {
	            String type = new String();
	            switch (entry.getValue()) {
	            	case 1: type = "TEXT"; break;   //文字
	            	case 2: type = "INT"; break;   //整数
	            	case 3: type = "DOUBLE"; break;   //小数
	            	case 4: type = "TIMESTAMP"; break;   //日期
	            	default: throw new Exception("类型出错！"); 
	            }
	            converted_columns.put(entry.getKey(), type);
	        }
		
			//写入数据到数据库
			generaldao.addNewTable(tablemeta, converted_columns);
			generaldao.createNewTable(tablemeta);
			generaldao.addPermission(perlist);
			generaldao.addProvenance(create);
        	re.setSuccess(true);
        	re.setMsg("创建成功");
        } catch (Exception e) {
        	re.setMsg("系统出错");
        	re.setContent(e.getMessage());
        	e.printStackTrace();
        }
		return re;
	}
	
	
	public Result<Object> getPermissionLevel (User user, TableMeta tablemeta) {
		Result<Object> re = new Result<Object>();
		try {
			int level = generaldao.getPermissionLevel(user, tablemeta);
			if(user.getLevel() >= tablemeta.getLevel()) {
				level = (level>tablemeta.getPermissionlevel()) ? level : tablemeta.getPermissionlevel();
			}
			if (user.getLevel() == 3) level = 3;
			Permission permission = new Permission(user.getUserid(), tablemeta.getTableid(), level);
			re.setContent(permission);
			re.setSuccess(true);
			re.setMsg("生成对象成功");
			
		} catch (Exception e) {
			re.setMsg("系统出错");
        	re.setContent(e.getMessage());
		}
		return re;
	}
	
	public Result<Object> searchHistoricalOperatedTableList(User user) {
		Result<Object> re = new Result<Object>();
		try {
			//根据用户信息检索该用户创建的台账
        	List<Map<String, Object>> result = generaldao.searchHistoricalOperatedTableList(user);
        	if(result.isEmpty()) {
        		re.setMsg("该用户没有操作过台账");
        	} else {
        		re.setSuccess(true);
        		List<JSONObject> tablelist = new ArrayList<JSONObject>();
            	Iterator<Map<String, Object>> iter = result.iterator();
            	while(iter.hasNext()) {
            		tablelist.add(JSONObject.parseObject(JSON.toJSONString(iter.next())));
            	}
        		re.setMsg("查找成功");
        		re.setContent(tablelist);
        	}
        } catch (Exception e) {
        	re.setMsg("系统故障");
        	re.setContent(e.getMessage());
            e.printStackTrace();
        }
		return re;
	}
}
