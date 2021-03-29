package com.eluon.vepc.mano.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/glance")
public class GlanceRestController {

	@RequestMapping("/test.do")
	public String test() {
		return "{test:'test11'}";
	}
	
	@RequestMapping("/images.do")
	public List<Object> images() {
		
		List<Object> list = new ArrayList<Object>();

		for (int i = 0; i < 10; i++) {

			int format = (int) (Math.random() * 10000) + 1;
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "name #" + i);
			map.put("type", "type #" + i);
			map.put("status", "status #" + i);
			map.put("public", "public #" + i);
			map.put("protected", "protected #" + i);
			map.put("disk_format", "disk_format #" + format);
			map.put("size", "size #" + i);

			list.add(map);
		}
		
		return list;
	}
}
