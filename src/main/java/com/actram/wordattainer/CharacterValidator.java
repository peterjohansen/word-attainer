package com.actram.wordattainer;

import java.util.regex.Pattern;

/**
 *
 *
 * @author Peter André Johansen
 */
public class CharacterValidator {
	private boolean allowLetters;
	private boolean allowDigits;
	private boolean allowPunctuation;
	private boolean allowCustom;
	private char[] customCharacters;

	public CharacterValidator() {
		setAllowLetters(true);
		setAllowDigits(false);
		setAllowPunctuation(false);
		setAllowCustom(false);
		setCustomCharacters(new char[0]);
	}

	public boolean allowCustom() {
		return allowCustom;
	}

	public boolean allowDigits() {
		return allowDigits;
	}

	public boolean allowLetters() {
		return allowLetters;
	}

	public boolean allowPunctuation() {
		return allowPunctuation;
	}

	public char[] getCustomCharacters() {
		return customCharacters;
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
		if (allowCustom) {
			for (char custom : customCharacters) {
				if (custom == c) {
					return true;
				}
			}
		}
		return false;
	}

	public void setAllowCustom(boolean allowCustom) {
		this.allowCustom = allowCustom;
	}

	public void setAllowDigits(boolean allowDigits) {
		this.allowDigits = allowDigits;
	}

	public void setAllowLetters(boolean allowLetters) {
		this.allowLetters = allowLetters;
	}

	public void setAllowPunctuation(boolean allowPunctuation) {
		this.allowPunctuation = allowPunctuation;
	}

	public void setCustomCharacters(char[] customCharacters) {
		this.customCharacters = customCharacters;
	}
}