package com.leo.liclitelicense.asyncs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import ch.ethz.ssh2.Connection;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.staticdata.LicLiteData;
import com.leo.liclitelicense.utils.NetWorkUtil;


public class RenderInTimeResultAsyncTask extends AsyncTask<Object, Object, String>{

	private int serverBeanIndex;
	private Activity activity = null;
	private ProgressDialog bar = null;
	
	private TextView inTimeResultTextView = null;
	
	public RenderInTimeResultAsyncTask(int serverBeanIndex, Activity activity){
		this.serverBeanIndex = serverBeanIndex;
		this.activity = activity;
		
		bar = new ProgressDialog(activity);
		bar.setCancelable(true);
		bar.setMessage(activity.getResources().getString(
				R.string.load_servers_from_db));
		bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	
	
	@Override
	protected void onPreExecute() {
		bar.show();
		bar.setCancelable(true);
	}

	@Override
	protected String doInBackground(Object... params) {
		ServerBean bean = LicLiteData.serverBeanList.get(serverBeanIndex);
System.out.println("---->      " + bean.getConnection()+"    "+bean.getServerCmd());		 
		String[] outputArr = NetWorkUtil.getCmdOutputStrs(bean.getConnection(), bean.getServerCmd());
		
		
		return outputArr[0];
	}

	@Override
	protected void onPostExecute(String result) {
		inTimeResultTextView = (TextView)activity.findViewById(R.id.textview_render_in_time_result);
		inTimeResultTextView.setText(result);
		
		bar.dismiss();
	}
	
}







