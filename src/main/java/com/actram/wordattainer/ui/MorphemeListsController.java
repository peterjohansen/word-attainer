package com.actram.wordattainer.ui;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.actram.wordattainer.MorphemeList;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;

/**
 *
 *
 * @author Peter André Johansen
 */
public class MorphemeListsController implements Initializable {
	@FXML
	private ListView<String> morphemeListView;
	@FXML
	private Button removeButton;
	@FXML
	private Button clearButton;
	@FXML
	private Button moveUpButton;
	@FXML
	private Button moveDownButton;

	private WordAttainer program;
	private MorphemeList morphemes;

	private FileChooser fileChooser;

	private ListProperty<String> morphemeListProperty;

	@FXML
	public void addList(ActionEvent event) {
		List<File> files = fileChooser.showOpenMultipleDialog(program.getStage());
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
			if (program.showConfirmAlert("Confirm clear", "Are you sure you want to clear the morpheme lists?\n\n"
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
		this.morphemes = program.getMorphemes();

		this.fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.setTitle("Open Morpheme Lists");

		this.morphemeListProperty = new SimpleListProperty<>();
		morphemeListView.setItems(FXCollections.observableArrayList(morphemes));
		morphemeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		morphemeListView.getSelectionModel().selectedIndexProperty();

		morphemeListView.itemsProperty().bindBidirectional(morphemeListProperty);
		updateMorphemeListsUI();

		BooleanBinding emptySelection = morphemeListView.getSelectionModel().selectedItemProperty().isNull();
		clearButton.disableProperty().bind(morphemeListProperty.emptyProperty());
		removeButton.disableProperty().bind(emptySelection);
		moveUpButton.disableProperty().bind(emptySelection);
		moveDownButton.disableProperty().bind(emptySelection);
	}

	private void moveLists(boolean down) {
		ObservableList<Integer> selectedIndices = morphemeListView.getSelectionModel().getSelectedIndices();
		Integer[] newIndices = selectedIndices.toArray(new Integer[selectedIndices.size()]);
		morphemes.shift(newIndices, down);

		morphemes.setIndices(newIndices);
		// morphemeListView.getSelectionModel().selectIndices(firstIndex,
		// restIndices);
		updateMorphemeListsUI();
	}

	@FXML
	public void moveListDown(ActionEvent event) {
		moveLists(true);
	}

	@FXML
	public void moveListUp(ActionEvent event) {
		moveLists(false);
	}

	@FXML
	public void removeList(ActionEvent event) {
		System.out.println("remove list");
		updateMorphemeListsUI();
	}

	private void updateMorphemeListsUI() {
		morphemeListProperty.set(FXCollections.observableArrayList(morphemes));
	}
}