package com.example.demo.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.annotation.PermissionVerify;
import com.example.demo.dao.TableOpDAO;
import com.example.demo.dynamic.DynamicBean;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Provenance;
import com.example.demo.pojo.Result;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AdvancedService {
	
	@Autowired
	private TableOpDAO tabledao;
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	/***
	 * 批量添加记录
	 * @param
	 * @return
	 */
	public Result<Object> addRecords(Permission permission, Map<String, Integer> metadata, List<JSONObject> data_raw) {
		Result<Object> re = new Result<Object>();
		SqlSession sqlsession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		
		try {
			//验证权限信息是否真实
			boolean is_true = PermissionVerify.verify(permission, tabledao);
			if(!is_true) {
				re.setMsg("你用的权限真的还是假的你心里有点数");
				return re;
			}
			
			/**
			 * 预处理需要添加的数据——判断数据是否都是add类型，并将optype属性洗去
			 */
			List<JSONObject> data = new ArrayList<JSONObject>();
			for(int i = 0; i < data_raw.size(); i++) {
				JSONObject json = data_raw.get(i);
				if(json.getInteger("optype") == 0) {
					json.remove("optype");
					data.add(json);
				}
			}
			
			/**
			 * 把需要插入的数据排序，后期需用这个list配合生成对象的list往数据库中添加信息，并返回插入数据库后的主键
			 */
			List<Map<String, Object>> data_map = new ArrayList<Map<String, Object>>();
			Iterator<JSONObject> iter = data.iterator();
			while(iter.hasNext()) {
				Map<String, Object> datum = new TreeMap<String, Object>((Map<String, Object>)iter.next());
				data_map.add(datum);
			}
			
			/**
			 * 根据台账的元数据动态生成类及对象。
			 */
			HashMap<String, Class<?>> propertymap = new HashMap<String, Class<?>>();
            propertymap.put("c_id", Class.forName("java.lang.Integer"));
			//遍历list，生成多个对象,关键在对象的顺序与数据列表的顺序一致，以保证返回的主键与记录对应
			List<Object> contents = new ArrayList<Object>();
			Iterator<Map<String, Object>> it = data_map.iterator();			
			while(it.hasNext()) {
				it.next();
				DynamicBean bean = new DynamicBean(propertymap);
				Object mybean = bean.getObject();
				contents.add(mybean);
			}
		
			/**
			 * 使用sqlsession的batch模式在对应的台账中添加记录，并同步生成对应的历史数据
			 */
			for(int i = 0; i < data_map.size(); i++) {
				(sqlsession.getMapper(TableOpDAO.class)).addRecords(permission.getTableid(), data_map.get(i), contents.get(i));
			}
			sqlsession.commit();
			sqlsession.clearCache();
			
			for(int i = 0; i < contents.size(); i++) {
				Object o = contents.get(i);
 				Field f_c_id = o.getClass().getDeclaredField("$cglib_prop_c_id");
 				f_c_id.setAccessible(true);
 				int index = (Integer) f_c_id.get(o);
				Provenance add = Provenance.pronenanceAdd(permission.getTableid(), index, data.get(i), new Date(), permission.getUserid());
				(sqlsession.getMapper(TableOpDAO.class)).addProvenance(add);
			}
			sqlsession.commit();
			sqlsession.close();
			
			re.setSuccess(true);
			re.setMsg("添加成功");
			re.setContent(contents);
			
		} catch (Exception e) {
			sqlsession.rollback();
			re.setMsg("系统出错");
        	re.setContent(e.getMessage());
        	e.printStackTrace();
		} finally {
			if(sqlsession != null) {
				sqlsession.clearCache();
				sqlsession.close();
			}
				
		}
		return re;
	}
}
