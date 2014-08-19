package com.tesis.app.paciente.tareas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.tesis.app.paciente.GraficaEstadistica;
import com.tesis.app.paciente.SessionManagement;
import com.tesis.app.paciente.UrlServer;
import com.tesis.app.paciente.wsobj.RegistroEstadistico;

public class TareaWSEstadisticas extends AsyncTask<Void, Void, Void> {

	private ArrayList<RegistroEstadistico> registro;
	//private Estadisticas mActivity;
	private GraficaEstadistica mActivity;
	private String tipoChequeo;
	private String fechaInicio;
	private String fechaFin;
	
	public void setParams(GraficaEstadistica activity, String tipoChequeo, String fechaInicio, String fechaFin){
		
		mActivity = activity;
		this.tipoChequeo = tipoChequeo;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		
		SessionManagement session = new SessionManagement(mActivity);
		UrlServer config = new UrlServer(mActivity);
		String url = config.getUrl();
		
		final String NAMESPACE = "http://ws.simop.com/";
		final String URL = "http://" + url + "/SIMOP/SIMOP?WSDL";
		final String METHOD_NAME = "obtenerEstadisticas";
		final String SOAP_ACTION = "http://ws.simop.com/SIMOP/obtenerEstadisticasRequest";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		request.addProperty("correo", session.getEmail());
		request.addProperty("clave", session.getPass());
		request.addProperty("fechaInicio", fechaInicio);
		request.addProperty("fechaFin", fechaFin);
		request.addProperty("tipoChequeo", tipoChequeo);
		request.addProperty("modo", 0);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		
		envelope.setOutputSoapObject(request);
		
		ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
		headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
		
		HttpTransportSE transporte = new HttpTransportSE(URL);
		
		try {
			transporte.call(SOAP_ACTION, envelope, headerPropertyArrayList);
			
			SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
			
			String respuesta = resultado_xml.toString();
			
			int inicio, fin;
			
			inicio = -1;
			fin = respuesta.indexOf("\n");
			
			registro = new ArrayList<RegistroEstadistico>();
			
			while(fin != -1){
				
				String linea = respuesta.substring(inicio + 1, fin);
				
				String[] datos = linea.split(";");
					
				RegistroEstadistico temp = new RegistroEstadistico();
				temp.setValor(datos[0]);
				temp.setUnidades(datos[1]);
				temp.setFecha(datos[2]);
				temp.setHora(datos[3]);
				temp.setDescripcion(datos[4]);
				registro.add(temp);
				
				inicio = fin;
				fin = respuesta.indexOf("\n", inicio + 1);
			}
			
		} catch (Exception e) {
			System.out.println("---se produjo una exception---");
			e.printStackTrace();
		}
		
		return null;
	}
	
//	@Override
//	protected void onPostExecute(Void result) {
//		super.onPostExecute(result);
//		
//		if(registro != null && registro.size() > 0){
//			
//			//SimpleDateFormat formato = new SimpleDateFormat("yyyy", Locale.US);
//			
//			try {
//				if(tipoChequeo.equals("glucosa")){
//					
//					List<Number> valoresAntes = new ArrayList<Number>();
//					List<Number> valoresDespues = new ArrayList<Number>();
//					List<String> fechasAntes = new ArrayList<String>();
//					List<String> fechasDespues = new ArrayList<String>();
//					
//					for(RegistroEstadistico temp : registro){
//						
//						if(temp.getDescripcion().equals("antes")){
//							
//							valoresAntes.add(Integer.parseInt(temp.getValor()));
//							fechasAntes.add(temp.getFecha());
//							System.out.println(temp.getFecha());
//							
//						} else if (temp.getDescripcion().equals("despues")) {
//							
//							valoresDespues.add(Integer.parseInt(temp.getValor()));
//							fechasDespues.add(temp.getFecha());
//							System.out.println(temp.getFecha());
//						}
//					}
//					
//					String[] fAntes = new String[fechasAntes.size()];
//					String[] fDespues = new String[fechasDespues.size()];
//					
//					fechasAntes.toArray(fAntes);
//					fechasDespues.toArray(fDespues);
//					
//					mActivity.plotear(valoresAntes, valoresDespues, fAntes, fDespues, 0, 400,
//							"Niveles de Glucosa", "tiempo", "nivel (mg/dL)");
//					
//				} else if(tipoChequeo.equals("presion")){
//					
//					// definir como utilizar los valores con el formato ej: 120/80
//	//				List<Number> valores = new ArrayList<>();
//	//				
//	//				for(RegistroEstadistico temp : registro){
//	//					valores.add(Integer.parseInt(temp.getValor()));
//	//				}
//	//				
//	//				mActivity.plotear(valores, null);
//					
//				} else if(tipoChequeo.equals("arritmia")){
//					
//					List<Number> valores2 = new ArrayList<Number>();
//					List<String> fechas2 = new ArrayList<String>();
//					
//					for(RegistroEstadistico temp : registro){
//						valores2.add(Integer.parseInt(temp.getValor()));
//						fechas2.add(temp.getFecha());
//					}
//					
//					String[] f = new String[fechas2.size()];
//					
//					fechas2.toArray(f);
//					
//					mActivity.plotear(valores2, null, f, null, 0, 260, "Ritmo Cardiaco", "tiempo", "ppm");
//				}
//				
//			} catch (Exception e) {
//				System.err.println("Error!!!");
//				e.printStackTrace();
//			}
//			
//		}
//		
//	}
	
