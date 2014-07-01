package com.task.tools.component;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.task.client.Client;
import com.task.client.ClientInputThread;
import com.task.client.ClientOutputThread;
import com.task.client.MessageListener;
import com.task.common.bean.Department;
import com.task.common.bean.Position;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.Constants;

public class MyService extends Service {
	private String TAG = "MyService";
	private MyApplication application;
	private Client client;
	private boolean isStart = false;// 是否与服务器连接上
	private Gson gson = new Gson();
	private DbUtils db;

	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate……");
		super.onCreate();
		application = (MyApplication) this.getApplication();
		db = DbUtils.create(this);
		client = application.getClient();
		// 使允许在主线程中连接socket
		try {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e(TAG, "onStartCommand……"); 
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.e(TAG, "onStart……");
		super.onStart(intent, startId);
		Log.e(TAG,"client.start " + isStart);
		isStart = client.start();
		Log.e(TAG,"client.start " + isStart);
		if (isStart) {
			application.setClientStart(isStart);
			ClientInputThread in = client.getClientInputThread();
			// 消息监听
			in.setMessageListener(new MessageListener() {
				@Override
				public void Message(TranObject msg) {
					Log.e(TAG, new Gson().toJson(msg));
					switch(msg.getType()){
					case LOGIN : 
						doLogin(msg);
						break;
					case UPDATE_HEAD : 
						doUpdateHead(msg);
					case GET_USER : 
						doGetUser(msg);
						break;
					case GET_DEPT : 
						doGetDept(msg);
						break;
					case GET_POSITION : 
						doGetPosition(msg);
						break;
					default :
						break;
					}
					Intent broadCast = new Intent();
					broadCast.setAction(Constants.ACTION);
					broadCast.putExtra(Constants.MSGKEY, msg);
					sendBroadcast(broadCast);// 把收到的消息已广播的形式发送出去
				}

			});
			initLocalData();
		}
	}
	

	private void doUpdateHead(TranObject msg) {
		if(msg.isSuccess()){
			User user = gson.fromJson(msg.getJson(), User.class);
			user.setHeadUrl(Constants.IMG_ROOT_URL+user.getHeadUrl());
			try {
				db.delete(User.class, WhereBuilder.b("userId", "==", user.getUserId()));
				db.save(user);
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private void doLogin(TranObject msg) {
		if(msg.isSuccess()){
			User user = gson.fromJson(msg.getJson(), User.class);
			application.setMy(user);
		}
	}
	private void doGetPosition(TranObject msg) {
		try {
			List<Position> list = gson.fromJson(msg.getJson(), new TypeToken<List<Position>>() {
			}.getType());
			if (list != null && list.size() > 0) {
				db.deleteAll(Position.class);
				for (Position u : list)
					db.save(u);
			}
		} catch (Exception e) {
			debug(e.getMessage());
		}
	}

	private void doGetDept(TranObject msg) {
		try {
			List<Department> list = gson.fromJson(msg.getJson(), new TypeToken<List<Department>>() {
			}.getType());
			if (list != null && list.size() > 0) {
				db.deleteAll(Department.class);
				for (Department u : list)
					db.save(u);
			}
		} catch (Exception e) {
			debug(e.getMessage());
		}
	}

	/**
	 * 更新本地所有用户数据
	 * @param msg
	 */
	private void doGetUser(TranObject msg) {
		try {
			List<User> list = gson.fromJson(msg.getJson(), new TypeToken<List<User>>() {
			}.getType());
			if (list != null && list.size() > 0) {
				db.deleteAll(User.class);
				for (User u : list){
					if(!TextUtils.isEmpty(u.getHeadUrl()))
						u.setHeadUrl(Constants.IMG_ROOT_URL+u.getHeadUrl());
					db.save(u);
				}
				//List<User> lu = db.findAll(User.class);
				//debug("db.findAll(User.class) = "+lu); 
			}
		} catch (Exception e) {
			debug(e.getMessage());
		}
	}

	/**
	 * 初始化本地数据库的用户、部门、职位数据
	 */
	private void initLocalData() {
		Client client = application.getClient();
		ClientOutputThread out = client.getClientOutputThread();
		//部门
		List<Department> deptListTmp = null;
		try {
			deptListTmp = db.findAll(Department.class);
			if (deptListTmp == null || deptListTmp.size() == 0) {
				TranObject o = new TranObject(TranObjectType.GET_DEPT);
				out.setMsg(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//职位
		List<Position> positionListTmp = null;
		try {
			positionListTmp = db.findAll(Position.class);
			if (positionListTmp == null || positionListTmp.size() == 0) {
				TranObject o = new TranObject(TranObjectType.GET_POSITION);
				out.setMsg(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 每次登陆都更新用户数据
		List<User> userListTmp = null;
		try {
			userListTmp = db.findAll(User.class);
			if(userListTmp == null || userListTmp.size() == 0){
				TranObject o = new TranObject(TranObjectType.GET_USER);
				out.setMsg(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	/******************************/
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void debug(String s){
		Log.v(TAG, s);
	}

}
