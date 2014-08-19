package com.tesis.app.paciente;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tesis.app.paciente.DatePickerFragment.DatePickerFragmentListener;
import com.tesis.app.paciente.R.id;

public class ConfigEstadistica extends FragmentActivity implements DatePickerFragmentListener {

	private TextView labelFechaInicio;
	private TextView labelFechaFin;
	private String fechaInicio;
	private String fechaFin;
	private boolean esFechaInicio;
	private boolean esFechaFin;
	private SessionManagement session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_estadistica);
		
		session = new SessionManagement(getApplicationContext());
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(session.getName());
		
		final Spinner spinTipoChequeo = (Spinner) findViewById(id.spinnerTiposChequeos);
		
		labelFechaInicio = (TextView) findViewById(id.tvFechaInicio);
		labelFechaFin = (TextView) findViewById(id.tvFechaFin);
		
		ImageButton btnFechaInicio = (ImageButton) findViewById(id.buttonFecha1);
		ImageButton btnFechaFin = (ImageButton) findViewById(id.buttonFecha2);
		Button btnVer = (Button) findViewById(id.buttonVer);
		
		fechaInicio = null;
		fechaFin = null;
		
		esFechaInicio = false;
		esFechaFin = false;
		
//		spinTipoChequeo.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		btnFechaInicio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				esFechaInicio = true;
				selecFecha();
			}
		});
		
		btnFechaFin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				esFechaFin = true;
				selecFecha();
			}
		});
		
		btnVer.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View arg0) {
				
				if(fechaInicio != null && fechaFin != null){
					
					String tipoChequeo = (String)spinTipoChequeo.getSelectedItem();
					
//					Intent intent = new Intent(getApplicationContext(), Estadisticas.class);
//					
//					intent.putExtra("TipoChequeo", tipoChequeo.toLowerCase());
//					intent.putExtra("FechaInicio", fechaInicio);
//					intent.putExtra("FechaFin", fechaFin);
					
					Intent intent = new Intent(getApplicationContext(), GraficaEstadistica.class);
					
					intent.putExtra("TipoChequeo", tipoChequeo.toLowerCase());
					intent.putExtra("FechaInicio", fechaInicio);
					intent.putExtra("FechaFin", fechaFin);
					
					startActivity(intent);
				}
			}
		});
		
	}

	@Override
	public void onFinishDatePickerDialog(int year, int month, int day) {
		
		if(esFechaInicio){
			
			fechaInicio = year + "-" + month + "-" + day;
			labelFechaInicio.setText(fechaInicio);
			esFechaInicio = false;
			
		} else if(esFechaFin){
			
			fechaFin = year + "-" + month + "-" + day;
			labelFechaFin.setText(fechaFin);
			esFechaFin = false;
		}
	}
	
	private void selecFecha(){
		
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

}
