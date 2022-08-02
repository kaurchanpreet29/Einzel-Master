package com.floreantpos.main;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.floreantpos.report.MenuUsageReport;
import com.floreantpos.report.MenuUsageReport.MenuUsageReportData;
public class ChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public JFreeChart getChart(MenuUsageReport report) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		List<MenuUsageReportData> list = report.getReportDatas();
		for(Iterator<MenuUsageReportData> itr = list.iterator();itr.hasNext();)
		{
			MenuUsageReportData data = itr.next();
			if(data.getCount() > 0)
				dataset.setValue(data.getCategoryName(), data.getCount());
		}
		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart =ChartFactory.createPieChart("Menu", dataset, legend, tooltips, urls);
		// Specify the colors here 
		// all possible colors for each piece of the pie
		Color[] colors = new Color[] { new Color(232, 124, 35),
		        new Color(51, 109, 178), new Color(182, 52, 49),
		        new Color(103, 131, 45), new Color(108, 77, 146),
		        new Color(46, 154, 183), new Color(151, 64, 64) };
        
        /*for(int i = 0; i<200;i++)
        {
        	Color color = null; 
        	if(i%2 == 0)
        		color = new Color(255-i,i+25,255-(i+10));
        	else
        		color = new Color(255-(i+20),i+50,i+20);
        	colors[i] = color;
        }*/
        PieRenderer renderer = new PieRenderer(colors); 
        PiePlot plot = (PiePlot)chart.getPlot(); 
        renderer.setColor(plot, dataset);
		return chart;
	}
	public static class PieRenderer 
    { 
        private Color[] color; 
        
        public PieRenderer(Color[] color) 
        { 
            this.color = color; 
        }        
        
        public void setColor(PiePlot plot, DefaultPieDataset dataset) 
        { 
            List <Comparable> keys = dataset.getKeys(); 
            int aInt; 
            
            for (int i = 0; i < keys.size(); i++) 
            { 
                aInt = i % this.color.length; 
                plot.setSectionPaint(keys.get(i), this.color[aInt]); 
            } 
        } 
    } 
}