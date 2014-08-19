package com.tesis.app.paciente;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		TextView resultado = (TextView) findViewById(R.id.textRecuperarCuenta);
		TextView tip = (TextView) findViewById(R.id.textTip);
		
		Bundle bundle = this.getIntent().getExtras();
		
		resultado.setText(bundle.getString("RESULTADO"));
		tip.setText(bundle.getString("TIP"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

}
