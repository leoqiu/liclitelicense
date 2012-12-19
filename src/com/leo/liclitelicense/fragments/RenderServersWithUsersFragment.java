package com.leo.liclitelicense.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.leo.liclitelicense.R;

public class RenderServersWithUsersFragment extends Fragment{

	private final static RenderServersWithUsersFragment instance = new RenderServersWithUsersFragment();

	private ExpandableListView serverWithExpandableListView = null;
	private AutoCompleteTextView autoCompleteTextViewWithUsers = null;
	private SimpleExpandableListAdapter simplerAdapterHasUsers = null;
	
	private RenderServersWithUsersFragment() {
		
	}
	
	public static RenderServersWithUsersFragment getInstance(){
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_render_with_users, container, false);
		
		this.serverWithExpandableListView = (ExpandableListView) v.findViewById(R.id.expandablelistview_servers_with_users);
		this.autoCompleteTextViewWithUsers = (AutoCompleteTextView) v.findViewById(R.id.autocomplete_fragment_render_with_users);
//		this.autoCompleteTextViewWithUsers
//				.setOnItemClickListener(new AutoCompleteWithUsersOnItemClickListenerImpl());
		this.autoCompleteTextViewWithUsers.setDropDownWidth(0);
		this.autoCompleteTextViewWithUsers.setHint("search by feature name");
		
		if(this.simplerAdapterHasUsers != null) {
			this.serverWithExpandableListView.setAdapter(simplerAdapterHasUsers);
			//this.autoCompleteTextViewWithUsers.setAdapter(simplerAdapterHasUsers);
		}
		
		return v;
	}
	
	/**
	 * getter and setter
	 * 
	 * @return
	 */
	public ExpandableListView getServerWithExpandableListView() {
		return serverWithExpandableListView;
	}

	public void setServerWithExpandableListView(
			ExpandableListView serverWithExpandableListView) {
		this.serverWithExpandableListView = serverWithExpandableListView;
	}

	public AutoCompleteTextView getAutoCompleteTextViewWithUsers() {
		return autoCompleteTextViewWithUsers;
	}

	public void setAutoCompleteTextViewWithUsers(
			AutoCompleteTextView autoCompleteTextViewWithUsers) {
		this.autoCompleteTextViewWithUsers = autoCompleteTextViewWithUsers;
	}

	public SimpleExpandableListAdapter getSimplerAdapterHasUsers() {
		return simplerAdapterHasUsers;
	}

	public void setSimplerAdapterHasUsers(
			SimpleExpandableListAdapter simplerAdapterHasUsers) {
		this.simplerAdapterHasUsers = simplerAdapterHasUsers;
	}
	
//	@Override
//	public void onAttach(Activity activity) {
//		
//		super.onAttach(activity);
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public void onDestroy() {
//
//		super.onDestroy();
//	}
//
//	@Override
//	public void onDetach() {
//
//		super.onDetach();
//	}
}


