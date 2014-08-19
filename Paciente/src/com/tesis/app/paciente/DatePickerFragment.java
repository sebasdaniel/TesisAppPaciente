package com.tesis.app.paciente;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {
	
	// Interface creada para implementar un método que podamos utilizar como
	// enlace de la actividad en curso y le enviemos la información
	// seleccionada por el usuario.
	public interface DatePickerFragmentListener {
		void onFinishDatePickerDialog(int year, int month, int day);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker arg0, int year, int month, int day) {
		
		DatePickerFragmentListener activity = (DatePickerFragmentListener) getActivity();
		activity.onFinishDatePickerDialog(year, month + 1, day);
	}
}