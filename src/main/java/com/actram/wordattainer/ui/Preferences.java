package com.actram.wordattainer.ui;

import java.util.Objects;

import com.actram.wordattainer.CharacterValidator;
import com.actram.wordattainer.ui.generator.GeneratorMode;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Preferences {
	private GeneratorMode generatorMode;
	private CharacterValidator characterValidator;

	public Preferences() {
		this.generatorMode = GeneratorMode.LIST;
		this.characterValidator = new CharacterValidator();
	}

	public CharacterValidator getCharacterValidator() {
		return characterValidator;
	}

	public GeneratorMode getGeneratorMode() {
		return generatorMode;
	}

	public void setGeneratorMode(GeneratorMode generatorMode) {
		Objects.requireNonNull(generatorMode, "the generator mode cannot be null");
		this.generatorMode = generatorMode;
	}
}