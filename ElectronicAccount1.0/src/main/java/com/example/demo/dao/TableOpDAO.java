package com.example.demo.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Provenance;

@Mapper
@Repository
public interface TableOpDAO {
	
	/**通用子功能**/
	//批量创建权限
	int addPermission(@Param("list")ArrayList<Permission> list);
	//批量创建历史记录
	int addProvenance(@Param("list")ArrayList<Provenance> list);
	//验证权限信息
	Integer verifyPermission_step_one(@Param("permission")Permission permission);
	Integer verifyPermission_step_two(@Param("permission")Permission permission);
	
	//获得某个台账的详细信息
	List<Map<String, Object>> getTableDetail(@Param("permission")Permission permission);
	
	//批量向某个台账中添加记录, 需批量返回主键id。
	void addRecords(@Param("tableid")String tableid, @Param("columns")Map<String, Integer> columns, @Param("list")List<Object> list);
	
	//批量修改台账中的记录
	int modifyRecords(@Param("list")ArrayList<Permission> list);
	//批量删除台账中的记录
	int deleteRecords(@Param("list")ArrayList<Permission> list);
	//修改台账结构
	int updateTableStructure();
	//删除台账(逻辑删除)  将 tablelist 表中该表的状态改为0
	int deleteTable(@Param("permission")Permission permission);  
	
	void insert_test(@Param("list")List<Object> list);
}
