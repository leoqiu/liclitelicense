package com.leo.liclitelicense.listenerimpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.activities.RenderInTimeResultActivity;
import com.leo.liclitelicense.adapters.ServerListSimpleAdapter;
import com.leo.liclitelicense.asyncs.GetSshSessionAsyncTask;
import com.leo.liclitelicense.beans.ServerBean;
import com.leo.liclitelicense.networks.SshManager;
import com.leo.liclitelicense.staticdata.LicLiteData;
import com.leo.liclitelicense.utils.UIUtil;

public class ServerListOnItemClickListenerImpl implements OnItemClickListener{

	
	private Activity activity = null;
	private ServerListSimpleAdapter serverListSimpleAdapter = null;
	
	
	public ServerListOnItemClickListenerImpl(Activity activity, ServerListSimpleAdapter serverListSimpleAdapter){
		this.activity = activity;
		this.serverListSimpleAdapter = serverListSimpleAdapter;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		 final int clickedPos = position;
		
		 LayoutInflater factory = LayoutInflater.from(activity);
		 final View textEntryView = factory.inflate(R.layout.alert_dialog_login, null);
		 //get the server name for rendering on login dialog and for future conn use
		 ServerBean serverBean = LicLiteData.serverBeanList.get(position);
		 
		 
		 if(serverBean.getIsLogin() != LicLiteData.IS_LOGINED){
			 final String serverName = serverBean.getServerName();
			 new AlertDialog.Builder(activity)
			     .setIconAttribute(android.R.attr.alertDialogIcon)
			     .setTitle(LicLiteData.LOGIN_DIALOG + serverName)
			     .setView(textEntryView)
			     .setPositiveButton(R.string.alert_dialog_login_ok_button, new DialogInterface.OnClickListener() {
			    	 //click ok button event to popup login dialog
			     public void onClick(DialogInterface dialog, int whichButton) {
			
			         /* User clicked OK so do some stuff */
			    	 EditText userNameEditView = (EditText)textEntryView.findViewById(R.id.username_edit);
			    	 EditText pwdEditView = (EditText)textEntryView.findViewById(R.id.password_edit);
			    	 String userName = userNameEditView.getText().toString();
			    	 String passWord = pwdEditView.getText().toString();
			    	 
			    	 if(userName.equals("") || passWord.equals("")){
			    		 UIUtil.showToast(activity.getResources().getString(R.string.login_no_user_or_pwd), activity);
			    	 }else{
			    		 SshManager sshManager = new SshManager(serverName, userName, passWord);
						 GetSshSessionAsyncTask getSshSessionAsyncTask = new GetSshSessionAsyncTask(
								sshManager, serverListSimpleAdapter, clickedPos, activity);	 
						 getSshSessionAsyncTask.execute();
			
			    	 }
			    	 
			     }
			 /*
			  * set cancel click event
			  */
			 }).setNegativeButton(R.string.alert_dialog_login_cancel_button, new DialogInterface.OnClickListener() {
				 
			     public void onClick(DialogInterface dialog, int whichButton) {
			    	 
	 
			    	 dialog.dismiss();
			     } 
			 }).create().show();
		 }else{
System.out.println("login .........");
//FragmentTransaction transaction =  activity.getFragmentManager().beginTransaction();
//transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
//        R.animator.fragment_slide_left_exit,
//        R.animator.fragment_slide_right_enter,
//        R.animator.fragment_slide_right_exit);
//transaction.replace(R.id.activity_start_content, RenderInTimeResultFragment.getInstance());
//transaction.addToBackStack(null);
//if(transaction != null)
//	transaction.commit();
//TextView titleBar = (TextView) activity.findViewById(R.id.title_bar_value);
//titleBar.setText("In time result");
Intent intent = new Intent(activity, RenderInTimeResultActivity.class);
intent.putExtra(LicLiteData.SERVER_LOGIN_INDEX, clickedPos);
intent.putExtra(LicLiteData.IS_HISTORY_RESULT, false);
activity.startActivity(intent);
activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);





		 }
		 
		 
		 
		 
		 
	}

	
	
	
	
	
}
