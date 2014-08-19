package com.tesis.app.paciente;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class AlertDialogManager {

	public void showAlertDialog(Context context, String title, String message){
		
		Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setPositiveButton("Aceptar", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		
		alertDialogBuilder.show();
	}
}
