package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.pojo.Permission;
import com.example.demo.pojo.Provenance;
import com.example.demo.pojo.TableMeta;
import com.example.demo.pojo.User;

@Mapper
@Repository
public interface GeneralOpDAO{
	//查找是否有用户
	User findUserByOfficenum(User user);
	//查询用户自己创建的台账列表
	List<Map<String, Object>> searchOwnedTableList(User user);
	//根据搜索条件获取用户有权限的台账列表
	List<Map<String, Object>> searchTableList(@Param("map") Map<String, Object> info);
	
	//在数据库中创建一个新表
	int addNewTable(@Param("map") Map<String, Object> map, @Param("tablemeta") TableMeta tablemeta);
	//在table list表中添加一个表的记录
	int createNewTable(TableMeta tablemeta);
	//批量创建权限
	int addPermission(@Param("list") ArrayList<Permission> list);
	//批量创建历史记录
	int addProvenance(@Param("list") ArrayList<Provenance> list);
	
	//获得用户关于某个台账的权限
	int getPermissionLevel(@Param("user") User user, @Param("tablemeta") TableMeta tablemeta);

	//获得用户历史操作过的台账
	List<Map<String, Object>> searchHistoricalOperatedTableList(User user);
}
