package com.actram.wordattainer;

import java.util.Objects;
import java.util.function.Consumer;

/**
 *
 *
 * @author Peter André Johansen
 */
public enum ResultCase {

	/**
	 * Lower case every morpheme.
	 */
	LOWER_CASE(morphemes -> {
		for (int i = 0; i < morphemes.length; i++) {
			morphemes[i] = morphemes[i].toLowerCase();
		}
	}),

	/**
	 * Upper case every morpheme.
	 */
	UPPER_CASE(morphemes -> {
		for (int i = 0; i < morphemes.length; i++) {
			morphemes[i] = morphemes[i].toUpperCase();
		}
	}),

	/**
	 * Capitalize every first letter of each morpheme.
	 */
	CAMEL_CASE(morphemes -> {
		LOWER_CASE.fixCapitalization(morphemes);
		for (int i = 0; i < morphemes.length; i++) {
			morphemes[i] = upperCaseFirstLetter(morphemes[i]);
		}
	}),

	/**
	 * Capitalize every first letter of each morpheme, except for the first
	 * morpheme which is entirely lower case.
	 */
	LOWER_CAMEL_CASE(morphemes -> {
		CAMEL_CASE.fixCapitalization(morphemes);
		if (morphemes.length >= 1) {
			morphemes[0] = lowerCaseFirstLetter(morphemes[0]);
		}
	}),

	/**
	 * Capitalize the morphemes as a sentence in relation to each other (i.e.
	 * the first morpheme's first letter is upper case, everything else is lower
	 * case).
	 */
	SENTENCE_CASE(morphemes -> {
		LOWER_CASE.fixCapitalization(morphemes);
		if (morphemes.length >= 1) {
			morphemes[0] = upperCaseFirstLetter(morphemes[0]);
		}
	});

	private static String lowerCaseFirstLetter(String str) {
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	private static String upperCaseFirstLetter(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	private final Consumer<String[]> capitalizer;

	private ResultCase(Consumer<String[]> capitalizer) {
		Objects.requireNonNull(capitalizer, "capitalizer cannot be null");
		this.capitalizer = capitalizer;
	}

	public void fixCapitalization(String[] morphemes) {
		Objects.requireNonNull(morphemes, "morphemes cannot be null");
		this.capitalizer.accept(morphemes);
	}
}