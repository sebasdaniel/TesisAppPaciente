package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tesis.app.paciente.UrlServer;

public class RegistrarPaciente extends AsyncTask<String, Void, Void> {

	private String resultado;
	private Context context;
	
	public void setParams(Context context){
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		UrlServer config = new UrlServer(context);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "registrarPaciente";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/registrarPacienteRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", params[0]);
		request.addProperty("clave", params[1]);
		request.addProperty("tipoId", params[2]);
		request.addProperty("numeroId", params[3]);
		request.addProperty("nombres", params[4]);
		request.addProperty("apellidos", params[5]);
		request.addProperty("sexo", params[6]);
		request.addProperty("fechaNacimiento", params[7]);
		request.addProperty("direccion", params[8]);
		request.addProperty("telefono", params[9]);
		request.addProperty("idMunicipio", Integer.parseInt(params[10]));
		
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
		
		System.out.println(resultado);
		
		if(resultado.equals("ok")){
			Toast toast = Toast.makeText(context, "Paciente registrado correctamente", Toast.LENGTH_LONG);
			toast.show();
			
		} else {
			
			Toast toast = Toast.makeText(context, "Error al registrar paciente", Toast.LENGTH_LONG);
			toast.show();
		}
	}

}
