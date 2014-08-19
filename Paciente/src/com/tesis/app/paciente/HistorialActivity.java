package com.tesis.app.paciente;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.tesis.app.paciente.R.id;
import com.tesis.app.paciente.tareas.ObtenerHistorial;

public class HistorialActivity extends Activity {

	private TextView tvHistorial;
	private TextView tvDatosPaciente;
//	private TextView tvTituloEntidad;
	private TextView tvDatosEntidad;
//	private TextView tvTituloAutor;
	private TextView tvDatosAutor;
//	private TextView tvTituloCustodio;
	private TextView tvDatosCustodio;
	private TextView tvTituloRegistro;
	private TableLayout tableRegistro;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historial);
		
		tvHistorial = (TextView) findViewById(id.tvHistorial);
		tvDatosPaciente = (TextView) findViewById(id.tvDatosPaciente);
//		tvTituloEntidad = (TextView) findViewById(id.tvTituloEntidad);
		tvDatosEntidad = (TextView) findViewById(id.tvDatosEntidad);
//		tvTituloAutor = (TextView) findViewById(id.tvTituloAutor);
		tvDatosAutor = (TextView) findViewById(id.tvDatosAutor);
//		tvTituloCustodio = (TextView) findViewById(id.tvTituloCustodio);
		tvDatosCustodio = (TextView) findViewById(id.tvDatosCustodio);
		tvTituloRegistro = (TextView) findViewById(id.tvTituloRegistro);
		
		tableRegistro = (TableLayout) findViewById(id.tableRegistro);
		
		ObtenerHistorial oh = new ObtenerHistorial();
		oh.setParam(this);
		oh.execute();
	}
	
	public void cargarHistorial(String xmlHistorial){
		
//		String salida = "";
		String salida1 = "";
		
		Document doc = getDomElement(xmlHistorial);
		
		NodeList nodosTitle = doc.getElementsByTagName("title");
		
//		salida += nodosTitle.item(0).getFirstChild().getNodeValue() + "\n";
		
		salida1 = nodosTitle.item(0).getFirstChild().getNodeValue();
		tvHistorial.setText(salida1);
		salida1 = "";
		
		Node nodoRecordTarget = doc.getElementsByTagName("recordTarget").item(0);
		
		Node nrtPatientRole = nodoRecordTarget.getFirstChild();
//		Node nprAddr = nrtPatientRole.getChildNodes().item(1);
//		Node naCity = nprAddr.getFirstChild();
//		Node naState = nprAddr.getLastChild();
		
//		salida += naCity.getFirstChild().getNodeValue() + "\n" + naState.getFirstChild().getNodeValue() + "\n";
//		salida1 = naCity.getFirstChild().getNodeValue() + " - " + naState.getFirstChild().getNodeValue() + "\n";
		
		Node nprPatient = nrtPatientRole.getChildNodes().item(2);
		Node npName = nprPatient.getChildNodes().item(1);
		
		NodeList nodoNombres = npName.getChildNodes();
		
		salida1 = "Paciente: ";
		
		for(int i=0; i<nodoNombres.getLength(); i++){
			//Element e = (Element) nodoNombres.item(i);
//			salida += nodoNombres.item(i).getFirstChild().getNodeValue() + "\n";
			salida1 += nodoNombres.item(i).getFirstChild().getNodeValue() + " ";
		}
		
		tvDatosPaciente.setText(salida1);
		salida1 = "";
		
		Node nodoProviderOrganization = nrtPatientRole.getChildNodes().item(3);
//		salida += nodoProviderOrganization.getChildNodes().item(1).getFirstChild().getNodeValue() + "\n";
		salida1 = nodoProviderOrganization.getChildNodes().item(1).getFirstChild().getNodeValue() + "\n";
		
		Node nodoAddr2 = nodoProviderOrganization.getChildNodes().item(3);
		
		String[] etiquetas = {"Ciudad", "Departamento", "Pais"};
		
		NodeList datosNodoAddr2 = nodoAddr2.getChildNodes();
		
		for(int i=0; i<datosNodoAddr2.getLength(); i++){
			
			String temp = null;
			
			if( datosNodoAddr2.item(i).getFirstChild() != null){
				temp = datosNodoAddr2.item(i).getFirstChild().getNodeValue();
			}
			
//			salida += etiquetas[i] + ": " + ((temp != null) ? temp : "") + "\n";
			salida1 += etiquetas[i] + ": " + ((temp != null) ? temp : "--") + "\n";;
		}
		
		tvDatosEntidad.setText(salida1);
		salida1 = "";
		
		
		Node nodoAuthor = doc.getElementsByTagName("author").item(0);
		Node nodoName = nodoAuthor.getChildNodes().item(1).getChildNodes().item(1).getChildNodes().item(0);
		
//		salida += nodoName.getFirstChild().getFirstChild().getNodeValue() + "\n";
		salida1 = "Sistema " + nodoName.getFirstChild().getFirstChild().getNodeValue();
		
		tvDatosAutor.setText(salida1);
		salida1 = "";
		
		
		Node nodoCustodian = doc.getElementsByTagName("custodian").item(0);
		Node nodoName2 = nodoCustodian.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(1);
//		salida += nodoName2.getFirstChild().getNodeValue() + "\n\n";
		salida1 = nodoName2.getFirstChild().getNodeValue();
		
		tvDatosCustodio.setText(salida1);
		salida1 = "";
		
//		salida += nodosTitle.item(1).getFirstChild().getNodeValue() + "\n";
		salida1 = nodosTitle.item(1).getFirstChild().getNodeValue();
		
		tvTituloRegistro.setText(salida1);
		
		Node nodoTable = doc.getElementsByTagName("table").item(0);
		Node nodoThead = nodoTable.getFirstChild();
		Node nodoTbody = nodoTable.getLastChild();
		
		NodeList datosNodoThead = nodoThead.getChildNodes().item(0).getChildNodes();
		String[] cabeceras = new String[6];
		
		for(int i=0; i<datosNodoThead.getLength(); i++){
			
//			salida += datosNodoThead.item(i).getFirstChild().getNodeValue() + " ";
			cabeceras[i] = datosNodoThead.item(i).getFirstChild().getNodeValue();
		}
		
//		salida += "\n";
		
		NodeList datosNodoTbody = nodoTbody.getChildNodes();
		
		
		String[][] datos = new String[datosNodoTbody.getLength()][6];
		
		for(int i=0; i<datosNodoTbody.getLength(); i++){
			
			NodeList temp = datosNodoTbody.item(i).getChildNodes();
			
			for(int j=0; j<temp.getLength(); j++){
				
//				salida += temp.item(j).getFirstChild().getNodeValue() + " ";
				datos[i][j] = temp.item(j).getFirstChild().getNodeValue();
			}
//			salida += "\n";
		}
		
		cargarTabla(cabeceras, datos);
		
//		TableRow trCabezera = new TableRow(getApplicationContext());
//		trCabezera.setLayoutParams(new TableLayout.LayoutParams(
//			       LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//			    tableRegistro.addView(trCabezera);
//		tvHistorial.setText(salida);
	}
	
	private Document getDomElement(String xml){
		
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);
            
            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }
                // return DOM
            return doc;
    }
	
