package com.qn.controller;

import java.util.ArrayList;
import java.util.List;

import com.qn.service.ConfigService;
import com.qn.service.PushManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qn.model.User;

/**
 * @description:
 * @author: Youq
 * @create: 2019-03-07 14:52
 */
@RestController
@RequestMapping("/user1")
public class RestUserController {

	@Autowired
	ConfigService configService;
	@Autowired
	PushManager pushManager;

	@RequestMapping("/{id}") 
	public User getUser(@PathVariable Integer id) {
		return new User(id,"张三",20,"中国广州");
	}
	
	@RequestMapping("/list") 
	public List<User> listUser() {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i <10; i++) {
			userList.add(new User(i,"张三"+i,20+i,"中国广州"));
		}

//		String str1 = "ffmpeg -rtsp_transport tcp -i ";
//		String str2 = "\"rtsp://admin:qn1234567890@192.168.1.64:554/h264/ch1/main/av_stream\"";
//		String str3 = " -s:v 1280x720 -bufsize:v 500k -b:v 1000k -bt 1000k -maxrate:v 950k -minrate:v 850k -g 50 -keyint_min 2 -nal-hrd cbr ";
//		String str4 = " -pass 1 -passlogfile ffmpeg2pass -codec:v libx264 -sc_threshold 0 -bf 0 -b_strategy 0 -r 20 ";
//		String str5 = " -profile:v high -preset:v fast -tune:v zerolatency ";
//		String str6 = " -f flv \"rtmp://192.168.1.133:1935/live/push\" ";
		//pushManager.startPush();

		return userList;
	}
	@RequestMapping("/kill")
	public void killProcess(){
		pushManager.killProcess("ffmpeg");
	}
}
