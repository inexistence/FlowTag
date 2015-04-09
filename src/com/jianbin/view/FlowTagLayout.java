package com.jianbin.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowTagLayout extends ViewGroup {

	public FlowTagLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowTagLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowTagLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// wrap_content
		int width = 0;
		int height = 0;

		int lineWidth = 0;// �п�
		int lineHeight = 0;// �и�

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

			if (lineWidth + childWidth > sizeWidth) {// ����
				width = Math.max(width, lineWidth);
				lineWidth = childWidth;// reset line width
				// record height
				height += lineHeight;
				lineHeight = childHeight;
			} else {// ������
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

	/**
	 * save all tags
	 */
	private List<List<View>> mAllTags = new ArrayList<List<View>>();
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
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

	/**
	 * �뵱ǰViewGroup��Ӧ��LayoutParams
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

}
