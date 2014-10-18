package com.example.floatwindowdemo;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author tr
 * @time 2014-2-17
 * @description С����������
 */
public class FloatWindowSmallView extends LinearLayout {
	/**
	 * ��¼С�������Ŀ��
	 */
	public static int viewWidth;

	/**
	 * ��¼С�������ĸ߶�
	 */
	public static int viewHeight;
	/**
	 * ��¼ϵͳ״̬���ĸ߶�
	 */
	private static int statusBarHeight;
	/**
	 * ���ڸ���С��������λ��
	 */
	private WindowManager windowManager;
	 /** 
     * С�������Ĳ��� 
     */  
    private WindowManager.LayoutParams mParams;  
  
    /** 
     * ��¼��ǰ��ָλ������Ļ�ϵĺ�����ֵ 
     */  
    private float xInScreen;  
  
    /** 
     * ��¼��ǰ��ָλ������Ļ�ϵ�������ֵ 
     */  
    private float yInScreen;  
  
    /** 
     * ��¼��ָ����ʱ����Ļ�ϵĺ������ֵ 
     */  
    private float xDownInScreen;  
  
    /** 
     * ��¼��ָ����ʱ����Ļ�ϵ��������ֵ 
     */  
    private float yDownInScreen;  
  
    /** 
     * ��¼��ָ����ʱ��С��������View�ϵĺ������ֵ 
     */  
    private float xInView;  
  
    /** 
     * ��¼��ָ����ʱ��С��������View�ϵ��������ֵ 
     */  
    private float yInView;  
    
    /**С����������*/
    private View view;

	public FloatWindowSmallView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//��ȡWindowManager����
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		//��ȡ�����ļ�
		LayoutInflater.from(context).inflate(R.layout.floatwindowsmall, this);
		view = findViewById(R.id.smallwindowlayout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		TextView percentView = (TextView) findViewById(R.id.percent);
		percentView.setText(MyWindowManager.getUsedPercentValue(context));

	/*	view = new ImageView(context);
		view.setImageResource(R.drawable.ic_launcher);
		viewWidth = 80;
		viewHeight = 80;

		addView(view, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));*/

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//��ָ����ʱ��¼��Ҫ���ݣ��������ֵ����Ҫ��ȥ״̬���ĸ߶�
				xInView = event.getX();
				yInView = event.getY();
				
				
				xDownInScreen = event.getRawX();
				yDownInScreen = event.getRawY() - getStatusBarHeight();
				
				xInScreen = event.getRawX();
				yInScreen = event.getRawY() - getStatusBarHeight();
				//������������޸Ĵ��屳����ɫ
				view.setBackgroundColor(Color.GREEN);
				break;
			case MotionEvent.ACTION_MOVE:
				xInScreen = event.getRawX();  
	            yInScreen = event.getRawY() - getStatusBarHeight();  
	            // ��ָ�ƶ���ʱ�����С��������λ��  
	            updateViewPosition();  
				break;
			case MotionEvent.ACTION_UP:
				// �����ָ�뿪��Ļʱ��xDownInScreen��xInScreen��ȣ���yDownInScreen��yInScreen��ȣ�����Ϊ�����˵����¼���  
	            if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {  
	                openBigWindow();  
	            }  
				//�ͷ�����������ԭ���屳����ɫ
				view.setBackgroundColor(Color.RED);
				break;

		}
		return true;
	}
	
	/**
	 * ��С�������Ĳ������룬���ڸ���С��������λ��
	 * @param params С�������Ĳ���
	 */
	public void setParams(WindowManager.LayoutParams params){
		mParams = params;
	}
	
	/**����С����������Ļ�е�λ��*/
	private void updateViewPosition(){
		mParams.x = (int)(xInScreen - xInView);
		mParams.y = (int)(yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}
	
	/**�򿪴���������ͬʱ�ر�С������*/
	private void openBigWindow(){
		MyWindowManager.createBigWindow(getContext());
		MyWindowManager.removeSmallWindow(getContext());
	}
	
	/**
	 * ���ڻ�ȡ״̬���ĸ߶�
	 * @return ����״̬���߶ȵ�����ֵ
	 */
	private int getStatusBarHeight(){
		if(statusBarHeight == 0){
			try{
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}
}
