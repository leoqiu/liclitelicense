package com.leo.liclitelicense.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import ch.ethz.ssh2.Connection;

import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.staticdata.LicLiteData;


public class UIUtil{
	
	/**
	 * show a toast
	 * 
	 * @param msg
	 * @param context
	 */
	public static void showToast(String msg, Context context){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	public static boolean isServerExist(String serverName, String lmgrdCmd){
		boolean isExist = false;
		
		for(ServerBean serverBean : LicLiteData.serverBeanList){
			if(serverBean.getServerName().equals(serverName) && serverBean.getServerCmd().equals(lmgrdCmd)){
				isExist = true;
				break;
			}
		}

		return isExist;
	}
	
	/**
	 * logout a specific server, and close its connection
	 * 
	 * @param position
	 */
//	public static void logOutTheServer(int position){
//		ServerBean serverBean = LicLiteData.serverBeanList.get(position);
//		serverBean.setIsLogin(LicLiteData.IS_NOT_LOGINED);
//		serverBean.getConnection().close();
//		serverBean.setConnection(null);
//	}
	
	/**
	 * close all the ssh connections and
	 * clear LicLiteData.serverBeanList
	 * 
	 */
//	public static void dispose(Context context){
//		//stop service
//		
//		
//		int size = LicLiteData.serverBeanList.size();
//		Connection connection;
//		ServerBean serverBean;
//		for(int i = 0; i < size; i++){
//			serverBean = LicLiteData.serverBeanList.get(i);
//			connection = serverBean.getConnection();
//			if(connection != null){
//				//isLogin control the service runnable
//				serverBean.setIsLogin(LicLiteData.IS_NOT_LOGINED);
//				serverBean.getConnection().close();
//				serverBean.setConnection(null);
//			}
//		}
//		LicLiteData.serverBeanList.clear();
//		stopAService(context);
//	}
	
	/**
	 * 
	 * @param context
	 */
//	public static void stopAService(Context context){
//		
//		context.stopService(new Intent(context, LicSenseLiteService.class));
//	}
	
	/**
	 * 
	 *  Before save new server info, check if this server 
	 *  already exist in server list
	 * 
	 * @param serverName
	 * @return
	 */
//	public static boolean isServerExist(String serverName){
//		boolean isExist = false;
//		
//		for(ServerBean serverBean : LicLiteData.serverBeanList) {
//			if(serverBean.getServerName().equals(serverName)){
//				isExist = true;
//				break;
//			}
//		}
//		
//		
//		return isExist;
//	}
}
