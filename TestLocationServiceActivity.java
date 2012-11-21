package com.example.andriodtest;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TestLocationServiceActivity extends Activity {

	private static final String TAG = "MainActivity";

	XL_Log mLog = new XL_Log(MainActivity.class);
	TextView mTextView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.btn_run_test).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(TAG, "btn_run_test onClick.");
						runTestCase();
					}
				});
		mTextView = (TextView) this.findViewById(R.id.textView1);
		init();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uninit();
	}

	private void init() {
		// 监听各种LBS位置变化信息。
		List<String> providers = getLocationManager().getAllProviders();
		mLog.debug("getAllProviders=" + providers);
		for (String provider : providers) {
			MyLocationListener listener = new MyLocationListener();
			getLocationManager().requestLocationUpdates(
					provider, 2000, 100, listener);
			mListenerList.add(listener);
		}
//		mGpsListener = new MyLocationListener();
//		mNetListener = new MyLocationListener();		
//		getLocationManager().requestLocationUpdates(
//				LocationManager.GPS_PROVIDER, 2000, 100, mGpsListener);
//		getLocationManager().requestLocationUpdates(
//				LocationManager.NETWORK_PROVIDER, 2000, 100, mNetListener);
	}

	private void uninit() {

		for (LocationListener listener : mListenerList) {
			if (listener != null)
				getLocationManager().removeUpdates(listener);
		}
		
//		if (mNetListener != null)
//			getLocationManager().removeUpdates(mNetListener);
//		if (mGpsListener != null)
//			getLocationManager().removeUpdates(mGpsListener);
	}

	public void showToastTips(String tips) {
		Toast toast = Toast.makeText(this, tips, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	static public final boolean isSupportNfcService(Context context) {
		// Android2.2 SDK 不支持一下代码，未定义NFC_SERVICE。
		//return (null != context.getSystemService(Context.NFC_SERVICE));
		try {
			// 尝试获取Context类型是否定义了NFC_SERVICE这个常量，判断是否支持NFC！
			final String THE_NFC_SERVICE = (String) Context.class
					.getField("NFC_SERVICE").get(context);

			return (null != context.getSystemService(THE_NFC_SERVICE));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void runTestCase() {
		Log.d(TAG, "runTestCase begin ...");
		// testCase1();
//		testCase2();
		NfcManager nfcManager = (NfcManager)getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = nfcManager.getDefaultAdapter();
		
		Log.d(TAG, "isSupportNfcService " + isSupportNfcService(this));
		Log.d(TAG, "isSupportNfcService " + (null != getSystemService(Context.NFC_SERVICE)));
		Log.d(TAG, "isSupportNfcService " + (null != adapter));
		Log.d(TAG, "runTestCase end.");
	}
	// / 测试Android系统自带API获取GPS信息
	// / ---- begin -----
	// 位置监听器
//	private LocationListener mGpsListener = null;
//	private LocationListener mNetListener = null;
	private ArrayList<LocationListener> mListenerList = new ArrayList<LocationListener>();
	private LocationManager mLoctionManager = null;

	private final LocationManager getLocationManager() {
		if (mLoctionManager == null) {
			// 通过系统服务，取得LocationManager对象
			mLoctionManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		return mLoctionManager;
	}

	private class MyLocationListener implements LocationListener {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			mLog.debug("onStatusChanged: provider" + provider.toString()
					+ status);
			mTextView.setText("onStatusChanged: provider" + provider.toString()
					+ status);
		}

		@Override
		public void onProviderEnabled(String provider) {
			mLog.debug("onProviderEnabled: " + provider.toString());
			mTextView.setText("onProviderEnabled: " + provider.toString());
		}

		@Override
		public void onProviderDisabled(String provider) {
			mLog.debug("onProviderDisabled: " + provider.toString());
			mTextView.setText("onProviderDisabled: " + provider.toString());
		}

		// 当位置变化时触发
		@Override
		public void onLocationChanged(Location location) {
			mLog.debug("onLocationChanged " + location.toString());
			updateCurBestLocation(location);
			mTextView.setText("onLocationChanged " + location.toString());
		}
	}
	private Location mCurBestLocation = null;
	private Location getLastKnownLocation() {
		// 从可用的位置提供器中，匹配以上标准的最佳提供器
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);//
		criteria.setAltitudeRequired(false);// 不要求海拔
		criteria.setBearingRequired(false);// 不要求方位
		criteria.setCostAllowed(true);// 允许有花费
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 低功耗
		String bestProvider = mLoctionManager.getBestProvider(criteria, true);//
		mLog.debug("bestProvider = " + bestProvider);
//		GpsStatus gpsStatus = getLocationManager().getGpsStatus(null);
//		mLog.debug("getGpsStatus = " + gpsStatus.getMaxSatellites());
		// 首先获取GPS定位信息，若获取失败再获取AGPS定位信息。
		// 获得最后一次变化的位置
		Location location = getLocationManager().getLastKnownLocation(
				LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = getLocationManager().getLastKnownLocation(
					LocationManager.NETWORK_PROVIDER);			
		}
		updateCurBestLocation(location);
		mLog.debug("getLastKnownLocation location=" + location);
		
		return location;
	}
	
	private void updateCurBestLocation(Location location) {
		if (location == null)
			return;
		
		if (isBetterLocation(location, mCurBestLocation)) {
			mCurBestLocation = location;
			mLog.debug("updateCurBestLocation mCurBestLocation=" + location);			
		}
		
	}
	
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private static boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	
	// ---- end -----

	public void testCase1() {
		// // 测试日志库XL_Log打印日志。
		// XL_Log log = new XL_Log(MainActivity.class);
		//
		// for (int i = 0; i < 100000; ++i) {
		// log.debug("输出Level.DEBUG级别日志，一般开发调试信息用。");
		// log.info ("输出Level.INFO级别日志。");
		// log.warn("输出Level.WARN级别日志。");
		// log.error("输出Level.ERROR级别日志。");
		// log.fatal("输出Level.FATAL级别日志。");
		// }
	}

	public void testCase2() {
		getLastKnownLocation();
	}
	
	void testCase4() {
		//String text = "https://www.google.com.hk/search?q=%E8%96%84%E7%86%99%E6%9D%A5&aq=f&oq=%E8%96%84%E7%86%99%E6%9D%A5&aqs=chrome.0.57j0l3.480&sugexp=chrome,mod=0&sourceid=chrome&ie=UTF-8";
		String text1 = "http://zh.wikipedia.org/wiki/习近平";
		String text2 = "http://zh.wikipedia.org/wiki/%E4%B9%A0%E8%BF%91%E5%B9%B3";
		Uri uri1 = Uri.parse(text1);
		Uri uri2 = Uri.parse(text2);
		Log.d(TAG, "uri1:" + uri1);
		Log.d(TAG, "uri2:" + uri2);
		Log.d(TAG, "Uri.encode(text1):" + Uri.encode(text1));
		Log.d(TAG, "Uri.encode(text2):" + Uri.encode(text2));
		Log.d(TAG, "Uri.decode(text1):" + Uri.decode(text1));
		Log.d(TAG, "Uri.decode(text2):" + Uri.decode(text2));		
	}
}
