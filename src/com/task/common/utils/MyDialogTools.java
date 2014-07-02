package com.task.common.utils;

import android.content.Context;

import com.task.tools.component.MyDialog;

public class MyDialogTools {
	private static MyDialog dialog =null;
	public static void showDialog(Context context){
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		dialog = new MyDialog(context);
		dialog.show();
	}
	
	public static void showDialog(Context context,String s){
		showDialog(context);
		setText(s);
	}
	
	public static void closeDialog(){
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	public static void setText(String s){
		if(dialog != null)
			dialog.setInfo(s);
	}
}
