package com.raizlabs.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * View which mixes between two images by showing some portion of one image and
 * the rest of another.
 * <br><br>
 * Note: The view is sized based off the first image.
 * @author Dylan James
 *
 */
public class ImageMixView extends View {
	/**
	 * Possible directions for the mix.
	 * @author Dylan James
	 *
	 */
	public static class MixDirection {
		public static final int VERTICAL = 0; // 0b0
		public static final int HORIZONTAL = 2; // 0b10
		static final int REVERSE = 1; // 0b01
		
		public static final int VERTICAL_REVERSE = VERTICAL | REVERSE;
		public static final int HORIZONTAL_REVERSE = HORIZONTAL | REVERSE;
	}
	
	private int mixDirection;
	/**
	 * Sets the mix direction for this view.
	 * @param direction One of {@link MixDirection} which represents the
	 * direction to do the mix.
	 */
	public void setMixDirection(int direction) {
		mixDirection = direction;
	}
	
	// Rectangles to sample from in the source images
	Rect src1, src2;
	// Rectangles to draw to in the output canvas
	Rect dst1, dst2;
	
	private Bitmap bitmap1, bitmap2;
	/**
	 * Sets the first image of the mix.
	 * @param bmp The {@link Bitmap} to use as the first image.
	 */
	public void setFirstImage(Bitmap bmp) {
		bitmap1 = bmp;
		src1 = new Rect(0, 0, bitmap1.getWidth(), bitmap1.getHeight());
		requestLayout();
	}
	/**
	 * Sets the first image of the mix.
	 * @param id The resource id of the image to use as the first image.
	 */
	public void setFirstImageResource(int id) {
		setFirstImage(BitmapFactory.decodeResource(getResources(), id));
	}
	
	/**
	 * Sets the second image of the mix.
	 * @param bmp The {@link Bitmap} to use as the second image.
	 */
	public void setSecondImage(Bitmap bmp) {
		bitmap2 = bmp;
		src2 = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
	}
	/**
	 * Sets the second image of the mix.
	 * @param id The resource id of the image to use as the second image.
	 */
	public void setSecondImageResource(int id) {
		setSecondImage(BitmapFactory.decodeResource(getResources(), id));
	}
	
	private float mixValue;
	/**
	 * Sets the current mix value.
	 * @param mix A value between 0 and 1 representing an empty or full
	 * mix respectively.
	 */
	public void setMixValue(float mix) {
		mixValue = com.raizlabs.baseutils.Math.clamp(mix, 0, 1);
		postInvalidate();
	}
	
	/**
	 * @return The current mix value - a number between 0 and 1 representing
	 * an empty or full mix respectively.
	 */
	public float getMixValue() { return mixValue; }
	
	public ImageMixView(Context context) {
		super(context);
		init();
	}
	public ImageMixView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public ImageMixView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		setMixDirection(MixDirection.VERTICAL);
		setMixValue(0);
		dst1 = new Rect();
		dst2 = new Rect();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		
		int imageWidth = (bitmap1 == null ? 0 : bitmap1.getWidth());
		int imageHeight = (bitmap1 == null ? 0 : bitmap1.getHeight());
		
		switch (widthMode) {
		case MeasureSpec.EXACTLY:
			break;
		case MeasureSpec.AT_MOST:
			width = Math.min(width, imageWidth);			
			break;
		case MeasureSpec.UNSPECIFIED:
			width = imageWidth;
			break;
		}
		
		switch (heightMode) {
		case MeasureSpec.EXACTLY:
			break;
		case MeasureSpec.AT_MOST:
			height = Math.min(height, imageHeight);			
			break;
		case MeasureSpec.UNSPECIFIED:
			height = imageHeight;
			break;
		}
		
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// Resize our output rectangles
		dst1.right = dst2.right = getWidth();
		dst1.bottom = dst2.bottom = getHeight();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bitmap1 == null || bitmap2 == null) return;
		// Read the bitmask to see if this is set to run in reverse
		final boolean reversed = (mixDirection & MixDirection.REVERSE) > 0;
		// If reversed, flip the mix value
		final float currMix = reversed ? 1 - mixValue : mixValue;
		
		// Get the non-reverse bits
		switch (mixDirection & ~MixDirection.REVERSE) {
		case MixDirection.HORIZONTAL:
			// Update the input rectangles
			final int srcLeft = (int) (currMix * bitmap1.getWidth());
			src1.left = srcLeft;
			src2.right = srcLeft;
			
			// Update the output rectangles
			final int dstLeft = (int) (currMix * getWidth());
			dst1.left = dstLeft;
			dst2.right = dstLeft;
			break;
		case MixDirection.VERTICAL:
			// Update the input rectangles
			final int srcHeight = bitmap1.getHeight();
			final int srcBottom = (int) (srcHeight - (currMix * srcHeight));
			src1.bottom = srcBottom;
			src2.top = srcBottom;
			
			// Update the output rectangles
			final int dstHeight = getHeight();
			final int dstBottom = (int) (dstHeight - (currMix * dstHeight));
			dst1.bottom = dstBottom;
			dst2.top = dstBottom;
			break;
		}
		
		canvas.drawBitmap(bitmap1, src1, dst1, null);
		canvas.drawBitmap(bitmap2, src2, dst2, null);
	}
}
