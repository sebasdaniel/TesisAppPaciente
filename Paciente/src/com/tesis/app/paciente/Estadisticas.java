package com.tesis.app.paciente;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
//import com.tesis.app.paciente.tareas.TareaWSEstadisticas;

public class Estadisticas extends Activity {

	private XYPlot plot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// fun little snippet that prevents users from taking screenshots on ICS+ devices :-)
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
 
        setContentView(R.layout.activity_estadisticas);
        
        //Bundle bundle = getIntent().getExtras();
 
        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        
//        TareaWSEstadisticas tarea = new TareaWSEstadisticas();
//        tarea.setParams(this, bundle.getString("TipoChequeo"), bundle.getString("FechaInicio"),
//        		bundle.getString("FechaFin"));
//        tarea.execute();
	}
	
	public void plotear(List<Number> series1Numbers, List<Number> series2Numbers, String[] series1Date,
			String[] series2Date, int min, int max, String title, String domainTitle, String rangeTitle){
		
		FechaFormat ff = new FechaFormat();
		ff.Labels = series1Date;
		
		plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, series1Date.length);
		//plot.setDomainStep(XYStepMode.SUBDIVIDE, series1Date.size());
		//plot.setDomainValueFormat(new DecimalFormat("0"));
		plot.setDomainValueFormat(ff);
		plot.setDomainStepValue(1);
		
		plot.setRangeBoundaries(min, max, BoundaryMode.FIXED);
		plot.setRangeStepValue(11);
		plot.setRangeValueFormat(new DecimalFormat("0"));
		
		plot.setTitle(title);
		plot.setDomainLabel(domainTitle);
		plot.setRangeLabel(rangeTitle);
		
		XYSeries series1 = new SimpleXYSeries(series1Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Antes");
 
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(0, 0, 255), Color.rgb(0, 100, 0),
        		null, null);
        
        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        
        if(series2Numbers != null && series2Numbers.size() > 0){
        	
        	ff.Labels = series2Date;
        	
        	XYSeries series2 = new SimpleXYSeries(series2Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Despues");
        	//XYSeries series2 = new SimpleXYSeries(series2Numbers, series2Date, "Despues");
        	LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.rgb(0, 0, 130), Color.rgb(0, 100, 0),
        			null, null);
        	plot.addSeries(series2, series2Format);
        }
        
        
        //plot.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.rgb(230, 230, 230));
 
        // reduce the number of range labels
        plot.setTicksPerRangeLabel(2);
        plot.getGraphWidget().setDomainLabelOrientation(-45);
        
        plot.redraw();
	}

}
