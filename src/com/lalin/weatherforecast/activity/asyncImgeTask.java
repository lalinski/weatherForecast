package com.lalin.weatherforecast.activity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class asyncImgeTask extends AsyncTask<String, Void, Bitmap>{
	
	ImageView image;
	public asyncImgeTask(ImageView image) {
		// TODO Auto-generated constructor stub
		this.image = image;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		Bitmap  bitmap = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		URLConnection conn;
		try {
			conn =  new URL(url).openConnection();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				is.close();
				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(image != null)
			image.setImageBitmap(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	

}
