package com.jianbin.flowtag;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.jianbin.view.FlowTagsLayout;

public class MainActivity extends ActionBarActivity {
	FlowTagsLayout mFlowTagsLayout;
	ArrayAdapter<String> mAdapter;
	List<String> mData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mFlowTagsLayout = (FlowTagsLayout) findViewById(R.id.fl_tags);
		mData = new ArrayList<String>();
		mData.add("HOT");
		mData.add("SHARE");
		mData.add("DESIGN");
		mData.add("ARTICLE");
		mData.add("DEVELOP");

		mAdapter = new ArrayAdapter<String>(this, R.layout.tags_item,
				R.id.btn_tags_item_text, mData);
		mFlowTagsLayout.setAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			mAdapter.add("JOKE");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
