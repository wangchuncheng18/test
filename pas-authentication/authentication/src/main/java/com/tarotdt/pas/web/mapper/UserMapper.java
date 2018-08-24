package com.tarotdt.pas.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tarotdt.pas.web.model.UserInfoModel;


public interface UserMapper {
	/**
	 * 保存用户信息
	 * 
	 * @param userInfoModel
	 *            用户信息类
	 */
	public void saveUserInfo(UserInfoModel userInfoModel);

	/**
	 * 
	 * 登陆验证
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 *
	 * @return 用户类
	 */
	public UserInfoModel login(@Param("userName") String userName, @Param("password") String password);

	/**
	 * 查询用户列表
	 * 
	 * @param userName
	 *            查询条件用户名
	 * @return
	 */
	public List<UserInfoModel> getUserInfoList(@Param("userName") String userName);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 *            用户id
	 * @param password
	 *            密码
	 */
	public void updatePassWord(@Param("userId") String userId, @Param("password") String password);

	/**
	 * 根据用户id及密码验证用户
	 * 
	 * @param userId
	 *            用户id
	 * @param password
	 *            密码
	 * @return
	 */
	public UserInfoModel verificationPassWord(@Param("userId") String userId, @Param("password") String password);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 *            用户id
	 */
	public void deleteUserinfo(@Param("userId") String userId);
	/**
	 * 删除用户
	 * 
	 * @param userId
	 *            用户id
	 */
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
	 * @param userInfoModel
	 *            用户信息类
	 */
	public void updateUserInfo(UserInfoModel userInfoModel);

	/**
	 * 删除组用户关联表数据
	 * 
	 * @param userId
	 *            用户id
	 */
	public void deleteUserGroupInfoByUserId(@Param("userId") String userId);

	/**
	 * 删除权限用户关联表数据
	 * 
	 * @param userId
	 *            用户id
	 */
	public void deletePowerInfoByUserId(@Param("userId") String userId);

	/**
	 * 根据项目id查询共享用户
	 * 
	 * @param projectId
	 *            项目id
	 * @return
	 */
	public List<UserInfoModel> getShareUserListByProjectId(@Param("projectId") String projectId);

	/**
	 * 验证用户名是否已使用
	 * 
	 * @param userName
	 *            用户名
	 * @return
	 */
	public UserInfoModel verificationUserName(@Param("userName") String userName);
}
