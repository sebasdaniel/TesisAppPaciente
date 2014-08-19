package com.tesis.app.paciente;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MonitorsActivity extends Activity {
	
//	private final String monitoreos[] = {"Glucosa", "Presion", "Electrocardiograma"};
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket socket;
	public static final int GLUCOMETRO = 1, TENSIOMETRO = 2, PULSIOMETRO = 3;
	private int monitorDevice;
	private String glucosaEstado;
	private SessionManagement session;
	private String lat;
	private String lon;
	private String mNota;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitors);
		
		session = new SessionManagement(getApplicationContext());
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(session.getName());
		
		ArrayList<ListEntrada> datos = new ArrayList<ListEntrada>();
		
		datos.add(new ListEntrada(R.drawable.ic_action_glucosa, "Glucosa", "Comenzar mis chequeos del dia"));
		datos.add(new ListEntrada(R.drawable.ic_action_presion, "Presion", "Comenzar mis chequeos del dia"));
		datos.add(new ListEntrada(R.drawable.ic_action_ecg, "Electrocardiograma", "Comenzar mis chequeos del dia"));
		
		ListView monitorsList = (ListView) findViewById(R.id.listMonitors);
		
//		ArrayAdapter<String>  adaptador = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, monitoreos);
		
		//monitorsList.setAdapter(adaptador);
		
		monitorsList.setAdapter(new ListAdaptador(this, R.layout.enter, datos) {
			
			@Override
			public void onEntrada(Object entrada, View view) {
				
				if (entrada != null){
					
					TextView texto_superior_enter = (TextView) view.findViewById(R.id.textView_superior);
					
					  if (texto_superior_enter != null)
						  texto_superior_enter.setText(((ListEntrada) entrada).get_textoEncima());
					  
					  ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);
					  
					  if (imagen_entrada != null)
						  imagen_entrada.setImageResource(((ListEntrada) entrada).get_IdImage());
			        }
			}
		});
		
		monitorsList.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> padre, View v, int posicionSeleccionada, long id) {
				
				switch (posicionSeleccionada) {
				case 0:
					
					AlertDialog.Builder builder = new AlertDialog.Builder(MonitorsActivity.this);
					builder.setTitle("Estado").setItems(R.array.glucosa_estados, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							switch (which) {
							case 0:
								glucosaEstado = "antes";
								break;
							case 1:
								glucosaEstado = "despues";
								break;
							default:
								glucosaEstado = "";
								break;
							}
							
							monitorDevice = GLUCOMETRO;
//							startBluetooth();
							mostrarDialogoNota(0, "");
						}
					});
					
					AlertDialog dialog = builder.create();
					dialog.show();
					break;
					
				case 1:
					//ShowMessage("Mensaje", "Seleccionaste prueba presion");
//					monitorDevice = TENSIOMETRO;
//					startBluetooth();
					LayoutInflater li = LayoutInflater.from(MonitorsActivity.this);
					View promptPresion = li.inflate(R.layout.prompt_presion, null);
					
					final EditText sistolica = (EditText) promptPresion.findViewById(R.id.edtSistolica);
					final EditText diastolica = (EditText) promptPresion.findViewById(R.id.edtDiastolica);
					
					TextView promptMsg = (TextView) promptPresion.findViewById(R.id.tvMsgPrompPresion);
					final TextView tvErrorP = (TextView) findViewById(R.id.tvErrorPresion);
					
					promptMsg.setText("Ingrese el valor de su presion");
					
					AlertDialog.Builder adBuilder = new AlertDialog.Builder(MonitorsActivity.this);
					adBuilder.setView(promptPresion);
					
					adBuilder.setCancelable(false).setPositiveButton("Aceptar", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							
							if(!sistolica.getText().toString().isEmpty()
									&& !diastolica.getText().toString().isEmpty()){
								
								localizar();
								
								if(Validacion.isNumeric(sistolica.getText().toString())
										&& Validacion.isNumeric(diastolica.getText().toString())){
									
									String valor = sistolica.getText().toString() + "/"
										+ diastolica.getText().toString();
									
//									GuardarMonitoreoPresion gmp = new GuardarMonitoreoPresion();
//						        	gmp.setContext(MonitorsActivity.this);
//						        	gmp.execute(valor, obtenerFecha(), obtenerHora(), lat, lon);
									mostrarDialogoNota(1, valor);
						        	
								} else {
									
									tvErrorP.setText("Error: Al parecer los datos no son correctos");
								}
								
							} else {
								
								tvErrorP.setText("Error: los campos no deben estar vacios");
							}
							
						}
					}).setNegativeButton("Cancelar", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							dialog.cancel();
						}
					});
					
					AlertDialog alertDialog = adBuilder.create();
					alertDialog.show();
					
					break;
					
				case 2:
					//ShowMessage("Mensaje", "Seleccionaste prueba ECG");
