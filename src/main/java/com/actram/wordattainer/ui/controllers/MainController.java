package com.actram.wordattainer.ui.controllers;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;
import com.actram.wordattainer.ui.WordAttainer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
	@FXML private PreferencesController preferencesController;
	@FXML private SelectionModeController selectionModeController;

	private Preferences preferences;
	private ResultList results;

	private WordAttainer program;

	public Alert createAlert(AlertType type, String title) {
		Objects.requireNonNull(type, "alert type cannot be null");

		Alert alert = new Alert(type);
		alert.initOwner(program.getStage());
		alert.setHeaderText(null);
		alert.setTitle(title != null ? program.getTitle() + " - " + title : program.getTitle());
		return alert;
	}

	private void forEachChildController(Consumer<MainControllerChild> controllerAccessor) {
		Object[] children = { generatorController, menuBarController, morphemeListsController, resultsController, preferencesController, selectionModeController };
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

	public Preferences getPreferences() {
		return preferences;
	}

	public PreferencesController getPreferencesController() {
		return preferencesController;
	}

	public ResultList getResults() {
		return results;
	}

	public ResultsController getResultsController() {
		return resultsController;
	}

	public File getRootFile() {
		return program.getRootFile();
	}

	public SelectionModeController getSelectionModeController() {
		return selectionModeController;
	}

	public Window getStage() {
		return program.getStage();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.program = WordAttainer.getInstance();
		this.preferences = new Preferences();
		this.results = new ResultList();

		// Load preferences controller manually
		FXMLLoader prefLoader = new FXMLLoader();
		prefLoader.setRoot(new VBox());
		Parent preferencesParent = program.load(prefLoader, "preferences.fxml");
		this.preferencesController = prefLoader.getController();
		preferencesController.setRootNode(preferencesParent);

		// Load selection mode controller manually
		FXMLLoader selectionLoader = new FXMLLoader();
		selectionLoader.setRoot(new BorderPane());
		Parent selectionParent = program.load(selectionLoader, "selection_mode.fxml");
		this.selectionModeController = selectionLoader.getController();
		selectionModeController.setRootNode(selectionParent);

		// Initialize child controllers
		forEachChildController(controller -> {
			controller.initialize(this, resources);
		});
		updateChildUI();

	}

	public Parent loadFXML(String name) {
		Objects.requireNonNull(name, ".fxml file name cannot be null");
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

		Alert alert = createAlert(AlertType.NONE, title);
		alert.getDialogPane().setContent(content);

		ButtonType okButton = new ButtonType(okButtonText, ButtonData.OK_DONE);
		alert.getButtonTypes().add(okButton);
		if (cancelButtonText != null) {
			ButtonType cancelButton = new ButtonType(cancelButtonText, ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().add(cancelButton);
		}

		Optional<ButtonType> result = alert.showAndWait();
		return (result.isPresent() && result.get().getButtonData() == ButtonData.OK_DONE);
	}

	public void showInfoAlert(String title, String content) {
		Objects.requireNonNull(content, "alert content text cannot be null");

		Alert confirmAlert = createAlert(Alert.AlertType.INFORMATION, title);
		confirmAlert.setContentText(content);
		confirmAlert.show();
	}

	/**
	 * Call to reflect changes in the UI.
	 */
	public void stateUpdated() {
		updateChildUI();
	}

	private void updateChildUI() {
		forEachChildController(controller -> {
			controller.updateUI(preferences, results);
		});
	}
}