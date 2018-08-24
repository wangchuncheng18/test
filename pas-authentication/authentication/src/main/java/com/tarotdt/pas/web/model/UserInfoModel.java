package com.tarotdt.pas.web.model;

import java.util.Date;

public class UserInfoModel {
	//用户ID
	private Long userId;
	//用户名
	private String userName;
	//密码
	private String password;
	//用户真实姓名
	private String realName;
	//用户备注说明
	private String describeText;
	//电话
	private String phone;
	//邮箱
	private String email;
	//公司名称
	private String corporateName;
	//职位名称
	private String positionName;
	//职务名称
	private String jobName;
	//地址
	private String address;
	//用户状态
	private int userState;
	//创建时间
	private Date createDate;
	//修改用户ID
	private Long operator;
	//修改时间
	private Date updateDate;
	//是否删除
	private String delState;
	//分组名称集合
	private String groupNameList;
	//分组id集合
	private String groupIdList;
	//权限表id
	private Long powerId;

	public UserInfoModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserInfoModel(Long userId, String userName, String password, String realName, String describeText,
			String phone, String email, String corporateName, String positionName, String jobName, String address,
			int userState, Date createDate, Long operator, Date updateDate, String delState) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.realName = realName;
		this.describeText = describeText;
		this.phone = phone;
		this.email = email;
		this.corporateName = corporateName;
		this.positionName = positionName;
		this.jobName = jobName;
		this.address = address;
		this.userState = userState;
		this.createDate = createDate;
		this.operator = operator;
		this.updateDate = updateDate;
		this.delState = delState;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDescribeText() {
		return describeText;
	}

	public void setDescribeText(String describeText) {
		this.describeText = describeText;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDelState() {
		return delState;
	}

	public void setDelState(String delState) {
		this.delState = delState;
	}
	
	public String getGroupNameList() {
		return groupNameList;
	}

	public void setGroupNameList(String groupNameList) {
		this.groupNameList = groupNameList;
	}

	public String getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(String groupIdList) {
		this.groupIdList = groupIdList;
	}

	public Long getPowerId() {
		return powerId;
	}

	public void setPowerId(Long powerId) {
		this.powerId = powerId;
	}
	
}
