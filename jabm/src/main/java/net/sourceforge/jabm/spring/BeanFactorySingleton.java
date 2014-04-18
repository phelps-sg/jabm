/*
 * JABM - Java Agent-Based Modeling Toolkit
 * Copyright (C) 2013 Steve Phelps
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package net.sourceforge.jabm.spring;

import java.util.Properties;

import net.sourceforge.jabm.util.SystemProperties;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class BeanFactorySingleton {

	protected static DefaultListableBeanFactory beanFactory;
	
	public static synchronized BeanFactory getBeanFactory() {
		if (beanFactory == null) {
			initialiseFactory();
		}
		return beanFactory;
	}

	public static synchronized void registerFactory(
			DefaultListableBeanFactory beanFactory) {
		BeanFactorySingleton.beanFactory = beanFactory;
	}
	
	public static Object getBean(String id) {
		return getBeanFactory().getBean(id);
	}
	
	public static void initialiseFactory(Resource resource) {
		
		beanFactory = new DefaultListableBeanFactory();

		XmlBeanDefinitionReader reader = 
				new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions(resource);
		
		// Caching must be disabled for
		// net.sourceforge.jabm.init.RandomVariateSimulationInitialiser
		beanFactory.setCacheBeanMetadata(false);
		beanFactory
				.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
		
		// Register the custom simulation scope
		beanFactory.registerScope(SimulationScope.ATTRIBUTE_VALUE,
				SimulationScope.getSingletonInstance());
	}

	public static void initialiseFactory() {
		Properties systemProperties = SystemProperties.jabsConfiguration();
		String configFile = systemProperties
				.getProperty(SystemProperties.PROPERTY_CONFIG);
		if (configFile == null) {
			throw new IllegalArgumentException(
					"Must specify a configuration file by setting the system property "
							+ SystemProperties.PROPERTY_BASE + "."
							+ SystemProperties.PROPERTY_CONFIG);
		}
		initialiseFactory(new FileSystemResource(configFile));
	}
	
}
