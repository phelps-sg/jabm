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

package net.sourceforge.jabm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.sourceforge.jabm.report.Report;
import net.sourceforge.jabm.report.ReportWithGUI;
import net.sourceforge.jabm.util.SystemProperties;
import net.sourceforge.jabm.view.PropertiesEditor;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

/**
 * The main class to run if running JABM with a graphical user interface.
 * The simulation model is specified as a Spring beans xml configuration file,
 * as per {@link SimulationManager}.
 * 
 * @see SimulationManager
 * 
 * @author Steve Phelps
 */
public class DesktopSimulationManager extends SimulationManager {

	protected RootWindow desktopPane;
	
	protected ViewMap viewMap;

	protected JFrame desktopFrame;
	
	protected LinkedList<View> reportViews;
	
	protected LinkedList<View> builtinViews;
	
	protected JMenuItem[] viewMenuItems;
	
	protected DockingWindowsTheme currentTheme = 
			new ShapedGradientDockingTheme();

	protected RootWindowProperties properties = new RootWindowProperties();

	protected SimulationController simulationController;
	
	protected Thread simulationThread;
	
	protected Properties simulationProperties = null;

	protected int propertiesViewId;

	protected JButton runButton;

	protected JToggleButton pauseButton;

	protected JButton terminateButton;

	protected View propertiesView;

	protected PropertiesEditor propertiesEditor;

	protected View outputView;
	
	static Logger logger = Logger.getLogger(DesktopSimulationManager.class);
	
	private static final int ICON_SIZE = 8;
	
	private static final Icon VIEW_ICON = new Icon() {
	    public int getIconHeight() {
	      return ICON_SIZE;
	    }

	    public int getIconWidth() {
	      return ICON_SIZE;
	    }

	    public void paintIcon(Component c, Graphics g, int x, int y) {
	      Color oldColor = g.getColor();

	      g.setColor(new Color(70, 70, 70));
	      g.fillRect(x, y, ICON_SIZE, ICON_SIZE);

	      g.setColor(new Color(100, 230, 100));
	      g.fillRect(x + 1, y + 1, ICON_SIZE - 2, ICON_SIZE - 2);

	      g.setColor(oldColor);
	    }
	  };

