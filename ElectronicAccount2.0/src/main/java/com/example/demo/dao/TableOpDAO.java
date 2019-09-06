package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Provenance;

@Mapper
@Repository
public interface TableOpDAO {
	
	/**通用子功能**/
	//批量创建权限
	int addPermission(@Param("list")List<Permission> list);
	//批量创建历史记录
	int addProvenance(Provenance provenance);
	//验证权限信息
	Integer verifyPermission_step_one(@Param("permission")Permission permission);
	Map<String, Integer> verifyPermission_step_two(@Param("permission")Permission permission);
	
	//获得某个台账的详细信息
	List<Map<String, Object>> getTableDetail(@Param("permission")Permission permission);
	
	//获得某个台账的某条记录
	List<JSONObject> getRecords(@Param("tableid")String tableid, @Param("list")List<Map<String, Object>> list);
	
	//批量向某个台账中添加记录, 需批量返回主键id。
	void addRecords(@Param("tableid")String tableid, @Param("rows")Map<String, Object> rows, @Param("o")Object o);
	//批量修改台账中的记录
	int editRecords(@Param("list")List<Permission> list);
	//批量删除台账中的记录
	void deleteRecords(@Param("tableid")String tableid, @Param("c_id")int c_id);
	
	//修改台账结构
	int updateTableStructure();
	
	//删除台账(逻辑删除)  将 tablelist 表中该表的状态改为0
	int deleteTable(@Param("permission")Permission permission);  
}
