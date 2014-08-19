package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.tesis.app.paciente.ResultadoBuscarSolicitud;
import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;
import com.tesis.app.paciente.wsobj.RegistroMedico;

import android.os.AsyncTask;

public class BuscarMedico extends AsyncTask<String, Void, Void> {

	private ResultadoBuscarSolicitud mContext;
	private ArrayList<RegistroMedico> registro;
	
	public void setParams(ResultadoBuscarSolicitud context){
		mContext = context;
		registro = null;
	}
	
	@Override
	protected Void doInBackground(String... params) {

		String nombreBuqueda = params[0];
		
		SessionManagement session = new SessionManagement(mContext);
		UrlServer config = new UrlServer(mContext);
		String url = config.getUrl();

		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL"; //cambiar la url
		final String METHOD_NAME = "buscarMedico";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/buscarMedicoRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", session.getEmail());
		request.addProperty("clave", session.getPass());
		request.addProperty("tipoBusqueda", 0);
		request.addProperty("criterio", nombreBuqueda);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try {
			transporte.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			
			String resultado = resultado_xml.toString();
			
			int inicio, fin;
			
			inicio = -1;
			fin = resultado.indexOf("\n");
			
			registro = new ArrayList<RegistroMedico>();
			
			while(fin != -1){
				
				String linea = resultado.substring(inicio + 1, fin);
				
				String[] datos = linea.split(";");
					
				RegistroMedico temp = new RegistroMedico();
				
				temp.setCedula(datos[0]);
				temp.setNombres(datos[1]);
				temp.setApellidos(datos[2]);
				temp.setTelefono(datos[3]);
				temp.setCorreo(datos[4]);
				temp.setSexo(datos[5]);
				
				registro.add(temp);
				
				inicio = fin;
				fin = resultado.indexOf("\n", inicio + 1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(registro != null && registro.size() > 0){
			
			RegistroMedico[] reg = new RegistroMedico[registro.size()];
			
			registro.toArray(reg);
			
			mContext.cargarListaMedico(reg);
		}
		
		
	}

}
