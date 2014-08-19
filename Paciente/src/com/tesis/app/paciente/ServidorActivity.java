package com.tesis.app.paciente;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tesis.app.paciente.R.id;

public class ServidorActivity extends Activity {

	private UrlServer config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_servidor);
		
		final EditText entrada = (EditText) findViewById(id.etDireccion);
		Button btnGuardar = (Button) findViewById(id.btnFechaNacimiento);
		
		config = new UrlServer(ServidorActivity.this);
		
		if(config.getUrl() != null){
			entrada.setText(config.getUrl());
		}
		
		btnGuardar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(entrada.getText().toString() != ""){
					
					UrlServer config = new UrlServer(ServidorActivity.this);
					config.saveUrl(entrada.getText().toString());
					
					Toast toast = Toast.makeText(ServidorActivity.this, "URL guardada correctamente", Toast.LENGTH_LONG);
					toast.show();
					
				} else {
					Toast toast = Toast.makeText(ServidorActivity.this, "Debe ingresar una url", Toast.LENGTH_LONG);
					toast.show();
				}
				
				
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.servidor, menu);
//		return true;
//	}

}
