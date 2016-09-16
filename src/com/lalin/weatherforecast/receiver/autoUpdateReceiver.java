package com.lalin.weatherforecast.receiver;

import com.lalin.weatherforecast.service.autoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class autoUpdateReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent intent2 = new Intent();
		intent2.setClass(context, autoUpdateService.class);
	}

}
