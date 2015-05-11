package com.jianbin.flowtag.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	int mRes;
	int mTextRes;
	Context mContext;
	List<String> mData;

	public MyAdapter(Context ctx, int res, int textRes, List<String> data) {
		mContext = ctx;
		mRes = res;
		mTextRes = textRes;
		mData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = View.inflate(mContext, mRes, null);
		TextView v = (TextView) convertView.findViewById(mTextRes);
		v.setText(getItem(position));
		return convertView;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public String getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mData.get(position).hashCode();
	}

	public void add(String str) {
		mData.add(str);
		this.notifyDataSetChanged();
	}
}
