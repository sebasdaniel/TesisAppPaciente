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

public class DatosMedico extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datos_medico);
		
		TextView tvNombres = (TextView) findViewById(id.tvNombresMedico);
		TextView tvSexo = (TextView) findViewById(id.tvSexoMedico);
		TextView tvTelefono = (TextView) findViewById(id.tvTel);
		
		Button btnEnviar = (Button) findViewById(id.btnEnviarSolicitud);
		
		Bundle bundle  = getIntent().getExtras();
		
		tvNombres.setText(bundle.getString("nombres"));
		tvSexo.setText("Sexo " + bundle.getString("sexo"));
		tvTelefono.setText("Telefono " + bundle.getString("telefono"));
		
		final String cedula = bundle.getString("cedula");
		
		btnEnviar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				HacerSolicitud solicitud = new HacerSolicitud();
				solicitud.setParams(null, DatosMedico.this);
				solicitud.execute("0", cedula);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.datos_medico, menu);
		return true;
	}

}
