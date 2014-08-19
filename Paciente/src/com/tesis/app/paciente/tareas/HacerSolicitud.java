package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

import com.tesis.app.paciente.DatosConsultorio;
import com.tesis.app.paciente.DatosMedico;
import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;

public class HacerSolicitud extends AsyncTask<String, Void, Void> {

	private String resultado;
	private DatosConsultorio consultorio;
	private DatosMedico medico;
	
	public void setParams(DatosConsultorio con, DatosMedico med){
		consultorio = con;
		medico = med;
		resultado = null;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		SessionManagement session = new SessionManagement((medico != null)? medico :  consultorio);
		UrlServer config = new UrlServer((medico != null)? medico :  consultorio);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL"; //cambiar la url
		final String METHOD_NAME = "hacerSolicitud";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/hacerSolicitudRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", session.getEmail());
		request.addProperty("clave", session.getPass());
		request.addProperty("entidad", Integer.parseInt(params[0]));
		request.addProperty("id", params[1]);
		
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
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		String msg = null;
		
		if(resultado != null && resultado.equals("ok")){
			msg = "La solicitud se envio correctamente";
		} else if(resultado != null && resultado.equals("fail")){
			msg = "Error al enviar la solicitud";
		}
		
		if(msg != null){
			
			new AlertDialog.Builder(((medico != null)? medico : consultorio)).setTitle("Mensaje").setMessage(msg)
			.setPositiveButton("Aceptar", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			}).show();
		}
		
	}

}
