package com.task.tools.component.menu;

import java.util.List;

import com.task.activity.PicCutAndUploadActivity;
import com.task.activity.R;
import com.task.activity.UpdatePersonalInfoActivity;
import com.task.common.utils.ActivityTag;
import com.task.common.utils.Utils;
import com.task.tools.component.menu.RadialMenuWidget.RadialMenuEntry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class MyPersonInfoMenu {
	private Context context;
	private Activity activity;
	private RadialMenuWidget PieMenu;
	private LinearLayout ll;

	public MyPersonInfoMenu(Context context,Context basecontext,View v) {
		this.context = context;
		this.activity = (Activity) context;
		ll = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		activity.addContentView(ll, params);
		
		int eventX = (int) v.getX();
		int eventY = (int) v.getY();
		//Screen Sizes
		int xScreenSize = (Utils.getScreenWidth(context));
		int yScreenSize = (Utils.getScreenHeight(context));
		int xLayoutSize = ll.getWidth();
		int yLayoutSize = ll.getHeight();
		int xCenter = xScreenSize/2;
		int xSource = eventX;
		int yCenter = yScreenSize/2;
		int ySource = eventY;
		
		if (xScreenSize != xLayoutSize) {
			xCenter = xLayoutSize/2;
			xSource = eventX-(xScreenSize-xLayoutSize);
		}
		if (yScreenSize != yLayoutSize) {
			yCenter = yLayoutSize/2;
			ySource = eventY-(yScreenSize-yLayoutSize);
		}				
		
		PieMenu = new RadialMenuWidget(basecontext);

		PieMenu.setSourceLocation(xSource,ySource);
		PieMenu.setShowSourceLocation(true);
		PieMenu.setCenterLocation(xCenter,yCenter);

		//PieMenu.setAnimationSpeed(0L);
		
		PieMenu.setIconSize(15, 30);
		PieMenu.setTextSize(13);

		PieMenu.setCenterCircle(new Close());
		PieMenu.addMenuEntry(new UploadHead());
		PieMenu.addMenuEntry(new Update());
		PieMenu.addMenuEntry(new UpdatePwd());
		PieMenu.addMenuEntry(new Setting());
		ll.bringToFront();
	}

	public class Close implements RadialMenuEntry {

		public String getName() {
			return "Close";
		}
		public String getLabel() {
			return null;
		}
		public int getIcon() {
			return android.R.drawable.ic_menu_close_clear_cancel;
		}
		public List<RadialMenuEntry> getChildren() {
			return null;
		}
		public void menuActiviated() {

			// System.out.println( "Close Menu Activated");
			// Need to figure out how to to the layout.removeView(PieMenu)
			// ll.removeView(PieMenu);
			((LinearLayout) PieMenu.getParent()).removeView(PieMenu);
			
		}
	}

	public class UploadHead implements RadialMenuEntry {

		public String getName() {
			return "上传头像";
		}
		public String getLabel() {
			return "上传头像";
		}
		public int getIcon() {
			return R.drawable.icon_head;
		}
		public List<RadialMenuEntry> getChildren() {
			return null;
		}
		public void menuActiviated() {

			((LinearLayout) PieMenu.getParent()).removeView(PieMenu);
			Intent intent1 = new Intent(context, PicCutAndUploadActivity.class);
			intent1.putExtra("user", Utils.getMy());
			activity.startActivityForResult(intent1, ActivityTag.PIC_CUT_AND_UPLOAD_HEAD);

		}
	}

	public class Update implements RadialMenuEntry {

		public String getName() {
			return "修改资料";
		}
		public String getLabel() {
			return "修改资料";
		}
		public int getIcon() {
			return R.drawable.icon_person_data;
		}
		public List<RadialMenuEntry> getChildren() {
			return null;
		}
		public void menuActiviated() {

			((LinearLayout) PieMenu.getParent()).removeView(PieMenu);
			Intent intent1 = new Intent(context, UpdatePersonalInfoActivity.class);
			intent1.putExtra("user", Utils.getMy());
			activity.startActivityForResult(intent1, ActivityTag.PERSONNAL_INFO);

		}
	}

	public class UpdatePwd implements RadialMenuEntry {

		public String getName() {
			return "修改密码";
		}
		public String getLabel() {
			return "修改密码";
		}
		public int getIcon() {
			return R.drawable.icon_pwd;
		}
		public List<RadialMenuEntry> getChildren() {
			return null;
		}
		public void menuActiviated() {

		}
	}

	public class Setting implements RadialMenuEntry {

		public String getName() {
			return "系统设置";
		}
		public String getLabel() {
			return "系统设置";
		}
		public int getIcon() {
			return R.drawable.icon_setting;
		}
		public List<RadialMenuEntry> getChildren() {
			return null;
		}
		public void menuActiviated() {

		}
	}

}
