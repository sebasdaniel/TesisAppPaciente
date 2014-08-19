package com.tesis.app.paciente;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.tesis.app.paciente.R.id;

public class BusquedaSolicitud extends Activity {

	private SessionManagement session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda_solicitud);
		
		session = new SessionManagement(getApplicationContext());
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(session.getName());
		
		final Spinner tipoBusqueda = (Spinner) findViewById(id.spinnerTipoBusqueda);
		
		final EditText nombreBusqueda = (EditText) findViewById(id.etNombre);
		
		Button botonBuscar = (Button) findViewById(id.buttonBuscar);
		
		botonBuscar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent intent = new Intent(getApplicationContext(), ResultadoBuscarSolicitud.class);
				
				intent.putExtra("criterio", (String)tipoBusqueda.getSelectedItem());
				intent.putExtra("nombre", nombreBusqueda.getText().toString());
				
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busqueda_solicitud, menu);
		return true;
	}

}
