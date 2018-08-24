package com.tarotdt.pas.web.intercepter;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 自定义权限拦截器 在接口调用之前进行权限判断
 *
 */
public class PermissionInterceptor implements HandlerInterceptor {

	private static Logger logger = Logger.getLogger(PermissionInterceptor.class);

	/**
	 * 在方法调用前从request中取cookie信息 从cookie中取用户信息 判断用户是否有权限访问接口
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			
		}
		return true;
	}

	/*
	
	  *//**
	     * 检查功能权限
	     *
	     * @param UserInfoModel
	     * @param value
	     * @return
	     */

	/*
	 * private boolean checkFunctionPermission(UserInfoModel UserInfoModel, String
	 * value) { if (UserInfoModel != null) { Role role =
	 * UserInfoModelRoleCache.CACHE.get(UserInfoModel.getId()); List<String> list
	 * = RolePermissionCache.CACHE.get(role.getId()); if (list != null &&
	 * list.size() > 0) { for (String permissionName : list) { if
	 * (value.equals(permissionName)) { return true; } } } } return false; }
	 * 
	 *//**
	   * 检查数据权限
	   *
	   * @param UserInfoModel
	   * @param request
	   * @return
	   *//*
	     * public boolean checkDataPermission(UserInfoModel UserInfoModel,
	     * HttpServletRequest request) { if (UserInfoModel != null) { Long
	     * loginUserInfoModelId = UserInfoModel.getId(); String owner =
	     * request.getParameter("owner"); if (owner == null || "".equals(owner) ||
	     * "null".equals(owner)) { return true; } Long dataOwner = null; try {
	     * dataOwner = Long.valueOf(owner); } catch (NumberFormatException e) {
	     * logger.debug("owner is wrong type，the owner：" + owner); return false; }
	     * //登录用户与数据拥有者一直，允许操作 if (loginUserInfoModelId.equals(dataOwner)) {
	     * return true; } else { Role loginUserInfoModelRole =
	     * UserInfoModelRoleCache.CACHE.get(loginUserInfoModelId);
	     * //登录用户是超级管理员，允许操作 if
	     * (RoleEnmu.superadmin.value().equals(loginUserInfoModelRole.getName()))
	     * { return true; } //如果数据拥有者不存在，则所有用户都可访问 if
	     * (!UserInfoModelRoleCache.CACHE.containsKey(owner)) { return true; }
	     * Role operatorUserInfoModelRole =
	     * UserInfoModelRoleCache.CACHE.get(dataOwner);
	     * //登录用户的角色权限大于数据拥有者的角色权限，允许操作 if (loginUserInfoModelRole.getLevel() <
	     * operatorUserInfoModelRole.getLevel()) { return true; } } } return
	     * false; }
	     */

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	    ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	    throws Exception {

	}

}
