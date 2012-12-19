package com.leo.liclitelicense.listenerimpl;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import ch.ethz.ssh2.Connection;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.adapters.ServerListSimpleAdapter;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.staticdata.LicLiteData;
import com.leo.liclitelicense.utils.DBUtil;
import com.leo.liclitelicense.utils.UIUtil;

public class ServerListOnItemLongClickListenerImpl implements OnItemLongClickListener{

	private Context context = null; 
	private ServerListSimpleAdapter serverListSimpleAdapter = null;
	
	private ServerBean serverBean = null;
	
	public ServerListOnItemLongClickListenerImpl(Context context, ServerListSimpleAdapter serverListSimpleAdapter){
		this.context = context;
		this.serverListSimpleAdapter = serverListSimpleAdapter;
	}
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
			long id) {

		final int clickedPos = position;
		serverBean = LicLiteData.serverBeanList.get(clickedPos);
		final String serverName = serverBean.getServerName();
		final String serverCmd = serverBean.getServerCmd();
		
		
		if(serverBean.getIsLogin() == LicLiteData.IS_LOGINED){
						
			new AlertDialog.Builder(context)
	        .setTitle(R.string.on_long_click_title)
	        .setItems(R.array.list_menu_login_not_has_history_items, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                /* logout*/
	            	if(which == 0){
	            		/* delete*/
	            		UIUtil.logOutTheServer(clickedPos, serverListSimpleAdapter); 
	            	}else if(which == 1){
	            		deleteFromList(clickedPos, serverName, serverCmd);		
	            	}
	            	
	            }
	        }).create().show();
		}else if(serverBean.getIsLogin() == LicLiteData.IS_NOT_LOGINED){
			new AlertDialog.Builder(context)
	        .setTitle(R.string.on_long_click_title)
	        .setItems(R.array.list_menu_not_login_not_has_history_items, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                /* delete*/
	            	deleteFromList(clickedPos, serverName, serverCmd);
	            }
	        }).create().show();
		}
		

		
		
		return false;
	}
	
	
	/**
	 * delete server from adapter, server bean list and db
	 * 
	 * @param clickedPos
	 * @param serverName
	 * @param serverCmd
	 */
	private void deleteFromList(int clickedPos, String serverName, String serverCmd){
    	ServerListSimpleAdapter.updateMyData(clickedPos);
    	serverListSimpleAdapter.notifyDataSetChanged();
 
    	//close ssh connection if not null
    	Connection conn = LicLiteData.serverBeanList.get(clickedPos).getConnection();
    	if(conn != null){
    		conn.close();
    		conn = null;
    	}
    	//del LicLiteData.serverBeanList
    	LicLiteData.serverBeanList.remove(clickedPos);
    	
    	//del from db
    	DBUtil.deleteServerFromDB(context, serverName, serverCmd);
	}

}













