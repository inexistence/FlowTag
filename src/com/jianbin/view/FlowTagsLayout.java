package com.jianbin.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class FlowTagsLayout extends AdapterView<ArrayAdapter<?>> {

	public FlowTagsLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowTagsLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowTagsLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		measureChildren(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * save all tags
	 */
	private List<List<View>> mAllTags = new ArrayList<List<View>>();
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// wrap_content
		int width = 0;
		int height = 0;

		int lineWidth = 0;// 行宽
		int lineHeight = 0;// 行高

		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();

			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;

			if (lineWidth + childWidth > sizeWidth) {// 换行
				width = Math.max(width, lineWidth);
				lineWidth = childWidth;// reset line width
				// record height
				height += lineHeight;
				lineHeight = childHeight;
			} else {// 不换行
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}
			// last tag
			if (i == cCount - 1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}
		setMeasuredDimension(modeWidth == MeasureSpec.AT_MOST ? width
				: sizeWidth, modeHeight == MeasureSpec.AT_MOST ? height
				: sizeHeight);
	}

	protected void layoutChildren() {
		mAllTags.clear();
		mLineHeight.clear();

		int width = getWidth();
		int lineWidth = 0;
		int lineHeight = 0;

		List<View> lineViews = new ArrayList<View>();

		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
				// record line height
				mLineHeight.add(lineHeight);
				mAllTags.add(lineViews);

				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

				lineViews = new ArrayList<View>();
			}

			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);

			lineViews.add(child);
		}

		// last line
		mLineHeight.add(lineHeight);
		mAllTags.add(lineViews);

		// set position of child view
		int left = 0;
		int top = 0;
		// num of lines
		int lineNum = mAllTags.size();

		for (int i = 0; i < lineNum; i++) {
			lineViews = mAllTags.get(i);
			lineHeight = mLineHeight.get(i);

			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();
				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();

				child.layout(lc, tc, rc, bc);
				left += child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
			}
			left = 0;
			top += lineHeight;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutChildren();
	}

	/**
	 * 与当前ViewGroup对应的LayoutParams
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	private ArrayAdapter<?> mAdapter;
	private DataSetObserver mDataSetObserver;

	@Override
	public ArrayAdapter<?> getAdapter() {
		return mAdapter;
	}

	private List<View> mConvertViewCache = new ArrayList<View>();

	void resetList() {
		removeAllViewsInLayout();
		if (mAdapter != null) {
			for (int i = 0; i < mAdapter.getCount(); i++) {
				View child = null;
				if (i < mConvertViewCache.size())
					child = mConvertViewCache.get(i);
				if (child == null) {
					child = mAdapter.getView(i, null, this);
					mConvertViewCache.add(child);
				}
				Log.d("FlowTagsLayout", "child==null ? " + (child == null));
				addViewInLayout(child, i, child.getLayoutParams());
			}
		}
		Log.d("FlowTagsLayout", "layout children num " + this.getChildCount());
		requestLayout();
		invalidate();
	}

	@Override
	public void setAdapter(ArrayAdapter<?> adapter) {
		if (mAdapter != null && mDataSetObserver != null) {
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
		}

		mAdapter = adapter;
		resetList();

		if (mAdapter != null) {

			mDataSetObserver = new DataSetObserver() {
				@Override
				public void onChanged() {
					resetList();
				}

				@Override
				public void onInvalidated() {
					super.onInvalidated();
				}
			};
			mAdapter.registerDataSetObserver(mDataSetObserver);
		}

		requestLayout();
	}

	@Override
	public View getSelectedView() {
		return null;
	}

	@Override
	public void setSelection(int position) {
	}

}
