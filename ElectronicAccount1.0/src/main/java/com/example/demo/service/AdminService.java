package com.example.demo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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
	 * 批量修改某个台账中的记录
	 * @param
	 * @return
	 */
	public Result modifyRecords() {
		Result re = new Result();
		return re;
	}
	
	/***
	 * 批量删除某个台账中的记录
	 * @param
	 * @return
	 */
	public Result deleteRecords() {
		Result re = new Result();
		return re;
	}
	
	/***
	 * 更新台账的结构
	 * @param
	 * @return
	 */
	public Result updateTableStructure() {
		Result re = new Result();
		return re;
	}
	
	/***
	 * 删除台账
	 * @param
	 * @return
	 */
	public Result deleteTable(Permission permission) {
		Result re = new Result();
		SqlSession sqlsession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		
		//验证权限信息是否真实
		Integer flag = tabledao.verifyPermission_step_one(permission);
		if (flag == null) {
			flag = tabledao.verifyPermission_step_two(permission);
			if(flag == null || flag != permission.getLevel()) {
				re.setMsg("你用的权限真的还是假的你心里有点数");
				return re;
			}
		}
		
		//创建一个操作历史(type： 删除)
		Provenance delete = Provenance.provenanceDelete(permission.getTableid(), new Date(), permission.getUserid());
		List<Provenance> prolist = new ArrayList<Provenance>();
		prolist.add(delete);
				
		try {
			//更改tablelist中该表的状态更改为0
			int affectedrow = tabledao.deleteTable(permission);
			if(affectedrow > 0) {
				//添加一条操作历史
				sqlsession.insert("com.example.demo.dao.TableOpDAO.addProvenance", prolist);
				sqlsession.commit();
				re.setSuccess(true);
				re.setMsg("台账删除成功");
			} else {
				re.setMsg("删除台账失败");
			}	
			} catch (Exception e) {
				sqlsession.rollback();
				re.setMsg("系统出错");
	        	re.setContent(e.getMessage());
	        	e.printStackTrace();
			} finally {
	        	if (sqlsession != null) {
	        		sqlsession.close();
	            }
	        }
		return re;
	}
}
