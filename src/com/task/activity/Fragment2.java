package com.task.activity;

import java.util.ArrayList;
import java.util.List;

import com.task.common.bean.Department;
import com.task.common.bean.User;
import com.task.tools.adapter.WorkmateAdapter;
import com.task.tools.interfaces.ItemHeaderClickedListener;
import com.task.tools.view.WorkmateListView;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;

public class Fragment2 extends RoboFragment {
	private static final String TAG = "Fragment2";
	private WorkmateListView workmateListView;
	private WorkmateAdapter workmateAdapter;
	private List<User> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate...");
		list = new ArrayList<User>(); 
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				User u = new User();
				u.setDeptName("部门"+i);
				u.setUserName("唐声杰 "+(i*10+j+1));
				u.setPositionName("position : xxx");
				list.add(u); 
			}
		}
		/**/
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "onCreateView...");
		View view = inflater.inflate(R.layout.fragment2, container, false);
		workmateListView = (WorkmateListView) view.findViewById(R.id.workmatelistview);
		workmateAdapter = new WorkmateAdapter(getActivity(), list, false);
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
		Log.v(TAG, "onDestroy...");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.v(TAG, "onResume...");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG, "onStop...");
	}

}
