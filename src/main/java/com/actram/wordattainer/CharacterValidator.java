package com.actram.wordattainer;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Tests characters to see if they are allowed in a result.
 *
 * @author Peter André Johansen
 */
public class CharacterValidator implements Serializable {
	private static final long serialVersionUID = 4925100950081975952L;

	private boolean allowLetters;
	private boolean allowDigits;
	private boolean allowPunctuation;
	private boolean allowCustom;
	private char[] customCharacters;

	public CharacterValidator() {
		allowLetters(true);
		allowDigits(false);
		allowPunctuation(false);
		allowCustom(false);
		setCustomCharacters(new char[0]);
	}

	public void allowCustom(boolean allowCustom) {
		this.allowCustom = allowCustom;
	}

	public void allowDigits(boolean allowDigits) {
		this.allowDigits = allowDigits;
	}

	public void allowLetters(boolean allowLetters) {
		this.allowLetters = allowLetters;
	}

	public void allowPunctuation(boolean allowPunctuation) {
		this.allowPunctuation = allowPunctuation;
	}

	public boolean isCustomAllowed() {
		return allowCustom;
	}

	public boolean isCustomChar(char c) {
		if (allowCustom) {
			for (char custom : customCharacters) {
				if (custom == c) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isDigitsAllowed() {
		return allowDigits;
	}

	public boolean isLettersAllowed() {
		return allowLetters;
	}

	public boolean isPunctuationAllowed() {
		return allowPunctuation;
	}

	public boolean isValid(char c) {
		if (allowLetters && Character.isLetter(c)) {
			return true;
		}
		if (allowDigits && Character.isDigit(c)) {
			return true;
		}
		if (allowPunctuation && Pattern.matches("\\p{Punct}", String.valueOf(c))) {
			return true;
		}
		if (allowCustom && isCustomChar(c)) {
			return true;
		}
		return false;
	}

	public void setCustomCharacters(char[] customCharacters) {
		Objects.requireNonNull("custom character cannot be null");
		this.customCharacters = customCharacters;
	}
}