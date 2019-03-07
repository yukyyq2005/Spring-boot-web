package com.qn.controller;

import java.util.ArrayList;
import java.util.List;

import com.qn.model.Config;
import com.qn.service.ConfigService;
import com.qn.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qn.model.User;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@Controller
@RequestMapping("/user3")
public class UserController {


	@Autowired
	ConfigService configService;

	@RequestMapping("/{id}") 
	public String  getUser(@PathVariable Integer id,Model model) {
		
		model.addAttribute("user",new User(id,"张三",20,"中国广州"));
		return "/user/detail";
	}
	
	@RequestMapping("/list")
	public Object  listUser(Model model) {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i <10; i++) {
			userList.add(new User(i,"张三"+i,20+i,"中国广州"));
		}

		Config config = new Config("1","2","3","4","5");
		configService.saveConfigInfo(config);

		System.out.println(configService.getConfigInfo());

		model.addAttribute("users", userList);
		return "/user/list";
	}
}
