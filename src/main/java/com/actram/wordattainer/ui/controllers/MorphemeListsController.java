package com.actram.wordattainer.ui.controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 *
 *
 * @author Peter André Johansen
 */
public class MorphemeListsController implements MainControllerChild {
	private static class MorphemeListListCell extends ListCell<String> {
		@Override
		protected void updateItem(String filePath, boolean empty) {
			super.updateItem(filePath, empty);
			if (empty) {
				setText(null);
			} else {
				setText(Paths.get(filePath).getFileName().toString());
				setTooltip(new Tooltip(filePath));
			}
		}
	}

	@FXML private ListView<String> morphemeListView;
	@FXML private Button removeButton;
	@FXML private Button clearButton;
	@FXML private Button moveUpButton;
	@FXML private Button moveDownButton;

	@FXML private CheckBox mapListsToMorphemesCheckBox;

	private MainController mainController;

	private FileChooser listFileChooser;

	@FXML
	public void addLists(ActionEvent event) {
		List<File> files = listFileChooser.showOpenMultipleDialog(mainController.getStage());
		if (files != null && !files.isEmpty()) {
			Path root = mainController.getRootFile().toPath();
			for (File file : files) {
				String path;
				if (file.toPath().startsWith(root)) {
					path = root.relativize(file.toPath()).toString();
				} else {
					path = file.getAbsolutePath();
				}
				mainController.getPreferences().getMorphemeFileList().add(path);
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
		morphemeListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				return new MorphemeListListCell();
			}
		});
		ListProperty<String> morphemelistProperty = new SimpleListProperty<>();
		morphemelistProperty.bind(morphemeListView.itemsProperty());
		morphemeListView.setPlaceholder(mainController.loadFXML("no_morpheme_lists_message.fxml"));

		BooleanBinding emptySelection = morphemeListView.getSelectionModel().selectedItemProperty().isNull();
		this.clearButton.disableProperty().bind(morphemelistProperty.emptyProperty());
		this.removeButton.disableProperty().bind(emptySelection);
		this.moveUpButton.disableProperty().bind(emptySelection);
		this.moveDownButton.disableProperty().bind(emptySelection);

		this.mapListsToMorphemesCheckBox.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			mainController.getPreferences().setMapListsToMorphemes(newValue);
			mainController.stateUpdated();
		});
	}

	public void moveLists(boolean down) {
		Preferences preferences = mainController.getPreferences();
		List<String> selection = new ArrayList<>(morphemeListView.getSelectionModel().getSelectedItems());
		if (down) {
			preferences.getMorphemeFileList().moveDown(selection);
		} else {
			preferences.getMorphemeFileList().moveUp(selection);
		}

		mainController.stateUpdated();

		morphemeListView.getSelectionModel().clearSelection();
		for (String list : selection) {
			morphemeListView.getSelectionModel().select(list);
		}
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
		mapListsToMorphemesCheckBox.setSelected(preferences.isMorphemesMappedToLists());
	}
}