package com.lalin.weatherforecast.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Build;
import android.util.Log;

public class httpUtil {
/*	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpURLConnection urlConn = null;
				//	URLConnection urlConn = null;
					try {
						URL url = new URL(address);
						urlConn = (HttpURLConnection)url.openConnection();
					//	urlConn = (URLConnection)url.openConnection();
						
						urlConn.setConnectTimeout(8000);
						urlConn.setReadTimeout(8000);
						BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
						//StringBuilder is faster than StringBuffer, but it is not thread safe
						StringBuilder response = new StringBuilder();
						String line = new String();
						char[] b = new char[1 * 1024];
						int len;
						while((len = reader.read(b)) != -1){
							response.append(b, 0, len);
						}
				//		while((line = reader.readLine()) != null){
				//			response.append(line);
				//		}
						Log.i("info", response.toString());
						if(listener != null){
							listener.onFinish(response.toString());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.i("info", e.getMessage(),e);
						if(listener != null)
							listener.onError(e);
					} finally{
						if(urlConn != null)
							urlConn.disconnect();
					}
				}
			}).start();
	}*/
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		myRunnable runnable = new myRunnable(address, listener); 
		Thread t = new Thread(runnable);
		t.setPriority(1);
		t.start();
	}
}
class myRunnable implements Runnable{
		private String address;
		private HttpCallbackListener listener;
	

        public myRunnable(final String address, final HttpCallbackListener listener){
	        this.address = address;
	        this.listener = listener;
        }
@Override
        public void run() {
			
			HttpURLConnection urlConn = null;
			try {
		//		URL url = new URL(address);
		//		urlConn = (HttpURLConnection)url.openConnection();
		//		urlConn.setRequestMethod("GET");
			//	urlConn.setConnectTimeout(8000);
			//	urlConn.setReadTimeout(8000);
		//		BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
				//StringBuilder is faster than StringBuffer, but it is not thread safe
		//		StringBuilder response = new StringBuilder();
				String response;
				String line = new String();
				char[] b = new char[1 * 1024];
				int len;
		//		while((len = reader.read(b)) != -1){
		//			response.append(b, 0, len);
		//		}
				response = runByHttpClient();
				Log.i("info", response.toString());
				if(listener != null){
					listener.onFinish(response.toString());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.i("info", e.getMessage(),e);
				if(listener != null)
					listener.onError(e);
			} finally{
				if(urlConn != null)
					urlConn.disconnect();
			}
	}
	public String runByHttpClient() throws Exception{
		
		HttpGet  httpGet = new HttpGet(address);
		HttpClient client = new DefaultHttpClient();
		String content = null;
		HttpResponse response = client.execute(httpGet);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			content = EntityUtils.toString(response.getEntity(),"utf-8");
		}
		return content;
		
	
	}
}

