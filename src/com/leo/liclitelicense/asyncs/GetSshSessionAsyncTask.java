package com.leo.liclitelicense.asyncs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import ch.ethz.ssh2.Connection;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.adapters.ServerListSimpleAdapter;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.networks.SshManager;
import com.leo.liclitelicense.staticdata.LicLiteData;
import com.leo.liclitelicense.utils.UIUtil;

public class GetSshSessionAsyncTask extends AsyncTask<Void, Void, Boolean>{

	private SshManager sshManager = null;
	private int position = -999;
	private Context context = null;
	private ServerListSimpleAdapter serverListSimpleAdapter = null;
	
	private ProgressDialog bar = null;
	
	public GetSshSessionAsyncTask(SshManager sshManager,
			ServerListSimpleAdapter serverListSimpleAdapter, int position,
			Context context) {
		this.sshManager = sshManager;
		this.serverListSimpleAdapter = serverListSimpleAdapter;
		this.position = position;
		this.context = context;
		
		bar = new ProgressDialog(this.context);
		bar.setCancelable(true);
		bar.setMessage(LicLiteData.LOGIN_IN_PROGRESS);
		bar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		bar.show();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		Boolean isLogin = false;
		
		ServerBean serverBean = LicLiteData.serverBeanList.get(position);
		int timeOut = Integer.valueOf(serverBean.getTimeOut()) * 1000;
		
		Connection connection = sshManager.getAuthenticatedSshConn(timeOut);
		
		//connection != null, means login successfully
		if(connection != null){
			isLogin = true;
			//set static serverBeanList
			serverBean.setConnection(connection);
			serverBean.setIsLogin(LicLiteData.IS_LOGINED);
			
			//start service

		}	
System.out.println("connection is --->  timeout is ----> " + connection + "  " + timeOut );		
		return isLogin;

	}

	/**
	 * We can not update main thread in doInBackgroud, do it in onPostExecute
	 */
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(result.equals(true)){
			//notify ServerListSimpleAdapter data set changed
	    	ServerListSimpleAdapter.getMyData().get(position).put("item_server_pic", String.valueOf(R.drawable.server_login));   
	    	serverListSimpleAdapter.notifyDataSetChanged();
	    	
			UIUtil.showToast(context.getResources().getString(
					R.string.login_successfully),  context);
		}else
			UIUtil.showToast(context.getResources().getString(
					R.string.login_failed),  context);

		bar.dismiss();
	}

}


