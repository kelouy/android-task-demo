package com.task.activity;

import java.util.ArrayList;
import java.util.List;

import com.task.common.bean.User;
import com.task.tools.adapter.WorkmateAdapter;
import com.task.tools.interfaces.ItemClickedListener;
import com.task.tools.interfaces.ItemHeaderClickedListener;
import com.task.tools.view.WorkmateListView;

import roboguice.fragment.RoboFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment2 extends RoboFragment {
	private static final String TAG = "Fragment2";
	private WorkmateListView workmateListView;
	private WorkmateAdapter workmateAdapter;
	private List<User> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		debug("onCreate...");
		String[] imgs = {
			"http://images.vancl.com/product/0/0/4/0049402/Big/0049402-7201103181319445202.jpg",
			"http://p3.vanclimg.com/product/0/1/8/0184323/big/0184323-1j201210291031293250.jpg",
			"http://img01.tooopen.com/Downs/images/2010/3/7/sy_2010030721125483905.jpg",
			"http://img2.gamfe.com/userfiles/32869/photo/show_201104060918068240.jpg",
			"http://p4.vanclimg.com/product/0/1/6/0168089/Big/0168089-10201201171407094350.jpg",
			"http://file2.zhituad.com/thumb/201104/19/201104190230353601FXlL8_priv.jpg",
			"http://gb.cri.cn/mmsource/images/2010/12/21/31636cf795e84588a5496b14b75e4590.jpg",
			"http://news.xinhuanet.com/photo/2013-03/15/11118124460949_811d.jpg",
			"http://news.xinhuanet.com/shuhua/2012-05/29/123168290_111n.jpg",
			"http://imgp.gmw.cn/forum/201310/16/231055vvqjnjlpafesjafp.jpg",
			"http://images.199u2.com/data/attachment/forum/201310/04/1722372zgf25gpggecg356.jpg",
			"http://news.xinhuanet.com/fashion/2013-03/31/11192124515231_41d.jpg",
			"http://news.xinhuanet.com/photo/2013-04/29/11118124648031_441d.jpg",
			"http://news.xinhuanet.com/forum/2013-01/19/11123124252708_701d.jpg",
			"http://news.xinhuanet.com/sports/2012-12/15/11112124098775_51d.jpg",
			"http://news.xinhuanet.com/fashion/2012-11/27/11192124004076_11d.jpg",
			"http://news.xinhuanet.com/fashion/2013-07/24/11192125054482_211d.jpg",
			"http://imgp.gmw.cn/forum/201309/11/135714tzwqoxgy3gc39dz6.jpeg",
			"http://imgp.gmw.cn/forum/201309/11/135713uppzkmo22odwo9fa.jpeg",
			"http://news.cnhubei.com/ctdsb/ctdsbsgk/ctdsb30/201201/W020120112271265307510.jpg",
			"http://news.xinhuanet.com/photo/2013-05/02/11118124655542_41d.jpg",
			"http://news.xinhuanet.com/forum/2013-02/19/11123124358513_451d.jpg",
			"http://news.xinhuanet.com/fashion/2013-06/08/11192124806793_651d.jpg",
			"http://news.xinhuanet.com/photo/2013-06/19/11118124693107_131d.jpg",
			"http://news.hainan.net/Editor/UploadFile/2007w7r31f/20077317167964.jpg",
			"http://news.xinhuanet.com/fashion/2013-09/05/11192125276116_321d.jpg",
			"http://news.xinhuanet.com/fashion/2013-07/04/11192124950139_171d.jpg",
			"http://news.xinhuanet.com/photo/2013-06/14/11118124853316_241d.jpg",
			"http://news.xinhuanet.com/photo/2013-03/09/11118124431972_151d.jpg",
			"http://news.xinhuanet.com/tech/2013-03/15/11119124442937_671d.jpg",
			"http://news.xinhuanet.com/fashion/2013-02/21/11192124372433_31d.jpg"
		};
		list = new ArrayList<User>(); 
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				int index = i*10+j+1;
				User u = new User();
				u.setDeptName("部门"+i);
				u.setRealName("唐声杰 "+index);
				u.setPositionName("position : xxx");
				u.setHeadUrl(imgs[index % imgs.length]);
				list.add(u); 
			}
		}
		/**/
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		debug("onCreateView...");
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
