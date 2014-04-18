package net.sourceforge.jabm.report;

import java.util.List;
import java.util.Map;

import net.sourceforge.jabm.event.SimEvent;

import org.springframework.beans.factory.annotation.Required;

public class CombiReportVariables implements ReportVariables {

	protected List<ReportVariables> reportVariables;
	
	protected String name = "";
	
	@Override
	public Map<Object, Number> getVariableBindings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eventOccurred(SimEvent event) {
		for(ReportVariables rv : reportVariables) {
			rv.eventOccurred(event);
		}
	}

	@Override
	public void compute(SimEvent event) {
		for(ReportVariables rv : reportVariables) {
			rv.compute(event);
		}
	}

	@Override
	public void dispose(SimEvent event) {
		for(ReportVariables rv : reportVariables) {
			rv.dispose(event);
		}
	}

	@Override
	public void initialise(SimEvent event) {
		for(ReportVariables rv : reportVariables) {
			rv.initialise(event);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public List<ReportVariables> getReportVariables() {
		return reportVariables;
	}

	@Required
	public void setReportVariables(List<ReportVariables> reportVariables) {
		this.reportVariables = reportVariables;
	}
	

}
