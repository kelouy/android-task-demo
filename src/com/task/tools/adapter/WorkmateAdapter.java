package com.task.tools.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.task.activity.PersonalInfoActivity;
import com.task.activity.R;
import com.task.common.bean.Department;
import com.task.common.bean.User;
import com.task.tools.interfaces.ItemClickedListener;
import com.task.tools.interfaces.ItemHeaderClickedListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkmateAdapter extends BaseAdapter implements StickyListHeadersAdapter {
	private List<User> mUserList;
	private List<Department> mDeptGroup;
	private List<Boolean> isShowList;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	private ItemClickedListener mItemClickedListener;
	private ItemHeaderClickedListener mItemHeaderClickedListener;

	private Context context;
	

	/**
	 * 
	 * @param context	activity上下文
	 * @param message_list	list数据
	 * @param isOpen  初始化时是否展开 
	 */
	@SuppressWarnings("static-access")
	public WorkmateAdapter(Context context, List<User> userlist, List<Boolean> isShowList) {
		this.context = context;
		this.mUserList = userlist;
		this.isShowList = isShowList;
		this.imageLoader = imageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.person_head)
				.showImageForEmptyUri(R.drawable.person_head)
				.showImageOnFail(R.drawable.person_head).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Config.ARGB_8888)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(10))//设置圆角
				//.displayer(new FadeInBitmapDisplayer(100))
				.build();
		initMessageList(userlist);
	}

	private void initMessageList(List<User> userlist) {
		this.mUserList = userlist;
		if (userlist != null && userlist.size() > 0) {
			getSectionIndicesAndGroupNames();
		}
	}

	@Override
	public int getCount() {
		return mUserList == null ? 0 : mUserList.size();
	}

	public int getRealCount() {
		return mUserList == null ? 0 : mUserList.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//Log.v("getView", "position="+position+"  convertView="+convertView+"  parent="+parent);
		
		ItemViewHolder holder;
		if (convertView == null) {
			holder = new ItemViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.workmate_list_item, null);
			holder.tvTextView = (TextView) convertView.findViewById(R.id.tvTitle);
			holder.positionTextView = (TextView) convertView.findViewById(R.id.tvPosition);
			holder.headImg = (ImageView) convertView.findViewById(R.id.list_head_img);
			holder.view = convertView.findViewById(R.id.rlItem);
			convertView.setTag(holder);
		} else {
			holder = (ItemViewHolder) convertView.getTag();
		}
		final User user = mUserList.get(position);
		boolean isShow = mDeptGroup.get(user.getDeptId()).isShown();
		//若合起分组，则里面的view不显示
		holder.view.setVisibility(isShow ? View.VISIBLE : View.GONE);
		if(isShow){
			holder.tvTextView.setText(user.getRealName());
			holder.positionTextView.setText(user.getPositionName());
			imageLoader.displayImage(user.getHeadUrl(), holder.headImg, options);
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mItemClickedListener != null) {
					mItemClickedListener.onItemClick(v, position);
				} else {
					Intent intent = new Intent(context, PersonalInfoActivity.class);
					intent.putExtra("user", user);
					context.startActivity(intent);
				}
				
			}
		});
		
		return convertView;
	}

	/**
	 * 分组  数据源group by deptname
	 */
	private void getSectionIndicesAndGroupNames() {
		mDeptGroup = new ArrayList<Department>();
		Department dept;
		int countGp = 0;
		String deptName = "";
		for (int i = 0; i < mUserList.size(); i++) {
			String deptName2 = mUserList.get(i).getDeptName();
			//Log.e("tsj","deptName2="+deptName2);
			if (!deptName2.equals(deptName)) {
				
				if(mDeptGroup.size()>0) {
					mDeptGroup.get(mDeptGroup.size()-1).setCount(countGp);
				}
				deptName = deptName2;
				countGp = 1;
				mUserList.get(i).setDeptId(mDeptGroup.size());
				dept = new Department();
				dept.setDeptName(deptName);
				dept.setFirstPositionInList(i);
				if(mDeptGroup.size() == isShowList.size())
					isShowList.add(false);
				mDeptGroup.add(dept);
			} else {
				countGp ++;
				mUserList.get(i).setDeptId(mDeptGroup.size() - 1);
			}
			
		}
		for(int i=0;i<isShowList.size()&&mDeptGroup.size()>i;i++){
			mDeptGroup.get(i).setShown(isShowList.get(i));
		}
		mDeptGroup.get(mDeptGroup.size()-1).setCount(countGp);

	}

	@Override
	public View getHeaderView(final int position, View convertView, ViewGroup parent) {
		//Log.v("getHeaderView", "position="+position+"  convertView="+convertView+"  parent="+parent);
		ItemHeaderViewHolder holder = null;
		if (convertView == null) {
			holder = new ItemHeaderViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.workmate_group_item, parent, false);
			holder.textViewGroupName = (TextView) convertView.findViewById(R.id.list_group_name);
			holder.textViewGroupCount = (TextView) convertView.findViewById(R.id.list_group_count);
			holder.imgArrow = (ImageView) convertView.findViewById(R.id.list_group_tagimg);
			convertView.setTag(holder);
		} else {
			holder = (ItemHeaderViewHolder) convertView.getTag();
		}

		
		Department dept = mDeptGroup.get(mUserList.get(position).getDeptId());
		holder.textViewGroupName.setText(dept.getDeptName());
		holder.textViewGroupCount.setText("[ " + dept.getCount() +" ]");
		
		 if(dept.isShown()) {
			 holder.imgArrow.setImageResource(R.drawable.ic_group_arrow_open);
		 } else {
			 holder.imgArrow.setImageResource(R.drawable.ic_group_arrow_close);
		 }
		
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return mUserList.get(position).getDeptId();
	}

	/**
	 * 当点击header时，即GroupName，可以展开合起
	 * @Description:
	 */
	public void onListHeaderClicked(int position) {
		int index = mUserList.get(position).getDeptId();
		Department dept = mDeptGroup.get(index);
		dept.setShown(!dept.isShown());
		isShowList.set(index, dept.isShown());
		WorkmateAdapter.this.notifyDataSetChanged();
	}
	
	/***************************************/
	public void setOnItemClickedListener(ItemClickedListener listener) {
		this.mItemClickedListener = listener;
	}
	
	public void setOnItemHeaderClickedListener(ItemHeaderClickedListener listener) {
		this.mItemHeaderClickedListener = listener;
	}
	
	public ItemHeaderClickedListener getmItemHeaderClickedListener() {
		return mItemHeaderClickedListener;
	}


	/***************************************/
	public class ItemHeaderViewHolder {

		TextView textViewGroupName;
		ImageView imgArrow;
		TextView textViewGroupCount;
	}

	public class ItemViewHolder {
		View view;
		TextView tvTextView;
		TextView positionTextView;
		ImageView headImg;
	}

}
