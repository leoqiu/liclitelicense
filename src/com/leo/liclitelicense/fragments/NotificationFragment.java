package com.leo.liclitelicense.fragments;

import com.leo.liclitelicense.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NotificationFragment extends Fragment {

	
	private static NotificationFragment instance = new NotificationFragment();
	
	private NotificationFragment() {
		
	}
	
	public static NotificationFragment getInstance(){
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_notification, container, false);
		
		return v;
	}

}
