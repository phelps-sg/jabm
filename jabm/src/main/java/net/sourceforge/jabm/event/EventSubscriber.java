package net.sourceforge.jabm.event;

import net.sourceforge.jabm.EventScheduler;

public interface EventSubscriber {

	public void subscribeToEvents(EventScheduler scheduler);
	
}
