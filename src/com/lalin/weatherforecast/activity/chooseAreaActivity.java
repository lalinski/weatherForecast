package com.lalin.weatherforecast.activity;


import java.util.ArrayList;
import java.util.List;

import com.lalin.weatherforecast.R;
import com.lalin.weatherforecast.model.City;
import com.lalin.weatherforecast.model.County;
import com.lalin.weatherforecast.model.Province;
import com.lalin.weatherforecast.model.weatherDB;
import com.lalin.weatherforecast.util.HttpCallbackListener;
import com.lalin.weatherforecast.util.Utility;
import com.lalin.weatherforecast.util.httpUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class chooseAreaActivity extends Activity{
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	private weatherDB db;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	private Province selectedProvince;
	private City selectedCity;
	private int currentLevel;
	private boolean isFromWeatherActivity;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean("city_selected", false) && ! isFromWeatherActivity){
			Intent intent = new Intent(this, weatherActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
			finish();
			return;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		listviewAnimation();
		db = weatherDB.getInstance(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(position);
					queryCities();
				}else if(currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(position);
					queryCounties();
				}else if(currentLevel == LEVEL_COUNTY){
					String countyCode = countyList.get(position).getCountyCode();
					Intent intent = new Intent(chooseAreaActivity.this, weatherActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
				//	finish();
				}
			}
		});
		queryProvinces();
	//	queryCounties();
	}
	private void queryProvinces(){
	//	queryFromServer(null, "province");
		provinceList = db.loadProvinces();
		if(provinceList.size() > 0){
			dataList.clear();
			for(Province p : provinceList){
				dataList.add(p.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listviewAnimation();
			listView.setSelection(0);
			titleText.setText("ÖÐ¹ú");
			currentLevel = LEVEL_PROVINCE;
		}else{
			queryFromServer(null, "province");
		}
		
	}
	private void queryCities(){
		cityList = db.loadCities(selectedProvince.getId());
		if(cityList.size() > 0){
			dataList.clear();
			for(City p : cityList){
				dataList.add(p.getCityName());
			}
			adapter.notifyDataSetChanged();
			listviewAnimation();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
		
	}
	private void queryCounties(){
	//	queryFromServer("0501", "county");
		
		countyList = db.loadCounties(selectedCity.getId());
		if(countyList.size() > 0){
			dataList.clear();
			for(County p : countyList){
				dataList.add(p.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listviewAnimation();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		}else{
			queryFromServer(selectedCity.getCityCode(), "county");
		//	queryFromServer("0501", "county");
		}
		
	}
	private void queryFromServer(final String code, final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}
		else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		httpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
		//		Log.i("info", response);
		//		System.out.println(response);
				// TODO Auto-generated method stub
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvincesResponse(db, response);
				}else if("city".equals(type)){
					result = Utility.handleCitiesResponse(db, response, selectedProvince.getId());
				}else if("county".equals(type)){
					result = Utility.handleCountiesResponse(db, response, selectedCity.getId());
				}
				if(result){
					closeProgressDialog();
					runOnUiThread(new Runnable(){
						public void run(){
					//		closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(chooseAreaActivity.this, "loading fails", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("loading.....");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	private void closeProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
		progressDialog.show();
	}
	public void onBackPressed(){
		if(currentLevel == LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			if(isFromWeatherActivity){
				Intent intent = new Intent(this, weatherActivity.class);
			//	overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
			}
			finish();
		}
	}
	public void listviewAnimation(){
		LayoutAnimationController lac=new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
	    lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
	    lac.setDelay(0.05f);
	    listView.setLayoutAnimation(lac);
	    listView.startLayoutAnimation();
	}
}
class animationOfList extends LayoutAnimationController{

	public animationOfList(Animation animation, float delay) {
		super(animation, delay);
		// TODO Auto-generated constructor stub
	}

	public animationOfList(Animation animation) {
		super(animation);
		// TODO Auto-generated constructor stub
	}

	public animationOfList(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected long getDelayForView(View view) {
		// TODO Auto-generated method stub
		return super.getDelayForView(view);
	}
	
}