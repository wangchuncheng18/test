package com.tarotdt.server.api.service;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.google.gson.JsonObject;

/**
 * Created by wangcc on 3/16/17.
 * 查询下级请求路径接口
 */
@FeignClient(name="server-register")
public interface ApiServiceI {


	@RequestMapping(value = "/server/registered/{name}", method = RequestMethod.GET)
	JsonObject getServerList(@PathVariable("name") String name);
}
