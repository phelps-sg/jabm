# JABM - Java Agent-Based Modelling toolkit 

\(C) 2015 [Steve Phelps](http://sphelps.net/)   

Overview
--------

JABM is a Java framework for building agent-based simulation models. A
simulation model is constructed using [dependency
injection](http://martinfowler.com/articles/injection.html) by creating
a [Spring beans configuration
file](http://unmaintainable.wordpress.com/2007/11/01/configuration-with-spring-beans/)
which specifies which classes to use in the simulation and the values of
any attributes (parameters). The Spring configuration file is specified
using the system property `jabm.config` .

The main application class is
[`DesktopSimulationManager`](http://jabm.sourceforge.net/doc/javadoc/net/sourceforge/jabm/DesktopSimulationManager.html)

Prerequisites
-------------

JABM requires [Java version 6 or
later](http://www.java.com/en/download/index.jsp). It has been tested
against version 1.6.0\_35 and 1.7.0\_75.  

Note that on Mac OS, you will need to use the Oracle version of Java instead of 
the default one shipped with the OS.

Installation
------------

The project archive can be imported directly into the Eclipse IDE as an existing 
project.  Alternatively, you can import the project into almost any Java IDE by 
importing the maven project file `pom.xml`.

In order to add jabm as a dependency to a Maven project, configure the following repository:

	<repositories>
		<repository>
			<id>jabm.sourceforge.net</id>
			<url>http://jabm.sourceforge.net/mvn-repo/jabm</url>
		</repository>
	</repositories> 

and then configure a dependency on the jabm artifact; for example:

	<dependencies>
		<dependency>
			<groupId>net.sourceforge.jabm</groupId>
			<artifactId>jabm</artifactId>
			<version>0.9.1</version>
		</dependency>
	</dependencies>
	
### Running the examples from the Eclipse IDE

The distribution archive can be imported directly into the [Eclipse
IDE](http://www.eclipse.org/) by using the
[File/Import](http://help.eclipse.org/helios/index.jsp?topic=/org.eclipse.platform.doc.user/tasks/tasks-importproject.htm)
menu item. Create a [launch
configuration](http://help.eclipse.org/helios/index.jsp?topic=/org.eclipse.jdt.doc.user/tasks/tasks-java-local-configuration.htm)
in the `jabm-examples` project with the main class
`net.sourceforge.jabm.DesktopSimulationManager` and specify which
configuration file you want to use by setting the system property
`jabm.config` using the JVM argument `-D` , for example

`-Djabm.config=config/elfarolbar.xml`

Documentation
-------------

-   [Javadoc and UML](doc/javadoc/index.html)

