package com.example.floatwindowdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

/**
 * 
 * @author tr
 * @time 2014-2-17
 * @description 对不同状态下切换悬浮窗的服务类
 */
public class FloatWindowService extends Service{

	/**用于在线程中创建或移除悬浮窗*/
	private Handler handler = new Handler();
	
	/**定时器，定时进行检测当前应该创建还是移除悬浮窗*/
	private Timer timer;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//开启定时器，每隔0.5s刷新一次
		if(timer == null){
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 5000);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//service被终止的同时也停止定时器
		timer.cancel();
		timer = null;
		
	}
	
	
	class RefreshTask extends TimerTask{

		@Override
		public void run() {
			
			//System.out.println("是否桌面："+isHome());
			//System.out.println("是否已有悬浮窗："+MyWindowManager.isWindowShowing());
			//当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗
			if(isHome() && !MyWindowManager.isWindowShowing()){
				handler.post(new Runnable() {
					
					@Override
					public void run() {
							//创建
						MyWindowManager.createSmallWindow(getApplicationContext());
					}
				});
				
			}
			//当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗
			else if(!isHome() && MyWindowManager.isWindowShowing()){
				handler.post(new Runnable() {
					
					@Override
					public void run() {
							//移除
						MyWindowManager.removeSmallWindow(getApplicationContext());
						MyWindowManager.removeBigWindow(getApplicationContext());
					}
				});
			}
			//当前界面是桌面，且有悬浮窗显示，则更新内存数据
			else if(isHome() && MyWindowManager.isWindowShowing()){
				handler.post(new Runnable() {
					
					@Override
					public void run() {
							//更新
						MyWindowManager.updateUsedPercent(getApplicationContext());
					}
				});
			}
		}
		
	}
	
	
	/**判断当前界面是否是桌面*/
	private boolean isHome(){
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		//System.out.println("length0:"+rti.size());
		//System.out.println("name:"+rti.get(0).topActivity.getPackageName());
		return getHomes().contains(rti.get(0).topActivity.getPackageName());
	}
	
	/**
	 * 获得属于桌面的应用的应用包名称
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes(){
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		//System.out.println("length:"+resolveInfo.size());
		for(ResolveInfo ri:resolveInfo){
			//System.out.println("name0:"+ri.activityInfo.packageName);
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
	
}
