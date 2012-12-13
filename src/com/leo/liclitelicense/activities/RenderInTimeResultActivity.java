package com.leo.liclitelicense.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.leo.liclitelicense.R;
import com.leo.liclitelicense.asyncs.RenderInTimeResultAsyncTask;
import com.leo.liclitelicense.staticdata.LicLiteData;

public class RenderInTimeResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// set window feature then can use custom title bar
		super.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.activity_render_in_time_result);

		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.activity_group_title_bar);

		TextView titleBar = (TextView) this.findViewById(R.id.title_bar_value);

		titleBar.setText("Server results");

		Intent intent = this.getIntent();

		int loginIndex = intent.getIntExtra(LicLiteData.SERVER_LOGIN_INDEX, 1);

		System.out.println("clcik on " + loginIndex);

		RenderInTimeResultAsyncTask renderInTimeResultAsyncTask = new RenderInTimeResultAsyncTask(
				loginIndex, RenderInTimeResultActivity.this);

		renderInTimeResultAsyncTask.execute();

	}

	/**
	 * 
	 * animation when press the back button
	 */

	@Override
	public void onBackPressed() {

		super.onBackPressed();

		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

	}

}
