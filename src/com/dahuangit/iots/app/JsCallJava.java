package com.dahuangit.iots.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;

/**
 * js调用java
 * 
 * @author 大黄
 * 
 *         2015年1月6日上午8:35:41
 */
public class JsCallJava {

	private Context context = null;

	private Handler mHandler = new Handler();

	public JsCallJava(Context context) {
		this.context = context;
	}

	/**
	 * 播放本地mp3文件
	 * 
	 * @param mp3Name
	 */
	public void playMp3(final String perceptionId, final String mp3Name) {
		mHandler.post(new Runnable() {
			public void run() {
				try {
					String packageName = context.getPackageName();
					Resources res = context.getResources();

					int mp3Id = res.getIdentifier(mp3Name, "raw", packageName);
					MediaPlayer mp = MediaPlayer.create(context, mp3Id);

					mp.prepare();
					mp.start();
					
//					showNotification();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void showNotification() {
		// 创建一个NotificationManager的引用
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		// 定义Notification的各种属性
		Notification notification = new Notification(R.drawable.ic_launcher, "IOTS", System.currentTimeMillis());

		// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// 表明在点击了通知栏中的"清除通知"后，此通知自动清除。
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 5000;

		// 设置通知的事件消息
		CharSequence contentTitle = "IOTS告警信息"; // 通知栏标题
		CharSequence contentText = "设备1(5)"; // 通知栏内容
		
		Intent notificationIntent = new Intent(context, LogListMainActivity.class);
		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		// 把Notification传递给NotificationManager
		notificationManager.notify(0, notification);
	}

	// 取消通知
	public void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		notificationManager.cancel(0);
	}
}
