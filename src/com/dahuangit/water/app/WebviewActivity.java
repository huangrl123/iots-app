package com.dahuangit.water.app;

import java.util.HashMap;
import java.util.Map;

import com.dahuangit.water.app.util.HttpUtils;
import com.dahuangit.water.app.util.Response;
import com.dahuangit.water.app.util.XmlUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebviewActivity extends Activity {
	private static final String TAG = WebviewActivity.class.getSimpleName();

	private WebView webview;

	private Handler handler = new Handler() {

		@Override
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {
			int what = msg.what;
			Bundle data = msg.getData();
			switch (what) {
			case 1:
				finish();
				Intent in = new Intent(getApplicationContext(), MainActivity.class);
				in.putExtra("isFromWeb", "true");
				startActivity(in);
				break;
			case 2:
				finish();
				break;
			}

		}

	};

	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);

		// 设置Web视图
		webview.setWebViewClient(new HelloWebViewClient());

		webview.addJavascriptInterface(new Object() {

			@JavascriptInterface
			public void exitSys() {
				android.app.AlertDialog.Builder b = new AlertDialog.Builder(WebviewActivity.this);
				b.setTitle("提示");
				b.setMessage("确认退出?");

				b.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						exitSystem(1);
					}
				});

				b.setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				b.show();
			}
		}, "app");

		progressDialog = ProgressDialog.show(WebviewActivity.this, null, "加载中...", true);

		// 加载需要显示的网页
		webview.loadUrl(InitConfig.prop.getProperty("server.index.url") + "?userId=" + InitConfig.userId);
	}

	private void exitSystem(final int what) {
		new Thread() {
			public void run() {
				Message msg = new Message();

				try {
					String host = InitConfig.prop.getProperty("server.logout.url");
					Map<String, String> params = new HashMap<String, String>();
					params.put("userId", InitConfig.userId.toString());

					String xml = HttpUtils.getHttpRequestContent(host, params);
					Response response = XmlUtils.xml2obj(InitConfig.mapping, xml, Response.class);

					msg.what = what;
					Bundle data = new Bundle();
					data.putSerializable("response", response);
					msg.setData(data);

				} catch (Exception e) {
				}

				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 设置回退
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 不做任何动作
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			String title = webview.getTitle();
			if ("功能列表".equals(title)) {
				android.app.AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle("提示");
				b.setMessage("确认关闭软件?");

				b.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							exitSystem(2);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				b.setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				b.show();
			} else {
				webview.goBack();
			}

			return true;
		}

		return false;
	}

	public void exit() {

	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			progressDialog.dismiss();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			progressDialog.dismiss();
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progressDialog.show();
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
