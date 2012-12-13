package com.leo.liclitelicense.asyncs;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.db.LDBHelper;
import com.leo.liclitelicense.db.LGeneralDBManager;
import com.leo.liclitelicense.staticdata.LicLiteData;

public class LoadServersFromDBAsyncTask extends AsyncTask<Object, Object, ArrayList<ServerBean>>{

	private ProgressDialog bar = null;
	private LGeneralDBManager lGeneralDBManager = null;
	private ListView serverList = null;
	private SimpleAdapter serverAdapter = null;
	
	public LoadServersFromDBAsyncTask(Context context, ListView serverList) {
		
		this.serverList = serverList;
		lGeneralDBManager = new LGeneralDBManager(new LDBHelper(context, LicLiteData.GENERAL_DB));
		
		bar = new ProgressDialog(context);
		bar.setCancelable(true);
		bar.setMessage(context.getResources().getString(
				R.string.load_servers_from_db));
		bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	
	@Override
	protected void onPreExecute() {
		bar.show();
		bar.setCancelable(false);
	}

	@Override
	protected ArrayList<ServerBean> doInBackground(Object... arg0) {
		
		return lGeneralDBManager.queryServerBean(LicLiteData.TABLE_SERVERS);
	}

	@Override
	protected void onPostExecute(ArrayList<ServerBean> result) {
		//serverAdapter = new SimpleAdapter();
		
		bar.dismiss();
	}

}




