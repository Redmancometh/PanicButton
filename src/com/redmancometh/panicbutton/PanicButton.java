package com.redmancometh.panicbutton;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.redmancometh.panicbutton.config.ConfigManager;
import com.redmancometh.panicbutton.config.SpringConfig;
import com.redmancometh.panicbutton.mediator.GUIManager;

import javafx.application.Application;
import javafx.stage.Stage;

@Configuration
@ComponentScan(basePackages = { "com.redmancometh.panicbutton.config.*", "com.redmancometh.panicbutton.controller" })
/**
 * 
 * @author Brendan T CUrry
 *
 */
public class PanicButton extends Application {
	private ConfigurableApplicationContext springContext;
	private static ConfigManager<SpringConfig> cfgMon = new ConfigManager("spring.json", SpringConfig.class);
	@Autowired
	private GUIManager gui;

	@Override
	/**
	 * Initialize and begin configuration of Spring
	 */
	public void init() throws Exception {
		Logger.getLogger(this.getClass().getName()).info("Initialize");
		cfgMon.init();
		SpringConfig cfg = cfgMon.getConfig();
		this.springContext = new AnnotationConfigApplicationContext(PanicButton.class);
		Map<String, Object> props = springContext.getEnvironment().getSystemProperties();
		cfg.getProperties().forEach((key, value) -> props.put(key, value));
	}

	@Override
	/**
	 * Start at the top of the spring stack, and configure/start JavaFX
	 */
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Panic Button");
		gui = springContext.getBean(GUIManager.class);
		gui.start(primaryStage);
	}

	/**
	 * Initialize a JavaFX stage and begin the initializer for spring
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(PanicButton.class, args);
	}
}
