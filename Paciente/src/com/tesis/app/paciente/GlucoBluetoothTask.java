package com.tesis.app.paciente;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

public class GlucoBluetoothTask extends AsyncTask<Void, Void, Void> {

	private BluetoothSocket mSocket;
	private InputStream mInStream;
	private OutputStream mOutStream;
	private byte num_records;
	private int[] datos = new int[9];
	private MonitorsActivity mContext;
//	private String salida = "";
//	private final String titulos[] = {"año", "mes", "dia", "temperatura", "resultado",
//			"evento", "hora", "minuto", "am/pm"};
	private ProgressDialog mmDialog;
	private String tipoMedicion;
	private String latitud;
	private String longitud;
	private String nota;
	
	public void setParams(MonitorsActivity mmContext, BluetoothSocket socket, String tipo, String lat, String lon,
			String nota){
		
		System.out.println("---Entro a setParams---");
		mSocket = socket;
		mContext = mmContext;
		tipoMedicion = tipo;
		latitud = lat;
		longitud = lon;
		this.nota = nota;
		
		try {
			mInStream = mSocket.getInputStream();
			mOutStream = mSocket.getOutputStream();
		} catch (IOException e) {
			System.out.println("Primer error!!!");
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		System.out.println("---Entro a Tarea---");
		int n = 0;
		
		try {
			System.out.println("---Entro a try1---");
			byte[] array = new byte[6];
			array[0] = -128;
			array[1] = 1;
			array[2] = (byte) (-1 ^ array[1]);
			array[3] = 0;
			array[4] = (byte) (-1 ^ (array[0] ^ array[2]));
			array[5] = (byte) (-1 ^ (array[1] ^ array[3]));
			
			byte[] dialogWithMeter;
			
			dialogWithMeter = dialogWithMeter(array, 7);
			
			num_records = dialogWithMeter[4];
			System.out.println(new StringBuilder("NUM_REC = ").append(this.num_records).toString());
			
			byte[] array2 = new byte[7];
			// esto va en un ciclo while(true)
			array2[0] = -128;
			array2[1] = 2;
			array2[2] = (byte) (-1 ^ array2[1]);
			array2[3] = 1;
			array2[4] = (byte) n;
			array2[5] = (byte) (-1 ^ (array2[0] ^ array2[2] ^ array2[4]));
			array2[6] = (byte) (-1 ^ (array2[1] ^ array2[3]));
			
			byte[] dialogWithMeter2;
			dialogWithMeter2 = dialogWithMeter(array2, 13);
			
			// hacer ciclo para imprimir arreglo
			//System.out.println(dialogWithMeter2);
			
			// decodificar y almacenar datos
			extractRecord(dialogWithMeter2, 5);
			
			// dar formatoa a los datos
			for(int i=0; i<datos.length; i++){
				System.out.println("- " + i + ": " + datos[i]);
				//salida += titulos[i] + ": " + datos[i] + "\n";
			}
			
			turnOffMeter();
			System.out.println("---Termino try1---");
		} catch (Exception e) {
			System.out.println("Segundo error!!!");
			e.printStackTrace();
		}
		
		System.out.println("---Termino Tarea---");
		return null;
	}

	private byte[] dialogWithMeter(byte[] array, int n) throws Exception {
		
		System.out.println("---Entro a dialogWithMeter---");
		for (int i = 0; i < 3; ++i) {
			System.out.println("---Entro a for de dialogWithMeter---");
			if (!writeToMeter(array)) {
				System.out.println("Tercer error!!!");
				throw new IOException("Unable to write into device");
			}
			System.out.println("---Paso primer if dialogWithMeter---");
			byte[] readFromMeter = readFromMeter(n);
			
			if (readFromMeter == null) {
				System.out.println("---Entro a if2 dialogWithMeter---");
				Thread.sleep(100);
			} else {
				if (readFromMeter[3] == array[3]) {
					System.out.println("---Entro a if de return en dialogWithMeter---");
					return readFromMeter;
				}
				System.out.println("Wrong: req:" + (int) array[3] + " res:" + (int) readFromMeter[3]);
				Thread.sleep(100);
			}
		}
		//throw new IOException("Wrong response from device");
		System.out.println("Cuarto error!!!");
		return null;
	}

	private byte[] readFromMeter(int n) throws Exception {
		
		System.out.println("---Entro a readFromMeter---");
		byte[] array = new byte[n];
		System.out.println("Need to read " + n + " bytes, available "	+ mInStream.available() + " bytes");
		
		int i;
		
		for (i = 0; i < n; ++i) {
			System.out.println("---Entro a for1 readFormMeter---");
			for (int n2 = 0; n2 < 50 && mInStream.available() <= 0; ++n2) {
				System.out.println("---Entro a for2 readFromMeter---");
				Thread.sleep(50L);
			}
			if (mInStream.available() == 0) {
				System.out.println("Septimo error!!!");
				return null;
			}
			System.out.println("---Paso if1 readFromMeter---");
			int read = mInStream.read();
			if (read == -1) {
				System.out.println("Octavo error!!!");
				throw new IOException();
			}
			System.out.println("---Paso if2 ReadFromMeter---");
			array[i] = (byte) read;
			
			System.out.println("Read " + i + " bytes");
		}
		System.out.println("---Termino readFromMeter---");
		return array;
	}

	private boolean writeToMeter(byte[] array) {
		System.out.println("---Entro a wrriteToMeter---");
		int j = 0;

		if ((mOutStream != null) && (mInStream != null)) {
			System.out.println("---Entro a if1 writeToMeter---");
			try {
				System.out.println("---Entro a try writeToMeter---");
				if (mInStream.available() <= 0) {
					System.out.println("Quinto error!!!");
					//return false;
				}
				System.out.println("---Paso if2 writeToMeter---");
				System.out.println("There are " + mInStream.available() + " bytes in input stream, skip it");
			
				mInStream.skip(mInStream.available());
				mOutStream.write(array);
				
				System.err.println("Wrote " + array.length + " bytes");
			
				while(true) {
					if (j >= 50) {
						System.out.println("No answer, repeat...");
						break;
					}
					if (mInStream.available() > 0) {
						return true;
					}
					Thread.sleep(50);
					j++;
				}
				System.out.println("---Paso while writeToMeter---");
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("---Termino writeToMeter---");
		System.out.println("Sexto error!!!");
		return false;
	}
	
	public boolean turnOffMeter(){
		System.out.println("---Entro a turnOffMeter---");
		byte[] array = new byte[6];
		array[0] = -128;
		array[1] = 1;
		array[2] = (byte) (-1 ^ array[1]);
		array[3] = 11;
		array[4] = (byte) (-1 ^ (array[0] ^ array[2]));
		array[5] = (byte) (-1 ^ (array[1] ^ array[3]));
		try {
			System.out.println("---Entro a try1 turnOffMeter---");
			this.dialogWithMeter(array, 6);
		} catch (Exception ex) {
			System.out.println("Noveno error!!!");
			System.out.println("Error : Communication error " + ex.getMessage() + ex.getClass());
			System.err.println(ex);
			return false;
		}
		System.out.println("---Paso try1 turnOffMeter---");
		try {
			System.out.println("---Entro a try2 turnOffMeter---");
			mSocket.close();
		} catch (IOException ex) {
			System.out.println("Decimo error!!!");
			System.err.println("close() of connect socket failed: " + ex.getMessage());
			return false;
		}
		System.out.println("---Termino turnOffMetro---");
		return true;
	}
	
	public void extractRecord(byte[] paramArrayOfByte, int paramInt) {
		datos[0] = (paramArrayOfByte[paramInt] >> 1);
		datos[1] = (((0x1 & paramArrayOfByte[paramInt]) << 3) + ((0xE0 & paramArrayOfByte[(paramInt + 1)]) >> 5));
		datos[2] = (0x1F & paramArrayOfByte[(paramInt + 1)]);
		datos[3] = (0x3F & paramArrayOfByte[(paramInt + 2)] >> 2);
		datos[4] = (((0x3 & paramArrayOfByte[(paramInt + 2)]) << 8) + (0xFF & paramArrayOfByte[(paramInt + 3)]));
		datos[5] = ((0xF8 & paramArrayOfByte[(paramInt + 4)]) >> 3);
		datos[6] = (((0x7 & paramArrayOfByte[(paramInt + 4)]) << 2) + ((0xC0 & paramArrayOfByte[(paramInt + 5)]) >> 6));
		datos[7] = (0x3F & paramArrayOfByte[(paramInt + 5)]);
		datos[8] = 0;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mmDialog = new ProgressDialog(mContext);
	    mmDialog.setMessage("Esperando datos, por favor espere.");
	    mmDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    mmDialog.setCancelable(false);
	    mmDialog.show();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mmDialog.dismiss();
		String fecha = "20" + datos[0] + "-" + datos[1] + "-" + datos[2];
		String hora = datos[6] + ":" + datos[7] + ":00";
		System.out.print("--Hora: " + hora);
		GuardarMonitoreoGlucosa gmg = new GuardarMonitoreoGlucosa();
    	gmg.setContext(mContext);
    	gmg.execute(tipoMedicion, ""+datos[4], fecha, hora, latitud, longitud, nota);
		//mContext.mostrarMensaje(salida);
	}

}
