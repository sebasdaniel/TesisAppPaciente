package com.tesis.app.paciente;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tesis.app.paciente.tareas.MonitoreoAutomatico;

public class HomeActivity extends Activity {

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
	private static final String PROPERTY_USER = "user";
	private static final long EXPIRATION_TIME_MS = 1000 * 3600 * 24;
	
	String SENDER_ID = "137850652350";

	static final String TAG = "GCMDemo";

	GoogleCloudMessaging gcm;
	
	private SessionManagement session;
	private String regid;
	
	private MonitoreoAutomatico auto;
	protected ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		session = new SessionManagement(getApplicationContext());
		
//		if(!session.isLoggedIn()){
//			session.logout();
//		}
		
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(session.getName());
		
		ArrayList<ListEntrada> datos = new ArrayList<ListEntrada>();
		
		datos.add(new ListEntrada(R.drawable.ic_action_monitoreo, "Monitoreo", "Comenzar mis chequeos del dia"));
		datos.add(new ListEntrada(R.drawable.ic_action_automatico, "Automatico", "Solo aplica para ECG"));
		datos.add(new ListEntrada(R.drawable.ic_action_diag, "Diagnostico", "Revisar los diagnosticos hechos por mi medico"));
		datos.add(new ListEntrada(R.drawable.ic_action_estadistica, "Estadisticas", "Revisar las estadisticas del estado de mi salud"));
		datos.add(new ListEntrada(R.drawable.ic_action_historial, "Historial", "Revisar mi historial"));
		
		ListView mListView = (ListView) findViewById(R.id.listViewHome);
		
