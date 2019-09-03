package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.TableOpDAO;
import com.example.demo.dynamic.DynamicBean;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Result;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdvancedService {
	
	@Autowired
	private TableOpDAO tabledao;
	
	/***
	 * 批量添加记录
	 * @param
	 * @return
	 */
	public Result addRecords(Permission permission, Map<String, Integer> metadata, List<JSONObject> data) {
		Result re = new Result();
		try {
			//验证权限信息是否真实
			Integer flag = tabledao.verifyPermission_step_one(permission);
			if (flag == null) {
				flag = tabledao.verifyPermission_step_two(permission);
				if(flag == null || flag != permission.getLevel()) {
					re.setMsg("你用的权限真的还是假的你心里有点数");
					return re;
				}
			}
			/**
			 * 根据台账的元数据动态生成类及对象。
			 */
			HashMap<String, Class<?>> propertymap = new HashMap<String, Class<?>>();
			//动态类中添加内置主键
            propertymap.put("c_id", Class.forName("java.lang.Integer"));
			for (Map.Entry<String, Integer> entry : metadata.entrySet()) {
	            String type = new String();
	            switch (entry.getValue()) {
	            	case 1: type = "java.lang.String"; break;   //文字
	            	case 2: type = "java.lang.Integer"; break;   //整数
	            	case 3: type = "java.lang.Float"; break;   //小数
	            	case 4: type = "java.util.Date"; break;   //日期
	            	default: throw new Exception("类型出错！");
	            }
	            propertymap.put(entry.getKey(), Class.forName(type));
	        }
			//遍历list，生成多个对象
			List<Object> contents = new ArrayList<Object>();
			Iterator<JSONObject> iter = data.iterator();
			while(iter.hasNext()) {
				DynamicBean bean = new DynamicBean(propertymap);
				for(Map.Entry<String, Object> entry : iter.next().entrySet()){
					bean.setValue(entry.getKey(), entry.getValue());
				}
				Object mybean = bean.getObject();
				contents.add(mybean);
			}
		
			/**
			 * 在对应的台账中添加记录
			 */
			//tabledao.addRecords(permission.getTableid(), metadata, contents);
			re.setSuccess(true);
			re.setMsg("添加成功");
			re.setContent(contents);

			/**
			 * 生成对应的历史记录
			 */
			
		
		} catch (Exception e) {
			re.setMsg("系统出错");
        	re.setContent(e.getMessage());
        	e.printStackTrace();
		}
		return re;
	}
}
