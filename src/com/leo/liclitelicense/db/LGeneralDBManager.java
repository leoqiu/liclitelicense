package com.leo.liclitelicense.db;


import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leo.liclitelicense.beans.AutoWordBean;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.staticdata.LicLiteData;

/**
 * LDBManager encapsulates all the db related
 * operations
 * 
 * @author leo
 *
 */
public class LGeneralDBManager {

	private SQLiteDatabase dbReadable;
	private SQLiteDatabase dbWritable;
	
	public LGeneralDBManager(LDBHelper dbHelper){
		dbReadable = dbHelper.getReadableDatabase();
		dbWritable = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Check if a table exist or not
	 * 
	 * @param tableName
	 * @return boolean
	 */
	private boolean isTableExist(String tableName){
		boolean isExist = false;

		Cursor cursor = null;
		try {
			cursor = dbReadable.rawQuery("select count(*) from "+ tableName, null);
			isExist = true;
		} catch (Exception e) {
			isExist = false;
		} finally {
			if(cursor != null)
				cursor.close();
		}
		
		return isExist;
	}
	
	/**
	 * Create table - servers
	 */
	private void createTableServers() {
		
		String sqlServers = "CREATE TABLE " + LicLiteData.TABLE_SERVERS + 
					       " (_id  INTEGER  PRIMARY KEY, " +
						   LicLiteData.SERVER_NAME +" varchar(30), " +
						   LicLiteData.SERVER_CMD + " varchar(30), " +
						   LicLiteData.SERVER_PORT + " varchar(2), " +
						   LicLiteData.SERVER_LOC + " varchar(30), " +
						   LicLiteData.SERVER_TIME_ZONE + " varchar(20), " +
						   LicLiteData.SERVER_TIME_OUT + " varchar(3), " +
						   LicLiteData.SERVER_RETRY_TIMES + " varchar(3), " +
						   LicLiteData.TIME_STAMP + " varchar(30))";
		
		dbReadable.execSQL(sqlServers);
	}
	
	/**
	 * Create table - auto_word
	 */
	private void createTableAutoWord(){
		String sqlAutoWord = "CREATE TABLE " + LicLiteData.TABLE_AUTO_WORD + 
				" (_id INTEGER PRIMARY KEY, " +
				LicLiteData.AUTO_SERVER_NAME + " varchar(15), " +
				LicLiteData.AUTO_SERVER_CMD +" varchar(15))";
		
		dbReadable.execSQL(sqlAutoWord);
	}
	
	
	/**
	 * Insert a server bean intgho the table - servers
	 * @param serverBean
	 */
	public long insertIntoAutoWord(AutoWordBean autoWordBean){
		
		if(!isTableExist(LicLiteData.TABLE_AUTO_WORD)){
			createTableAutoWord();
			System.out.println("create table serves...");
		}
		
		ContentValues values = new ContentValues();	
		values.put(LicLiteData.AUTO_SERVER_NAME, autoWordBean.getAutoServerName());
		values.put(LicLiteData.AUTO_SERVER_CMD, autoWordBean.getAutoServerCmd());
		
		long isInserted = dbWritable.insert(LicLiteData.TABLE_AUTO_WORD, null, values);
		return isInserted;
	}
	
	/**
	 * Insert a server bean intgho the table - servers
	 * @param serverBean
	 */
	public long insertIntoServers(ServerBean serverBean){
		
		if(!isTableExist(LicLiteData.TABLE_SERVERS)){
			createTableServers();
			System.out.println("create table serves...");
		}
		
		ContentValues values = new ContentValues();
		
		values.put(LicLiteData.SERVER_NAME, serverBean.getServerName());
		values.put(LicLiteData.SERVER_CMD, serverBean.getServerCmd());
		values.put(LicLiteData.SERVER_PORT, serverBean.getServerPort());
		values.put(LicLiteData.SERVER_LOC, serverBean.getServerLoc());
		values.put(LicLiteData.SERVER_TIME_ZONE, serverBean.getServerTimeZone());
		values.put(LicLiteData.SERVER_TIME_OUT, serverBean.getTimeOut());
		values.put(LicLiteData.SERVER_RETRY_TIMES, serverBean.getRetryTimes());
		values.put(LicLiteData.TIME_STAMP, serverBean.getTimeStamp());
		
		long isInserted = dbWritable.insert(LicLiteData.TABLE_SERVERS, null, values);
		return isInserted;
	}

	/**
	 * delete a row by serverName from database
	 * 
	 * @param serverName
	 */
	public int delete(String serverName, String serverCmd){
		return dbWritable.delete("servers", LicLiteData.SERVER_NAME+"=? AND " + LicLiteData.SERVER_CMD + "=?"
				, new String[]{serverName, serverCmd});
	}
	
	public ArrayList<AutoWordBean> queryAutoWordBean(String tableAutoWord){
		ArrayList<AutoWordBean> autoWordBeans = new ArrayList<AutoWordBean>();
		
		//if no such table return null
		if(!isTableExist(tableAutoWord)){
			System.out.println("table doesn't exist..");
			return null;
		}
		
		Cursor cursor = dbReadable.query(LicLiteData.TABLE_AUTO_WORD, 
				new String[]{LicLiteData.AUTO_SERVER_NAME,LicLiteData.AUTO_SERVER_CMD}, 
				null, null, null, null, null);
		
		while(cursor.moveToNext()){
			String autoWordName = cursor.getString(cursor.getColumnIndex(LicLiteData.AUTO_SERVER_NAME));
			String autoWordCmd = cursor.getString(cursor.getColumnIndex(LicLiteData.AUTO_SERVER_CMD));

			AutoWordBean autoWordBean = new AutoWordBean(autoWordName, autoWordCmd);
			autoWordBeans.add(autoWordBean);
			
		}
		
		return autoWordBeans;
	}
	
	/**
	 * Query all the rows from database and return a 
	 * array list of @ServerBean
	 * 
	 * @return ArrayList<ServerBean>
	 */
	public ArrayList<ServerBean> queryServerBean(String tableServer){
		
		ArrayList<ServerBean> serverBeans = new ArrayList<ServerBean>();
		
		//if no such table return null
		if(!isTableExist(tableServer)){
			System.out.println("table doesn't exist..");
			return null;
		}
		
		//Cursor cursor = db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
		//Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		//Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		Cursor cursor = dbReadable.query(LicLiteData.TABLE_SERVERS, 
				new String[]{LicLiteData.SERVER_NAME,LicLiteData.SERVER_CMD, LicLiteData.SERVER_PORT, 
				LicLiteData.SERVER_LOC, LicLiteData.SERVER_TIME_ZONE, LicLiteData.SERVER_TIME_OUT, 
				LicLiteData.SERVER_RETRY_TIMES, LicLiteData.TIME_STAMP}, null, null, null, null, LicLiteData.TIME_STAMP);
		while(cursor.moveToNext()){
			String serverName = cursor.getString(cursor.getColumnIndex(LicLiteData.SERVER_NAME));
			String serverCmd = cursor.getString(cursor.getColumnIndex(LicLiteData.SERVER_CMD));
			String serverPort = cursor.getString(cursor.getColumnIndex(LicLiteData.SERVER_PORT));
			String serverLoc = cursor.getString(cursor.getColumnIndex(LicLiteData.SERVER_LOC));
			String serverTimeZone = cursor.getString(cursor.getColumnIndex(LicLiteData.SERVER_TIME_ZONE));
			String timeOut = cursor.getString(cursor.getColumnIndex(LicLiteData.SERVER_TIME_OUT));
			String retryTimes = cursor.getString(cursor.getColumnIndex(LicLiteData.SERVER_RETRY_TIMES));
			String timeStamp = cursor.getString(cursor.getColumnIndex(LicLiteData.TIME_STAMP));

			ServerBean serverBean = new ServerBean(serverName, serverCmd,
					serverPort, serverLoc, serverTimeZone, timeOut, retryTimes, timeStamp);
			
			serverBeans.add(serverBean);
			
		}
		return serverBeans;
	}
	
	
	
	/**
	 * close db connection after db opration
	 */
	public void close(){
		if(dbReadable != null && dbReadable.isOpen()){
			dbReadable.close();
			dbReadable = null;
		}
		
		if(dbWritable != null && dbWritable.isOpen()){
			dbWritable.close();
			dbWritable = null;
		}
	}

}











