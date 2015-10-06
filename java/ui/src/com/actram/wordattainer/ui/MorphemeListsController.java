package com.actram.wordattainer.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class MorphemeListsController implements Initializable {
	@FXML private ListView<?> morphemeListsListView;

	@FXML
	public void addList(ActionEvent event) {
		System.out.println("add list");
	}

	@FXML
	public void clearLists(ActionEvent event) {
		System.out.println("clear lists");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	public void moveListDown(ActionEvent event) {
		System.out.println("move list down");
	}

	@FXML
	public void moveListUp(ActionEvent event) {
		System.out.println("move list up");
	}

	@FXML
	public void removeList(ActionEvent event) {
		System.out.println("remove list");
	}
}