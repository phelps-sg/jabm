package net.sourceforge.jabm.examples.elfarolbar;

public class AntiContrarianPredictionStrategy extends LowHighPredictionStrategy {

	@Override
	public void makePrediction() {
		if (wasOvercrowdedLastWeek()) {
			predictHigh();
		} else {
			predictLow();
		}
	}

}
