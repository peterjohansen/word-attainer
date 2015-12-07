package com.actram.wordattainer.ui.controllers;

import java.util.ResourceBundle;

import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.ResultList;
import com.actram.wordattainer.ui.Preferences;

/**
 * 
 *
 * @author Peter André Johansen
 */
public interface MainControllerChild {

	public void initialize(MainController mainController, ResourceBundle resources);

	public void updateUI(Preferences preferences, GeneratorSettings generatorSettings, ResultList results);

}