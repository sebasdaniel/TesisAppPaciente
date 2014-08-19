package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;

import com.tesis.app.paciente.ConfiguracionActivity;
import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;
import com.tesis.app.paciente.wsobj.RegistroPaciente;

public class CargarDatos extends AsyncTask<Void, Void, Void> {

	private String resultado;
	private ConfiguracionActivity context;
	private RegistroPaciente registro;
	
	public void setParams(ConfiguracionActivity context){
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		SessionManagement session = new SessionManagement(context);
		UrlServer config = new UrlServer(context);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "obtenerDatosCuenta";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/obtenerDatosCuentaRequest";
		
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
			
			registro = new RegistroPaciente();
			
			String[] datos = resultado.split(";");
			
			registro.setNumId(datos[0]);
			registro.setTipoId(datos[1]);
			registro.setNombres(datos[2]);
			registro.setApellidos(datos[3]);
			registro.setSexo(datos[4]);
			registro.setFechaNaciemiento(datos[5]);
			registro.setDireccion(datos[6]);
			registro.setTelefono(datos[7]);
			registro.setCorreo(datos[8]);
			registro.setClave(datos[9]);
			registro.setDepartamento(datos[10]);
			registro.setMunicipio(datos[11]);
			
		} catch (Exception e) {
			resultado = "";
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(registro != null){
			context.cargarDatos(registro);
		} else {
			System.out.println("Registro es null");
		}
		
	}

}