		mListView.setAdapter(new ListAdaptador(this, R.layout.enter, datos) {
			
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
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> padre, View v, int itemPosition,	long id) {
				
				switch (itemPosition) {
				
				case 0:
					Intent monitoreos = new Intent(HomeActivity.this, MonitorsActivity.class);
					startActivity(monitoreos);
					break;
					
				case 1:
					pDialog = new ProgressDialog(HomeActivity.this);
			        pDialog.setMessage("Monitoreo Automatico Activado.");
			        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			        pDialog.setCancelable(false);
			        pDialog.show();
//					UrlServer conf = new UrlServer(getApplicationContext());
//					auto = new MonitoreoAutomatico();
//					auto.execute(session.getEmail(), session.getPass(), conf.getUrl(), "0", "0");
					break;
					
				case 2:
					Intent diagnosticos = new Intent(getApplicationContext(), ListaDiagnostico.class);
					startActivity(diagnosticos);
					break;

				case 3:
					Intent estadisticas = new Intent(HomeActivity.this, ConfigEstadistica.class);
					startActivity(estadisticas);
					break;
					
				case 4:
					Intent historial = new Intent(HomeActivity.this, HistorialActivity.class);
					startActivity(historial);
					break;
//				case 5:
//					Intent solicitudes = new Intent(getApplicationContext(), BusquedaSolicitud.class);
//					startActivity(solicitudes);
//					break;
//					
//				case 7:
//					//SessionManagement session = new SessionManagement(getApplicationContext());
//					session.logout();
//					finish();
//					break;
					
				default:
					break;
				}
				
			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> padre, View v, int index, long id) {
				
				ListEntrada elegido = (ListEntrada) padre.getItemAtPosition(index);
				CharSequence texto = "Seleccionado: " + elegido.get_textoDebajo();
				Toast toast = Toast.makeText(HomeActivity.this, texto, Toast.LENGTH_LONG);
				toast.show();
				
				return true;
			}
		});
		
		verificarGCM();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}
	
	public void iniciarActividadEstadistica(){
		Intent intent = new Intent(HomeActivity.this, Estadisticas.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		
		case R.id.action_solicitudes:
			Intent solicitudes = new Intent(getApplicationContext(), BusquedaSolicitud.class);
			startActivity(solicitudes);
			return true;
			
		case R.id.action_configuracion:
			Intent config = new Intent(getApplicationContext(), ConfiguracionActivity.class);
			startActivity(config);
			return true;
			
		case R.id.action_salir:
			if(auto != null){
				auto.cancel(true);
			}
			session.logout();
			finish();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	private void verificarGCM(){
		//Chequemos si está instalado Google Play Services
        if(checkPlayServices()){
        	
            gcm = GoogleCloudMessaging.getInstance(HomeActivity.this);
 
            //Obtenemos el Registration ID guardado
            regid = getRegistrationId(getApplicationContext());
 
            //Si no disponemos de Registration ID comenzamos el registro
            if (regid.equals("")) {
                RegistroGCM tarea = new RegistroGCM();
                tarea.execute(session.getEmail());
            }
            
        } else {
        	Log.i(TAG, "No se ha encontrado Google Play Services.");
        }
	}
	
	private boolean checkPlayServices() {
		
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if (resultCode != ConnectionResult.SUCCESS) {
			
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				//Log.i(TAG, "Dispositivo no soportado.");
				System.out.println("Dispositivo no soportado.");
				finish();
			}
			return false;
		}
		
		return true;
	}

	private String getRegistrationId(Context context) {
		
		SharedPreferences prefs = getSharedPreferences("GcmPreferencesPaciente", Context.MODE_PRIVATE);

		String registrationId = prefs.getString(PROPERTY_REG_ID, "");

		if (registrationId.length() == 0) {
			//Log.d(TAG, "Registro GCM no encontrado.");
			System.out.println("Registro GCM no encontrado.");
			return "";
		}

		// mostrar dtos de registro almacenado en preferences
		String registeredUser = prefs.getString(PROPERTY_USER, "user");

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);

		long expirationTime = prefs.getLong(PROPERTY_EXPIRATION_TIME, -1);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
		
		String expirationDate = sdf.format(new Date(expirationTime));

//		Log.d(TAG, "Registro GCM encontrado (usuario=" + registeredUser
//				+ ", version=" + registeredVersion + ", expira="
//				+ expirationDate + ")");
		
		System.out.println("Registro GCM encontrado (usuario=" + registeredUser + ", version="
				+ registeredVersion + ", expira=" + expirationDate + ")");

		int currentVersion = getAppVersion(context);

		if (registeredVersion != currentVersion) {
			//Log.d(TAG, "Nueva versión de la aplicación.");
			System.out.println("Nueva versión de la aplicación.");
			return "";
		} else if (System.currentTimeMillis() > expirationTime) {
			//Log.d(TAG, "Registro GCM expirado.");
			System.out.println("Registro GCM expirado.");
			return "";
		} else if (!session.getEmail().equals(registeredUser)) {
			//Log.d(TAG, "Nuevo nombre de usuario.");
			System.out.println("Nuevo nombre de usuario.");
			return "";
		}

		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

			return packageInfo.versionCode;
			
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Error al obtener versión: " + e);
		}
	}
	
	private class RegistroGCM extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			
			String msg = "";

			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(HomeActivity.this);
				}

				// Nos registramos en los servidores de GCM
				regid = gcm.register(SENDER_ID);

				//Log.d(TAG, "Registrado en GCM: registration_id=" + regid);
				System.out.println("Registrado en GCM: registration_id=" + regid);

				// Nos registramos en nuestro servidor
				boolean registrado = registroServidor(params[0], regid);

				// Guardamos los datos del registro
				if (registrado) {
					setRegistrationId(HomeActivity.this, params[0], regid);
				}
			} catch (IOException ex) {
				//Log.d(TAG, "Error registro en GCM:" + ex.getMessage());
				System.out.println("Error registro en GCM:" + ex.getMessage());
			}

			return msg;
	        
		}

	}
	
	private boolean registroServidor(String usuario, String regId) {
		
		boolean reg = false;
		
		UrlServer config = new UrlServer(HomeActivity.this);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "registrarGCM";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/registrarGCMRequest";

		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("correo", usuario);
		request.addProperty("clave", session.getPass());
		request.addProperty("regGCM", regId);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(request);

		HttpTransportSE transporte = new HttpTransportSE(URL);

		try {
			transporte.call(SOAP_ACTION, envelope);
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			String res = resultado_xml.toString();

			if (res.equals("ok")) {
				//Log.d(TAG, "Registrado en mi servidor.");
				System.out.println("Registrado en mi servidor.");
				reg = true;
			}
			
			System.out.println("salida servidor: " + res);
		} catch (Exception e) {
//			Log.d(TAG, "Error registro en mi servidor: " + e.getCause()
//					+ " || " + e.getMessage());
			System.out.println("Error registro en mi servidor: " + e.getCause() + " || " + e.getMessage());
		}

		return reg;
	}
	
	private void setRegistrationId(Context context, String user, String regId) {
		
		SharedPreferences prefs = getSharedPreferences("GcmPreferencesPaciente", Context.MODE_PRIVATE);

		int appVersion = getAppVersion(context);

		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(PROPERTY_USER, user);
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.putLong(PROPERTY_EXPIRATION_TIME, System.currentTimeMillis() + EXPIRATION_TIME_MS);

		editor.commit();
	}

}
