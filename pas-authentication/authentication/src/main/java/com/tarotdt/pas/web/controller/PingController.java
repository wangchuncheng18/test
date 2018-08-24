package com.tarotdt.pas.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tarotdt.pas.web.model.HttpCode;
import com.tarotdt.pas.web.model.ResponseModel;
import com.tarotdt.pas.web.util.LoggerEx;

@RequestMapping("/api/ping")
@RestController
public class PingController {
	static LoggerEx logger = LoggerEx.getLogger("com.tarotdt.pas.web.controller.user");
		@RequestMapping(value = "/v1.0/ping", method = RequestMethod.GET)
	public ResponseModel<?> ping() {
		ResponseModel<String> responseModel = new ResponseModel<String>();
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setData("OK");
			responseModel.setMsg("测试通过");
		return responseModel;
	}
}
