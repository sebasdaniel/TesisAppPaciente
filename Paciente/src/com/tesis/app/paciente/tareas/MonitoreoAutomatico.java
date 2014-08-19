package com.tesis.app.paciente.tareas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

public class MonitoreoAutomatico extends AsyncTask<String, Void, Void> {

	//private boolean cancelar = false;
	@Override
	protected Void doInBackground(String... params) {
		
		Random random = new Random();
		
		while(true){
			
			int valor = random.nextInt(150 - 30) + 30;
			
			enviarDatos(params[0], params[1], params[2], String.valueOf(valor), params[3], params[4]);
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(isCancelled()){
				break;
			}
		}
		
		return null;
	}
	
	private void enviarDatos(String correo, String clave, String url, String valor, String lat, String lon){
		
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		
		String fecha = formater.format(Calendar.getInstance().getTime());
		
		formater = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
		
		String hora = formater.format(Calendar.getInstance().getTime());
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "guardarChequeo";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/guardarChequeoRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", correo);
		request.addProperty("clave", clave);
		request.addProperty("tipo", "arritmia");
		request.addProperty("descripcion", "reposo");
		request.addProperty("valor", valor);
		request.addProperty("unidades", "ppm");
		request.addProperty("fecha", fecha);
		request.addProperty("hora", hora);
		request.addProperty("latitud", lat);
		request.addProperty("longitud", lon);
		request.addProperty("nota", "Monitoreo Automatico");
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try {
			transporte.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			
			System.out.println(resultado_xml.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	@Override
//	protected void onCancelled() {
//		super.onCancelled();
//		
//		cancelar = true;
//	}

}
