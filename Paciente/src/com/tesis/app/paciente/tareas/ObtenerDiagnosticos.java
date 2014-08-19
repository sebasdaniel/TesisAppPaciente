package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

import com.tesis.app.paciente.ListaDiagnostico;
import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;
import com.tesis.app.paciente.wsobj.RegistroDiagnostico;

public class ObtenerDiagnosticos extends AsyncTask<Void, Void, Void> {

	private ListaDiagnostico mContext;
	private String resultado;
	private ArrayList<RegistroDiagnostico> registro;
	
	public void setParams(ListaDiagnostico context){
		mContext = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		SessionManagement session = new SessionManagement(mContext);
		UrlServer config = new UrlServer(mContext);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL"; //cambiar la url
		final String METHOD_NAME = "obtenerDiagnosticos";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/obtenerDiagnosticosRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", session.getEmail());
		request.addProperty("clave", session.getPass());
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try {
			transporte.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			
			resultado = resultado_xml.toString();
			
			if(resultado.equals("none")){
				return null;
			}
			
			int inicio, fin;
			
			inicio = -1;
			fin = resultado.indexOf("\n");
			
			registro = new ArrayList<RegistroDiagnostico>();
			
			while(fin != -1){
				
				String linea = resultado.substring(inicio + 1, fin);
				
				String[] datos = linea.split(";");
					
				RegistroDiagnostico temp = new RegistroDiagnostico();
				
				temp.setId(datos[0]);
				temp.setFecha(datos[1]);
				temp.setHora(datos[2]);
				temp.setContenido(datos[3]);
				
				registro.add(temp);
				
				inicio = fin;
				fin = resultado.indexOf("\n", inicio + 1);
			}
			
		} catch (Exception e) {
			resultado = "";
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(!resultado.equals("") && registro != null && !registro.isEmpty()){
			
			RegistroDiagnostico[] reg = new RegistroDiagnostico[registro.size()];
			
			registro.toArray(reg);
			
			mContext.llenarLista(reg);
		} else {
			
			mContext.detener();
		}
	}

}
