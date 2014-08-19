package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.tesis.app.paciente.LoginActivity;
import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;

//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

public class ValidarUsuario extends AsyncTask<String, Void, Void> {

	private String resultado;
	//private SharedPreferences preferences;
	private LoginActivity mActivity;
	private String correo;
	private String clave;
	
	public void setParams(LoginActivity activity){
		mActivity = activity;
		//this.preferences = preferences;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		correo = params[0];
		clave = params[1];
		
		UrlServer config = new UrlServer(mActivity);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL"; //cambiar la url
		final String METHOD_NAME = "validarUsuario";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/validarUsuarioRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", correo);
		request.addProperty("clave", clave);
		request.addProperty("roll", 0);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try {
			transporte.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			
			resultado = resultado_xml.toString();
			
		} catch (Exception e) {
			resultado = "none";
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(!resultado.equals("none")){
			
//			Editor preferenceEditor = preferences.edit();
//			preferenceEditor.putString("correo", correo);
//			preferenceEditor.putString("clave", clave);
//			
//			preferenceEditor.commit();
			
			SessionManagement session = new SessionManagement(mActivity);
			session.saveSession(correo, clave, resultado);
			mActivity.iniciar();
			
		} else {
			
			mActivity.mostrarResultado("Correo o clave incorrectos!");
		}
	}

}
