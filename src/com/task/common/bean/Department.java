package com.task.common.bean;

// Generated 2014-6-3 9:50:12 by Hibernate Tools 3.4.0.CR1

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.annotation.Id;
import com.task.common.utils.FieldMeta;

/**
 * Department generated by hbm2java
 */
public class Department implements java.io.Serializable {

	@Id
	private Integer deptId;
	private String deptName;
	private Timestamp createTime;
	private Timestamp lastUpdateTime;
	
	@FieldMeta(isField=false)
	private boolean isShown;
	@FieldMeta(isField=false)
	private int count;
	@FieldMeta(isField=false)
	private int firstPositionInList;
	
	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getFirstPositionInList() {
		return firstPositionInList;
	}

	public void setFirstPositionInList(int firstPositionInList) {
		this.firstPositionInList = firstPositionInList;
	}

	@FieldMeta(isField = false)//自定义注解，表现该字段非数据库字段
	private List<User> users = new ArrayList<User>();

	public Department() {
	}

	public Department(String deptName) {
		this.deptName = deptName;
	}

	public Department(String deptName, Timestamp createTime, Timestamp lastUpdateTime) {
		this.deptName = deptName;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "[" + deptName + "]";
	}

	
}
