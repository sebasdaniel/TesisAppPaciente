package com.tesis.app.paciente.wsobj;

public class RegistroMunicipio {
	
	private int id;
	private String nombre;
	
	public RegistroMunicipio(){
		
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Override
	public String toString() {
		return nombre;
	}
}
