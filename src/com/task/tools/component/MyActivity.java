package com.task.tools.component;

import com.task.common.transbean.TranObject;
import com.task.common.utils.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


import roboguice.activity.RoboActivity;

public abstract class MyActivity extends RoboActivity {

	/**
	 * 广播接收者，接收GetMsgService发送过来的消息
	 */
	private BroadcastReceiver msgReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			TranObject msg = (TranObject) intent.getSerializableExtra(Constants.MSGKEY);
			if (msg != null) {//如果不是空，说明是消息广播
				Log.e("BroadcastReceiver", "收到广播消息"+msg);
				getMessage(msg);// 把收到的消息传递给子类
			} else {//如果是空消息，说明是关闭应用的广播
				close();
			}
		}
	};
	
	/**
	 * 抽象方法
	 * @param msg  传递给子类处理的消息对象
	 */
	public abstract void getMessage(TranObject<?> msg);
	
	
	/**
	 * 子类直接调用这个方法关闭应用
	 */
	public void close() {
		Intent i = new Intent();
		i.setAction(Constants.ACTION);
		sendBroadcast(i);
		finish();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//注册广播接收者
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION);
		registerReceiver(msgReceiver, intentFilter);// 注册接受消息广播
	}
	
	@Override
	protected void onStop() {
		//注销广播接收者
		super.onStop();
		unregisterReceiver(msgReceiver);// 注销接受消息广播
	}
}
