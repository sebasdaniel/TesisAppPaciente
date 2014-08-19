package com.tesis.app.paciente;

import java.util.ArrayList;

import com.tesis.app.paciente.DatePickerFragment.DatePickerFragmentListener;
import com.tesis.app.paciente.R.id;
import com.tesis.app.paciente.tareas.ActualizarDatos;
import com.tesis.app.paciente.tareas.CargarDatos;
import com.tesis.app.paciente.tareas.ObtenerDepartamentos2;
import com.tesis.app.paciente.tareas.ObtenerMunicipios2;
import com.tesis.app.paciente.wsobj.RegistroDepartamento;
import com.tesis.app.paciente.wsobj.RegistroMunicipio;
import com.tesis.app.paciente.wsobj.RegistroPaciente;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class ConfiguracionActivity extends FragmentActivity implements DatePickerFragmentListener {

//	private String correo;
//	private String clave;
//	private String tipoId;
//	private String numId;
//	private String nombres;
//	private String apellidos;
//	private String sexo;
//	private String direccion;
//	private String telefono;
//	private String municipio;
//	
//	private String fecha;
	
	private RegistroPaciente paciente;
	
	private ArrayList<RegistroMunicipio> regMunicipios;
	private ArrayList<RegistroDepartamento> regDepartamento;
	
	private Button botonFecha;
	private Spinner listaDepartamento;
	private Spinner listaMunicipio;
	private EditText etNombres;
	private EditText etApellidos;
	private Spinner spinTipoId;
	private EditText etNumeroId;
	private EditText etCorreo;
	private EditText etClave;
	private Spinner spinSexo;
	private EditText etDireccion;
	private EditText etTelefono;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuracion);
		
		botonFecha = (Button) findViewById(id.btnFechaNacimientoConfig);
		Button botonActualizar = (Button) findViewById(id.btnActualizar);
		
		listaDepartamento = (Spinner) findViewById(id.spinnerDepartamentosConfig);
		listaMunicipio = (Spinner) findViewById(id.spinnerMunicipiosConfig);
		
		etNombres = (EditText) findViewById(id.edtNombresConfig);
		etApellidos = (EditText) findViewById(id.edtApellidosConfig);
		spinTipoId = (Spinner) findViewById(id.spinnerTipoIdConfig);
		etNumeroId = (EditText) findViewById(id.edtNumeroIdConfig);
		etCorreo = (EditText) findViewById(id.edtCorreoConfig);
		etClave = (EditText) findViewById(id.edtClaveConfig);
		spinSexo = (Spinner) findViewById(id.spinnerTipoSexoConfig);
		etDireccion = (EditText) findViewById(id.etDireccionConfig);
		etTelefono = (EditText) findViewById(id.etTelefonoConfig);
		
		botonFecha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				DatePickerFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		
		botonActualizar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				paciente.setNombres(etNombres.getText().toString());
				paciente.setApellidos(etApellidos.getText().toString());
				paciente.setTipoId(spinTipoId.getSelectedItem().toString());
				paciente.setNumId(etNumeroId.getText().toString());
				paciente.setCorreo(etCorreo.getText().toString());
				paciente.setClave(etClave.getText().toString());
				paciente.setSexo(spinSexo.getSelectedItem().toString());
				paciente.setFechaNaciemiento(botonFecha.getText().toString());
				paciente.setDireccion(etDireccion.getText().toString());
				paciente.setTelefono(etTelefono.getText().toString());
				paciente.setMunicipio(regMunicipios.get(listaMunicipio.getSelectedItemPosition()).getId() + "");
				
				ActualizarDatos actualizar = new ActualizarDatos();
				actualizar.setParams(getApplicationContext());
				actualizar.execute(paciente);
			}
		});
		
		ObtenerDepartamentos2 od = new ObtenerDepartamentos2();
		od.setParams(ConfiguracionActivity.this);
		od.execute();
		
		CargarDatos cd = new CargarDatos();
		cd.setParams(ConfiguracionActivity.this);
		cd.execute();
	}

	@Override
	public void onFinishDatePickerDialog(int year, int month, int day) {
		
		botonFecha.setText(year + "-" + month + "-" + day);
	}
	
	public void cargarDepartamentos(ArrayList<RegistroDepartamento> departamentos){
		
		regDepartamento = departamentos;
//		String datos[] = new String[departamentos.size()];
//		
//		for(int i=0; i<departamentos.size(); i++){
//			
//			datos[i] = departamentos.get(i).getNombre();
//		}
		
		ArrayAdapter<RegistroDepartamento> adaptador = new ArrayAdapter<RegistroDepartamento>(this,
				android.R.layout.simple_spinner_item, regDepartamento);
		
		listaDepartamento.setAdapter(adaptador);
		
		listaDepartamento.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int index, long id) {
				
				ObtenerMunicipios2 om = new ObtenerMunicipios2();
				om.setParams(ConfiguracionActivity.this);
				om.execute(regDepartamento.get(index).getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void cargarMunicipios(ArrayList<RegistroMunicipio> municipios){
		
		regMunicipios = municipios;
		
//		String datos[] = new String[regMunicipios.size()];
//		
//		for(int i=0; i<regMunicipios.size(); i++){
//			
//			datos[i] = regMunicipios.get(i).getNombre();
//		}
		
		ArrayAdapter<RegistroMunicipio> adaptador = new ArrayAdapter<RegistroMunicipio>(this,
				android.R.layout.simple_spinner_item, regMunicipios);
		
		listaMunicipio.setAdapter(adaptador);
		
		if(paciente != null){
			listaMunicipio.setSelection(indiceMunicipio(paciente.getMunicipio()));
		}
	}
	
	public void cargarDatos(RegistroPaciente registro){
		
		paciente = registro;
		
		etNombres.setText(paciente.getNombres());
		etApellidos.setText(paciente.getApellidos());
		spinTipoId.setSelection(paciente.getTipoId().equals("C.C")? 0 : 1);
		etNumeroId.setText(paciente.getNumId());
		etCorreo.setText(paciente.getCorreo());
		etClave.setText(paciente.getClave());
		spinSexo.setSelection(indiceTipoSexo(paciente.getSexo()));
		botonFecha.setText(paciente.getFechaNaciemiento());
		etDireccion.setText(paciente.getDireccion());
		etTelefono.setText(paciente.getTelefono());
		listaDepartamento.setSelection(indiceDepartamento(paciente.getDepartamento()));
//		listaMunicipio.setSelection(indiceMunicipio(registro.getMunicipio()));
		
	}
	
	private int indiceTipoSexo(String tipo){
		
		if(tipo.equalsIgnoreCase("masculino")){
			
			return 0;
		} else if (tipo.equalsIgnoreCase("femenino")) {
			
			return 1;
		} else {
			
			return 2;
		}
	}
	
	private int indiceDepartamento(String id){
		
		int idDep = Integer.parseInt(id);
		int cont = 0;
		
		for(RegistroDepartamento depto : regDepartamento){
			
			if(depto.getId() == idDep){
				return cont;
			}
			cont++;
		}
		
		return 0;
	}
	
	private int indiceMunicipio(String id){
		
		int idMun = Integer.parseInt(id);
		int cont = 0;
		
		for(RegistroMunicipio mun : regMunicipios){
			
			if(mun.getId() == idMun){
				return cont;
			}
			cont++;
		}
		
		return 0;
	}
}
