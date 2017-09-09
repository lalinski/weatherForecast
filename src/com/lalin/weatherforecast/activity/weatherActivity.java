package com.lalin.weatherforecast.activity;



import java.util.Date;

import com.lalin.weatherforecast.R;
import com.lalin.weatherforecast.service.autoUpdateService;
import com.lalin.weatherforecast.util.HttpCallbackListener;
import com.lalin.weatherforecast.util.Utility;
import com.lalin.weatherforecast.util.httpUtil;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class weatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;
	private Button switchCity, refreshWeather;
	private TextView dash;
	
	private ImageView dayImage;
	private ImageView nightImage;
	
	private String weatherCode = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout2);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		dash = (TextView) findViewById(R.id.dash);
		
		dayImage = (ImageView) findViewById(R.id.dayimage);
		nightImage = (ImageView) findViewById(R.id.nightimage);
		
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			publishText.setText("synchronizing...");
			weatherInfoLayout.setVisibility(View.VISIBLE);
			cityNameText.setVisibility(View.VISIBLE);
			queryWeatherCode(countyCode);
		}
		else{
			showWeather();
		}

	}
	public void onClick(View v){
		switch(v.getId()){
			case R.id.switch_city:
				Intent intent = new Intent(this, chooseAreaActivity.class);
				intent.putExtra("from_weather_activity", true);
				startActivity(intent);
				overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
				finish();
				break;
			case R.id.refresh_weather:
				publishText.setText("synchronizing...");
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				String weatherCode_fr = prefs.getString("weather_code", "");
				if(weatherCode != null || weatherCode != "")
					weatherCode_fr = weatherCode;
				if(!TextUtils.isEmpty(weatherCode_fr)){
					Log.i("info", "refreshWeather=" + weatherCode_fr + "  weatherCode=" + weatherCode);
					queryWeatherInfo(weatherCode_fr);
					
				}
				break;
			default:
				break;
		}
	}
	private void queryWeatherCode(String countyCode){
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
		Log.i("info", "countyCode" + address);
		queryFromServer(address, "countyCode");
	}
	private void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
		Log.i("info", "weatherInfo" + address);
		queryFromServer(address, "weatherCode");
	}
	private void queryFromServer(final String address, final String type){
		httpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String [] array = response.split("\\|");
						if(array != null && array.length == 2){
				//			String weatherCode = array[1];
							weatherCode = array[1];
							Log.i("info", "array[0] = " + array[0]);
							Log.i("info", "array[1] = " + array[1]);
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Utility.handleWeatherResponse(weatherActivity.this, response);
					runOnUiThread(new Runnable(){
						public void run(){
							showWeather();
						}
					});
				}
			}
			public void onError(Exception e){
				runOnUiThread(new Runnable(){
					public void run(){
						publishText.setText("synchronization fails");
					}
				});
			}
		});
	}
				
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp2", ""));
		temp2Text.setText(prefs.getString("temp1", ""));
		dash.setVisibility(View.VISIBLE);
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		
		Date date = new Date();
		int hour = date.getHours();
		if(hour < 18)
		    publishText.setText("yesterday " + prefs.getString("publish_time", "") + " published");
		else
			publishText.setText("today " + prefs.getString("publish_time", "") + " published");
			
		System.out.println(prefs.getString("publish_time", ""));
		Log.i("info", "publish_time" + prefs.getString("publish_time", ""));
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
		String urlDay = prefs.getString("dayimage", "");
		String urlNight = prefs.getString("nightimage", "");
		if(urlDay != null && urlDay != ""){
		//	urlDay = "http://m.weather.com.cn/img/" + "b0.gif";
		//	Log.i("info", urlDay);
			urlDay = "http://m.weather.com.cn/img/b" + urlDay.substring(1);
		    new asyncImgeTask(dayImage).execute(urlDay);
		}
		if(!TextUtils.isEmpty(urlNight)){
			urlNight = "http://m.weather.com.cn/img/b" + urlNight.substring(1);
			Log.i("info", urlNight);
		    new asyncImgeTask(nightImage).execute(urlNight);
		}
		Intent intent = new Intent(this, autoUpdateService.class);
		startService(intent);
	}

}
