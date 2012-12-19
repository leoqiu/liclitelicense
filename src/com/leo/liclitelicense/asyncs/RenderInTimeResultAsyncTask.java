package com.leo.liclitelicense.asyncs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leo.liclitelicense.R;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.leo.liclitelicense.beans.LicLiteServerInfoBean;
import com.leo.liclitelicense.beans.LicLiteUserInfoBean;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.fragments.RenderServersWithUsersFragment;
import com.leo.liclitelicense.fragments.RenderServersWithoutUsersFragment;
import com.leo.liclitelicense.staticdata.LicLiteData;
import com.leo.liclitelicense.utils.NetWorkUtil;
import com.leo.liclitelicense.utils.Parser;


/**
 * AsyncTask<Param, Param, Param>
 * 
 * #1. param passed into doInBackground
 * #2. param passed into onProgressUpdate
 * #3. param returned by doInBackground
 * 
 * #4  onProgressUpdate(Integer... values), pass a Integer array
 * @author shqiu
 *
 */

public class RenderInTimeResultAsyncTask extends AsyncTask<Object, Integer, String> {
	
	private ProgressDialog progress = null;
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);		
		
		if(values[0] == LicLiteData.PARSING_DATA){
			progress.setMessage("Parsinging data ...");
		}
		
		if(values[0] == LicLiteData.ITERATING_DATA){
			progress.setMessage("Iterating data ...");
		}
		
		if(values[0] > 0)
			progress.incrementProgressBy(values[0]);
	}

// private memebers
	private RenderServersWithoutUsersFragment renderServersWithoutUsersFragment = null;
	private RenderServersWithUsersFragment renderServersWithUsersFragment = null;
	private GridView gridviewToolbar = null;
	private int loginIndex = -1;
	
	// simpler adapter list
	List<Map<String, String>> licliteWithoutAdapterList = new ArrayList<Map<String, String>>();
//	List<Map<String, String>> licliteWithGroupAdapterList = new ArrayList<Map<String, String>>();
//	List<ArrayList<HashMap<String, String>>> licliteWithChildAdapterList = new ArrayList<ArrayList<HashMap<String, String>>>();
	List<Map<String, Object>> licliteWithAdapterList = new ArrayList<Map<String, Object>>();
	
	
	public RenderInTimeResultAsyncTask(RenderServersWithoutUsersFragment 
			renderServersWithoutUsersFragment, RenderServersWithUsersFragment renderServersWithUsersFragment,
			GridView gridviewToolbar, ProgressDialog progress, int loginIndex) {
		
		this.renderServersWithoutUsersFragment = renderServersWithoutUsersFragment;
		this.renderServersWithUsersFragment = renderServersWithUsersFragment;
		this.gridviewToolbar = gridviewToolbar;
		this.progress = progress;
		this.loginIndex = loginIndex;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected String doInBackground(Object... params) {
		
		//download raw data from server
		ServerBean serverBean = LicLiteData.serverBeanList.get(loginIndex);
		NetWorkUtil.executeSCPCmd(serverBean.getConnection(), serverBean.getServerCmd());
		this.publishProgress(LicLiteData.LOADING_PROGRESS_DOWNLOAD_DATA_FROM_SERVER_TIME);
		
		//parsing data locally
		this.publishProgress(LicLiteData.PARSING_DATA);
		List<LicLiteServerInfoBean> licliteServerInfoList = Parser.parseDownloadDataFile();
		this.publishProgress(LicLiteData.LOADING_PROGRESS_PARSING_TIME);

		//iterating data locally
		this.publishProgress(LicLiteData.ITERATING_DATA);
		int size = licliteServerInfoList.size();
		LicLiteServerInfoBean licLiteWitUserInfoBeanInside = null;
		for (int i = 0; i < size; i++) {
			// if without user info
			licLiteWitUserInfoBeanInside = licliteServerInfoList.get(i);
			if (licLiteWitUserInfoBeanInside.getLicLiteUserInfoBeanList()
					.size() == 0) {
				// System.out.println(licLiteWitUserInfoBeanHasNon.toString());
				Map<String, String> map = new HashMap<String, String>();
				map.put("server_feature_name_value",
						licLiteWitUserInfoBeanInside.getFeatureName());
				map.put("server_feature_num_issue_value",
						licLiteWitUserInfoBeanInside.getFeatureNumIssued());
				map.put("server_feature_num_in_used_value",
						licLiteWitUserInfoBeanInside.getFeatureNumInUsed());
				licliteWithoutAdapterList.add(map);
			} else { // if with user info expandable list
				
				Map<String, Object> withUsersMap = new HashMap<String, Object>();
				withUsersMap.put("server_feature_name_value",licLiteWitUserInfoBeanInside.getFeatureName());
				withUsersMap.put("server_feature_num_issue_value",licLiteWitUserInfoBeanInside.getFeatureNumIssued());
				withUsersMap.put("server_feature_num_in_used_value",licLiteWitUserInfoBeanInside.getFeatureNumInUsed());
				withUsersMap.put("user_array_list", licLiteWitUserInfoBeanInside.getLicLiteUserInfoBeanList());
				licliteWithAdapterList.add(withUsersMap);
				
			}
		}
		this.publishProgress(LicLiteData.LOADING_PROGRESS_ITERATING_TIME);
		
//		System.out.println("how many without items -> "
//				+ licliteWithoutAdapterList.size());
//		System.out.println("how many with items -> "
//				+ licliteWithGroupAdapterList.size());
//		System.out.println("how many items -> " + licliteServerInfoList.size());

		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progress.dismiss();
		this.gridviewToolbar.setVisibility(View.VISIBLE);
LicLiteData.endTime = (int) (System.currentTimeMillis() / 1000);
System.out.println("time cost is ----> "
+ (LicLiteData.endTime - LicLiteData.startTime));

		//initialize renderServersWithoutUsersFragment listview
		SimpleAdapter simplerAdapterHasNoUsers = new SimpleAdapter(
				this.gridviewToolbar.getContext(), licliteWithoutAdapterList,
				R.layout.item_server_info_listview, new String[] {
						"server_feature_name_value",
						"server_feature_num_issue_value",
						"server_feature_num_in_used_value" }, new int[] {
						R.id.server_feature_name_value,
						R.id.server_feature_num_issue_value,
						R.id.server_feature_num_in_used_value });
//		this.renderServersWithoutUsersFragment
//				.setLicliteWithoutAdapterList(licliteWithoutAdapterList);
		this.renderServersWithoutUsersFragment
				.setSimplerAdapterHasNoUsers(simplerAdapterHasNoUsers);
		this.renderServersWithoutUsersFragment.getServerWithoutListView()
				.setAdapter(simplerAdapterHasNoUsers);
		this.renderServersWithoutUsersFragment.getAutoCompleteTextViewWithoutUsers().setAdapter(simplerAdapterHasNoUsers);
		
		//initialize renderServersWithUsersFragment  
		SimpleAdapter simplerAdapterHasUsers = new SimpleAdapter(
				this.gridviewToolbar.getContext(), licliteWithAdapterList,
				R.layout.item_server_info_listview, new String[] {
						"server_feature_name_value",
						"server_feature_num_issue_value",
						"server_feature_num_in_used_value" }, new int[] {
						R.id.server_feature_name_value,
						R.id.server_feature_num_issue_value,
						R.id.server_feature_num_in_used_value });
		this.renderServersWithUsersFragment.setSimplerAdapterHasUsers(simplerAdapterHasUsers);
	}


}
