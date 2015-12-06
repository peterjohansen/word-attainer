package com.actram.wordattainer.ui;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.MorphemeList;
import com.actram.wordattainer.ResultList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

/**
 *
 * 
 * @author Peter André Johansen
 */
public class MainController implements Initializable {
	@FXML private GeneratorController generatorController;
	@FXML private MenuBarController menuBarController;
	@FXML private MorphemeListsController morphemeListsController;
	@FXML private ResultsController resultsController;

	private Preferences preferences;
	private GeneratorSettings generatorSettings;
	private ResultList results;

	private WordAttainer program;

	public void accessGeneratorSettings(Consumer<GeneratorSettings> modifier) {
		modifier.accept(generatorSettings);
		morphemeListsController.updateUI(preferences, generatorSettings, results);
	}

	public void accessPreferences(Consumer<Preferences> modifier) {
		modifier.accept(preferences);
		generatorController.updateUI(preferences, generatorSettings, results);
		menuBarController.updateUI(preferences, generatorSettings, results);
	}

	public void accessResults(Consumer<ResultList> modifier) {
		modifier.accept(results);
		resultsController.updateUI(preferences, generatorSettings, results);
	}

	public Alert createAlert(AlertType type, String title) {
		Objects.requireNonNull(type, "alert type cannot be null");

		Alert alert = new Alert(type);
		alert.initOwner(program.getStage());
		alert.setHeaderText(null);
		alert.setTitle(title != null ? program.getTitle() + " - " + title : program.getTitle());
		return alert;
	}

	private void forEachChildController(Consumer<MainControllerChild> controllerAccessor) {
		Object[] children = { generatorController, menuBarController, morphemeListsController, resultsController };
		for (Object child : children) {
			if (child == null) {
				continue;
			}
			if (child instanceof MainControllerChild) {
				controllerAccessor.accept((MainControllerChild) child);
			} else {
				throw new AssertionError("all controllers must be a main controller child");
			}
		}
	}

	public GeneratorController getGeneratorController() {
		return generatorController;
	}

	public MenuBarController getMenuBarController() {
		return menuBarController;
	}

	public MorphemeListsController getMorphemeListsController() {
		return morphemeListsController;
	}

	public ResultsController getResultsController() {
		return resultsController;
	}

	public Window getStage() {
		return program.getStage();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.program = WordAttainer.getInstance();
		this.preferences = new Preferences();
		this.generatorSettings = new GeneratorSettings();
		this.results = new ResultList();

		MorphemeList morphemes = generatorSettings.getMorphemes();
		morphemes.add("test1");
		morphemes.add("test2");
		morphemes.add("test3");
		morphemes.add("test4");

		results.add("test1");
		results.add("test2");
		results.add("test3");
		results.add("test4");

		// Initialize child controllers
		forEachChildController(controller -> {
			controller.initialize(this, resources);
		});
		forEachChildController(controller -> {
			controller.updateUI(preferences, generatorSettings, results);
		});

	}

	public Parent loadFXML(String name) {
		return program.loadFXML(name);
	}

	public boolean showConfirmAlert(String title, String content, String okButtonText, String cancelButtonText) {
		Objects.requireNonNull(content, "alert content text cannot be null");
		Objects.requireNonNull(okButtonText, "alert ok button text cannot be null");
		Objects.requireNonNull(cancelButtonText, "alert cancel button text cannot be null");

		Alert confirmAlert = createAlert(Alert.AlertType.CONFIRMATION, title);
		confirmAlert.setContentText(content);

		ButtonType okButton = new ButtonType(okButtonText, ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType(cancelButtonText, ButtonData.CANCEL_CLOSE);
		confirmAlert.getButtonTypes().setAll(okButton, cancelButton);

		return (confirmAlert.showAndWait().get().getButtonData() == ButtonData.OK_DONE);
	}

	public void showErrorAlert(String title, String content) {
		Objects.requireNonNull(content, "alert content text cannot be null");

		Alert confirmAlert = createAlert(Alert.AlertType.ERROR, title);
		confirmAlert.setContentText(content);
		confirmAlert.show();
	}

	public boolean showFormAlert(String title, Node content, String okButtonText, String cancelButtonText) {
		Objects.requireNonNull(content, "alert content cannot be null");
		Objects.requireNonNull(okButtonText, "alert ok button text cannot be null");
		Objects.requireNonNull(cancelButtonText, "alert cancel button text cannot be null");

		Alert alert = createAlert(AlertType.NONE, title);
		alert.getDialogPane().setContent(content);

		ButtonType okButton = new ButtonType(okButtonText, ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType(cancelButtonText, ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(okButton, cancelButton);

		return (alert.showAndWait().get().getButtonData() == ButtonData.OK_DONE);
	}
}