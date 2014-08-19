package com.tesis.app.paciente;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tesis.app.paciente.DatePickerFragment.DatePickerFragmentListener;
import com.tesis.app.paciente.R.id;
import com.tesis.app.paciente.tareas.ObtenerDepartamentos;
import com.tesis.app.paciente.tareas.ObtenerMunicipios;
import com.tesis.app.paciente.tareas.RegistrarPaciente;
import com.tesis.app.paciente.wsobj.RegistroDepartamento;
import com.tesis.app.paciente.wsobj.RegistroMunicipio;

public class RegisterActivity extends FragmentActivity implements DatePickerFragmentListener {

	private String correo;
	private String clave;
	private String tipoId;
	private String numId;
	private String nombres;
	private String apellidos;
	private String sexo;
	private String direccion;
	private String telefono;
	private String municipio;
	
	private String fecha;
	
	private ArrayList<RegistroMunicipio> regMunicipios;
	
	private Button botonFecha;
	private Spinner listaDepartamento;
	private Spinner listaMunicipio;
	private Button botonRegistrar;
	private CheckBox cbCondiciones;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		botonFecha = (Button) findViewById(id.btnFechaNacimiento);
		botonRegistrar = (Button) findViewById(id.btnRegistrar);
		
		listaDepartamento = (Spinner) findViewById(id.spinnerDepartamentos);
		listaMunicipio = (Spinner) findViewById(id.spinnerMunicipios);
		
		cbCondiciones = (CheckBox) findViewById(id.checkBoxCondiciones);
		
		botonFecha.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				DatePickerFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		
		botonRegistrar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				registrarUsuario();
			}
		});
		
		botonRegistrar.setEnabled(false);
		
		TextView tvCondiciones = (TextView) findViewById(id.tvCondiciones);
		
		tvCondiciones.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String condiciones = "Al hacer uso de nuestros servicios estas aceptando nuestras condiciones de uso, las cuales establecen que:\n" +
						"1. Podemos acceder a tu ubicación a través del GPS, la cual será almacenada en nuestros servidores.\n" +
						"2. Tendrán acceso a tu ubicación solo usuarios autorizados por ti, un paciente autoriza automáticamente a cualquier usuario médico que lo esté monitoreando.\n" +
						"3. La información de ubicación será usada con fines estadísticos y será suministrada a terceros, pero tu información personal no será utilizada para este propósito y permanecerá anónima.";
				
				AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
				
				dialog.setTitle("Condiciones de uso");
				dialog.setMessage(condiciones);
				dialog.setCancelable(false);
				dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				dialog.show();
			}
		});
		
		cbCondiciones.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(cbCondiciones.isChecked()){
					botonRegistrar.setEnabled(true);
				}else{
					botonRegistrar.setEnabled(false);
				}
			}
		});
		
		ObtenerDepartamentos od = new ObtenerDepartamentos();
		od.setParams(RegisterActivity.this);
		od.execute();
	}

	@Override
	public void onFinishDatePickerDialog(int year, int month, int day) {
		
		fecha = year + "-" + month + "-" + day;
		botonFecha.setText(fecha);
	}
	
	public void cargarDepartamentos(final ArrayList<RegistroDepartamento> departamentos){
		
		String datos[] = new String[departamentos.size()];
		
		for(int i=0; i<departamentos.size(); i++){
			
			datos[i] = departamentos.get(i).getNombre();
		}
		
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				datos);
		
		listaDepartamento.setAdapter(adaptador);
		
		listaDepartamento.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int index, long id) {
				
				ObtenerMunicipios om = new ObtenerMunicipios();
				om.setParams(RegisterActivity.this);
				om.execute(departamentos.get(index).getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void cargarMunicipios(ArrayList<RegistroMunicipio> municipios){
		
		regMunicipios = municipios;
		
		String datos[] = new String[municipios.size()];
		
		for(int i=0; i<municipios.size(); i++){
			
			datos[i] = municipios.get(i).getNombre();
		}
		
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				datos);
		
		listaMunicipio.setAdapter(adaptador);
	}
	
	private void registrarUsuario(){
		
		EditText etNombres = (EditText) findViewById(id.edtNombres);
		EditText etApellidos = (EditText) findViewById(id.edtApellidos);
		Spinner spinTipoId = (Spinner) findViewById(id.spinnerTipoId);
		EditText etNumeroId = (EditText) findViewById(id.edtNumeroId);
		EditText etCorreo = (EditText) findViewById(id.edtCorreo);
		EditText etClave = (EditText) findViewById(id.edtClave);
		Spinner spinSexo = (Spinner) findViewById(id.spinnerTipoSexo);
		EditText etDireccion = (EditText) findViewById(id.etDireccion);
		EditText etTelefono = (EditText) findViewById(id.etTelefono);
		
		nombres = etNombres.getText().toString();
		apellidos = etApellidos.getText().toString();
		tipoId = (String) spinTipoId.getSelectedItem();
		numId = etNumeroId.getText().toString();
		correo = etCorreo.getText().toString();
		clave = etClave.getText().toString();
		sexo = (String) spinSexo.getSelectedItem();
		direccion = etDireccion.getText().toString();
		telefono = etTelefono.getText().toString();
		municipio = regMunicipios.get(listaMunicipio.getSelectedItemPosition()).getId() + "";
		
		if(validarDatos()){
			
			RegistrarPaciente tarea = new RegistrarPaciente();
			tarea.setParams(getApplicationContext());
			tarea.execute(correo, clave, tipoId, numId, nombres, apellidos, sexo, fecha, direccion,
					telefono, municipio);
			
		} else {
			
			Toast toast = Toast.makeText(getApplicationContext(), "Debe llenar todos los campos",
					Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	private boolean validarDatos(){
		
		if(correo == null || correo.isEmpty()){
			return false;
		}
		
		if(clave == null || clave.isEmpty()){
			return false;
		}
		
		if(tipoId == null || tipoId.isEmpty()){
			return false;
		}
		
		if(numId == null || numId.isEmpty()){
			return false;
		}
		
		if(nombres == null || nombres.isEmpty()){
			return false;
		}
		
		if(apellidos == null || apellidos.isEmpty()){
			return false;
		}
		
		if(sexo == null || sexo.isEmpty()){
			return false;
		}
		
		if(direccion == null || direccion.isEmpty()){
			return false;
		}
		
		if(telefono == null || telefono.isEmpty()){
			return false;
		}
		
		if(fecha == null || fecha.isEmpty()){
			return false;
		}
		
		if(municipio == null || municipio.isEmpty()){
			return false;
		}
		
		return true;
		
	}

}
