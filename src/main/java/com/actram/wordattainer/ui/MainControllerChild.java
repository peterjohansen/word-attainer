package com.actram.wordattainer.ui;

import java.util.ResourceBundle;

import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.ResultList;

/**
 * 
 *
 * @author Peter André Johansen
 */
public interface MainControllerChild {

	public void initialize(MainController mainController, ResourceBundle resources);

	public void updateUI(Preferences preferences, GeneratorSettings generatorSettings, ResultList results);

}