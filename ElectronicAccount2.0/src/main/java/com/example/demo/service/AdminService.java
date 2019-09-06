package com.example.demo.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
public class AdminService {

	@Autowired
	private TableOpDAO tabledao;
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	/***
	 * 批量修改某个台账中的记录,包括添加、修改、删除记录
	 * @param
	 * @return
	 */
	public Result<Object> modifyRecords(Permission permission, Map<String, Integer> metadata, List<JSONObject> data_raw) {
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
			 * 预处理需要添加的数据——根据optype将data分为三个列表，并洗去optype属性
			 */
			List<JSONObject> data_add = new ArrayList<JSONObject>();
			List<JSONObject> data_edit = new ArrayList<JSONObject>();
			List<JSONObject> data_delete = new ArrayList<JSONObject>();
			for(int i = 0; i < data_raw.size(); i++) {
				JSONObject json = data_raw.get(i);
				Integer optype = json.getInteger("optype");
				json.remove("optype");
				switch(optype) {
					case 0: data_add.add(json); break;
					case 1: data_edit.add(json); break;
					case 2: data_delete.add(json); break;
					default: break;
 				}
			}
			
			//给这个三个列表中的各属性排序
			Iterator<JSONObject> iter = data_add.iterator();
			List<Map<String, Object>> data_add_map = new ArrayList<Map<String, Object>>();
			while(iter.hasNext()) {
				Map<String, Object> datum = new TreeMap<String, Object>((Map<String, Object>)iter.next());
				data_add_map.add(datum);
			}
			List<Map<String, Object>> data_edit_map = new ArrayList<Map<String, Object>>();
			iter = data_edit.iterator();
			while(iter.hasNext()) {
				Map<String, Object> datum = new TreeMap<String, Object>((Map<String, Object>)iter.next());
				data_edit_map.add(datum);
			}
			List<Map<String, Object>> data_delete_map = new ArrayList<Map<String, Object>>();
			iter = data_delete.iterator();
			while(iter.hasNext()) {
				Map<String, Object> datum = new TreeMap<String, Object>((Map<String, Object>)iter.next());
				data_delete_map.add(datum);
			}
			
			//给这三个列表分别生成对应的对象列表
			HashMap<String, Class<?>> propertymap = new HashMap<String, Class<?>>();
            propertymap.put("c_id", Class.forName("java.lang.Integer"));
            List<Object> add_contents = new ArrayList<Object>();
            List<Object> edit_contents = new ArrayList<Object>();
            List<Object> delete_contents = new ArrayList<Object>();		

			//使用sqlsession的batch模式修改台账内容
			//添加部分
			if(data_add_map != null && data_add_map.size() != 0) {
				Iterator<Map<String, Object>> it = data_add_map.iterator();			
				while(it.hasNext()) {
					it.next();
					DynamicBean bean = new DynamicBean(propertymap);
					Object mybean = bean.getObject();
					add_contents.add(mybean);
				}
				
				for(int i = 0; i < data_add_map.size(); i++) {
					(sqlsession.getMapper(TableOpDAO.class)).addRecords(permission.getTableid(), data_add_map.get(i), add_contents.get(i));
				}
				sqlsession.commit();
				sqlsession.clearCache();
				
				for(int i = 0; i < add_contents.size(); i++) {
					Object o = add_contents.get(i);
	 				Field f_c_id = o.getClass().getDeclaredField("$cglib_prop_c_id");
	 				f_c_id.setAccessible(true);
	 				int index = (Integer) f_c_id.get(o);
					Provenance add = Provenance.pronenanceAdd(permission.getTableid(), index, data_add.get(i), new Date(), permission.getUserid());
					(sqlsession.getMapper(TableOpDAO.class)).addProvenance(add);
				}
				sqlsession.commit();
			}    
			
			//修改部分(修改部分分为三个步骤，先查询出所有需修改的记录的原始内容，再删除原有记录，最后添加一条新纪录指向原来的记录)
			if(data_edit_map != null && data_edit_map.size() != 0) {
				List<Provenance> pro_list = new ArrayList<Provenance>();
				List<JSONObject> o_contents_list = tabledao.getRecords(permission.getTableid(), data_edit_map);
				//此时o_contents_list、data_edit、data_edit_map三个列表的顺序不一致，需要将这三个列表的顺序变为一致
				//在这里用c_id的值作为排序 
				Collections.sort(o_contents_list, new Comparator<JSONObject>() {
					@SuppressWarnings("unchecked")
					@Override
					public int compare(JSONObject o1, JSONObject o2) {
						return ((Comparable<Integer>) o1.get("c_id")).compareTo((Integer) o2.get("c_id"));
					}
				});
				Collections.sort(data_edit, new Comparator<JSONObject>() {
					@SuppressWarnings("unchecked")
					@Override
					public int compare(JSONObject o1, JSONObject o2) {
						return ((Comparable<Integer>) o1.get("c_id")).compareTo((Integer) o2.get("c_id"));
					}
				});
				Collections.sort(data_edit_map, new Comparator<Map<String, Object>>() {
					@SuppressWarnings("unchecked")
					@Override
					public int compare(Map<String, Object> map1, Map<String, Object> map2) {
						return ((Comparable<Integer>) map1.get("c_id")).compareTo((Integer) map2.get("c_id"));
					}
				});	
				
				Iterator<Map<String, Object>> it = data_edit_map.iterator();
				while(it.hasNext()) {
					it.next();
					DynamicBean bean = new DynamicBean(propertymap);
					Object mybean = bean.getObject();
					edit_contents.add(mybean);
				}
				
				for(int i = 0; i < data_edit_map.size(); i++) {
					if(data_edit_map.get(i).get("c_id") == null)
						throw new Exception("数据出错");
					//删除原有记录
					(sqlsession.getMapper(TableOpDAO.class)).deleteRecords(permission.getTableid(), (int)data_edit_map.get(i).get("c_id"));
					//添加新的记录
					data_edit_map.get(i).put("c_id", null);
					(sqlsession.getMapper(TableOpDAO.class)).addRecords(permission.getTableid(), data_edit_map.get(i), edit_contents.get(i));
					JSONObject originalcontent = JSONObject.parseObject(JSONObject.toJSONString(o_contents_list.get(i)));
					Provenance pro = Provenance.pronenanceEditRecord(permission.getTableid(), data_edit.get(i).getIntValue("c_id"), originalcontent, data_edit.get(i), new Date(), permission.getUserid());
					pro_list.add(pro);
				}
				sqlsession.commit();
				sqlsession.clearCache();
				
				for(int i = 0; i < pro_list.size(); i++) 
					(sqlsession.getMapper(TableOpDAO.class)).addProvenance(pro_list.get(i));
				sqlsession.commit();
			}     
			
			//删除部分
			if(data_delete_map != null && data_delete_map.size() != 0) {
				Iterator<Map<String, Object>> it = data_delete_map.iterator();
				while(it.hasNext()) {
					it.next();
					DynamicBean bean = new DynamicBean(propertymap);
					Object mybean = bean.getObject();
					delete_contents.add(mybean);
				}

				for(int i = 0; i < data_delete_map.size(); i++) {
					if (data_delete_map.get(i).get("c_id") == null) 
						throw new Exception("数据出错");
					(sqlsession.getMapper(TableOpDAO.class)).deleteRecords(permission.getTableid(), (int)data_delete_map.get(i).get("c_id"));
				}
				sqlsession.commit();
				sqlsession.clearCache();
				
				for(int i = 0; i < data_delete.size(); i++) {
					Provenance add = Provenance.pronenanceDeleteRecord(permission.getTableid(), data_delete.get(i).getIntValue("c_id"), data_delete.get(i), new Date(), permission.getUserid());
					(sqlsession.getMapper(TableOpDAO.class)).addProvenance(add);
				}
				sqlsession.commit();
			} 
			re.setSuccess(true);
			re.setMsg("台账修改成功");
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
	
	/***
	 * 更新台账的结构
	 * @param
	 * @return
	 */
	public Result<Object> updateTableStructure() {
		Result<Object> re = new Result<Object>();
		return re;
	}
	
	/***
	 * 删除台账
	 * @param
	 * @return
	 */
	public Result<Object> deleteTable(Permission permission) {
		Result<Object> re = new Result<Object>();
		
		//验证权限信息是否真实
		boolean is_true = PermissionVerify.verify(permission, tabledao);
		if(!is_true) {
			re.setMsg("你用的权限真的还是假的你心里有点数");
			return re;
		}
		
		//创建一个操作历史(type： 删除)
		Provenance delete = Provenance.provenanceDelete(permission.getTableid(), new Date(), permission.getUserid());
				
		try {
			//更改tablelist中该表的状态更改为0
			int affectedrow = tabledao.deleteTable(permission);
			if(affectedrow > 0) {
				//添加一条操作历史
				tabledao.addProvenance(delete);
				re.setSuccess(true);
				re.setMsg("台账删除成功");
			} else {
				re.setMsg("删除台账失败");
			}	
			} catch (Exception e) {
				re.setMsg("系统出错");
	        	re.setContent(e.getMessage());
	        	e.printStackTrace();
			} 
		return re;
	}
}
