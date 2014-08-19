package com.tesis.app.paciente;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UrlServer {

	private SharedPreferences preferences;
	private Editor editor;
	private Context context;
	private static final String PREF = "ServerConfig";
	private static final String KEY = "url";
	
	public UrlServer(Context context){
		
		this.context = context;
		preferences = this.context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	public void saveUrl(String url){
		
		editor.putString(KEY, url);
		editor.commit();
	}
	
	public String getUrl(){
		
		return preferences.getString(KEY, null);
	}
}
