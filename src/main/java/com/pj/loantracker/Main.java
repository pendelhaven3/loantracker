package com.pj.loantracker;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * 
 * @author PJ Miranda
 *
 */
@Component
public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		String[] configLocations = new String[] {"applicationContext.xml", "datasource.xml"};
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
		context.registerShutdownHook();
		
		stage.setTitle("Credit Card Management");
	}

}