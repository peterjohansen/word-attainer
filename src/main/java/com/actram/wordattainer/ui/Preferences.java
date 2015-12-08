package com.actram.wordattainer.ui;

import java.util.Objects;

import com.actram.wordattainer.Generator;
import com.actram.wordattainer.GeneratorSettings;
import com.actram.wordattainer.StandardGenerator;
import com.actram.wordattainer.ui.generator.GeneratorMode;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Preferences extends GeneratorSettings {
	private Generator generator;
	private GeneratorMode generatorMode;
	private int resultAmount;

	public Preferences() {
		setGenerator(new StandardGenerator());
		setGeneratorMode(GeneratorMode.LIST);
		setResultAmount(32);
	}

	public Generator getGenerator() {
		return generator;
	}

	public GeneratorMode getGeneratorMode() {
		return generatorMode;
	}

	public int getResultAmount() {
		return resultAmount;
	}

	public void setGenerator(Generator generator) {
		Objects.requireNonNull(generator, "the generator cannot be null");
		this.generator = generator;
	}

	public void setGeneratorMode(GeneratorMode generatorMode) {
		Objects.requireNonNull(generatorMode, "the generator mode cannot be null");
		this.generatorMode = generatorMode;
	}

	public void setResultAmount(int resultAmount) {
		if (resultAmount < 1) {
			throw new IllegalArgumentException("result amount cannot be less than one");
		}
		this.resultAmount = resultAmount;
	}
}