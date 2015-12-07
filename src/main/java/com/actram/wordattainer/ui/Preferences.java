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
	private int resultAmount;

	public Preferences() {
		setGeneratorMode(GeneratorMode.LIST);
		setCharacterValidator(new CharacterValidator());
		setResultAmount(32);
	}

	public CharacterValidator getCharacterValidator() {
		return characterValidator;
	}

	public GeneratorMode getGeneratorMode() {
		return generatorMode;
	}

	public int getResultAmount() {
		return resultAmount;
	}

	public void setCharacterValidator(CharacterValidator characterValidator) {
		this.characterValidator = characterValidator;
	}

	public void setGeneratorMode(GeneratorMode generatorMode) {
		Objects.requireNonNull(generatorMode, "the generator mode cannot be null");
		this.generatorMode = generatorMode;
	}

	public void setResultAmount(int resultAmount) {
		this.resultAmount = resultAmount;
	}
}