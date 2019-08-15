package com.example.demo.dao;


import java.io.Serializable;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TableMeta;

@Mapper
@Repository
public interface CreateDAO {

	boolean createNewTable(TableMeta tablemeta);
}
