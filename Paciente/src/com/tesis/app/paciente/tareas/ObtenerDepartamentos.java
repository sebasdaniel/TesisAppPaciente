package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

import com.tesis.app.paciente.RegisterActivity;
import com.tesis.app.paciente.UrlServer;
import com.tesis.app.paciente.wsobj.RegistroDepartamento;

public class ObtenerDepartamentos extends AsyncTask<Void, Void, Void> {

	private String resultado;
	private RegisterActivity mActivity;
	private ArrayList<RegistroDepartamento> registro;
	
	public void setParams(RegisterActivity activity){
		mActivity = activity;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		
		UrlServer config = new UrlServer(mActivity);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "listaDepartamento";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/listaDepartamentoRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try {
			transporte.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			
			resultado = resultado_xml.toString();
			
			int inicio, fin;
			
			inicio = -1;
			fin = resultado.indexOf("\n");
			
			registro = new ArrayList<RegistroDepartamento>();
			
			while(fin != -1){
				
				String linea = resultado.substring(inicio + 1, fin);
				
				String[] datos = linea.split(";");
					
				RegistroDepartamento temp = new RegistroDepartamento();
				
				temp.setId(Integer.parseInt(datos[0]));
				temp.setNombre(datos[1]);
				
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
		
		if(!resultado.isEmpty() && !registro.isEmpty()){
			
			mActivity.cargarDepartamentos(registro);
		} else {
			System.out.println(resultado);
		}
	}

}
