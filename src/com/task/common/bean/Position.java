package com.task.common.bean;

import java.sql.Timestamp;

import com.lidroid.xutils.db.annotation.Id;

public class Position implements java.io.Serializable {
	
	@Id
	private int positionId;
	private String positionName;
	private Timestamp createTime;
	private Timestamp lastUpdateTime;
	
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	
}
