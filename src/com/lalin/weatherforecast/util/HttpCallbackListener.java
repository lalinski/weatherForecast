package com.lalin.weatherforecast.util;

public interface HttpCallbackListener {
	public void onFinish(String response);
	public void onError(Exception e);
}