//	private String getValue(Element item, String str) {
//	    NodeList n = item.getElementsByTagName(str);
//	    return this.getElementValue(n.item(0));
//	}
//	
//	private final String getElementValue( Node elem ) {
//        Node child;
//        if( elem != null){
//            if (elem.hasChildNodes()){
//                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
//                    if( child.getNodeType() == Node.TEXT_NODE  ){
//                        return child.getNodeValue();
//                    }
//                }
//            }
//        }
//        return "";
//	}
	
	private void cargarTabla(String[] cabeceras, String[][] datos){
		
	    tableRegistro.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	    		LayoutParams.MATCH_PARENT));
	    
	    tableRegistro.setShrinkAllColumns(true);
//	    tableRegistro.setColumnStretchable(1, true);
	 
	    // Cabecera de la tabla
	    TableRow cabecera = new TableRow(this);
	    cabecera.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	    		LayoutParams.WRAP_CONTENT));
	    tableRegistro.addView(cabecera);
	 
	    // Textos de la cabecera
	    for (int i=0; i<cabeceras.length; i++){
	    	
	       TextView columna = new TextView(this);
	       
	       columna.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
	    		   LayoutParams.WRAP_CONTENT));
	       
	       columna.setText(cabeceras[i]);
	       columna.setTextColor(Color.parseColor("#005500"));
	       columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
	       columna.setGravity(Gravity.CENTER_HORIZONTAL);
//	       columna.setPadding(3, 3, 3, 3);
	       
	       cabecera.addView(columna);
	    }
	 
	    // Línea que separa la cabecera de los datos
	    TableRow separador_cabecera = new TableRow(this);
	    separador_cabecera.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	    		LayoutParams.WRAP_CONTENT));
	    FrameLayout linea_cabecera = new FrameLayout(this);
	    TableRow.LayoutParams linea_cabecera_params = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, 2);
	    linea_cabecera_params.span = 6;
	    linea_cabecera.setBackgroundColor(Color.parseColor("#FFFFFF"));
	    separador_cabecera.addView(linea_cabecera, linea_cabecera_params);
	    tableRegistro.addView(separador_cabecera);
	    
	    
	    TableRow cuerpo = new TableRow(this);
	    cuerpo.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
	    		LayoutParams.WRAP_CONTENT));
	    
	    for(int i=0; i<datos.length; i++){
	    	
	    	TableRow fila  = new TableRow(this);
	    	fila.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		    		LayoutParams.WRAP_CONTENT));
	    	
	    	for (int j=0; j<datos[i].length; j++){
	    		
	    		TextView columna = new TextView(this);
	    		columna.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
		    		   LayoutParams.WRAP_CONTENT));
	    		
	    		columna.setText(datos[i][j]);
	    		//columna.setTextColor(Color.parseColor("#005500"));
	    		columna.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
	    		columna.setGravity(Gravity.CENTER_HORIZONTAL);
//	    		columna.setPadding(5, 5, 5, 5);
	    		
	    		fila.addView(columna);
		    }
	    	
//	    	cuerpo.addView(fila);
	    	tableRegistro.addView(fila);
	    }
	    
//	    tableRegistro.addView(cuerpo);
	    
	}
}
