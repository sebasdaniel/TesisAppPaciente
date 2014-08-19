package com.tesis.app.paciente.tareas;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.tesis.app.paciente.MonitorsActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.util.Log;

public class EnviarDatosGlucosa3 extends AsyncTask<String, Void, Void> {

	ProgressDialog pDialog;
	MonitorsActivity mContext;
	String resultado;
	String tip;
	
	public void setContext(MonitorsActivity contex){
		this.mContext = contex;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		
		resultado = params[1];
		
		try {
			
            HttpClient mClient = new DefaultHttpClient();
            HttpPost mpost = new HttpPost("http://sistemamp.tk/guardar/glucosa/");
            
            List<NameValuePair> nameValuepairs = new ArrayList<NameValuePair>(4);
            
            nameValuepairs.add(new BasicNameValuePair("tipo", params[0]));
            nameValuepairs.add(new BasicNameValuePair("valor", params[1]));
            nameValuepairs.add(new BasicNameValuePair("fecha", params[2]));
            nameValuepairs.add(new BasicNameValuePair("hora", params[3]));
            
            mpost.setEntity(new UrlEncodedFormEntity(nameValuepairs));
            
            HttpResponse response = mClient.execute(mpost);
            
            HttpEntity entity = response.getEntity();
            
            BufferedReader buf = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder sb1 = new StringBuilder();
            String line = null;
 
            while ((line = buf.readLine()) != null) {
                sb1.append(line+"\r\n");
            }
            
            //System.out.println(sb1.toString());
            
            JSONObject json = new JSONObject(sb1.toString());
            System.out.println(json.getString("mensaje"));
            /*System.out.println(json.getString("tip"));*/
            
            tip = json.getString("tip");
 
        } catch (Exception e) {
            Log.w(" error ", e.toString());
        }
		
		return null;
	}
	
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Procesando, espere.");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //pDialog.setCancelable(true);
        pDialog.show();
	}
	
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		pDialog.dismiss();
		
		new AlertDialog.Builder(mContext).setTitle("Resultado")
		.setMessage("Su resultado fue " + resultado + " mg/dL\n\n" + tip)
	    .setPositiveButton("OK", new OnClickListener() {
	    	
	        public void onClick(DialogInterface arg0, int arg1) {
	          //finish();
	        }
	        
	    }).show();
	}

}
