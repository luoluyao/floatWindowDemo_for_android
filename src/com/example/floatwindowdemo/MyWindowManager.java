package com.example.floatwindowdemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author tr
 * @time 2014-2-17
 * @description ������������
 */
public class MyWindowManager {
	
	/**С������view��ʵ��*/
	private static FloatWindowSmallView smallWindow;
	
	/**��������view��ʵ��*/
	private static FloatWindowBigView bigWindow;
	
	/**С������view�Ĳ���*/
	private static LayoutParams smallWindowParams;
	
	/**��������view�Ĳ���*/
	private static LayoutParams bigWindowParams;

	/**���ڿ�������Ļ����ӻ��Ƴ�������*/
	private static WindowManager mWindowManager;
	
	/**���ڻ�ȡ�ֻ������ڴ�*/
	private static ActivityManager mActivityManager;
	/**
	 * ����һ��С����������ʼλ������Ļ���Ҳ��м�λ��
	 * @param context ����ΪӦ�ó����Context
	 */
	public static void createSmallWindow(Context context){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		
		if(smallWindow == null){
			smallWindow = new FloatWindowSmallView(context);
			if(smallWindowParams == null){
				smallWindowParams = new LayoutParams();
				smallWindowParams.x = screenWidth;
				smallWindowParams.y = screenHeight/2 ;
				smallWindowParams.type = LayoutParams.TYPE_PHONE;
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = FloatWindowSmallView.viewWidth;
				smallWindowParams.height = FloatWindowSmallView.viewHeight;
				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
			}
		}
		System.out.println("���С������");
		smallWindow.setParams(smallWindowParams);
		windowManager.addView(smallWindow, smallWindowParams);
		
	}
	
	/**
	 * ��С����������Ļ���Ƴ�
	 * @param context ����ΪӦ�ó����context
	 */
	public static void removeSmallWindow(Context context){
		if(smallWindow != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}
		
	}
	/**
	 * ����һ��������������ʼλ������Ļ���Ҳ��м�λ��
	 * @param context ����ΪӦ�ó����Context
	 */
	public static void createBigWindow(Context context){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		
		if(bigWindow == null){
			bigWindow = new FloatWindowBigView(context);
			if(bigWindowParams == null){
				bigWindowParams = new LayoutParams();
				bigWindowParams.x = screenWidth/2 - FloatWindowBigView.viewWidth/2;
				bigWindowParams.y = screenHeight/2 - FloatWindowBigView.viewHeight/2;
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				bigWindowParams.width = FloatWindowBigView.viewWidth;
				bigWindowParams.height = FloatWindowBigView.viewHeight;
			}
		}
		windowManager.addView(bigWindow, bigWindowParams);
		
	}
	
	/**
	 * ��������������Ļ���Ƴ�
	 * @param context ����ΪӦ�ó����context
	 */
	public static void removeBigWindow(Context context){
		if(bigWindow != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(bigWindow);
			bigWindow = null;
		}
		
	}
	
    /** 
     * ����С��������TextView�ϵ����ݣ���ʾ�ڴ�ʹ�õİٷֱȡ� 
     *  
     * @param context 
     *            �ɴ���Ӧ�ó��������ġ� 
     */  
    public static void updateUsedPercent(Context context) {  
        if (smallWindow != null) {  
            TextView percentView = (TextView) smallWindow.findViewById(R.id.percent);  
            
           percentView.setText(getUsedPercentValue(context));  
        	//ImageView View = new ImageView(context);
        	//View.setImageResource(R.drawable.ic_launcher);
        }  
    } 
	
	/**
	 * �Ƿ���������(����С�������ʹ�������)��ʾ����Ļ��
	 * @return ����������ʾ����Ļ�Ϸ���true��û�еĻ�����false
	 */
	public static boolean isWindowShowing(){
		return smallWindow != null || bigWindow != null;
	}
	
	/**
	 * ���windowmanager��δ�������򴴽�һ���µ�windowmanager���ء����򷵻ص�ǰ�Դ�����windowmanager
	 * @param context ����ΪӦ�ó����context
	 * 
	 * @return windowmanager��ʵ�������ڿ�����Ļ����ӻ��Ƴ�������
	 */
	private static WindowManager getWindowManager(Context context){
		if(mWindowManager == null){
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
	
	/**
	 *  ���acitivtymanager��δ�������򴴽�һ���µ�activitymanager���أ����򷵻ص�ǰ�Ѵ�����activitymanager
	 * @param context �ɴ���Ӧ�ó����������
	 * @return anctivitymanager��ʵ�������ڻ�ȡ�ֻ������ڴ�
	 */
	private static ActivityManager getActivityManager(Context context){
		if(mActivityManager == null){
			mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}
	
	/**
	 * ������ʹ���ڴ�İٷֱȣ�������
	 * @param context �ɴ���Ӧ�ó���������
	 * @return ��ʹ���ڴ�İٷֱȣ����ַ�����ʽ����
	 */
	public static String getUsedPercentValue(Context context){
		String dir = "/proc/meminfo";
		try{
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
			 long availableSize = getAvailableMemory(context) / 1024;  
	            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);  
	            return percent + "%";  
		}catch(IOException e){
			e.printStackTrace();
		}
		return "0%";
	}
	
	/**
	 *  ��ȡ��ǰ�����ڴ棬�����������ֽ�Ϊ��λ
	 * @param context �ɴ���Ӧ�ó���������
	 * @return ��ǰ�����ڴ�
	 */
	private static long getAvailableMemory(Context context){
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return mi.availMem;
	}
}
