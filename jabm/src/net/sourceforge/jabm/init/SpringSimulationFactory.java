package net.sourceforge.jabm.init;

import net.sourceforge.jabm.Simulation;
import net.sourceforge.jabm.SimulationController;
import net.sourceforge.jabm.SpringSimulationController;

import org.springframework.beans.factory.BeanFactory;

public class SpringSimulationFactory implements SimulationFactory {

	@Override
	public Simulation initialise(SimulationController simulationController) {
		SpringSimulationController springSimulationController = 
				(SpringSimulationController) simulationController;
		String simulationBeanName =
				springSimulationController.getSimulationBeanName();
		BeanFactory beanFactory = 
				springSimulationController.getBeanFactory();
		return (Simulation) beanFactory.getBean(simulationBeanName);
	}

}
