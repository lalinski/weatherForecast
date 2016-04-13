package com.lalin.weatherforecast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class weatherOpenHelper extends SQLiteOpenHelper{

	public static final String CREATE_PROVINCE = "create table if not exists Province (_id integer primary key autoincrement,"
			                                      + " province_name text, province_code text)";
	public static final String CREATE_CITY = "create table if not exists City (_id integer primary key autoincrement, city_name text,"
												+ " city_code text, province_id integer)";
	public static final String CREATE_COUNTRY = "create table if not exists Country (_id integer primary key autoincrement, country_name text,"
												+ " country_code text, city_id integer)";
	public weatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}