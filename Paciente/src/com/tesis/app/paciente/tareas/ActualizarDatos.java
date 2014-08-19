package com.tesis.app.paciente.tareas;

import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;
import com.tesis.app.paciente.wsobj.RegistroPaciente;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ActualizarDatos extends AsyncTask<RegistroPaciente, Void, Void> {

	private String resultado;
	private Context context;
	
	public void setParams(Context context){
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(RegistroPaciente... params) {
		
		SessionManagement session = new SessionManagement(context);
		UrlServer config = new UrlServer(context);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "actualizarPaciente";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/actualizarPacienteRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", session.getEmail());
		request.addProperty("clave", session.getPass());
		request.addProperty("nuevoCorreo", params[0].getCorreo());
		request.addProperty("nuevaClave", params[0].getClave());
		request.addProperty("tipoId", params[0].getTipoId());
		request.addProperty("numeroId", params[0].getNumId());
		request.addProperty("nombres", params[0].getNombres());
		request.addProperty("apellidos", params[0].getApellidos());
		request.addProperty("sexo", params[0].getSexo());
		request.addProperty("fechaNacimiento", params[0].getFechaNaciemiento());
		request.addProperty("direccion", params[0].getDireccion());
		request.addProperty("telefono", params[0].getTelefono());
		request.addProperty("idMunicipio", Integer.parseInt(params[0].getMunicipio()));
		
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
		
		//System.out.println(resultado);
		
		if(resultado.equals("ok")){
			
			Toast toast = Toast.makeText(context, "Datos actualizados correctamente", Toast.LENGTH_LONG);
			toast.show();
			
		} else {
			
			Toast toast = Toast.makeText(context, "Error al actualizar los datos", Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
