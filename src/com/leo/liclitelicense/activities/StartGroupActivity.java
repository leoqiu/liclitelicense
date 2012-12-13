package com.leo.liclitelicense.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.leo.liclitelicense.R;
import com.leo.liclitelicense.adapters.GroupFragmentImageAdapter;
import com.leo.liclitelicense.fragments.AddServerFragment;
import com.leo.liclitelicense.fragments.HistoryFragment;
import com.leo.liclitelicense.fragments.NotificationFragment;
import com.leo.liclitelicense.fragments.ServersFragment;
import com.leo.liclitelicense.staticdata.LicLiteData;



public class StartGroupActivity extends Activity {

	private GridView gridviewToolbar; // group grid tool bar
	private GroupFragmentImageAdapter groupFragmentImageAdapter = null; // picture adapter
//	private TextView content = null; // sub activity area
	private int group_img[] = new int[] { R.drawable.group_servers, R.drawable.group_add,
			R.drawable.group_notifications, R.drawable.group_history}; // groupActivityImageAdapter images resources
	private int screenWidth = 0; // screen width
	private int screenHeight = 0; // screen height
	
	private TextView titleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
//      super.requestWindowFeature(Window.FEATURE_NO_TITLE); // no title
        //customize title bar
    	super.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_start_group);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_group_title_bar);
        titleBar = (TextView) this.findViewById(R.id.title_bar_value);
        
        this.gridviewToolbar = (GridView) this.findViewById(R.id.gridviewbar);
 //       this.content = (TextView) this.findViewById(R.id.content);
        
        //gridview setups
		this.gridviewToolbar.setNumColumns(this.group_img.length); // 求出可以保存的个数
		this.gridviewToolbar.setSelector(new ColorDrawable(Color.TRANSPARENT));
		this.gridviewToolbar.setGravity(Gravity.CENTER);
		this.gridviewToolbar.setVerticalSpacing(0);
		
		
		this.screenWidth = super.getWindowManager().getDefaultDisplay().getWidth()/ this.group_img.length;
		this.screenHeight = super.getWindowManager().getDefaultDisplay().getHeight() / 10 ;
		
		//set GroupActivityImageAdapter
		groupFragmentImageAdapter = new GroupFragmentImageAdapter(this, group_img, screenWidth, screenHeight, R.drawable.group_selected);
		this.gridviewToolbar.setAdapter(groupFragmentImageAdapter);
		this.switchActivity(0);
		this.gridviewToolbar.setOnItemClickListener(new GridviewToolbarOnItemClickListenerImpl());
    }

	private void switchActivity(int id) { // 切换选中的操作
		// set background pic for selected fragment pic
		this.groupFragmentImageAdapter.setFocus(id); 
		
		switch (id) {
		case 0:
			replaceFragment(id);
			break;
		case 1:
			replaceFragment(id);
			break;
		case 2:
			replaceFragment(id);
			break;
		case 3:
			replaceFragment(id);
			break;

		}		
	}
	
	private void replaceFragment(int position){
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
		if(position == 0) {
			transaction.replace(R.id.content, ServersFragment.getInstance());
			titleBar.setText("servers");
		}else if(position == 1){
			transaction.replace(R.id.content, AddServerFragment.getInstance());
			titleBar.setText("add a server");
		}else if(position == 2){
			transaction.replace(R.id.content, NotificationFragment.getInstance());
			titleBar.setText("notification");
		}else if(position == 3){
			transaction.replace(R.id.content, HistoryFragment.getInstance());
			titleBar.setText("history");
		}

//		transaction.addToBackStack(null);
		if(transaction != null)
			transaction.commit();
		
	}
	
	/**
	 * 
	 * @author shqiu
	 *
	 */
	private class GridviewToolbarOnItemClickListenerImpl implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			switchActivity(position);
			
		}
		
	}
    
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK) {
//			this.exitDialog() ;
//		}
//		return false ;
//	}
	
	/**
	 * 
	 */
	private void exitDialog() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("Exist").setMessage("Do you want to logout the logined sessions?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
LicLiteData.serverBeanList.clear();
LicLiteData.autoServerNameSet.clear();
LicLiteData.autoLmgrdCmdSet.clear();
						StartGroupActivity.this.finish() ;
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						LicLiteData.autoServerNameSet.clear();
						LicLiteData.autoLmgrdCmdSet.clear();
						StartGroupActivity.this.finish() ;
					}
				}).create();

		dialog.show();
	}
}




































