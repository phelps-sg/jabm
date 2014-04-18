package net.sourceforge.jabm.examples.elfarolbar;

public abstract class LowHighPredictionStrategy extends
		AbstractPredictionStrategy {

	protected double low;
	protected double high;
	protected int lag = 1;

	public LowHighPredictionStrategy() {
		super();
	}

	public boolean wasOvercrowdedLastWeek() {
		return barTender.getAttendanceAtLag(lag) >= 60;
	}

	public void predictLow() {
		this.currentPrediction = low;
	}

	public void predictHigh() {
		this.currentPrediction = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public int getLag() {
		return lag;
	}

	public void setLag(int lag) {
		this.lag = lag;
	}

	@Override
	public String toString() {
		return "ContrarianPredictionStrategy [low=" + low + ", high=" + high
				+ ", lag=" + lag + "]";
	}

}