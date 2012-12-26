package com.example.andriodtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

public class TestRandomAccessFile {

	private static final String TAG = "TestRandomAccessFile";
	private RandomAccessFile mFileInput = null;
	private FileChannel mFileInput2 = null;

	private String mFileUri = null;

	TestRandomAccessFile(Activity activity) {
		mActivity = activity;
	}

	public final String fileUri() {
		return mFileUri;
	}

	public void setFileUri(String mFileUri) {
		this.mFileUri = mFileUri;
	}

	private void closeFile() {
		if (mFileInput != null) {
			try {
				mFileInput.close();
				mFileInput = null;
			} catch (IOException e) {
				Log.e(TAG, "closeFile error! " + e);
				e.printStackTrace();
			}
		}
		if (mFileInput2 != null) {
			try {
				mFileInput2.close();
				mFileInput2 = null;
			} catch (IOException e) {
				Log.e(TAG, "closeFile error! " + e);
				e.printStackTrace();
			}
		}
	}

	private Activity mActivity = null;

	private final InputStream getInputStream(Uri uri)
			throws FileNotFoundException {
		ContentResolver cr = mActivity.getContentResolver();
		return cr.openInputStream(uri);
	}

	private boolean openFile() {
		Log.d(TAG, "openFile2");
		if (mFileUri != null && mFileInput == null) {

			Uri uri = Uri.parse(mFileUri);
			try {
				Log.d(TAG,
						"uri:" + uri.toString() + " getPath:" + uri.getPath()
								+ " getEncodedPath:" + uri.getEncodedPath());
				File file = new File(uri.getPath());
				mFileInput = new RandomAccessFile(file, "rwd");
				mFileInput.seek(mFileInput.length());
				return true;
			} catch (FileNotFoundException e) {
				Log.e(TAG, "openFile error! " + e);
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG, "openFile error! " + e);
				e.printStackTrace();
			}
		}

		return false;
	}

	private boolean openFile2() {
		Log.d(TAG, "openFile2");
		if (mFileUri != null && mFileInput2 == null) {

			Uri uri = Uri.parse(mFileUri);
			try {
				Log.d(TAG,
						"uri:" + uri.toString() + " getPath:" + uri.getPath()
								+ " getEncodedPath:" + uri.getEncodedPath());
				FileInputStream fileInputStream = (FileInputStream) getInputStream(uri);
				mFileInput2 = fileInputStream.getChannel();
				Log.d(TAG, "file size:" + mFileInput2.size());
				Log.d(TAG, "file position:" + mFileInput2.position());
				Log.d(TAG, "set position:" + mFileInput2.position(100));
				Log.d(TAG, "file position:" + mFileInput2.position());
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Log.e(TAG, "openFile2 error! " + e);
			} catch (IOException e) {
				Log.e(TAG, "openFile2 error! " + e);
				e.printStackTrace();
			}
		}

		return false;
	}

	public void runTestCase() {
		boolean ret = openFile();
		closeFile();
		Log.d(TAG, "openFile return " + ret);
		ret = openFile2();
		closeFile();
		Log.d(TAG, "openFile return " + ret);
	}
}
