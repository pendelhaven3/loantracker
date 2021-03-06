package com.pj.loantracker.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.loantracker.ControllerFactory;
import com.pj.loantracker.Parameter;
import com.pj.loantracker.model.Client;
import com.pj.loantracker.model.Loan;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class StageController {

	private static final double WIDTH = 1200d;
	private static final double HEIGHT = 640d;
	
	@Autowired private ControllerFactory controllerFactory;
	
	private Stage stage;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void loadSceneFromFXML(String file) {
		loadSceneFromFXML(file, null);
	}
	
	private void loadSceneFromFXML(String file, Map<String, Object> model) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setControllerFactory(controllerFactory);
		
		Parent root = null;
		try {
			root = fxmlLoader.load(getClass().getResourceAsStream("/fxml/" + file + ".fxml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		stage.setScene(null);
		stage.setScene(new Scene(root, WIDTH, HEIGHT));
		stage.getScene().getStylesheets().add("css/application.css");
		
		if (fxmlLoader.getController() instanceof AbstractController) {
			AbstractController controller = (AbstractController)fxmlLoader.getController();
			if (model != null && !model.isEmpty()) {
				mapParameters(controller, model);
			}
			controller.updateDisplay();
		}
	}

	private void mapParameters(AbstractController controller, Map<String, Object> model) {
		Class<? extends AbstractController> clazz = controller.getClass();
		for (String key : model.keySet()) {
			try {
				Field field = clazz.getDeclaredField(key);
				if (field != null && field.getAnnotation(Parameter.class) != null) {
					field.setAccessible(true);
					field.set(controller, model.get(key));
				}
			} catch (Exception e) {
				System.out.println("Error setting parameter " + key);
			}
		}
	}

	public void setTitle(String title) {
		stage.setTitle(title);
	}

	public void showMainMenuScreen() {
		loadSceneFromFXML("mainMenu");
	}

	public void showAddClientScreen() {
		loadSceneFromFXML("client");
	}

	public void showClientListScreen() {
		loadSceneFromFXML("clientList");
	}

	public void showUpdateClientScreen(Client client) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("client", client);
		
		loadSceneFromFXML("client", paramMap);
	}

	public void showLoanListScreen() {
		loadSceneFromFXML("loanList");
	}

	public void showAddLoanScreen() {
		loadSceneFromFXML("addEditLoan");
	}

	public void showUpdateLoanScreen(Loan loan) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("loan", loan);
		
		loadSceneFromFXML("addEditLoan", paramMap);
	}

	public void showLoanScreen(Loan loan) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("loan", loan);
		
		loadSceneFromFXML("loan", paramMap);
	}

}