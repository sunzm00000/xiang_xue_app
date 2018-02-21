package com.example.fishingport.app.tools;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class KeyBoardHelper {

	private Activity activity;
	private OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener;
	private int screenHeight;
	private int blankHeight = 0;

	public KeyBoardHelper(Activity activity) {
		this.activity = activity;
		screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	public void onCreate() {
		View content = activity.findViewById(android.R.id.content);
		// content.addOnLayoutChangeListener(listener); 这个方法有时会出现一些问题
		content.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	public void onDestory() {
		View content = activity.findViewById(android.R.id.content);
		content.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	private OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {

		@Override
		public void onGlobalLayout() {
			Rect rect = new Rect();
			activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
			int newBlankheight = screenHeight - rect.bottom;
			if (newBlankheight != blankHeight) {
				if (newBlankheight > blankHeight) {
					// keyboard pop
					if (onKeyBoardStatusChangeListener != null) {
						onKeyBoardStatusChangeListener.OnKeyBoardPop(newBlankheight);
					}
				} else { // newBlankheight < blankHeight
					// keyboard close
					if (onKeyBoardStatusChangeListener != null) {
						onKeyBoardStatusChangeListener.OnKeyBoardClose(blankHeight);
					}
				}
			}
			blankHeight = newBlankheight;
		}
	};

	public void setOnKeyBoardStatusChangeListener(
			OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener) {
		this.onKeyBoardStatusChangeListener = onKeyBoardStatusChangeListener;
	}

	public void showKeyBoard(final View editText) {
		editText.requestFocus();
		InputMethodManager manager = (InputMethodManager) activity.getSystemService("input_method");
		manager.showSoftInput(editText, 0);
	}

	public void hideKeyBoard(View editText) {
		InputMethodManager manager = (InputMethodManager) activity.getSystemService("input_method");
		manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public interface OnKeyBoardStatusChangeListener {

		void OnKeyBoardPop(int keyBoardheight);

		void OnKeyBoardClose(int oldKeyBoardheight);
	}

}
