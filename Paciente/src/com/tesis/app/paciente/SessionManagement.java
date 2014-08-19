package com.tesis.app.paciente;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManagement {

	private SharedPreferences preferences;
	private Editor editor;
	private Context context;
	private static final String PREF_NAME = "PacienteSessionValues";
	private static final String KEY_EMAIL = "correo";
	private static final String KEY_PASS = "clave";
	private static final String KEY_NAME = "nombre";
	
	@SuppressLint("CommitPrefEdits")
	public SessionManagement(Context context){
		
		this.context = context;
		preferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	public void saveSession(String email, String pass, String userName){
		
		editor.putString(KEY_EMAIL, email);
		editor.putString(KEY_PASS, pass);
		editor.putString(KEY_NAME, userName);
		
		editor.commit();
	}
	
	public String getEmail(){
		return preferences.getString(KEY_EMAIL, null);
	}
	
	public String getPass(){
		return preferences.getString(KEY_PASS, null);
	}
	
	public String getName(){
		return preferences.getString(KEY_NAME, null);
	}
	
	public boolean isLoggedIn(){
		
		if(getEmail() != null && getPass() != null){
			return true;
		}
		
		return false;
	}
	
	public void logout(){
		
		editor.clear();
		editor.commit();
		
		Intent intent = new Intent(context, LoginActivity.class);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		context.startActivity(intent);
	}
}
