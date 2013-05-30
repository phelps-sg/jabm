package net.sourceforge.jabm.spring;

import org.junit.Before;
import org.junit.Test;

public class PRNGSeedFactoryBeanTest {

	PRNGSeedFactoryBean bean;
	
	@Before
	public void setUp() throws Exception {
		bean = new PRNGSeedFactoryBean();
	}

	@Test
	public void test() {
//		Frequency bag = new Frequency();
		for(int i=0; i<10; i++) {
			Integer result = bean.getObject();
			System.out.println(result);
		}
	}

}
