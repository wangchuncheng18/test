package com.tarotdt.pas.web.service.impl;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tarotdt.pas.web.mapper.UserMapper;
import com.tarotdt.pas.web.model.UserInfoModel;
import com.tarotdt.pas.web.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	/**
	 * 用户注册
	 * 
	 * @param userInfoModel
	 *            用户类
	 */
	public void userRegister(UserInfoModel userInfoModel) {
		userMapper.saveUserInfo(userInfoModel);
	}

	/**
	 * 
	 * 登陆验证
	 * 
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            密码
	 *
	 * @return 用户类
	 */
	public UserInfoModel userLogin(String userName, String passWord) {
		return userMapper.login(userName, passWord);
	}

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
	public PageInfo<UserInfoModel> getUserInfoList(String userName, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		return new PageInfo<UserInfoModel>(userMapper.getUserInfoList(userName));
	}

	/**
	 * 验证密码
	 * 
	 * @param userId
	 *            用户id
	 * @param passWord
	 *            用户密码
	 * @return
	 */
	public boolean verificationPassWord(String userId, String passWord) {
		UserInfoModel um = userMapper.verificationPassWord(userId, passWord);
		if (um != null) {
			return true;
		}
		return false;
	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 *            用户id
	 * @param passWord
	 *            用户密码
	 */
	public void updatePassWord(String userId, String passWord) {
		userMapper.updatePassWord(userId, passWord);
	}

	/**
	 * 删除用户
	 * 
	 * @param userId
	 *            用户id
	 */
	public void deleteUserinfo(String userId) {
		userMapper.deleteUserinfo(userId);
		// 删除关联表信息
		userMapper.deleteUserGroupInfoByUserId(userId);
		userMapper.deletePowerInfoByUserId(userId);
	}
	
	public void deleteAllUserinfo() {
		userMapper.deleteAllUserinfo();
	}
	

	/**
	 * 修改用户状态
	 * 
	 * @param userId
	 *            用户id
	 * @param userState
	 *            用户状态
	 */
	public void updateUserInfoState(String userId, String userState) {
		userMapper.updateUserInfoState(userId, userState);
	}

	/**
	 * 根据用户id查询用户详细信息
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public UserInfoModel getUserInfoById(@Param("userId") String userId) {
		return userMapper.getUserInfoById(userId);
	}

	/**
	 * 修改用户信息
	 * 
	 * @param userInfoModel
	 *            用户信息类
	 */
	public void updateUserInfo(UserInfoModel userInfoModel) {
		userMapper.updateUserInfo(userInfoModel);
	}
	
	/**
	 * 验证用户名是否已使用
	 * 
	 * @param userName
	 *            用户名
	 * @return
	 */
	public boolean verificationUserName(@Param("userName") String userName){
		if(userMapper.verificationUserName(userName)!=null){
			return true;
		}else{
			return false;
		}
	}
}
