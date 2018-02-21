package com.example.fishingport.app.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.widget.Toast;

public class JavaScriptinterface {

	private Context mContext;
	//这个一定要定义，要不在showToast()方法里没办法启动intent
	Activity activity;

	/** Instantiate the interface and set the context */
	public JavaScriptinterface(Context c) {
		mContext = c;
		activity = (Activity) c;
	}

	/** 与js交互时用到的方法，在js里直接调用的 */
	public void showToast() {

		Toast.makeText(mContext, "111", Toast.LENGTH_SHORT).show();
	}
}
