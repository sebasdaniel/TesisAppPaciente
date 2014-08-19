package com.tesis.app.paciente;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tesis.app.paciente.R.id;
import com.tesis.app.paciente.tareas.HacerSolicitud;

public class DatosConsultorio extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datos_consultorio);
		
		TextView tvNombre = (TextView) findViewById(id.tvNombreConsultorio);
		TextView tvDireccion = (TextView) findViewById(id.tvDireccion);
		TextView tvTelefono = (TextView) findViewById(id.tvTelefono);
		
		Button btnEnviar = (Button) findViewById(id.btnEnviarSolicitudConsultorio);
		
		Bundle bundle = getIntent().getExtras();
		
		tvNombre.setText(bundle.getString("nombre"));
		tvDireccion.setText("Direccion " + bundle.getString("direccion"));
		tvTelefono.setText("Telefono " + bundle.getString("telefono"));
		
		final String id = bundle.getString("id");
		
		btnEnviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				HacerSolicitud solicitud = new HacerSolicitud();
				solicitud.setParams(DatosConsultorio.this, null);
				solicitud.execute("1", id);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.datos_consultorio, menu);
		return true;
	}

}
