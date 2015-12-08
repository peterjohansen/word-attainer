package com.actram.wordattainer.ui.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;

/**
 *
 *
 * @author Peter André Johansen
 */
public class MorphemeListsController implements MainControllerChild {
	@FXML private ListView<String> morphemeListView;
	@FXML private Button removeButton;
	@FXML private Button clearButton;
	@FXML private Button moveUpButton;
	@FXML private Button moveDownButton;
	@FXML private CheckBox retainOrderCheckBox;

	private MainController mainController;

	private FileChooser listFileChooser;

	@FXML
	public void addLists(ActionEvent event) {
		List<File> files = listFileChooser.showOpenMultipleDialog(mainController.getStage());
		if (files != null && !files.isEmpty()) {
			for (File file : files) {
				mainController.getPreferences().getMorphemeFileList().add(file.getAbsolutePath());
			}
			mainController.stateUpdated();
		}
	}

	@FXML
	public void clearLists(ActionEvent event) {
		Preferences preferences = mainController.getPreferences();
		if (!preferences.getMorphemeFileList().isEmpty()) {
			// @formatterOff
			if (mainController.showConfirmAlert("Confirm Clear",
												"Are you sure you want to clear the morpheme lists?\n\n"
												+ "The files will not be removed from your system.",
												"Clear lists", "Cancel")) {
				// @formatterOn
				preferences.getMorphemeFileList().clear();
			}
			mainController.stateUpdated();
		}
	}

	@Override
	public void initialize(MainController mainController, ResourceBundle resources) {
		this.mainController = mainController;

		this.listFileChooser = new FileChooser();
		listFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		listFileChooser.setTitle("Open Morpheme Lists");

		this.morphemeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ListProperty<String> morphemelistProperty = new SimpleListProperty<>();
		morphemelistProperty.bind(morphemeListView.itemsProperty());

		BooleanBinding emptySelection = morphemeListView.getSelectionModel().selectedItemProperty().isNull();
		this.clearButton.disableProperty().bind(morphemelistProperty.emptyProperty());
		this.removeButton.disableProperty().bind(emptySelection);
		this.moveUpButton.disableProperty().bind(emptySelection);
		this.moveDownButton.disableProperty().bind(emptySelection);

		this.retainOrderCheckBox.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			mainController.getPreferences().setMapListsToMorphemes(newValue);
			mainController.stateUpdated();
		});
	}

	private void moveLists(boolean down) {
		Preferences preferences = mainController.getPreferences();
		List<String> selection = new ArrayList<>(morphemeListView.getSelectionModel().getSelectedItems());
		if (down) {
			preferences.getMorphemeFileList().moveDown(selection);
		} else {
			preferences.getMorphemeFileList().moveUp(selection);
		}
		morphemeListView.getSelectionModel().clearSelection();
		for (String list : selection) {
			morphemeListView.getSelectionModel().select(list);
		}
		mainController.stateUpdated();
	}

	@FXML
	public void moveListsDown(ActionEvent event) {
		moveLists(true);
	}

	@FXML
	public void moveListsUp(ActionEvent event) {
		moveLists(false);
	}

	@FXML
	public void removeLists(ActionEvent event) {
		List<String> selection = new ArrayList<>(morphemeListView.getSelectionModel().getSelectedItems());
		mainController.getPreferences().getMorphemeFileList().removeAll(selection);
		mainController.stateUpdated();
	}

	@Override
	public void updateUI(Preferences preferences, ResultList results) {
		morphemeListView.getItems().setAll(preferences.getMorphemeFileList().getFileList());
	}
}