//					monitorDevice = PULSIOMETRO;
//					startBluetooth();
					LayoutInflater li2 = LayoutInflater.from(MonitorsActivity.this);
					View prompt2 = li2.inflate(R.layout.prompt_arritmia, null);
					
					final EditText entrada2 = (EditText) prompt2.findViewById(R.id.etPrompt);
					TextView promptMsg2 = (TextView) prompt2.findViewById(R.id.tvPromptMsg);
					final TextView tvErrorA = (TextView) findViewById(R.id.tvErrorArritmia);
					
					promptMsg2.setText("Ingrese el valor de su ritmo cardiaco");
					
					AlertDialog.Builder adBuilder2 = new AlertDialog.Builder(MonitorsActivity.this);
					adBuilder2.setView(prompt2);
					
					adBuilder2.setCancelable(false).setPositiveButton("Aceptar", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							
							if(entrada2.getText().toString() != ""){
								
								if(Validacion.isNumeric(entrada2.getText().toString())){
									localizar();
								
//									GuardarMonitoreoArritmia gma = new GuardarMonitoreoArritmia();
//									gma.setContext(MonitorsActivity.this);
//									gma.execute(entrada2.getText().toString(), obtenerFecha(), obtenerHora(), lat, lon);
									mostrarDialogoNota(2, entrada2.getText().toString());
								} else {
									tvErrorA.setText("Error: el valor no es valido");
								}
								
							} else {
								tvErrorA.setText("Error: el campo no puede estar vacio");
							}
						}
					}).setNegativeButton("Cancelar", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							dialog.cancel();
						}
					});
					
					AlertDialog alertDialog2 = adBuilder2.create();
					alertDialog2.show();
					break;
					
				default:
					break;
				}
				
			}
		});
	}
	
	private void mostrarDialogoNota(final int opcion, final String valor){
		
		LayoutInflater inflater = LayoutInflater.from(MonitorsActivity.this);
		View promptPresion = inflater.inflate(R.layout.prompt_nota, null);
		
		final EditText nota = (EditText) promptPresion.findViewById(R.id.edtNota);
		
		AlertDialog.Builder adBuilder = new AlertDialog.Builder(MonitorsActivity.this);
		adBuilder.setView(promptPresion);
		
		adBuilder.setCancelable(false).setPositiveButton("Aceptar", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
				mNota = nota.getText().toString();
				
				switch (opcion) {
				case 0:
					startBluetooth();
					break;
				case 1:
					GuardarMonitoreoPresion gmp = new GuardarMonitoreoPresion();
		        	gmp.setContext(MonitorsActivity.this);
		        	gmp.execute(valor, obtenerFecha(), obtenerHora(), lat, lon, mNota);
					break;
				case 2:
					GuardarMonitoreoArritmia gma = new GuardarMonitoreoArritmia();
					gma.setContext(MonitorsActivity.this);
					gma.execute(valor, obtenerFecha(), obtenerHora(), lat, lon, mNota);
					break;
				default:
					break;
				}
			}
		}).setNegativeButton("Omitir", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				switch (opcion) {
				case 0:
					startBluetooth();
					break;
				case 1:
					GuardarMonitoreoPresion gmp = new GuardarMonitoreoPresion();
		        	gmp.setContext(MonitorsActivity.this);
		        	gmp.execute(valor, obtenerFecha(), obtenerHora(), lat, lon, "");
					break;
				case 2:
					GuardarMonitoreoArritmia gma = new GuardarMonitoreoArritmia();
					gma.setContext(MonitorsActivity.this);
					gma.execute(valor, obtenerFecha(), obtenerHora(), lat, lon, "");
					break;
				default:
					break;
				}
				
				dialog.cancel();
			}
		});
		
		AlertDialog alertDialog = adBuilder.create();
		alertDialog.show();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			conectarDispositivo();
		} else {
			ShowMessage("Alerta", "No prendiste el Bluetooth, de esta forma no se pueden obtener los datos");
		}
	}
	
	private void startBluetooth(){
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(mBluetoothAdapter != null){
			
			if(!mBluetoothAdapter.isEnabled()){
				
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				int REQUEST_ENABLE_BT = 1;
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				
			} else {
				conectarDispositivo();
			}
			
		} else {
			ShowMessage("Advertencia", "Lo sentimos pero tu dispositivo no soporta Bluetooth");
		}
	}
	
	public void conectarDispositivo(){
		
		Set<BluetoothDevice> pariedDevices = mBluetoothAdapter.getBondedDevices();
		
		if(pariedDevices.size() > 0){
			
			Boolean encontrado = false;
			
			String nameDevice = "";
			
			switch (monitorDevice) {
			
			case GLUCOMETRO:
				nameDevice = "myglucohealth";
				break;
				
			case TENSIOMETRO:
				nameDevice = "53B45-PC";
				break;
				
			case PULSIOMETRO:
				nameDevice = "53B45-PC";
			break;

			default:
				nameDevice = "";
				break;
			}
			
			for(BluetoothDevice device : pariedDevices){
				
				if(device.getName().equalsIgnoreCase(nameDevice)){
					
					UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
					
					try {
						socket = device.createRfcommSocketToServiceRecord(uuid);
						
						mBluetoothAdapter.cancelDiscovery();
						
						socket.connect();
						
//						String lat, lon;
//						
//						LocationManager locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
//						
//						if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//							//System.out.println("----gps habilitado----");
//							Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//							
//							if(location != null){
//								lat = String.valueOf(location.getLatitude());
//								lon = String.valueOf(location.getLongitude());
//							} else {
//								lat = "0";
//								lon = "0";
//							}
//							
//							//System.out.println("latitud: " + lat + "\nlongitud" + lon);
//						} else {
//							//System.out.println("----gps no habilitado----");
//							lat = "0";
//							lon = "0";
//						}
						
						localizar();
						
						
						if(nameDevice.equals("myglucohealth")){
							GlucoBluetoothTask tarea = new GlucoBluetoothTask();
							tarea.setParams(MonitorsActivity.this, socket, glucosaEstado, lat, lon, mNota);
							tarea.execute();
						}else{
							LeerBluetooth mLeerBluetooth = new LeerBluetooth();
							mLeerBluetooth.establecerParametros(MonitorsActivity.this, socket, glucosaEstado,
									monitorDevice, lat, lon, mNota);
							mLeerBluetooth.execute();
						}
						
					} catch (IOException e) {
						e.printStackTrace();
						ShowMessage("Alerta", "Error al conectar con el dispositivo, verifique que este encendido");
					}
					
					encontrado = true;
					break;
				}
			}
			
			if(!encontrado){
				ShowMessage("Mensaje", "No se encontro el dispositivo");
			}
			
		} else {
			ShowMessage("Advertencia", "No tienes dispositivos vinculados");
		}
		
	}
	
	@SuppressLint("SimpleDateFormat")
	public String obtenerFecha(){
		
		Calendar c = Calendar.getInstance();
		//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}
	
	@SuppressLint("SimpleDateFormat")
	public String obtenerHora(){
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(c.getTime());
	}
	
	public void ShowMessage( String title, String message ){
		
	    new AlertDialog.Builder(this).setTitle( title ).setMessage( message )
	    .setPositiveButton("OK", new OnClickListener() {
	    	
	        public void onClick(DialogInterface arg0, int arg1) {
	          //finish();
	        }
	        
	    }).show();
	    
	}
	
	private void localizar(){
		
		lat = "0";
		lon = "0";
		
		LocationManager locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			
			Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if(location != null){
				lat = String.valueOf(location.getLatitude());
				lon = String.valueOf(location.getLongitude());
			}
		}
		
	}
	
}

