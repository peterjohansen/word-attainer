package com.actram.wordattainer.ui;

import java.util.Objects;

import com.actram.wordattainer.ui.generator.GeneratorMode;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Preferences {

	private GeneratorMode generatorMode;

	public Preferences() {
		this.generatorMode = GeneratorMode.LIST;
	}

	public GeneratorMode getGeneratorMode() {
		return generatorMode;
	}

	public void setGeneratorMode(GeneratorMode generatorMode) {
		Objects.requireNonNull(generatorMode, "the generator mode cannot be null");
		this.generatorMode = generatorMode;
	}

}