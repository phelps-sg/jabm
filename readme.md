JABM - Java Agent-Based Modelling framework
===========================================

\(C) 2014 [Steve Phelps](http://sphelps.net/)   

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
[`DesktopSimulationManager`](doc/javadoc/net/sourceforge/jabm/DesktopSimulationManager.html).

Prerequisites
-------------

JABM requires [Java version 6 or
later](http://www.java.com/en/download/index.jsp). It has been tested
against version 1.6.0\_35 and 1.7.0\_51.

Running the examples
--------------------

### Using Apache Ant

The examples can be run using [Apache Ant](ant.apache.org). Launch a
command window or shell and set your working directory to the
`jabm-examples/` subdirectory. Next [run
ant](http://ant.apache.org/manual/running.html) and pass it the argument
elfarolbar in order to run the El Farol Bar example. For example:

` ant elfarolbar `

### From the command line

Alternatively, configure the
[CLASSPATH](http://download.oracle.com/javase/tutorial/essential/environment/paths.html)
environment variable to include all the `jar` files in the `lib/`
directory and the `dist/` directory and use a command similar to the
following:

`java -Djabm.config=config/elfarolbar.xml         net.sourceforge.jabm.DesktopSimulationManager`

### From the Eclipse IDE

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

