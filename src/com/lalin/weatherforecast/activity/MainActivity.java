package com.lalin.weatherforecast.activity;



import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.lalin.weatherforecast.R;

public class MainActivity extends Activity{

	ImageView image;
	final String url = "http://www.weather.com.cn/m/i/weatherpic/29x20/d0.gif";
	//http://www.weather.com.cn/m/i/weatherpic/29x20/d0.gif
	//"http://m.weather.com.cn/img/d0.gif"
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		image = (ImageView) findViewById(R.id.imageView1); 
		
		new asyncImgeTask(image).execute(url);
	}
	
}
