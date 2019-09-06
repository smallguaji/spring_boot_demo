package com.example.demo.annotation;

import java.util.Map;

import com.example.demo.dao.TableOpDAO;
import com.example.demo.pojo.Permission;


public class PermissionVerify {
	
	public static boolean verify(Permission permission, TableOpDAO tabledao) {
		Integer flag = tabledao.verifyPermission_step_one(permission);
		if (flag == null) {
			Map<String, Integer> map = tabledao.verifyPermission_step_two(permission);
			flag = map.get("permissionlevel");
			if(flag == null || flag > permission.getLevel()) 
				return false;
			if (flag == permission.getLevel())
				return true;
			if (flag < permission.getLevel()) {
				if (map.get("level") == 3 && permission.getLevel() == 3)
					return true;
				return false;
			}
		}
		return true;
	}
}
