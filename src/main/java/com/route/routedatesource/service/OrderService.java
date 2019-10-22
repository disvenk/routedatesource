package com.route.routedatesource.service;

import com.route.routedatesource.conf.DatabaseContextHolder;
import com.route.routedatesource.mapper.HeMapper;
import com.route.routedatesource.mapper.MyMapper;
import com.route.routedatesource.model.He;
import com.route.routedatesource.model.My;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
	
	@Autowired
	private MyMapper myMapper;
	@Autowired
	private HeMapper heMapper;
	
	public My getMy(Long id) {
		//先设置数据源
		DatabaseContextHolder.setDbType("my");
		//当执行查询的时候就会先走动态数据源切换
		My my = myMapper.getMyById(id);
		DatabaseContextHolder.clearDbType();
		return my;
	}
	
	public He getHe(Long id){
		DatabaseContextHolder.setDbType("he");
		He he = heMapper.getHeById(id);
		DatabaseContextHolder.clearDbType();
		return he;
	}

}
