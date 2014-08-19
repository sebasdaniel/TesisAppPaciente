package com.tesis.app.paciente;

public class ListEntrada {

	private int IdImage;
	private String textoEncima;
	private String textoDebajo;
	
	public ListEntrada(int IdImage, String textoEncima, String textoDebajo){
		
		this.IdImage = IdImage;
		this.textoEncima = textoEncima;
		this.textoDebajo = textoDebajo;
	}
	
	public String get_textoEncima(){
		return textoEncima;
	}
	
	public String get_textoDebajo(){
		return textoDebajo;
		
	}
	
	public int get_IdImage(){
		return IdImage;
	}
	
	
}
