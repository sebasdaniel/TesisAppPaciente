package com.tesis.app.paciente;

public class Validacion {

	public static boolean isNumeric(String text){
		
		try{
			Integer.parseInt(text);
			return true;
			
		} catch(NumberFormatException ex){
			
			return false;
		}
	}
}
