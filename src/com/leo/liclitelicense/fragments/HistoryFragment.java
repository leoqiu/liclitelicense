package com.leo.liclitelicense.fragments;

import com.leo.liclitelicense.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryFragment extends Fragment {

	
	private static HistoryFragment instance = new HistoryFragment();
	
	private HistoryFragment(){
		
	}
	
	public static HistoryFragment getInstance(){
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_history, container, false);
		
		return v;
	}

}



