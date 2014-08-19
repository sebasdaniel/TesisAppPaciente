package com.tesis.app.paciente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

public class LeerBluetooth extends AsyncTask<Void, Void, Void> {

	private MonitorsActivity mmContext;
	private BluetoothSocket mmSocket;
	private String tipoMedicion;
	private ProgressDialog mmDialog;
	private String lectura = "";
	private int mmDevice;
	private String latitud;
	private String longitud;
	private String nota;
	
	public void establecerParametros(MonitorsActivity context, BluetoothSocket socket, String tipo, int device,
			String lat, String lon, String nota){
		
		mmContext = context;
		mmSocket = socket;
		tipoMedicion = tipo;
		mmDevice = device;
		latitud = lat;
		longitud = lon;
		this.nota = nota;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		try {
			InputStream in = mmSocket.getInputStream();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
			
			lectura = bReader.readLine();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mmDialog = new ProgressDialog(mmContext);
	    mmDialog.setMessage("Esperando datos, por favor espere.");
	    mmDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    mmDialog.setCancelable(false);
	    mmDialog.show();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		mmDialog.dismiss();
		
		if(lectura != ""){
			
//			GuardarMonitoreoGlucosa gmg = new GuardarMonitoreoGlucosa();
//        	gmg.setContext(mmContext);
//        	gmg.execute(tipoMedicion, lectura, mmContext.obtenerFecha(), mmContext.obtenerHora());
        	
			switch (mmDevice) {
			
			case MonitorsActivity.GLUCOMETRO:
				GuardarMonitoreoGlucosa gmg = new GuardarMonitoreoGlucosa();
	        	gmg.setContext(mmContext);
	        	gmg.execute(tipoMedicion, lectura, mmContext.obtenerFecha(), mmContext.obtenerHora());
				break;
				
			case MonitorsActivity.TENSIOMETRO:
				GuardarMonitoreoPresion gmp = new GuardarMonitoreoPresion();
	        	gmp.setContext(mmContext);
	        	gmp.execute(lectura, mmContext.obtenerFecha(), mmContext.obtenerHora(), latitud, longitud, nota);
	        	break;

			case MonitorsActivity.PULSIOMETRO:
				GuardarMonitoreoArritmia gma = new GuardarMonitoreoArritmia();
				gma.setContext(mmContext);
				gma.execute(lectura, mmContext.obtenerFecha(), mmContext.obtenerHora(), latitud, longitud, nota);
				break;
				
			default:
				break;
			}
        	
        }
		
		cerraSocket();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		cerraSocket();
	}
	
	public void cerraSocket(){
		try {
			mmSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
