package com.tesis.app.paciente;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

public class GuardarMonitoreoPresion extends AsyncTask<String, Void, Void>{

	ProgressDialog pDialog;
	MonitorsActivity mContext;
	String resultado;
	String tip;
	
	public void setContext(MonitorsActivity contex){
		this.mContext = contex;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		SessionManagement session = new SessionManagement(mContext);
		UrlServer config = new UrlServer(mContext);
		String url = config.getUrl();
		
		resultado = params[0];
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL"; //cambiar la url
		final String METHOD_NAME = "guardarChequeo";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/guardarChequeoRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", session.getEmail());
		request.addProperty("clave", session.getPass());
		request.addProperty("tipo", "presion");
		request.addProperty("descripcion", "reposo");
		request.addProperty("valor", params[0]);
		request.addProperty("unidades", "mmHg");
		request.addProperty("fecha", params[1]);
		request.addProperty("hora", params[2]);
		request.addProperty("latitud", params[3]);
		request.addProperty("longitud", params[4]);
		request.addProperty("nota", params[5]);
		
//		request.addProperty("valor", params[0]);
//		request.addProperty("unidades", "mmHg");
//		request.addProperty("fecha", params[1]);
//		request.addProperty("hora", params[2]);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try {
			transporte.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			
			tip = resultado_xml.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Procesando, espere.");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //pDialog.setCancelable(true);
        pDialog.show();
	}
	
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		pDialog.dismiss();
		
		new AlertDialog.Builder(mContext).setTitle("Resultado")
		.setMessage("Su resultado fue " + resultado + " mmHg\n\n" + tip)
	    .setPositiveButton("OK", new OnClickListener() {
	    	
	        public void onClick(DialogInterface arg0, int arg1) {
	          //finish();
	        }
	        
	    }).show();
	}
}
