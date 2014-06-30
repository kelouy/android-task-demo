package com.task.test;


import android.util.Log;

import com.google.gson.Gson;
import com.task.client.Client;
import com.task.client.ClientInputThread;
import com.task.client.ClientOutputThread;
import com.task.client.MessageListener;
import com.task.common.bean.Department;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.common.utils.Constants;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client client = new Client(Constants.SERVER_IP, Constants.SERVER_PORT);
		boolean isStart = client.start();
		System.out.println("isStart = "+isStart);
		if (isStart) {
			ClientInputThread in = client.getClientInputThread();
			// 消息监听
			in.setMessageListener(new MessageListener() {
				@Override
				public void Message(TranObject msg) {
					System.out.println("_"+msg.getType());
					switch (msg.getType()) {
						case TEST :
							Gson gson = new Gson();
							System.out.println( "get message:" + msg.getJson());
							break;
 
						default :
							break;
					}
				}
			});
			
			ClientOutputThread out = client.getClientOutputThread();
			TranObject tranObject = new TranObject(TranObjectType.TEST);
			out.setMsg(tranObject);
		}
	}

}
