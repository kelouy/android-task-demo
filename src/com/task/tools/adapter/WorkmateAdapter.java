package com.task.tools.adapter;

import java.util.ArrayList;
import java.util.List;

import com.task.activity.R;
import com.task.common.bean.Department;
import com.task.common.bean.User;
import com.task.tools.interfaces.ItemClickedListener;
import com.task.tools.interfaces.ItemHeaderClickedListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkmateAdapter extends BaseAdapter implements StickyListHeadersAdapter {

	private List<User> mMessageList;
	private List<Department> mMessageListGroup;
	
	private ItemClickedListener mItemClickedListener;
	private ItemHeaderClickedListener mItemHeaderClickedListener;

	private Context context;
	
	private boolean mIsOpen;//初始化View时分组是否展开

	/**
	 * 
	 * @param context	activity上下文
	 * @param message_list	list数据
	 * @param isOpen  初始化时是否展开
	 */
	public WorkmateAdapter(Context context, List<User> message_list, boolean isOpen) {
		this.context = context;
		this.mMessageList = message_list;
		this.mIsOpen = isOpen;
		initMessageList(message_list);
	}

	private void initMessageList(List<User> message_list) {
		this.mMessageList = message_list;
		if (message_list != null && message_list.size() > 0) {
			getSectionIndicesAndGroupNames();
		}
	}

	@Override
	public int getCount() {
		return mMessageList == null ? 0 : mMessageList.size();
	}

	public int getRealCount() {
		return mMessageList == null ? 0 : mMessageList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ItemViewHolder holder;
		if (convertView == null) {
			holder = new ItemViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.workmate_list_item, null);
			holder.textViewInfo = (TextView) convertView.findViewById(R.id.tvTitle);
			convertView.setTag(holder);
		} else {
			holder = (ItemViewHolder) convertView.getTag();
		}
		holder.textViewInfo.setText(mMessageList.get(position).getUserName());
		
		//若合起分组，则里面的view不显示
		holder.textViewInfo.setVisibility(mMessageListGroup.get(mMessageList.get(position).getDeptId()).isShown() ? View.VISIBLE : View.GONE);

		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mItemClickedListener != null) {
					mItemClickedListener.onItemClick(v, position);
				}
				
			}
		});
		
		return convertView;
	}

	/**
	 * 分组  数据源group by deptname
	 */
	private void getSectionIndicesAndGroupNames() {
		mMessageListGroup = new ArrayList<Department>();
		Department gp;
		int countGp = 0;
		String deptName = "";
		for (int i = 0; i < mMessageList.size(); i++) {
			String deptName2 = mMessageList.get(i).getDeptName();
			if (!deptName2.equals(deptName)) {
				
				if(mMessageListGroup.size()>0) {
					mMessageListGroup.get(mMessageListGroup.size()-1).setCount(countGp);
				}
				deptName = deptName2;
				countGp = 1;
				mMessageList.get(i).setDeptId(mMessageListGroup.size());
				gp = new Department();
				gp.setDeptName(deptName);
				gp.setFirstPositionInList(i);
				gp.setShown(mIsOpen);
				mMessageListGroup.add(gp);
			} else {
				countGp ++;
				mMessageList.get(i).setDeptId(mMessageListGroup.size() - 1);
			}

		}

	}

	@Override
	public View getHeaderView(final int position, View convertView, ViewGroup parent) {

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

		
		Department gp = mMessageListGroup.get(mMessageList.get(position).getDeptId());
		holder.textViewGroupName.setText(gp.getDeptName());
		
		holder.textViewGroupCount.setText("[ " + gp.getCount() +" ]");
		
		 if(gp.isShown()) {
			 holder.imgArrow.setImageResource(R.drawable.ic_group_arrow_open);
		 } else {
			 holder.imgArrow.setImageResource(R.drawable.ic_group_arrow_close);
		 }
		
		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		return mMessageList.get(position).getDeptId();
	}

	/**
	 * 当点击header时，即GroupName，可以展开合起
	 * @Description:
	 */
	public void onListHeaderClicked(int position) {
		Department gp = mMessageListGroup.get(mMessageList.get(position).getDeptId());
		gp.setShown(!gp.isShown());
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

		TextView textViewInfo;
	}

}
