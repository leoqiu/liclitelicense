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
import com.leo.liclitelicense.fragments.RenderServersWithoutUsersFragment;
import com.leo.liclitelicense.staticdata.LicLiteData;
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
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
System.out.println("onProgressUpdate --> "+values[0]);
		progress.incrementProgressBy(values[0]);
	}

// private memebers
	private RenderServersWithoutUsersFragment renderServersWithoutUsersFragment = null;
	private GridView gridviewToolbar = null;

	// simpler adapter list
	List<Map<String, String>> licliteWithoutAdapterList = new ArrayList<Map<String, String>>();
	List<Map<String, String>> licliteWithGroupAdapterList = new ArrayList<Map<String, String>>();
	List<ArrayList<HashMap<String, String>>> licliteWithChildAdapterList = new ArrayList<ArrayList<HashMap<String, String>>>();


	public RenderInTimeResultAsyncTask(RenderServersWithoutUsersFragment 
			renderServersWithoutUsersFragment,
			GridView gridviewToolbar, ProgressDialog progress) {
		
		this.renderServersWithoutUsersFragment = renderServersWithoutUsersFragment;
		this.gridviewToolbar = gridviewToolbar;
		this.progress = progress;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected String doInBackground(Object... params) {
		
		this.publishProgress(LicLiteData.LOADING_PROGRESS_INITIAL_TIME);
		
		List<LicLiteServerInfoBean> licliteServerInfoList = Parser.parseDownloadDataFile();
		this.publishProgress(LicLiteData.LOADING_PROGRESS_PARSING_TIME);
		
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
				Map<String, String> groupMap = new HashMap<String, String>();
				groupMap.put("server_feature_name_value",
						licLiteWitUserInfoBeanInside.getFeatureName());
				groupMap.put("server_feature_num_issue_value",
						licLiteWitUserInfoBeanInside.getFeatureNumIssued());
				groupMap.put("server_feature_num_in_used_value",
						licLiteWitUserInfoBeanInside.getFeatureNumInUsed());
				licliteWithGroupAdapterList.add(groupMap);

				ArrayList<LicLiteUserInfoBean> licLiteUserInfoBeanList = licLiteWitUserInfoBeanInside
						.getLicLiteUserInfoBeanList();
				// expandable list child size
				int sizeChild = licLiteUserInfoBeanList.size();
				ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
				for (int j = 0; j < sizeChild; j++) {
					HashMap<String, String> childMap = new HashMap<String, String>();
					LicLiteUserInfoBean licLiteUserInfoBeanChild = licLiteUserInfoBeanList
							.get(j);

					childMap.put("user_name",
							licLiteUserInfoBeanChild.getUserName());
					childMap.put("user_server",
							licLiteUserInfoBeanChild.getUserServerName());
					childMap.put("user_start_time",
							licLiteUserInfoBeanChild.getUserStartTime());
					childMap.put("user_num_license_in_used",
							licLiteUserInfoBeanChild.getUserNumOfLicenses());

					childList.add(childMap);
				}

				licliteWithChildAdapterList.add(childList);
			}
		}
		this.publishProgress(LicLiteData.LOADING_PROGRESS_ITERATING_TIME);
		
		System.out.println("how many without items -> "
				+ licliteWithoutAdapterList.size());
		System.out.println("how many with items -> "
				+ licliteWithGroupAdapterList.size());
		System.out.println("how many items -> " + licliteServerInfoList.size());

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
	}


}
