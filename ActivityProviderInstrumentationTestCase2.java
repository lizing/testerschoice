package com.testerschoice.moneybook.test;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockApplication;
import android.test.mock.MockContentResolver;
import android.view.Window;

public abstract class ActivityProviderInstrumentationTestCase2 <T extends Activity, P extends ContentProvider> extends
		ActivityInstrumentationTestCase2 {

	private Class<T> mActivityClass;
	private Class<P> mProviderClass;
	private String mAuthority;
	private Application mApplication;
	private MockParent mMockParent;
	
	private MockContextWithMockContentProvider mContext;
	private T mActivity;
	
	public ActivityProviderInstrumentationTestCase2(Class<T> activityClass, Class<P> providerClass, String authority){
		super(activityClass);
		mActivityClass = activityClass;
		mProviderClass = providerClass;
		mAuthority = authority;
	}
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		//MockContext 생성
		mContext = new MockContextWithMockContentProvider(getInstrumentation().getTargetContext(), mProviderClass, mAuthority);
		mContext.makeExistingFilesAndDbsAccessible();
	}
	
	//MockContext를 이용한 Activity를 생성 후 반환
	public T startActivity(){
		T newActivity = null;
		
		try{			
			if(mApplication == null){
				setApplication(new MockApplication());
			}
			
			IBinder token = null;
			ComponentName cn = new ComponentName(mActivityClass.getPackage().getName(), mActivityClass.getName());
			
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setClassName(mContext, mActivityClass.getName());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(cn);
			mMockParent = new MockParent();
			
			newActivity = (T) getInstrumentation().newActivity(mActivityClass, mContext, token, mApplication, 
					intent, new ActivityInfo(), mActivityClass.getName(), mMockParent, null, null);
		}catch(Exception e){
			assertNotNull(newActivity);
		}
		
		assertNotNull(newActivity);
		setActivity(newActivity);
		
		getInstrumentation().callActivityOnCreate(newActivity, null);
		
		return newActivity;
	}
	
	
	@Override
	protected void setActivity(Activity testActivity) {
		// TODO Auto-generated method stub
		mActivity = (T) testActivity;
	}

	@Override
	public void tearDown() throws Exception{
		super.tearDown();
	}
	
	public void setApplication(Application application){
		mApplication = application;
	}
	
	@Override
	public T getActivity(){
		if(mActivity != null)
			return mActivity;
		else
			return null;
	}
	
	private static class MockParent extends Activity{
		public int mRequestedOrientation = 0;
		public Intent mStartedActivityIntent = null;
		public int mStartedActivityRequest = -1;
		public boolean mFinished = false;
		public int mFinishedActivityRequest = -1;
		
		@Override
		public void setRequestedOrientation(int requestedOrientation){
			mRequestedOrientation = requestedOrientation;
		}
		
		@Override
		public int getRequestedOrientation(){
			return mRequestedOrientation;
		}
		
		@Override
		public Window getWindow(){
			return null;
		}
		
		@Override
		public void startActivityFromChild(Activity child, Intent intent, int requestCode){
			mStartedActivityIntent = intent;
			mStartedActivityRequest = requestCode;
		}
		
		@Override
		public void finishFromChild(Activity child){
			mFinished = true;
		}
		
		@Override
		public void finishActivityFromChild(Activity child, int requestCode){
			mFinished = true;
			mFinishedActivityRequest = requestCode;
		}
	}
	
	private class MockContextWithMockContentProvider <P extends ContentProvider> extends RenamingDelegatingContext{

		private static final String MOCK_FILE_PREFIX = "test.";
		private MockContentResolver mResolver;
		
		public MockContextWithMockContentProvider(Context context, Class<P> providerClass, String authority) throws Exception {
			super(context, MOCK_FILE_PREFIX);
			init(authority, providerClass);
		}
		//MockContentResolver 초기화
		private void init(String authority, Class<P> providerClass) throws Exception{
			mResolver = new MockContentResolver();
			P mProvider = providerClass.newInstance();
			mProvider.attachInfo(this, null);
			assertNotNull(mProvider);
			mResolver.addProvider(authority, mProvider);
		}
		
		//MockContext를 이용해 만든 Activity에서 getContentResolver()를 호출 시 이 메소드가 호출됨
		//MockContentResolver를 반환
		@Override
		public ContentResolver getContentResolver(){
			return mResolver;
		}
	}
}
