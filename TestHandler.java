package com.example.andriodtest;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class TestHandler implements Runnable {
//	// 一个线程的Looper对象允许给多个不同的Handler对象使用，而且之间相互独立的。示例如下：
//	程序输出：
//	 MyHandler： MSG_ID_ZERO handler1
//	 MyHandler2： MSG_ID_ZERO handler2

	
	private static final String TAG = "TestHandler";
	private HandlerThread mThread = null;
	private class MyHandler extends Handler {
		private static final String TAG = "MyHandler";
		public static final int MSG_ID_DEFAULT = -1;
		public static final int MSG_ID_ZERO = 0;
		public static final int MSG_ID_ONE = 1;
		public static final int MSG_ID_TWO = 2;
		public static final int MSG_ID_THREE = 3;
		public static final int MSG_ID_FOUR = 4;
		
		public MyHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_ID_ZERO:
				Log.d(TAG, "MSG_ID_ZERO " + (String)msg.obj);
				break;
			case MSG_ID_ONE:
				Log.d(TAG, "MSG_ID_ONE " + (String)msg.obj);
				break;
			case MSG_ID_TWO:
				Log.d(TAG, "MSG_ID_TWO " + (String)msg.obj);
				break;
			case MSG_ID_THREE:
				Log.d(TAG, "MSG_ID_THREE " + (String)msg.obj);
				break;
			case MSG_ID_FOUR:
				Log.d(TAG, "MSG_ID_FOUR " + (String)msg.obj);
				break;
			default:
				Log.d(TAG, "MSG_ID_DEFAULT " + (String)msg.obj);
				
			}
			super.handleMessage(msg);
		}
	}
	
	private class MyHandler2 extends Handler {
		private static final String TAG = "MyHandler2";
		public static final int MSG_ID_DEFAULT = -1;
		public static final int MSG_ID_ZERO = 0;
		public static final int MSG_ID_ONE = 1;
		public static final int MSG_ID_TWO = 2;
		public static final int MSG_ID_THREE = 3;
		public static final int MSG_ID_FOUR = 4;
		
		public MyHandler2(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_ID_ZERO:
				Log.d(TAG, "MSG_ID_ZERO " + (String)msg.obj);
				break;
			case MSG_ID_ONE:
				Log.d(TAG, "MSG_ID_ONE " + (String)msg.obj);
				break;
			case MSG_ID_TWO:
				Log.d(TAG, "MSG_ID_TWO " + (String)msg.obj);
				break;
			case MSG_ID_THREE:
				Log.d(TAG, "MSG_ID_THREE " + (String)msg.obj);
				break;
			case MSG_ID_FOUR:
				Log.d(TAG, "MSG_ID_FOUR " + (String)msg.obj);
				break;
			default:
				Log.d(TAG, "MSG_ID_DEFAULT " + (String)msg.obj);
				
			}
			super.handleMessage(msg);
		}
	}
	
	private Handler mHandler1, mHandler2;
	public void init() {
		mThread = new HandlerThread(TAG);
		mThread.start();
		mHandler1 = new MyHandler(mThread.getLooper());
		mHandler2 = new MyHandler2(mThread.getLooper());
	}

	public void uninit() {
		mThread.getLooper().quit();
		mHandler1 = null;
		mHandler2 = null;
		mThread = null;
	}
	
	@Override
	public void run() {
		Message msg1 = Message.obtain();
		msg1.what = MyHandler.MSG_ID_ZERO;
		msg1.obj = "handler1";
		mHandler1.sendMessage(msg1);

		Message msg2 = Message.obtain();
		msg2.what = MyHandler.MSG_ID_ZERO;
		msg2.obj = "handler2";
		mHandler2.sendMessage(msg2);
	}
	
}
