package com.task.activity;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.task.client.ClientOutputThread;
import com.task.common.bean.User;
import com.task.common.transbean.TranObject;
import com.task.common.transbean.TranObjectType;
import com.task.tools.adapter.WorkmateAdapter;
import com.task.tools.component.MyApplication;
import com.task.tools.interfaces.ItemHeaderClickedListener;
import com.task.tools.view.WorkmateListView;

import roboguice.fragment.RoboFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2 extends RoboFragment {
	private static final String TAG = "Fragment2";
	private WorkmateListView workmateListView;
	private WorkmateAdapter workmateAdapter;
	private MyApplication application;
	private List<User> list =  new ArrayList<User>();
	private List<Boolean> isShowList = new ArrayList<Boolean>();
	private DbUtils db; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate...");
		db = DbUtils.create(this.getActivity());
		application = (MyApplication) this.getActivity().getApplication();
		try{
			if(application.isClientStart()){
				TranObject o = new TranObject(TranObjectType.GET_USER);
				ClientOutputThread out = application.getClient().getClientOutputThread();
				out.setMsg(o);
			}
		}catch (Exception e){
			debug(e.getMessage());
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		debug("onCreateView...");
		View view = inflater.inflate(R.layout.fragment2, container, false);
		try {
			list = db.findAll(Selector.from(User.class).where("deptName","!=",null).orderBy("deptId,positionId"));
		} catch (DbException e) {
			e.printStackTrace();
		}
		workmateListView = (WorkmateListView) view.findViewById(R.id.workmatelistview);
		workmateAdapter = new WorkmateAdapter(getActivity(), list, isShowList);
		workmateListView.setAdapter(workmateAdapter);
		workmateListView.setOnItemHeaderClickedListener(new ItemHeaderClickedListener() {
			@Override
			public void onItemHeaderClick(View header, int itemPosition, long headerId) {
				if(workmateAdapter != null)
					workmateAdapter.onListHeaderClicked(itemPosition);
			}
		});
		return view;
	}
	


	/**********************************************/
	@Override
	public void onDestroy() {
		super.onDestroy();
		debug("onDestroy...");
	}

	@Override
	public void onResume() {
		super.onResume();
		debug("onResume...");
	}

	@Override
	public void onStop() {
		super.onStop();
		debug("onStop...");
	}
	
	private void debug(String s){
		Log.v(TAG, s);
	}

}
