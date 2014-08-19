package com.tesis.app.paciente;

import java.util.ArrayList;

//import com.tesis.app.paciente.R.id;


import android.os.Bundle;
import android.app.Activity;
//import android.content.Intent;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
//import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

private ListView lista;

	//private TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setContentView(R.layout.list);
		
		ArrayList<ListEntrada> datos = new ArrayList<ListEntrada>();
		
		datos.add(new ListEntrada(R.drawable.ic_action_monitoreo, "Monitoreo", "Comenzar mis chequeos del dia"));
		datos.add(new ListEntrada(R.drawable.ic_action_automatico, "Automatico", "Solo aplica para ECG"));
		datos.add(new ListEntrada(R.drawable.ic_action_diag, "Diagnostico", "Revisar los diagnosticos hechos por mi medico"));
		datos.add(new ListEntrada(R.drawable.ic_action_estadistica, "Estadisticas", "Revisar las estadisticas del estado de mi salud"));
		datos.add(new ListEntrada(R.drawable.ic_action_historial, "Historial", "Revisar mi historial"));
		datos.add(new ListEntrada(R.drawable.ic_action_solicitar, "Solicitudes", ""));
		datos.add(new ListEntrada(R.drawable.ic_action_configuracion, "Configuracion", ""));
		datos.add(new ListEntrada(R.drawable.ic_action_salir, "Salir", ""));
		
lista = (ListView) findViewById(R.id.ListView_listado);
lista.setAdapter(new ListAdaptador(this, R.layout.enter, datos) {
	
	@Override
	public void onEntrada(Object entrada, View view) {
		// TODO Auto-generated method stub
		if (entrada != null){
			TextView texto_superior_enter = (TextView) view.findViewById(R.id.textView_superior);
			  if (texto_superior_enter != null) 
	            	texto_superior_enter.setText(((ListEntrada) entrada).get_textoEncima()); 
	              
//	            TextView texto_inferior_enter = (TextView) view.findViewById(R.id.textView_inferior); 
//	            if (texto_inferior_enter != null)
//	            	texto_inferior_enter.setText(((ListEntrada) entrada).get_textoDebajo()); 
	              
	            ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen); 
	            if (imagen_entrada != null)
	            	imagen_entrada.setImageResource(((ListEntrada) entrada).get_IdImage());
	        }
		}
	});
			
	lista.setOnItemLongClickListener(new OnItemLongClickListener() {
	
	
		@Override
		public boolean onItemLongClick(AdapterView<?> pariente, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			ListEntrada elegido = (ListEntrada) pariente.getItemAtPosition(position);
			CharSequence texto = "Seleccionado: " + elegido.get_textoDebajo();
			Toast toast = Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG);
			toast.show();
		return true;
		}
	});
		
		

		
//		Button botonIngreso = (Button) findViewById(R.id.buttonMonitoreo);
//
//		result = (TextView) findViewById(id.textResult);
//		
//		botonIngreso.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent ingreso = new Intent(LoginActivity.this, HomeActivity.class);
//			
//				startActivity(ingreso);
//				
//
//				
//			}
//		});
//	}
//	
//	public void iniciar(){
//		
//		Intent ingreso = new Intent(LoginActivity.this, HomeActivity.class);
//		ingreso.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		
//		startActivity(ingreso);
//		
//		finish();
//	}
//	
//	public void mostrarResultado(String txt){
//		result.setText(txt);
//	}
//	
//	

}
}
