package com.leo.liclitelicense.utils;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;
import ch.ethz.ssh2.Connection;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.adapters.ServerListSimpleAdapter;
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
	public static void logOutTheServer(int position, ServerListSimpleAdapter serverListSimpleAdapter){
		ServerBean serverBean = LicLiteData.serverBeanList.get(position);
		serverBean.setIsLogin(LicLiteData.IS_NOT_LOGINED);
		serverBean.getConnection().close();
		serverBean.setConnection(null);
		
		//notify ServerListSimpleAdapter data set changed
		ServerListSimpleAdapter
				.getMyData()
				.get(position)
				.put("item_server_pic", String.valueOf(R.drawable.server_not_login));
    	serverListSimpleAdapter.notifyDataSetChanged();
	}
	
	/**
	 * close all the ssh connections and
	 * clear LicLiteData.serverBeanList
	 * 
	 */
	public static void dispose(){
		//stop service
		
		
		int size = LicLiteData.serverBeanList.size();
		Connection connection;
		ServerBean serverBean;
		for(int i = 0; i < size; i++){
			serverBean = LicLiteData.serverBeanList.get(i);
			connection = serverBean.getConnection();
			if(connection != null){
				//isLogin control the service runnable
				serverBean.setIsLogin(LicLiteData.IS_NOT_LOGINED);
				serverBean.getConnection().close();
				serverBean.setConnection(null);
			}
		}
		LicLiteData.serverBeanList.clear();
		LicLiteData.autoServerNameSet.clear();
		LicLiteData.autoLmgrdCmdSet.clear();
//		stopAService(context);
	}
	
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
	
	
    /**
     * This is a recursive method.
     * If file is found, total file size is calculated.
     * If it is a folder, we recurse further.
     */
    public static double getFileSize(File folder) {
    	
    	DecimalFormat fmt =new DecimalFormat("#.##");
    	
        long fileSizeByte = 0;
 
        File[] filelist = folder.listFiles();
        for (int i = 0; i < filelist.length; i++) {
            if (filelist[i].isDirectory()) {
            	fileSizeByte += getFileSize(filelist[i]);
            } else {
            	fileSizeByte += filelist[i].length();
            }
        }
        
        double fileSizeMB=Double.valueOf(fmt.format(fileSizeByte /(1024*1024)));
 
        return fileSizeMB;
    }
    
    /**
     * 
     * @param activity
     */
    
	public static void listAllFileNames(){
		File folder = new File(LicLiteData.licLiteDataDir);
		File[] listOfFiles = folder.listFiles();  
		int size = listOfFiles.length;
		
		
		for(int i = 0; i < size; i ++){
			System.out.println("file name --> " + listOfFiles[i]);
		}
		
		for(int j = 0; j < LicLiteData.NUMBER_DATA_FILE_TO_BE_DELETE; j++){
			System.out.println("file deleted-> " + listOfFiles[j].delete());
		}
		
	}
	
	public static void createDataDir(Activity activity){
		//check if LicLiteData.licLiteDataDir exists if not create one accordingly
System.out.println("hahahah --->  "+activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
System.out.println("sdcard mounted....");
			LicLiteData.licLiteDataDir = Environment.getExternalStorageDirectory().toString()
					+ File.separator + LicLiteData.DIR;
			File licliteDataDir = new File(LicLiteData.licLiteDataDir);		
			if(!licliteDataDir.exists()){
				licliteDataDir.mkdirs();
System.out.println("create data folder...");
			}
		}
		
System.out.println("sdcard unmounted....");
	}
	
}



