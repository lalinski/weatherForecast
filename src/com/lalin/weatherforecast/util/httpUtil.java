package com.lalin.weatherforecast.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;

public class httpUtil {
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpURLConnection urlConn = null;
					try {
						URL url = new URL(address);
						urlConn = (HttpURLConnection)url.openConnection();
						urlConn.setRequestMethod("GET");
						urlConn.setConnectTimeout(8000);
						urlConn.setReadTimeout(8000);
						BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
						StringBuilder response = new StringBuilder();
						String line = new String();
						while((line = reader.readLine()) != null){
							response.append(line);
						}
						if(listener != null){
							listener.onFinish(response.toString());
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						if(listener != null)
							listener.onError(e);
					} finally {
						if(urlConn != null){
							urlConn.disconnect();
						}
					}
				}
			}).start();
	}
}
