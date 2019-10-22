package com.route.routedatesource.controller;

import com.alibaba.fastjson.JSONObject;
import com.route.routedatesource.model.He;
import com.route.routedatesource.model.My;
import com.route.routedatesource.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/person")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping(path="/my/{userId}", method={RequestMethod.GET})
	public JSONObject getMy(@PathVariable("userId") Long userId) {
		My my = orderService.getMy(userId);
		JSONObject json = new JSONObject();
		json.put("id",my.getId());
		json.put("name",my.getName());
		json.put("age",my.getAge());
		return json;

	}

	@RequestMapping(path="/he/{userId}", method={RequestMethod.GET})
	public JSONObject getHe(@PathVariable("userId") Long userId) {
		He he = orderService.getHe(userId);
		JSONObject json = new JSONObject();
		json.put("id",he.getId());
		json.put("name",he.getName());
		json.put("age",he.getAge());
		return json;

	}


	public static void main(String[] args) {
		System.out.println(new Date().getTime()%1024);
		System.out.println();

	}
}
