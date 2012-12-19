package com.leo.liclitelicense.utils;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.leo.liclitelicense.beans.AutoWordBean;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.db.LDBHelper;
import com.leo.liclitelicense.db.LGeneralDBManager;
import com.leo.liclitelicense.staticdata.LicLiteData;

public class DBUtil {

	private static LGeneralDBManager lGeneralDBManager = null;
	
	/**
	 * Insert serverBean and autoWordBean into db
	 * 
	 * LGeneralDBManager is like a session, 
	 * create a new one -> use it -> recycle it
	 * 
	 * @param serverBean
	 * @param autoWordBean
	 * @param context
	 * @return boolean
	 */					
	public static boolean insertBeansIntoDBAndServerList(ServerBean serverBean, AutoWordBean autoWordBean, 
			ArrayAdapter<String> autoServerNameAdapter, ArrayAdapter<String> autoServerCmdAdapter, Context context){
		boolean isInserted = false;
		//insert server bean into table - servers
		// update static arraylist and db
		LicLiteData.serverBeanList.add(serverBean);
		LicLiteData.autoServerNameSet.add(autoWordBean.getAutoServerName());
		LicLiteData.autoLmgrdCmdSet.add(autoWordBean.getAutoServerCmd());
		autoServerNameAdapter.notifyDataSetChanged();
		autoServerCmdAdapter.notifyDataSetChanged();
		lGeneralDBManager = new LGeneralDBManager(new LDBHelper(context, LicLiteData.GENERAL_DB));
		if ((lGeneralDBManager.insertIntoServers(serverBean) != -1) && (lGeneralDBManager.insertIntoAutoWord(autoWordBean) != -1)) {
			isInserted = true;
			lGeneralDBManager.close();
		} else {
			// throw exception....
		}
		
		return isInserted;
	}
	
	/**
	 * 
	 * @param context
	 * @return ArrayList<ServerBean>
	 */
	public static ArrayList<ServerBean> loadServerFromDB(Context context) {
		lGeneralDBManager = new LGeneralDBManager(new LDBHelper(context, LicLiteData.GENERAL_DB));
		ArrayList<ServerBean> serverBeans = lGeneralDBManager.queryServerBean(LicLiteData.TABLE_SERVERS);
		
		lGeneralDBManager.close();
		return serverBeans;
	}
	
	/**
	 * initialize auto edit text adapter
	 */
	public static void initializeAutoArrayList(Context context){
		lGeneralDBManager = new LGeneralDBManager(new LDBHelper(context, LicLiteData.GENERAL_DB));
		ArrayList<AutoWordBean> autoWordBeans = lGeneralDBManager.queryAutoWordBean(LicLiteData.TABLE_AUTO_WORD);
		
		if(autoWordBeans != null){
			for(AutoWordBean autoWordBean : autoWordBeans){
				LicLiteData.autoServerNameSet.add(autoWordBean.getAutoServerName());
				LicLiteData.autoLmgrdCmdSet.add(autoWordBean.getAutoServerCmd());
			}
		}
		
		lGeneralDBManager.close();
	}
	
	/**
	 * delete a specific row from table - servers
	 * 
	 * @param context
	 * @param serverName
	 * @param serverCmd
	 */
	public static void deleteServerFromDB(Context context, String serverName, String serverCmd){
		lGeneralDBManager = new LGeneralDBManager(new LDBHelper(context, LicLiteData.GENERAL_DB));
		if(lGeneralDBManager.delete(serverName, serverCmd) == 1){
			//
		}else{
			//throw exception
		}
	}
}