	@SuppressLint("SimpleDateFormat")
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(registro != null && registro.size() > 0){
			
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			
			try {
				if(tipoChequeo.equals("glucosa")){
					
					List<Integer> valoresAntes = new ArrayList<Integer>();
					List<Integer> valoresDespues = new ArrayList<Integer>();
					List<Date> fechasAntes = new ArrayList<Date>();
					List<Date> fechasDespues = new ArrayList<Date>();
					
					for(RegistroEstadistico temp : registro){
						
						if(temp.getDescripcion().equals("antes")){
							
							valoresAntes.add(Integer.parseInt(temp.getValor()));
							fechasAntes.add(formato.parse(temp.getFecha()));
							//System.out.println(temp.getFecha());
							
						} else if (temp.getDescripcion().equals("despues")) {
							
							valoresDespues.add(Integer.parseInt(temp.getValor()));
							fechasDespues.add(formato.parse(temp.getFecha()));
							//System.out.println(temp.getFecha());
						}
					}
					
					int[] vAntes = new int[valoresAntes.size()];
					int[] vDespues = new int[valoresDespues.size()];
					
					Date[] fAntes = new Date[fechasAntes.size()];
					Date[] fDespues = new Date[fechasDespues.size()];
					
					fechasAntes.toArray(fAntes);
					fechasDespues.toArray(fDespues);
					
					for(int i=0; i<valoresAntes.size(); i++){
						vAntes[i] = valoresAntes.get(i);
					}
					
					for(int i=0; i<valoresDespues.size(); i++){
						vDespues[i] = valoresDespues.get(i);
					}
					
					if(vAntes.length  > 0){
						
						mActivity.openChart(vAntes, vDespues, fAntes, fDespues, "Antes de comida", "Despues de comida",
								"Glucosa");
						
					} else if(vDespues.length > 0){
						
						mActivity.openChart(vDespues, vAntes, fDespues, fAntes, "Despues de comida", "Antes de comida",
								"Glucosa");
					}
					
					
				} else if(tipoChequeo.equals("presion")){
					
					List<Integer> valoresSistolica = new ArrayList<Integer>();
					List<Integer> valoresDiastolica = new ArrayList<Integer>();
					List<Date> fechas = new ArrayList<Date>();
					
					for(RegistroEstadistico temp : registro){
						
						String[] div = temp.getValor().split("/");
						
						valoresSistolica.add(Integer.parseInt(div[0]));
						valoresDiastolica.add(Integer.parseInt(div[1]));
						
						fechas.add(formato.parse(temp.getFecha()));
					}
					
					int[] vSistolica = new int[valoresSistolica.size()];
					int[] vDiastolica = new int[valoresDiastolica.size()];
					Date[] fechasValores = new Date[fechas.size()];
					
					for(int i=0; i<valoresSistolica.size(); i++){
						vSistolica[i] = valoresSistolica.get(i);
					}
					
					for(int i=0; i<valoresDiastolica.size(); i++){
						vDiastolica[i] = valoresDiastolica.get(i);
					}
					
					fechas.toArray(fechasValores);
					
					
					mActivity.openChart(vSistolica, vDiastolica, fechasValores, fechasValores, "Sistolica", "Diastolica",
							"Presion");
					
				} else if(tipoChequeo.equals("arritmia")){
					
					List<Integer> valores2 = new ArrayList<Integer>();
					List<Date> fechas2 = new ArrayList<Date>();
					
					for(RegistroEstadistico temp : registro){
						valores2.add(Integer.parseInt(temp.getValor()));
						fechas2.add(formato.parse(temp.getFecha()));
					}
					
					int[] vals = new int[valores2.size()];
					
					Date[] f = new Date[fechas2.size()];
					
					fechas2.toArray(f);
					
					for(int i=0; i<valores2.size(); i++){
						vals[i] = valores2.get(i);
					}
					
					mActivity.openChart(vals, null, f, null, "ppm", null, "Ritmo Cardico");
				}
				
			} catch (Exception e) {
				System.err.println("Error!!!");
				e.printStackTrace();
			}
			
		} else {
			mActivity.mostrarMensaje();
		}
		
	}

}