	public void initialise()  {
		
		this.simulationController = getSimulationController();
		loadSimulationProperties();
		
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					initialiseGUI();
				}
		});
	}
	
	public void saveSimulationProperties() {
		if (this.propFile != null) {
			FileOutputStream out;
			try {
				out = new FileOutputStream(propFile);
				String comments = getClass().toString();
				simulationProperties.store(out, comments);
			} catch (IOException e) {
				handleExceptionWithErrorDialog(e);
			}
		}
	}

	public void loadSimulationProperties() {
		if (this.simulationProperties == null) {
			this.simulationProperties = new Properties();
		}
		if (this.propFile != null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {
						simulationProperties.clear();
						simulationProperties
								.load(new FileInputStream(propFile));
						if (propertiesEditor != null) {
							propertiesEditor.propertiesChanged();
						}
					} catch (IOException e) {
						handleExceptionWithErrorDialog(e);
					}
				}
			});
		}
	}
	
	public void initialiseViews() {
		viewMap = new ViewMap();
		
		reportViews = new LinkedList<View>();
		int viewNumber = 0;
		for (Report report : getReports()) {
			if (report instanceof ReportWithGUI) {
				String name = report.getName();
				if (name == null || "".equals(name)) {
					name = "Report " + viewNumber;
				}
				View view = new View(name, VIEW_ICON,
						((ReportWithGUI) report).getComponent());
				viewMap.addView(viewNumber, view);
				reportViews.add(view);
				viewNumber++;
			}
		}
		
		builtinViews  = new LinkedList<View>();

		outputView = 
				new View("Output Console", VIEW_ICON, createOutputFrame());
		builtinViews.add(outputView);
		viewMap.addView(viewNumber, outputView);
		viewNumber++;
		
		if (this.simulationProperties != null) {
			propertiesEditor = new PropertiesEditor(simulationProperties);
			propertiesView = new View("Simulation Properties", VIEW_ICON,
					new JScrollPane(propertiesEditor));
			builtinViews.add(propertiesView);
			viewMap.addView(viewNumber, propertiesView);
			this.propertiesViewId = viewNumber;
			viewNumber++;
		}
	}

	public JComponent createOutputFrame() {
		JTextArea outArea = new JTextArea(20, 50);
		JScrollPane pane = new JScrollPane(outArea);
		Logger.getRootLogger().addAppender(new JTextAreaAppender(outArea));
		System.setErr(new PrintStream(new TextAreaOutputStream(outArea, System.err)));
		return pane;
	}
		
	public void initialiseGUI() {
		
		initialiseViews();
		
		desktopPane = DockingUtil.createRootWindow(viewMap, true);
		
		try {
			restoreLayout();
		} catch (IOException e) {
			handleExceptionWithErrorDialog(e);
		}
		
	    // Set gradient theme. The theme properties object is the super object of our properties object, which
	    // means our property value settings will override the theme values
	    properties.addSuperObject(currentTheme.getRootWindowProperties());

	    // Our properties object is the super object of the root window properties object, so all property values of the
	    // theme and in our property object will be used by the root window
	    desktopPane.getRootWindowProperties().addSuperObject(properties);

		desktopFrame = new JFrame("JABM");
		desktopFrame.getContentPane().add(desktopPane, BorderLayout.CENTER);
		desktopFrame.getContentPane().add(createToolBar(), BorderLayout.NORTH);
		
		desktopFrame.setPreferredSize(new Dimension(1200, 900));
		desktopFrame.pack();

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		menuBar.add(createViewMenu());
		menuBar.add(createHelpMenu());
		
		desktopFrame.setJMenuBar(menuBar);
		desktopFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		desktopFrame.addWindowListener(new SaveLayoutOnExitWindowListener());
		desktopFrame.setVisible(true);
	}
	
	protected JMenuItem createViewMenuItem(String title, final View view) {
		JMenuItem result = new JMenuItem(title);
		result.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (view.getRootWindow() != null) {
					view.restoreFocus();
				} else {
					DockingUtil.addWindow(view, desktopPane);
				}
			}
		});
		return result;
	}
	
	public void populateViewMenu(JMenu menu, List<View> views) {
		for (View view : views) {
			menu.add(createViewMenuItem(view.getTitle(), view));
		}
	}
	
	public JMenu createReportMenu() {
		JMenu reportMenu = new JMenu("Reports");
		populateViewMenu(reportMenu, reportViews);
		return reportMenu;
	}
	
	protected JMenu createViewMenu() {
		JMenu menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);
		menu.add(createReportMenu());
		populateViewMenu(menu, builtinViews);
		return menu;
	}

	protected JMenu createHelpMenu() {
		JMenu menu = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				helpDialog();
			}
		});
		menu.add(about);
		return menu;
	}

	protected void helpDialog() {
		String modelDescription = 
				getSimulationController().getModelDescription();
		String message = Version.getVerboseVersion() + "\n" 
						  + Version.getCopyright();
		if (modelDescription != null) {
			message += "\n\n" + "Model: " + modelDescription;
		}
		JOptionPane.showMessageDialog(this.desktopFrame,
				message, "About JABM", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	public JToolBar createToolBar() {
		
		JToolBar toolBar = new JToolBar();

		ImageIcon runIcon = createImageIcon("icons/Play24.gif",
                "run");
		runButton = new JButton(runIcon);
		runButton.setToolTipText("Launch (a batch of) simulation(s)");
		this.simulationThread = new Thread(this);
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runButton.setEnabled(false);
				simulationThread.start();
			}
		});
		toolBar.add(runButton);
		
		ImageIcon stopIcon = createImageIcon("icons/Stop24.gif",
                "stop");
		terminateButton = new JButton(stopIcon);
		terminateButton.setToolTipText("Terminate all simulations");
		terminateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				terminate();
			}
		});
		toolBar.add(terminateButton);
		
		ImageIcon pauseIcon = createImageIcon("icons/Pause24.gif",
                "pause");
		pauseButton = new JToggleButton(pauseIcon);
		pauseButton.setToolTipText("Pause the current simulation");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pauseButton.isSelected()) {
					pause();
				} else {
					resume();
				}
			}
		});
		toolBar.add(pauseButton);
		
		toolBar.addSeparator();
		
		JSlider speedSlider = new JSlider(0, 1000, 0);
		speedSlider.setToolTipText("Simulation step sleep interval in ms");
		speedSlider.setMajorTickSpacing(250);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				simulationController.slow(((JSlider) e
						.getSource()).getValue());
			}
			
		});
		toolBar.add(speedSlider);
		
		return toolBar;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        throw new RuntimeException("Couldn't find file: " + path);
	    }
	}
	
	protected JMenu createFileMenu() {
		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser(propFile);
				fc.setFileFilter(new FileNameExtensionFilter(
						"JABM properties files", "properties"));
				int status = fc.showOpenDialog(desktopFrame);
				if (status == JFileChooser.APPROVE_OPTION) {
					propFile = fc.getSelectedFile().getPath();
					loadSimulationProperties();
					propertiesView.restore();
					propertiesView.restoreFocus();
				}
			}
		});
		open.setMnemonic(KeyEvent.VK_O);
		menu.add(open);
		
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSimulationProperties();
			}
		});
		save.setMnemonic(KeyEvent.VK_S);
		menu.add(save);
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeApplication();
			}
		});
		exit.setMnemonic(KeyEvent.VK_X);
		menu.add(exit);
		
		return menu;
	}
	
	public void closeApplication() {
		cleanUp();
		System.exit(0);
	}
	
	public void cleanUp() {
		try {
			terminate();
			saveLayout();
		} catch (IOException ex) {
			logger.error("Could not save layout: " + ex);
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		outputView.restoreFocus();
		this.pauseButton.setEnabled(true);
		this.terminateButton.setEnabled(true);
		runSingleExperiment();
		this.runButton.setEnabled(true);
		this.terminateButton.setEnabled(false);
		this.pauseButton.setEnabled(false);
	}
	
	@Override
	public void runSingleExperiment() {
		try {
			if (simulationProperties != null) {
				super.runSingleExperiment(simulationProperties);
			} else {
				super.runSingleExperiment();
			}
			
		} catch (Exception e) {
			handleExceptionWithErrorDialog(e);
		}
		
		// Ready the simulation thread for another run.
		this.simulationThread = new Thread(this);
	}

	public String getLayoutFileName() throws IOException {
		File propertiesFile = new File(SystemProperties.jabsConfiguration()
				.getProperty(SystemProperties.PROPERTY_CONFIG)); 
		int modelHash = propertiesFile.getCanonicalPath().hashCode();
		String outFileName = System.getProperty("user.home") + "/"
				+ ".jabm" + modelHash + ".layout";
		return outFileName;
	}
	
	public void saveLayout() throws IOException {
		logger.debug("Saving layout..");
		FileOutputStream fos = new FileOutputStream(getLayoutFileName());
		ObjectOutputStream out = new ObjectOutputStream(fos);
		desktopPane.write(out);
		out.close();
		logger.debug("layout saved.");
	}
	
	public void restoreLayout() throws IOException {
		File layoutFile = new File(getLayoutFileName());
		if (layoutFile.exists()) {
			FileInputStream fis = new FileInputStream(layoutFile);
			ObjectInputStream in = new ObjectInputStream(fis);
			desktopPane.read(in);
			in.close();
		} else {
			intialiseLayout();
		}
	}
	
	public void intialiseLayout() {
		desktopPane.setWindow(new SplitWindow(false, 0.75f, 
			    new TabWindow(getWindows(reportViews)), 
			    new TabWindow(getWindows(builtinViews))));
	}
	
	public DockingWindow[] getWindows(List<View> windows) {
		DockingWindow[] result = new DockingWindow[windows.size()];
		int i = 0;
		for(DockingWindow window : windows) {
			result[i++] = window;
		}
		return result;
	}
	
	public void handleExceptionWithErrorDialog(Exception e) {
		e.printStackTrace();
		String message = e.getMessage();
		if (message == null || "".equals(message)) {
			message = "Fatal exception (see console output)";
		} else if (message.length() > 80) {
			message = message.substring(0, 80) + "...";
		}
		JOptionPane.showMessageDialog(this.desktopFrame,
			    message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void terminate() {
		getSimulationController().terminate();
	}

	public void pause() {
		getSimulation().pause();
	}

	public void resume() {
		getSimulation().resume();
	}
	
	public List<Report> getReports() {
		return simulationController.getReports();
	}
	
	public Simulation getSimulation() {
		return simulationController.getSimulation();
	}

	public static void main(String[] args) {
		DesktopSimulationManager manager = new DesktopSimulationManager();
		manager.initialise();
	}
	
	
	class JTextAreaAppender extends AppenderSkeleton {
		JTextArea textControl;

		public JTextAreaAppender(JTextArea t) {
			super();
			textControl = t;
		}

		@Override
		public void close() {
		}

		@Override
		public boolean requiresLayout() {
			return false;
		}

		@Override
		protected void append(LoggingEvent event) {
			textControl.append(event.getRenderedMessage() + "\n");
			textControl.setCaretPosition(textControl.getDocument().getLength());
		}

	}
	
	class TextAreaOutputStream extends OutputStream {
	    
		protected JTextArea textControl;
		
		protected OutputStream originalOut;
	  
		public TextAreaOutputStream(JTextArea control, OutputStream originalOut) {
			textControl = control;
			this.originalOut = originalOut;
		}
	   
		public void write(int b) throws IOException {
			textControl.append(String.valueOf((char) b));
			textControl.setCaretPosition(textControl.getDocument().getLength());
			originalOut.write(b);
		}
	}
	  
	class SaveLayoutOnExitWindowListener implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			cleanUp();
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}
		
	}

}
