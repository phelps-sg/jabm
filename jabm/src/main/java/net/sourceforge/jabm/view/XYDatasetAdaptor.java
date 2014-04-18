package net.sourceforge.jabm.view;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.jabm.report.DataSeriesWriter;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

/**
 * An adaptor that allows multiple DataSeriesWriter objects to be combined
 * and presented as an XYDataset suitable for charting by JFreeChart.
 * 
 * @author Steve Phelps
 *
 */
public class XYDatasetAdaptor implements XYDataset {

	protected LinkedList<DatasetChangeListener> listeners 
		= new LinkedList<DatasetChangeListener>();
	
	protected List<DataSeriesWriter> dataSeries;
	
	protected List<String> seriesNames;
	
	public XYDatasetAdaptor(List<DataSeriesWriter> dataSeries,
								List<String> seriesNames) {
		this.dataSeries = dataSeries;
		this.seriesNames = seriesNames;
	}
	
	public DataSeriesWriter getDataSeries(int series) {
		return dataSeries.get(series);
	}
	
	@Override
	public int getSeriesCount() {
		return dataSeries.size();
	}

	@Override
	public Comparable getSeriesKey(int series) {
		return seriesNames.get(series);
	}

	@Override
	public int indexOf(Comparable seriesKey) {
		return seriesNames.indexOf(seriesKey);
	}

	@Override
	public void addChangeListener(DatasetChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeChangeListener(DatasetChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public DatasetGroup getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGroup(DatasetGroup group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DomainOrder getDomainOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getItemCount(int series) {
		return getDataSeries(series).length();
	}

	@Override
	public Number getX(int series, int item) {
		return getDataSeries(series).getXCoord(item);
	}

	@Override
	public double getXValue(int series, int item) {
		return getDataSeries(series).getYCoord(item);
	}

	@Override
	public Number getY(int series, int item) {
		return getDataSeries(series).getYCoord(item);
	}

	@Override
	public double getYValue(int series, int item) {
		return getDataSeries(series).getXCoord(item);
	}
	
	public void datasetChanged(Object source) {
		for(DatasetChangeListener listener : listeners) {
			listener.datasetChanged(new DatasetChangeEvent(source, this));
		}
	}
	
}
