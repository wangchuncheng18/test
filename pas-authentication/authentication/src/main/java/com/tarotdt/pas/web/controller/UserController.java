package com.tarotdt.pas.web.controller;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tarotdt.pas.web.model.HttpCode;
import com.tarotdt.pas.web.model.ResponseModel;
import com.tarotdt.pas.web.model.UserInfoModel;
import com.tarotdt.pas.web.service.UserService;
import com.tarotdt.pas.web.util.LoggerEx;
import com.tarotdt.pas.web.util.Utils;

/**
 * 用户类
 * 
 * @author lir
 *
 */
@RequestMapping("/api/user")
@RestController
public class UserController {
	static LoggerEx logger = LoggerEx.getLogger("com.tarotdt.pas.web.controller.user");
	@Autowired
	private UserService userService;

	/**
	 * 用户注册 http://localhost:8011/api/user/v1.0/userRegister
	 * 参数：{"userName":"liran","password":"wowangle","realName":"李冉","describeTex t":"测试用户保存信息","phone":"13439004327","email":"2311320821@qq.com","corporateName":"翺旗科技","positionName":"软件开发","jobName":"员工","address":"北京市朝阳区"}
	 * 
	 * @param userInfoModel
	 *          用户类
	 * @return
	 */
	@RequestMapping(value = "/v1.0/userRegister", method = RequestMethod.POST)
	public ResponseModel<?> userRegister(@RequestBody UserInfoModel userInfoModel) {
		ResponseModel<UserInfoModel> responseModel = new ResponseModel<UserInfoModel>();
		try {
			// 密码加密
			String md5Password = Utils.md5Base64(userInfoModel.getPassword());
			userInfoModel.setPassword(md5Password);
			// 存储用户信息
			userService.userRegister(userInfoModel);
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setData(userInfoModel);
			responseModel.setMsg("用户注册成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}
	
	/****
	 * 
	 * @return
	 */
	
	@RequestMapping(value = "/v1.0/getUserInfo", method = RequestMethod.POST)
	public ResponseModel<?> getUserInfo() {
		ResponseModel<UserInfoModel> responseModel = new ResponseModel<UserInfoModel>();
		try {
			responseModel.setCode(HttpCode.OK.value());
			//responseModel.setData( u);
			responseModel.setMsg("用户注册成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	

	/**
	 * 用户登陆 http://localhost:8011/api/user/v1.0/userLogin/admin/admin
	 * 
	 * @param userName
	 *          用户名
	 * @param password
	 *          密码
	 * @return
	 */
	@RequestMapping(value = "/v1.0/userLogin/{userName}/{password}", method = RequestMethod.GET)
	public ResponseModel<?> userLogin(HttpServletRequest req, HttpServletResponse response,
	    @PathVariable("userName") String userName, @PathVariable("password") String password) {
		ResponseModel<UserInfoModel> responseModel = new ResponseModel<UserInfoModel>();
		try {
			// 密码加密
			String md5Password = Utils.md5Base64(password);
			// 登陆验证
			UserInfoModel um = userService.userLogin(userName, md5Password);
			if (um == null) {
				responseModel.setCode(HttpCode.EXCEPTION_VALUE.value());
				responseModel.setMsg("用户名密码有误！");
			} else {
				// 判断用户是否为正常状态
				if (um.getUserState() == 1) {
					responseModel.setCode(HttpCode.OK.value());
					responseModel.setData(um);
					req.getSession().setAttribute("CurrentUser", um);
					Cookie cookie = new Cookie("userId", URLEncoder.encode(um.getUserId().toString(), "utf-8"));
					cookie.setPath("/");
					cookie.setMaxAge(60 * 60 * 24);
					response.addCookie(cookie);
					Cookie roleCookie = new Cookie("RoleCookie", URLEncoder.encode("user", "utf-8"));
					roleCookie.setMaxAge(60 * 60 * 24);
					roleCookie.setPath("/");
					response.addCookie(roleCookie);
					responseModel.setMsg("用户登陆成功！");
				} else {
					responseModel.setCode(HttpCode.EXCEPTION_VALUE.value());
					responseModel.setMsg("用户已被停用请联系管理员！");
				}
			}
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 用户登陆 http://localhost:8011/api/user/v1.0/userLogin/admin/admin
	 * 
	 * @param userName
	 *          用户名
	 * @param password
	 *          密码
	 * @return
	 */
	@RequestMapping(value = "/v1.0/userLogout/{userName}", method = RequestMethod.GET)
	public ResponseModel<?> userLogin(HttpServletRequest req, HttpServletResponse response,
	    @PathVariable("userName") String userName) {
		ResponseModel<UserInfoModel> responseModel = new ResponseModel<UserInfoModel>();
		try {
			// 判断用户是否为正常状态
			responseModel.setCode(HttpCode.OK.value());
			req.getSession().removeAttribute("CurrentUser");
			responseModel.setMsg("用户注销成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 查询用户列表
	 * http://localhost:8011/api/user/v1.0/getUserInfoList?userName=g&pageNum=1&pageSize=20
	 * 
	 * @param userName
	 *          查询条件用户名
	 * @param pageNum
	 *          当前页
	 * @param pageSize
	 *          每页多少数据
	 * @return
	 */
	@RequestMapping(value = "/v1.0/getUserInfoList", method = RequestMethod.GET)
	public ResponseModel<?> getUserInfoList(@RequestParam String userName, @RequestParam int pageNum,
	    @RequestParam int pageSize) {
		ResponseModel<PageInfo<UserInfoModel>> responseModel = new ResponseModel<PageInfo<UserInfoModel>>();
		try {
			// 处理返回值
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setData(userService.getUserInfoList(userName, pageNum, pageSize));
			responseModel.setMsg("查询用户列表成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 修改密码 http://localhost:8011/api/user/v1.0/updatePassWord/1/admin/wowangle
	 * 
	 * @param userId
	 *          用户id
	 * @param oldPassword
	 *          旧密码
	 * @param newPassword
	 *          新密码
	 * @return
	 */
	@RequestMapping(value = "/v1.0/updatePassWord/{userId}/{oldPassword}/{newPassword}", method = RequestMethod.PUT)
	public ResponseModel<?> updatePassWord(@PathVariable("userId") String userId,
	    @PathVariable("oldPassword") String oldPassword, @PathVariable("newPassword") String newPassword) {
		ResponseModel<String> responseModel = new ResponseModel<String>();
		try {
			String md5OldPassword = Utils.md5Base64(oldPassword);
			String md5NewPassword = Utils.md5Base64(newPassword);
			// 验证旧密码
			if (userService.verificationPassWord(userId, md5OldPassword)) {
				userService.updatePassWord(userId, md5NewPassword);
				// 处理返回值
				responseModel.setCode(HttpCode.OK.value());
				responseModel.setMsg("修改密码成功！");
			} else {
				responseModel.setCode(HttpCode.EXCEPTION_VALUE.value());
				responseModel.setMsg("用户输入密码有误！");
			}
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 重置密码 http://localhost:8011/api/user/v1.0/ResetPassWord/1
	 * 
	 * @param userId
	 *          用户id
	 * @return
	 */
	@RequestMapping(value = "/v1.0/ResetPassWord/{userId}", method = RequestMethod.PUT)
	public ResponseModel<?> ResetPassWord(@PathVariable("userId") String userId) {
		ResponseModel<String> responseModel = new ResponseModel<String>();
		try {
			// 重置密码默认六个零
			String md5Password = Utils.md5Base64("000000");
			userService.updatePassWord(userId, md5Password);
			// 处理返回值
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setMsg("重置密码成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 修改用户信息 http://localhost:8011/api/user/v1.0/updateUserInfo
	 * 参数：{"userId":1,"realName":"李冉1","describeText":"测试用户保存信息1","phone":"134390043271","email":"23113208211@qq.com","corporateName":"翺旗科技1","positionName":"软件开发1","jobName":"员工1","address":"北京市朝阳区1","operator":1}
	 * 
	 * @param userInfoModel
	 *          用户类
	 * @return
	 */
	@RequestMapping(value = "/v1.0/updateUserInfo", method = RequestMethod.POST)
	public ResponseModel<?> updateUserInfo(@RequestBody UserInfoModel userInfoModel) {
		ResponseModel<String> responseModel = new ResponseModel<String>();
		try {
			userService.updateUserInfo(userInfoModel);
			// 处理返回值
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setMsg("修改用户信息成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 删除用户 http://localhost:8011/api/user/v1.0/deleteUserinfo/2
	 * 
	 * @param userInfoModel
	 * @return
	 */
	@RequestMapping(value = "/v1.0/deleteUserinfo/{userId}", method = RequestMethod.PUT)
	public ResponseModel<?> deleteUserinfo(@PathVariable("userId") String userId) {
		ResponseModel<String> responseModel = new ResponseModel<String>();
		try {
			userService.deleteUserinfo(userId);
			// 处理返回值
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setMsg("删除用户成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 修改用户状态 http://localhost:8011/api/user/v1.0/updateUserInfoState/2/2
	 * 
	 * @param userId
	 *          用户id
	 * @param userState
	 *          用户状态
	 * @return
	 */
	@RequestMapping(value = "/v1.0/updateUserInfoState/{userId}/{userState}", method = RequestMethod.PUT)
	public ResponseModel<?> updateUserInfoState(@PathVariable("userId") String userId,
	    @PathVariable("userState") String userState) {
		ResponseModel<String> responseModel = new ResponseModel<String>();
		try {
			userService.updateUserInfoState(userId, userState);
			// 处理返回值
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setMsg("用户状态修改成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 根据用户id查看用户详情 http://localhost:8011/api/user/v1.0/getUserInfoById/1
	 * 
	 * @param userId
	 *          用户id
	 * @return
	 */
	@RequestMapping(value = "/v1.0/getUserInfoById/{userId}", method = RequestMethod.GET)
	public ResponseModel<?> getUserInfoById(@PathVariable("userId") String userId) {
		ResponseModel<UserInfoModel> responseModel = new ResponseModel<UserInfoModel>();
		try {
			// 处理返回值
			responseModel.setCode(HttpCode.OK.value());
			responseModel.setData(userService.getUserInfoById(userId));
			responseModel.setMsg("用户状态修改成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}

	/**
	 * 验证用户名是否已使用
	 * 
	 * @param userName
	 *          用户名
	 * 
	 * @return
	 */
	@RequestMapping(value = "/v1.0/verificationUserName/{userName}", method = RequestMethod.GET)
	public ResponseModel<?> verificationUserName(@PathVariable("userName") String userName) {
		ResponseModel<UserInfoModel> responseModel = new ResponseModel<UserInfoModel>();
		try {
			if (userService.verificationUserName(userName)) {
				responseModel.setCode(HttpCode.EXCEPTION_VALUE.value());
			} else {
				responseModel.setCode(HttpCode.OK.value());
			}
			// 处理返回值
			responseModel.setMsg("验证用户名成功！");
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			responseModel.setCode(HttpCode.INTERNAL_SERVER_ERROR.value());
			responseModel.setMsg("服务器内部出错了！");
		}
		return responseModel;
	}
}
