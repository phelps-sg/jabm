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

import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.beans.factory.config.RuntimeBeanReference;

public class PropertyOverrideWithReferencesConfigurer extends
		PropertyOverrideConfigurer {

	@Override
	protected void applyPropertyValue(ConfigurableListableBeanFactory factory,
			String beanName, String property, String value) {
		if (value != null && value.length() > 0 && !(value.charAt(0) == '&')) {
			super.applyPropertyValue(factory, beanName, property, value);
		} else {
			BeanDefinition bd = factory.getBeanDefinition(beanName);
			while (bd.getOriginatingBeanDefinition() != null) {
				bd = bd.getOriginatingBeanDefinition();
			}
			Object referencedValue = new RuntimeBeanReference(
					value.substring(1));
			PropertyValue pv = new PropertyValue(property, referencedValue);
			pv.setOptional(false);
			bd.getPropertyValues().addPropertyValue(pv);
		}
	}
	
}
