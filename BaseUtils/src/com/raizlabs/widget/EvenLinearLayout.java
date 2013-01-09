package com.raizlabs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * {@link LinearLayout} implementation which evenly spaces its children to fit
 * its bounds.
 * @author Dylan James
 *
 */
public class EvenLinearLayout extends LinearLayout {

	public EvenLinearLayout(Context context) {
		super(context);
	}

	public EvenLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (getOrientation() == VERTICAL) {
			layoutVertical();
		} else {
			layoutHorizontal();
		}
	}

	void layoutVertical() {
		final int paddingLeft = getPaddingLeft();
		/** Height after padding */
		final int totalHeight = getHeight() - getPaddingTop() - getPaddingBottom();
		/** The top to set for the children. This will be incremented as we go. */
		int childTop = getPaddingTop();

		/** Total width of the view */
		final int width = getRight() - getLeft();
		/** The right edge of the usable space */
		int rightEdge = width - getPaddingRight();

		/** How much space we have for all of the children */
		int totalChildSpace = rightEdge - paddingLeft;

		final int childCount = getChildCount();
		/** Sum of the heights of the children */
		int totalChildHeight = 0;
		for (int i = 0; i < childCount; ++i) {
			totalChildHeight += getChildAt(i).getMeasuredHeight();
		}

		/** How much extra space each child should be assigned */
		final float marginPerChild = (float)(totalHeight - totalChildHeight) / (float)(childCount + 1);
		
		int childLeft;
		int childWidth;

		View currChild;
		int currTop;
		for (int i = 0; i < childCount; ++i) {
			currChild = getChildAt(i);
			currTop = (int) (childTop + marginPerChild);
			childWidth = currChild.getMeasuredWidth();

			final LayoutParams lp = (LayoutParams) currChild.getLayoutParams();

			// Align the left side using the specified gravity. We're aligning
			// vertically ourselves, so we don't need to respect vertical settings.
			switch (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
			case Gravity.LEFT:
				childLeft = paddingLeft + lp.leftMargin;
				break;
			case Gravity.CENTER_HORIZONTAL:
				childLeft = paddingLeft + ((totalChildSpace - childWidth) / 2)
						+ lp.leftMargin - lp.rightMargin;
				break;
			case Gravity.RIGHT:
				childLeft = rightEdge - childWidth - lp.rightMargin;
				break;
			default:
				childLeft = paddingLeft;
				break;
			}

			// Layout the child
			currChild.layout(childLeft, currTop, childLeft + childWidth, currTop + currChild.getMeasuredHeight());
			// Update the top
			childTop = currTop + currChild.getMeasuredHeight();
		}
	}

	void layoutHorizontal() {
		final int paddingTop = getPaddingTop();
		/** Width after padding */
		final int totalWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		/** The left to set for the children. This will be incremented as we go. */
		int childLeft = getPaddingLeft();

		/** Total height of the view */
		final int height = getBottom() - getTop();
		/** The bottom edge of the usable space */
		int bottomEdge = height - getPaddingBottom();

		/** How much space we have for all of the children */
		int totalChildSpace = bottomEdge - paddingTop;

		final int childCount = getChildCount();
		/** Sum of the widths of the children */
		int totalChildWidth = 0;
		for (int i = 0; i < childCount; ++i) {
			totalChildWidth += getChildAt(i).getMeasuredWidth();
		}

		/** How much extra space each child should be assigned */
		final float marginPerChild = (float)(totalWidth - totalChildWidth) / (float)(childCount + 1);

		int childTop;
		int childHeight;

		View currChild;
		int currLeft;
		for (int i = 0; i < childCount; ++i) {
			currChild = getChildAt(i);
			currLeft = (int) (childLeft + marginPerChild);
			childHeight = currChild.getMeasuredHeight();

			final LayoutParams lp = (LayoutParams) currChild.getLayoutParams();

			// Align the top using the specified gravity. We're aligning
			// horizontally ourselves, so we don't need to respect horizontal
			// settings.
			switch (lp.gravity & Gravity.VERTICAL_GRAVITY_MASK) {
			case Gravity.TOP:
				childTop = paddingTop + lp.topMargin;
				break;
			case Gravity.CENTER_VERTICAL:
				childTop = paddingTop + ((totalChildSpace - childHeight) / 2)
						+ lp.topMargin - lp.bottomMargin;
				break;
			case Gravity.BOTTOM:
				childTop = bottomEdge - childHeight - lp.bottomMargin;
				break;
			default:
				childTop = paddingTop;
				break;
			}

			// Layout the child
			currChild.layout(currLeft, childTop, currLeft + currChild.getMeasuredWidth(), childTop + childHeight);
			// Update the left
			childLeft = currLeft + currChild.getMeasuredWidth();
		}
	}
}
