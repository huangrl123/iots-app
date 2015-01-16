package com.dahuangit.iots.app;

import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint({ "NewApi", "JavascriptInterface" })
public class LogListMainActivity extends Activity {
	private static final String TAG = LogListMainActivity.class.getSimpleName();

	/** js全局变量名称: 用于从js端调用到安卓本地java方法 */
	private static final String GLOBAL_JS_VAR_NAME = "iots";

	private WebView webview = null;

	private Handler mHandler = new Handler();

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		webview = (WebView) findViewById(R.id.webview);

		webview.getSettings().setJavaScriptEnabled(true);

		Properties prop = new Properties();

		try {
			prop.load(getAssets().open("config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 来自消息通知栏
		webview.loadUrl(prop.getProperty("server.url.appPerceptionFunctionList"));

		// 设置Web视图
		webview.setWebViewClient(new HelloWebViewClient());

		webview.addJavascriptInterface(new JsCallJava(this), GLOBAL_JS_VAR_NAME);
	}

	/**
	 * 设置回退
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 不做任何动作
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			return false;
		}

		return false;
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			String data = description + "&nbsp;&nbsp;&nbsp;&nbsp;<a href='" + failingUrl + "'>刷新</a>";
			view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
