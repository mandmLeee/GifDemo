package com.example.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	protected static final String IMAGE_URL = "http://img.blog.csdn.net/20150410135837339";
	// protected static final String IMAGE_URL =
	// "http://img.blog.csdn.net/20150310123909933";

	protected static final String TAG = "MainActivity";
	private GifImageView myGifImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myGifImageView = (GifImageView) findViewById(R.id.myGifView);
		new AsyncTask<Void, Void, GifDrawable>() {

			@Override
			protected GifDrawable doInBackground(Void... params) {
				byte[] gifbyte = null;
				HttpURLConnection conn = null;
				try {
					URL url = new URL(IMAGE_URL);
					conn = (HttpURLConnection) url.openConnection();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					InputStream in = conn.getInputStream();
					if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
						// 连接不成功
						Log.i(TAG, "连接不成功");
						return null;
					}

					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					gifbyte = out.toByteArray();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					conn.disconnect();
				}

				// 写入文件
				/*FileOutputStream fos = null;
				try {

					File root = Environment.getExternalStorageDirectory();
					File myFile = new File(root, "test.gif");
					Log.v(TAG, myFile.getAbsolutePath());
					fos = new FileOutputStream(myFile);
					fos.write(gifbyte);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}*/

				GifDrawable gifDrawable = null;
				try {
					gifDrawable = new GifDrawable(gifbyte);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return gifDrawable;
			}

			protected void onPostExecute(GifDrawable drawable) {

				myGifImageView.setImageDrawable(drawable);
			};

		}.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
