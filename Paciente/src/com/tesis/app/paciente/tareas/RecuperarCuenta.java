package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.tesis.app.paciente.UrlServer;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

public class RecuperarCuenta extends AsyncTask<String, Void, Void> {

	private Context context;
	private String resultado;
	private ProgressDialog pDialog;
	
	public void setParams(Context context){
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		UrlServer config = new UrlServer(context);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "recuperarClave";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/recuperarClaveRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", params[0]);
		
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
			resultado = "";
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(context);
        pDialog.setMessage("Enviando, por favor espere.");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.show();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		pDialog.dismiss();
		
		String msg;
		
		if(resultado.equals("ok")){
			
			msg = "La contraseña ha sido enviada a su correo";
		} else {
			
			msg = "Ocurrio un error al enviar los datos, por favor intente de nuevo";
		}
		
		new AlertDialog.Builder(context).setTitle("Informacion")
		.setMessage(msg).setPositiveButton("Aceptar", new OnClickListener() {
	    	
	        public void onClick(DialogInterface arg0, int arg1) {
	        	
	        }
	        
	    }).show();
	}

}
