package com.tesis.app.paciente;

import com.tesis.app.paciente.R.id;
import com.tesis.app.paciente.tareas.ObtenerDiagnosticos;
import com.tesis.app.paciente.wsobj.RegistroDiagnostico;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListaDiagnostico extends Activity {

	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_dignostico);
		
		mListView = (ListView) findViewById(id.lvDiagnosticos);
		
		ObtenerDiagnosticos lista = new ObtenerDiagnosticos();
		lista.setParams(this);
		lista.execute();
	}
	
	public void llenarLista(final RegistroDiagnostico[] diagnostico){
		
		String[] datos = new String[diagnostico.length];
		
		for(int i=0; i<diagnostico.length; i++){
			String temp = diagnostico[i].getContenido();
			
			datos[i] = diagnostico[i].getFecha() + " - " + diagnostico[i].getHora() + "\n"
					+ temp.substring(0, (temp.length()>30)? 30: temp.length()) + "...";
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				datos);
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> padre, View v, int index, long id) {
				
				AlertDialogManager alerta = new AlertDialogManager();
				alerta.showAlertDialog(ListaDiagnostico.this, "Diagnostico", diagnostico[index].getContenido());
			}
		});
	}
	
	public void detener(){
		
		new AlertDialog.Builder(ListaDiagnostico.this).setTitle("Mansaje").setMessage("Al parecer no hay diagnosticos")
		.setPositiveButton("Aceptar", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				finish();
			}
		}).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_dignostico, menu);
		return true;
	}

}
