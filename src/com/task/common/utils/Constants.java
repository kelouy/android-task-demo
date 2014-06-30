package com.task.common.utils;

public class Constants {
	//public static final String SERVER_IP = "192.168.70.4";
	public static final String SERVER_IP = "172.16.0.6";
	public static final int SERVER_PORT = 8765;
	public static final int CLIENT_SERVER_PORT = 8766;
	
	public static final int REGISTER_FAIL = 0;//注册失败
	public static final String ACTION = "com.jackie.message";//消息广播action
	public static final String MSGKEY = "message";//消息的key
	public static final String IP_PORT = "ipPort";//保存ip、port的xml文件名
	public static final String SAVE_USER = "saveUser";//保存用户信息的xml文件名
	public static final String BACKKEY_ACTION="com.jackie.backKey";//返回键发送广播的action
	public static final int NOTIFY_ID = 0x911;//通知ID
	public static final String DBNAME = "task.db";//数据库名称
	
	public static final String TASK_SYNC_SERVICE = "com.task.TASK_SYNC_SERVICE";
//	public static final String IMG_SERVER_URL = "http://116.204.65.11/Amall";
	public static final String IMG_ROOT_URL = "http://116.204.65.11/task/uploadImg/";
	public static final String IMG_SERVER_URL = "http://116.204.65.11/task/FileServlet?action=upload";
}
