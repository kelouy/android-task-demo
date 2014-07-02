package com.task.tools.component;

import com.task.activity.R;
import com.task.common.utils.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MyDialog extends Dialog {
    Context context;
    TextView infoTV;
    
    public void setInfo(String s){
    	if(infoTV != null)
    		infoTV.setText(s);
    }
    
    public MyDialog(Context context) {
        this(context,R.style.dialog);
    }
    public MyDialog(Context context, int theme){
        super(context, theme);
        this.context = context;
        init();
    }
    
    private void init() {
    	Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int width = Utils.getScreenWidth(context);
		lp.width = (int) (0.6 * width);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_layout);
        infoTV = (TextView) findViewById(R.id.tvLoad);
    }

}
