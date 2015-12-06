package com.actram.wordattainer.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.actram.wordattainer.MorphemeList;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class MorphemeListsController implements Initializable {
	@FXML private ListView<String> morphemeListView;
	@FXML private Button removeButton;
	@FXML private Button clearButton;
	@FXML private Button moveUpButton;
	@FXML private Button moveDownButton;
	@FXML private CheckBox retainOrderCheckBox;

	private WordAttainer program;
	private MorphemeList morphemes;

	private FileChooser listFileChooser;

	private ListProperty<String> morphemeListProperty;

	@FXML
	public void addLists(ActionEvent event) {
		List<File> files = listFileChooser.showOpenMultipleDialog(program.getStage());
		if (files != null && !files.isEmpty()) {
			for (File file : files) {
				morphemes.add(file.getAbsolutePath());
			}
			updateMorphemeListsUI();
		}
	}

	@FXML
	public void clearLists(ActionEvent event) {
		if (!morphemes.isEmpty()) {
			// @formatterOff
			if (program.showConfirmAlert("Confirm Clear", "Are you sure you want to clear the morpheme lists?\n\n"
					+ "The files will not be removed from your system.", "Clear lists", "Cancel")) {
				// @formatterOn
				morphemes.clear();
				updateMorphemeListsUI();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.program = WordAttainer.getInstance();
		this.morphemes = program.getSettings().getMorphemes();

		this.listFileChooser = new FileChooser();
		listFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		listFileChooser.setTitle("Open Morpheme Lists");

		this.morphemeListProperty = new SimpleListProperty<>();
		morphemeListView.setItems(FXCollections.observableArrayList(morphemes));
		morphemeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		morphemeListView.getSelectionModel().selectedIndexProperty();

		morphemeListView.itemsProperty().bindBidirectional(morphemeListProperty);
		updateMorphemeListsUI();

		BooleanBinding emptySelection = morphemeListView.getSelectionModel().selectedItemProperty().isNull();
		this.clearButton.disableProperty().bind(morphemeListProperty.emptyProperty());
		this.removeButton.disableProperty().bind(emptySelection);
		this.moveUpButton.disableProperty().bind(emptySelection);
		this.moveDownButton.disableProperty().bind(emptySelection);

		this.retainOrderCheckBox.selectedProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
			program.getSettings().setRetainMorphemesOrder(newValue);
		});
	}

	@FXML
	public void moveListsDown(ActionEvent event) {
		moveLists(true);
	}

	private void moveLists(boolean down) {
		List<String> selection = new ArrayList<>(morphemeListView.getSelectionModel().getSelectedItems());
		if (down) {
			morphemes.moveDown(selection);
		} else {
			morphemes.moveUp(selection);
		}
		updateMorphemeListsUI();
		morphemeListView.getSelectionModel().clearSelection();
		for (String list : selection) {
			morphemeListView.getSelectionModel().select(list);
		}
	}

	@FXML
	public void moveListsUp(ActionEvent event) {
		moveLists(false);
	}

	@FXML
	public void removeLists(ActionEvent event) {
		List<String> selection = new ArrayList<>(morphemeListView.getSelectionModel().getSelectedItems());
		morphemes.removeAll(selection);
		updateMorphemeListsUI();
	}

	private void updateMorphemeListsUI() {
		morphemeListProperty.set(FXCollections.observableArrayList(morphemes));
	}
}