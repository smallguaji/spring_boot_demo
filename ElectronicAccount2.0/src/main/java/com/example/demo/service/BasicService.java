package com.example.demo.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.annotation.PermissionVerify;
import com.example.demo.dao.TableOpDAO;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Result;

/***
 * @author 小呱唧
 *创建新表的服务逻辑
 */

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BasicService {

	@Autowired
	private TableOpDAO tabledao;
	
	/***
	 * 获得某个台账中的所有内容
	 * @param permission
	 * @return result
 	 */
	public Result<Object> getTableDetail (Permission permission) {
		Result<Object> re = new Result<Object>();
		try {
			//验证权限信息是否真实
			boolean is_true = PermissionVerify.verify(permission, tabledao);
			if(!is_true) {
				re.setMsg("你用的权限真的还是假的你心里有点数");
				return re;
			}
			
			//通过权限信息验证后，从数据库中获取该台账的内容
			List<Map<String, Object>> rawcontents = tabledao.getTableDetail(permission);
			if(rawcontents.isEmpty()) {
	    		re.setMsg("该表格还未添加内容");
	    	} else {
	    		re.setSuccess(true);
	    		List<JSONObject> tablecontents = new ArrayList<JSONObject>();
	    		Iterator<Map<String, Object>> iter = rawcontents.iterator();
	    		while(iter.hasNext()) {
	    			tablecontents.add(JSONObject.parseObject(JSON.toJSONString(iter.next())));
            	}
	    		re.setMsg("获取成功");
        		re.setContent(tablecontents);
	    	}
		} catch (Exception e) {
			re.setMsg("系统故障");
        	re.setContent(e.getMessage());
            e.printStackTrace();
		}
		return re;
	}

}
