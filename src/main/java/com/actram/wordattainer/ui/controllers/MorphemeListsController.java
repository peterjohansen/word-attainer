package com.actram.wordattainer.ui.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.actram.wordattainer.GeneratorSettings;
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
		mainController.accessGeneratorSettings(settings -> {
			List<File> files = listFileChooser.showOpenMultipleDialog(mainController.getStage());
			if (files != null && !files.isEmpty()) {
				for (File file : files) {
					settings.getMorphemeListFiles().add(file.getAbsolutePath());
				}
			}
		});
	}

	@FXML
	public void clearLists(ActionEvent event) {
		mainController.accessGeneratorSettings(settings -> {
			if (!settings.getMorphemeListFiles().isEmpty()) {
			// @formatterOff
			if (mainController.showConfirmAlert("Confirm Clear",
												"Are you sure you want to clear the morpheme lists?\n\n"
												+ "The files will not be removed from your system.",
												"Clear lists", "Cancel")) {
				// @formatterOn
					settings.getMorphemeListFiles().clear();
				}
			}
		});
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
			mainController.accessGeneratorSettings(settings -> {
				settings.setMapListsToMorphemes(newValue);
			});
		});
	}

	private void moveLists(boolean down) {
		mainController.accessGeneratorSettings(settings -> {
			List<String> selection = new ArrayList<>(morphemeListView.getSelectionModel().getSelectedItems());
			if (down) {
				settings.getMorphemeListFiles().moveDown(selection);
			} else {
				settings.getMorphemeListFiles().moveUp(selection);
			}
			morphemeListView.getSelectionModel().clearSelection();
			for (String list : selection) {
				morphemeListView.getSelectionModel().select(list);
			}
		});
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
		mainController.accessGeneratorSettings(settings -> {
			List<String> selection = new ArrayList<>(morphemeListView.getSelectionModel().getSelectedItems());
			settings.getMorphemeListFiles().removeAll(selection);
		});
	}

	@Override
	public void updateUI(Preferences preferences, GeneratorSettings generatorSettings, ResultList results) {
		morphemeListView.getItems().setAll(generatorSettings.getMorphemeListFiles().getFileList());
	}
}