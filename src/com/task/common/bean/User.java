package com.task.common.bean;


import java.sql.Timestamp;

import com.task.common.utils.FieldMeta;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {
	
	private int id;
	private int userId;
	private int positionId;
	private int deptId; 
	private String userName;
	private String realName;
	private String password;
	private int flag;
	private String headUrl;
	private int qq;
	private String email;
	private Timestamp createTime;
	private Timestamp lastLoginTime;
	private String phoneNum;
	private int sex;
	
	@FieldMeta(isNecessary=true)
	private String deptName;
	@FieldMeta(isNecessary=true)
	private String positionName;
	

	public User() {
	}

	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public User(String userName, String realName, String password, Byte flag, String headUrl, int qq, String email, Timestamp createTime, Timestamp lastLoginTime) {
		this.userName = userName;
		this.realName = realName;
		this.password = password;
		this.flag = flag;
		this.headUrl = headUrl;
		this.qq = qq;
		this.email = email;
		this.createTime = createTime;
		this.lastLoginTime = lastLoginTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public int getQq() {
		return this.qq;
	}

	public void setQq(int qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}


	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int i) {
		this.flag = i;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}


	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", positionId=" + positionId + ", deptId=" + deptId + ", userName=" + userName + ", realName=" + realName + ", password=" + password + ", flag=" + flag
				+ ", headUrl=" + headUrl + ", qq=" + qq + ", email=" + email + ", createTime=" + createTime + ", lastLoginTime=" + lastLoginTime + ", phoneNum=" + phoneNum + ", sex=" + sex
				+ ", deptName=" + deptName + ", positionName=" + positionName + "]\n";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean eq(User u){
		if(u.getRealName()!=null&&this.realName!=null){
			if(!u.getRealName().equals(this.realName))
				return false;
		}
		if(u.getEmail()!=null&&this.email!=null){
			if(!u.getEmail().equals(this.email))
				return false;
		}
		if(u.getPhoneNum()!=null&&this.phoneNum!=null){
			if(!u.getPhoneNum().equals(this.phoneNum))
				return false;
		}
		if(u.getQq()!=this.qq){
			return false;
		}
		if(u.getDeptId()>0&&u.getDeptId()!= this.deptId){
			return false;
		}
		if(u.getPositionId()>0&&u.getPositionId()!=this.positionId){
			return false;
		}
		if(u.getSex()!=this.sex)
			return false;
		return true;
	}




}
