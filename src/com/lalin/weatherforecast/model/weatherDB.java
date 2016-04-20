package com.lalin.weatherforecast.model;

import java.util.ArrayList;
import java.util.List;

import com.lalin.weatherforecast.db.weatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class weatherDB {
	public static final String DB_NAME = "weather.db";
	public static final int VERSION = 1;
	private static weatherDB mWeatherDB;
	private SQLiteDatabase db;
	
	public weatherDB(Context mContext){
		weatherOpenHelper dbHelper = new weatherOpenHelper(mContext, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	public synchronized static weatherDB getInstance(Context mContext){
		if(mWeatherDB == null){
			mWeatherDB = new weatherDB(mContext);
		}
		return mWeatherDB;
	}
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		//	db.execSQL("insert into Province(province_name, province_code) values(" + province.getProvinceName() + ","
		//				+ province.getProvinceCode()+")");
			
		}
	}
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cr = db.query("Province", null, null, null, null, null, null);
		if(cr != null){
			while(cr.moveToNext()){
				Province province = new Province();
				province.setId(cr.getInt(cr.getColumnIndex("_id")));
				province.setProvinceName(cr.getString(cr.getColumnIndex("province_name")));
				province.setProvinceCode(cr.getString(cr.getColumnIndex("province_code")));
				list.add(province);
			}
			cr.close();
		}
		return list;
		
	}
	public void saveCity(City city){
		if(city !=null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_Id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	public List<City> loadCities(int province_Id){
		List<City> list = new ArrayList<City>();
		Cursor cr = db.query("City", null, "province_id = ?", new String[]{String.valueOf(province_Id)}, null, null, null);
		if(cr != null){
			while(cr.moveToNext()){
				City city = new City();
				city.setId(cr.getInt(cr.getColumnIndex("_id")));
				city.setCityName(cr.getString(cr.getColumnIndex("city_name")));
				city.setCityCode(cr.getString(cr.getColumnIndex("city_code")));
				city.setProvinceId(province_Id);
				list.add(city);
			}
			cr.close();
		}
		return list;
		
	}
	public void saveCounty(County county){
		if(county !=null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_Id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	public List<County> loadCounties(int city_Id){
		List<County> list = new ArrayList<County>();
		Cursor cr = db.query("City", null, "city_id = ?", new String[]{String.valueOf(city_Id)}, null, null, null);
		if(cr != null){
			while(cr.moveToNext()){
				County county = new County();
				county.setId(cr.getInt(cr.getColumnIndex("_id")));
				county.setCountyName(cr.getString(cr.getColumnIndex("county_name")));
				county.setCountyCode(cr.getString(cr.getColumnIndex("county_code")));
				county.setCityId(city_Id);
				list.add(county);
			}
			cr.close();
		}
		return list;
		
	}
}
