package com.leo.liclitelicense.listenerimpl;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.leo.liclitelicense.adapters.ServerListSimpleAdapter;
import com.leo.liclitelicense.beans.ServerBean;

public class ServerListOnItemLongClickListenerImpl implements OnItemLongClickListener{

	private Context context = null; 
	private ServerListSimpleAdapter serverAdapter = null;
	
	private ServerBean serverBean = null;
	
	public ServerListOnItemLongClickListenerImpl(Context context, ServerListSimpleAdapter serverAdapter){
		this.context = context;
		this.serverAdapter = serverAdapter;
	}
	
	
	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
			long id) {

		System.out.println("on item long click... " + position);
		
		
		
		
		return false;
	}



}













