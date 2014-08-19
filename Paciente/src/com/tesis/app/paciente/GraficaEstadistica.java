package com.tesis.app.paciente;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.tesis.app.paciente.tareas.TareaWSEstadisticas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GraficaEstadistica extends Activity {

	private GraphicalView mChart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grafica_estadistica);
		
		Bundle bundle = getIntent().getExtras();
		
		TareaWSEstadisticas tarea = new TareaWSEstadisticas();
        tarea.setParams(this, bundle.getString("TipoChequeo"), bundle.getString("FechaInicio"),
        		bundle.getString("FechaFin"));
        tarea.execute();
		
		//openChart();
	}
	
	public void openChart(int[] series1, int[] series2, Date[] dates1, Date[] dates2,
			final String series1Title, final String series2Title, final String graphTitle){
		
		// Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        
        // set means to the render
        //multiRenderer.setLabelsTextSize(24);
        float val = getResources().getDimension(R.dimen.graph_labels_text_size);
        
        multiRenderer.setChartTitleTextSize(getResources().getDimension(R.dimen.graph_chart_text_size));
        multiRenderer.setAxisTitleTextSize(val);
        multiRenderer.setLabelsTextSize(val);
        multiRenderer.setLegendTextSize(val);
        multiRenderer.setPointSize(getResources().getDimension(R.dimen.graph_point));
        multiRenderer.setMargins(new int[]{
                getResources().getDimensionPixelOffset(R.dimen.graph_margin_top),
                getResources().getDimensionPixelOffset(R.dimen.graph_margin_left),
                getResources().getDimensionPixelOffset(R.dimen.graph_margin_bottom),
                getResources().getDimensionPixelOffset(R.dimen.graph_margin_right)});
        
        multiRenderer.setBackgroundColor(Color.BLACK);
        multiRenderer.setApplyBackgroundColor(true);
        
        // Creating TimeSeries for series1
        TimeSeries timeSeries1 = new TimeSeries(series1Title);
        
        // Adding data to timeSeries1
        for(int i=0; i<dates1.length; i++){
        	timeSeries1.add(dates1[i], series1[i]);
        }
        
        // Adding timeSeries1 to the dataset
        dataset.addSeries(timeSeries1);
        
        // Creating XYSeriesRenderer to customize series1
        XYSeriesRenderer visitsRenderer = new XYSeriesRenderer();
        visitsRenderer.setColor(Color.BLUE);
        visitsRenderer.setPointStyle(PointStyle.CIRCLE);
        visitsRenderer.setFillPoints(true);
        //visitsRenderer.setLineWidth(2);
        visitsRenderer.setLineWidth(getResources().getDimension(R.dimen.graph_line));
        visitsRenderer.setPointStrokeWidth(getResources().getDimension(R.dimen.graph_point));
        visitsRenderer.setDisplayChartValues(true);
        
        multiRenderer.setChartTitle(graphTitle);
        multiRenderer.setXTitle("Fecha");
        multiRenderer.setYTitle("Valor");
        multiRenderer.setZoomButtonsVisible(true);
        
        // Adding visitsRenderer and viewsRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(visitsRenderer);
        
        // If there is series2 and it have data
        if(series2 != null && series2.length > 0){
        	
        	System.out.println("Longitud de Series2: " + series2.length);
        	// Creating TimeSeries for series2
	        TimeSeries timeSeries2 = new TimeSeries(series2Title);
	        
	        // Adding data to timeSeries2
	        for(int i=0; i<dates2.length; i++){
	        	timeSeries2.add(dates2[i], series2[i]);
	        }
	        // Adding timeSeries2 to dataset
	        dataset.addSeries(timeSeries2);
	        
	        // Creating XYSeriesRenderer to customize series2
	        XYSeriesRenderer viewsRenderer = new XYSeriesRenderer();
	        viewsRenderer.setColor(Color.RED);
	        viewsRenderer.setPointStyle(PointStyle.CIRCLE);
	        viewsRenderer.setFillPoints(true);
	        //viewsRenderer.setLineWidth(2);
	        viewsRenderer.setLineWidth(getResources().getDimension(R.dimen.graph_line));
	        viewsRenderer.setPointStrokeWidth(getResources().getDimension(R.dimen.graph_point));
	        viewsRenderer.setDisplayChartValues(true);
	        
	        multiRenderer.addSeriesRenderer(viewsRenderer);
        }
        
 
        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
 
        // Creating a Time Chart
        mChart = (GraphicalView) ChartFactory.getTimeChartView(getBaseContext(), dataset, multiRenderer,"dd-MMM-yyyy");
 
        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(10);
 
        // Setting a click event listener for the graph
        mChart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
			@Override
            public void onClick(View v) {
                Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
 
                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();
 
                if (seriesSelection != null) {
                    int seriesIndex = seriesSelection.getSeriesIndex();
                    String selectedSeries=series1Title;
                    if(seriesIndex==0)
                        selectedSeries = series1Title;
                    else
                        selectedSeries = series2Title;
 
                    // Getting the clicked Date ( x value )
                    long clickedDateSeconds = (long) seriesSelection.getXValue();
                    Date clickedDate = new Date(clickedDateSeconds);
                    String strDate = formatter.format(clickedDate);
 
                    // Getting the y value
                    int amount = (int) seriesSelection.getValue();
 
                    // Displaying Toast Message
                    Toast.makeText(getBaseContext(), graphTitle + " " + selectedSeries + " el "  + strDate + " : "
                    		+ amount , Toast.LENGTH_LONG).show();
                    }
                }
            });
 
            // Adding the Line Chart to the LinearLayout
            chartContainer.addView(mChart);
	}
	
	public void mostrarMensaje(){
		
		new AlertDialog.Builder(this).setTitle("Informacion")
		.setMessage("Al parecer no hay registros para la fecha especificada")
	    .setPositiveButton("Aceptar", new OnClickListener() {
	    	
	        public void onClick(DialogInterface arg0, int arg1) {
	          finish();
	        }
	        
	    }).show();
	}

}
