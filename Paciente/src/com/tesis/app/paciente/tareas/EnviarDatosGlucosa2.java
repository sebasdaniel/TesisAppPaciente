package com.tesis.app.paciente.tareas;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import android.os.AsyncTask;

public class EnviarDatosGlucosa2 extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... params) {
		
		String tipo = params[0], valor = params[1], fecha = params[2], hora = params[3];
		
        try {
            HttpClient httpclient = new DefaultHttpClient();
            
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            HttpPost httppost = new HttpPost("http://sistemamp.tk/guardar/glucosa/");
            
            MultipartEntity mpEntity = new MultipartEntity();
            
            mpEntity.addPart("valor", new StringBody(valor));
            mpEntity.addPart("fecha", new StringBody(fecha));
            mpEntity.addPart("hora", new StringBody(hora));
            mpEntity.addPart("tipo", new StringBody(tipo));
            
            httppost.setEntity(mpEntity);
            
            httpclient.execute(httppost);
            httpclient.getConnectionManager().shutdown();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return null;
	}

}
