package com.tesis.app.paciente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tesis.app.paciente.R.id;
import com.tesis.app.paciente.tareas.BuscarConsultorio;
import com.tesis.app.paciente.tareas.BuscarMedico;
import com.tesis.app.paciente.wsobj.RegistroConsultorio;
import com.tesis.app.paciente.wsobj.RegistroMedico;

public class ResultadoBuscarSolicitud extends Activity {

	private String tipo, busqueda;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultado_buscar_solicitud);
		
		Bundle bundle = getIntent().getExtras();
		
		tipo = bundle.getString("criterio");
		busqueda = bundle.getString("nombre");
		
		if(tipo.equalsIgnoreCase("medico")){
			
			BuscarMedico bm = new BuscarMedico();
			bm.setParams(this);
			bm.execute(busqueda);
			
		} else if(tipo.equalsIgnoreCase("consultorio")){
			
			BuscarConsultorio bc = new BuscarConsultorio();
			bc.setParams(this);
			bc.execute(busqueda);
		}
	}

	public void cargarListaMedico(final RegistroMedico[] registro){
		
		String[] nombres = new String[registro.length];
		
		for(int i=0; i<registro.length; i++){
			nombres[i] = registro[i].getNombres() + " " + registro[i].getApellidos();
		}
		
		ListView mListView = (ListView) findViewById(id.lvResultadoBuscar);
		
		ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				nombres);
		
		mListView.setAdapter(mArrayAdapter);
		
		mListView.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> padre, View v, int index, long id) {
				
				Intent intent = new Intent(getApplicationContext(), DatosMedico.class);
				
				intent.putExtra("cedula", registro[index].getCedula());
				intent.putExtra("nombres", registro[index].getNombres() + " " +registro[index].getApellidos());
				intent.putExtra("telefono", registro[index].getTelefono());
				intent.putExtra("sexo", registro[index].getSexo());
				
				startActivity(intent);
			}
		});
		
	}
	
	public void cargarListaConsultorio(final RegistroConsultorio[] registro){
		
		String[] nombre = new String[registro.length];
		
		for(int i=0; i<registro.length; i++){
			nombre[i] = registro[i].getNombre();
		}
		
		ListView mListView = (ListView) findViewById(id.lvResultadoBuscar);
		
		ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				nombre);
		
		mListView.setAdapter(mArrayAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> padre, View v, int index, long id) {
				
				Intent intent = new Intent(getApplicationContext(), DatosConsultorio.class);
				
				intent.putExtra("id", registro[index].getId());
				intent.putExtra("nombre", registro[index].getNombre());
				intent.putExtra("direccion", registro[index].getDireccion());
				intent.putExtra("telefono", registro[index].getTelefono());
				
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resultado_buscar_solicitud, menu);
		return true;
	}

}
