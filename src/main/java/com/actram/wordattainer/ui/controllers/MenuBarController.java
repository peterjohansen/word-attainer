package com.actram.wordattainer.ui.controllers;

import java.util.ResourceBundle;

import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;
import com.actram.wordattainer.ui.generator.GeneratorMode;
import com.actram.wordattainer.util.BiMap;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 *
 *
 * @author Peter André Johansen
 */
public class MenuBarController implements MainControllerChild {
	private MainController mainController;

	private BiMap<GeneratorMode, RadioMenuItem> modeRadioItemMap;

	@FXML private ToggleGroup generatorModeGroup;

	@FXML private RadioMenuItem listModeRadioItem;
	@FXML private RadioMenuItem selectionModeRadioItem;

	@FXML
	public void addLists(ActionEvent evt) {
		mainController.getMorphemeListsController().addLists(evt);
	}

	@FXML
	public void clearLists(ActionEvent evt) {
		mainController.getMorphemeListsController().clearLists(evt);
	}

	@FXML
	public void editPreferences(ActionEvent evt) {
		System.out.println("edit preferences");
	}

	@FXML
	public void editResults(ActionEvent evt) {
		mainController.getResultsController().editResults(null);
	}

	@FXML
	public void exit(ActionEvent evt) {
		Platform.exit();
	}

	@FXML
	public void exportProfile(ActionEvent evt) {
		System.out.println("export profile");
	}

	@FXML
	public void generateResults(ActionEvent evt) {
		mainController.getGeneratorController().generate(evt);
	}

	@FXML
	public void importProfile(ActionEvent evt) {
		System.out.println("import profile");
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.modeRadioItemMap = new BiMap<>();
		modeRadioItemMap.put(GeneratorMode.LIST, listModeRadioItem);
		modeRadioItemMap.put(GeneratorMode.SELECTION, selectionModeRadioItem);
		generatorModeGroup.selectedToggleProperty().addListener((ChangeListener<Toggle>) (observable, oldValue, newValue) -> {
			RadioMenuItem radioItem = (RadioMenuItem) newValue;
			if (newValue != null) {
				GeneratorMode mode = modeRadioItemMap.getPrimary(radioItem);
				if (mode == null) {
					throw new AssertionError("no generator mode found for fx:id: " + radioItem.getId());
				}
				mainController.getPreferences().setGeneratorMode(mode);
				mainController.stateUpdated();
			}
		});
	}

	@FXML
	public void mergeResults(ActionEvent evt) {
		mainController.getResultsController().mergeResults(null);
	}

	@FXML
	public void moveListsDown(ActionEvent evt) {
		mainController.getMorphemeListsController().moveListsDown(evt);
	}

	@FXML
	public void moveListsUp(ActionEvent evt) {
		mainController.getMorphemeListsController().moveListsUp(evt);
	}

	@FXML
	public void removeLists(ActionEvent evt) {
		mainController.getMorphemeListsController().removeLists(evt);
	}

	@FXML
	public void removeResults(ActionEvent evt) {
		mainController.getResultsController().removeResults(null);
	}

	@FXML
	public void showAbout(ActionEvent evt) {
		System.out.println("show about");
	}

	@FXML
	public void sortResults(ActionEvent evt) {
		mainController.getResultsController().sortResults(null);
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		modeRadioItemMap.getSecondary(preferences.getGeneratorMode()).setSelected(true);
	}
}