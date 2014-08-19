package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

import com.tesis.app.paciente.ResultadoBuscarSolicitud;
import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;
import com.tesis.app.paciente.wsobj.RegistroConsultorio;

public class BuscarConsultorio extends AsyncTask<String, Void, Void> {

	private ResultadoBuscarSolicitud mContext;
	private ArrayList<RegistroConsultorio> registro;
	
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
		//final String URL = "http://192.168.43.214:8080/SIMOP/SIMOP?WSDL"; //cambiar la url
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "buscarConsultorio";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/buscarConsultorioRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", session.getEmail());
		request.addProperty("clave", session.getPass());
		request.addProperty("nombre", nombreBuqueda);
		
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
			
			registro = new ArrayList<RegistroConsultorio>();
			
			while(fin != -1){
				
				String linea = resultado.substring(inicio + 1, fin);
				
				String[] datos = linea.split(";");
					
				RegistroConsultorio temp = new RegistroConsultorio();
				temp.setId(datos[0]);
				temp.setNombre(datos[1]);
				temp.setDireccion(datos[2]);
				temp.setTelefono(datos[3]);
				registro.add(temp);
				
				inicio = fin;
				fin = resultado.indexOf("\n", inicio + 1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(registro != null && registro.size() > 0){
			
			RegistroConsultorio[] reg = new RegistroConsultorio[registro.size()];
			
			registro.toArray(reg);
			
			mContext.cargarListaConsultorio(reg);
		}
		
		
	}

}
