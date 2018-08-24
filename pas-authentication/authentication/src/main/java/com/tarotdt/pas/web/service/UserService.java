package com.tarotdt.pas.web.service;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.PageInfo;
import com.tarotdt.pas.web.model.UserInfoModel;

public interface UserService {
	/**
	 * 用户注册
	 * 
	 * @param userInfoModel
	 *            用户类
	 */
	public void userRegister(UserInfoModel userInfoModel);

	/**
	 * 用户登陆
	 * 
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            密码
	 * @return
	 */
	public UserInfoModel userLogin(String userName, String passWord);

	/**
	 * 查询用户列表
	 * 
	 * @param userName
	 *            查询条件用户名
	 * @param pageNum
	 *            当前页
	 * @param pageSize
	 *            每页多少数据
	 * @return
	 */
	public PageInfo<UserInfoModel> getUserInfoList(String userName, int pageNum, int pageSize);

	/**
	 * 验证密码
	 * 
	 * @param userId
	 *            用户id
	 * @param passWord
	 *            用户密码
	 * @return
	 */
	public boolean verificationPassWord(String userId, String passWord);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 *            用户id
	 * @param passWord
	 *            用户密码
	 */
	public void updatePassWord(String userId, String passWord);
	
	/**
	 * 删除用户
	 * 
	 * @param userId
	 *            用户id
	 */
	public void deleteUserinfo(@Param("userId") String userId);

	public void deleteAllUserinfo();
	/**
	 * 修改用户状态
	 * 
	 * @param userId
	 *            用户id
	 * @param userState
	 *            用户状态
	 */
	public void updateUserInfoState(@Param("userId") String userId, @Param("userState") String userState);
	
	/**
	 * 根据用户id查询用户详细信息
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public UserInfoModel getUserInfoById(@Param("userId") String userId);
	
	/**
	 * 修改用户信息
	 * 
	 * @param userInfoModel 用户信息类
	 */
	public void updateUserInfo(UserInfoModel userInfoModel);
	
	/**
	 * 验证用户名是否已使用
	 * 
	 * @param userName
	 *            用户名
	 * @return
	 */
	public boolean verificationUserName(@Param("userName") String userName);
}
