package com.leo.liclitelicense.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.beans.AutoWordBean;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.staticdata.LicLiteData;
import com.leo.liclitelicense.utils.DBUtil;
import com.leo.liclitelicense.utils.UIUtil;

public class AddServerFragment extends Fragment{
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	//views
	private AutoCompleteTextView autoServerName = null;
	private AutoCompleteTextView autoLmgrdCmd = null;
	private EditText edittextServerPort = null;
	private Spinner spinnerServerLoc = null;
	private Spinner spinnerserverTimeZone = null;
	private EditText edittextServerTimeout = null;
	private EditText edittextServerRetryTimes = null;
	private Button addServerButton = null;
	
	private String serverName = null;
	private String lmgrdCmd = null;
	private String portNum = null;
	private String serverLoc = null;
	private String serverTimeZone = null;
	private String serverTimeout = null;
	private String serverRetryTimes = null;
	
	private ArrayAdapter<String> autoServerNameAdapter  = null;
	private ArrayAdapter<String> autoServerCmdAdapter = null;
	
	private static AddServerFragment instance = new AddServerFragment();
	
	private AddServerFragment(){
		
	}
	
	public static AddServerFragment getInstance(){
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_add_server, container, false);
		
		autoServerName = (AutoCompleteTextView) v.findViewById(R.id.auto_server_name);
		autoLmgrdCmd = (AutoCompleteTextView) v.findViewById(R.id.auto_lmgrd_cmd);
		edittextServerPort = (EditText) v.findViewById(R.id.edittext_server_port);
		spinnerServerLoc = (Spinner) v.findViewById(R.id.spinner_server_location);
		spinnerserverTimeZone = (Spinner) v.findViewById(R.id.spinner_server_time_zone);
		edittextServerTimeout = (EditText) v.findViewById(R.id.edittext_server_timeout);
		edittextServerRetryTimes = (EditText) v.findViewById(R.id.edittext_server_retry);
		addServerButton = (Button) v.findViewById(R.id.button_add_server);
		
		//autoedittext adapter setup
		//first loaded, initialize auto adapters
		if(LicLiteData.autoServerNameSet.size() == 0 || LicLiteData.autoLmgrdCmdSet.size() == 0){
			DBUtil.initializeAutoArrayList(getActivity());
		}
		
		//create and set up auto adapters
		autoServerNameAdapter  = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, LicLiteData.autoServerNameSet.toArray(new String[0]));
		autoServerName.setAdapter(autoServerNameAdapter);
		autoServerCmdAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_dropdown_item_1line, LicLiteData.autoLmgrdCmdSet.toArray(new String[0]));
		autoLmgrdCmd.setAdapter(autoServerCmdAdapter);
		
		
		//spinner adapter setup
		ArrayAdapter<CharSequence> spinnerServerLocAdapter = ArrayAdapter.createFromResource(getActivity(),
						R.array.spinner_server_location_array, android.R.layout.simple_spinner_dropdown_item);
		spinnerServerLoc.setAdapter(spinnerServerLocAdapter);
		ArrayAdapter<CharSequence> spinnerserverTimeZoneAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.spinner_server_time_zone_array, android.R.layout.simple_spinner_dropdown_item);
		spinnerserverTimeZone.setAdapter(spinnerserverTimeZoneAdapter);
		
		//set Add Server button listener
		addServerButton.setOnClickListener(new AddServerButtonOnClickListenerImpl());
		
		return v;
	}

	/**
	 * Add server bean into servers table listener impl 
	 * 
	 * @author shqiu
	 *
	 */
	private class AddServerButtonOnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View view) {
			serverName = autoServerName.getText().toString();
			lmgrdCmd = autoLmgrdCmd.getText().toString();
			portNum = edittextServerPort.getText().toString();
			serverLoc = spinnerServerLoc.getItemAtPosition(spinnerServerLoc.getSelectedItemPosition()).toString();
			serverTimeZone = spinnerserverTimeZone.getItemAtPosition(spinnerserverTimeZone.getSelectedItemPosition()).toString();
			serverTimeout = edittextServerTimeout.getText().toString();
			serverRetryTimes = edittextServerRetryTimes.getText().toString();
			
			if(serverName.equals("") || lmgrdCmd.equals("")){
				UIUtil.showToast(getActivity().getResources().getString(
							R.string.add_server_no_servername_or_lmgrdcmd), getActivity());
			}else {
				if(portNum.equals("")){
					portNum = getActivity().getResources().getString(
							R.string.add_server_default_port);
				}
				if(serverTimeout.equals("")){
					serverTimeout = getActivity().getResources().getString(
							R.string.add_server_default_server_timeout);
				}
				if(serverRetryTimes.equals("")){
					serverRetryTimes = getActivity().getResources().getString(
							R.string.add_server_default_server_retry_times);
				}
				
				if(!UIUtil.isServerExist(serverName, lmgrdCmd)){
					ServerBean serverBean = new ServerBean(serverName, lmgrdCmd, portNum, serverLoc, 
							serverTimeZone, serverTimeout, serverRetryTimes);
					AutoWordBean autoWordBean = new AutoWordBean(serverName, lmgrdCmd);					
					//insert auto word bean into table - auto_word		

					if(DBUtil.insertBeansIntoDBAndServerList(serverBean, autoWordBean,autoServerNameAdapter, 
							autoServerCmdAdapter, getActivity())){
						clearAllEditText();
						UIUtil.showToast(getActivity().getResources().getString(
								R.string.add_server_successfully), getActivity());
					}

				}else{
					UIUtil.showToast(getActivity().getResources().getString(
							R.string.add_server_server_exist), getActivity());
				}
				

			}
		}
	}
	
	/**
	 * clear all the edit text fields
	 */
	private void clearAllEditText(){
		autoServerName.setText("");
		autoLmgrdCmd.setText("");
		edittextServerPort.setText("");
		edittextServerTimeout.setText("");
		edittextServerRetryTimes.setText("");
	}	
	
}














