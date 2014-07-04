package com.task.common.utils;

import com.task.common.bean.User;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Utils {
	
	private static User my;
	
	public static void getScreenWidthAndHeight(Context context,Point p){
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		display.getSize(p);
	}

	/** 
     * 得到设备屏幕的宽度 
     */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	
	/** 
     * 得到设备屏幕的高度 
     */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
	 /** 
     * 得到设备的密度 
     */
    public static float getScreenDensity(Context context) {  
        return context.getResources().getDisplayMetrics().density;  
    }  
      
    /** 
     * 把密度转换为像素 
     */
    public static int dip2px(Context context, float px) {  
        final float scale = getScreenDensity(context);  
        return (int) (px * scale + 0.5);  
    } 



	public static User getMy() {
		return my;
	}

	public static void setMy(User my) {
		Utils.my = my;
	}

}