package com.task.tools.component;

import java.util.List;

import com.google.gson.Gson;
import com.task.client.Client;
import com.task.client.ClientInputThread;
import com.task.client.MessageListener;
import com.task.common.bean.Department;
import com.task.common.transbean.TranObject;
import com.task.common.utils.Constants;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;

public class MyService extends Service {
	private String TAG = "MyService";
	private MyApplication application;
	private Client client;
	private boolean isStart = false;// 是否与服务器连接上

	@Override
	public void onCreate() {
		Log.e(TAG, "onCreate……");
		super.onCreate();
		application = (MyApplication) this.getApplication();
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
					Intent broadCast = new Intent();
					broadCast.setAction(Constants.ACTION);
					broadCast.putExtra(Constants.MSGKEY, msg);
					sendBroadcast(broadCast);// 把收到的消息已广播的形式发送出去
				}
			});
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
