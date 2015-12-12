package com.actram.wordattainer;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 *
 * @author Peter André Johansen
 */
public class Result {
	private static int lengthOf(String[] parts) {
		Objects.requireNonNull(parts, "string parts cannot be null");
		int length = 0;
		for (String part : parts) {
			length += part.length();
		}
		return length;
	}

	private String[] parts;
	private int length;

	public Result(int capacity) {
		if (capacity < 0) {
			throw new IllegalArgumentException("capacity cannot be negative");
		}
		String[] parts = new String[capacity];
		Arrays.fill(parts, "");
		setParts(parts);
	}

	public void capitalize(ResultCase capitalization) {
		Objects.requireNonNull(capitalization, "capitalization cannot be null");
		capitalization.fixCapitalization(parts);
	}

	public char charAt(int index) {
		requireValidIndex(index);

		int newIndex = index;
		int partIndex = 0;
		for (int i = 0; i < partCount(); i++) {
			partIndex += parts[i].length();
			if (index < partIndex) {
				partIndex = i;
				return parts[partIndex].charAt(newIndex);
			}
			newIndex -= parts[i].length();
		}

		throw new AssertionError("part of code unexpectedly reached");
	}

	public boolean isValue(int partIndex, String value) {
		if (partIndex < 0 || partIndex > parts.length - 1) {
			return false;
		}
		return parts[partIndex].equals(value);
	}

	public String join(String separator) {
		StringBuilder builder = new StringBuilder(length);
		for (String part : parts) {
			if (builder.length() != 0) {
				builder.append(separator);
			}
			builder.append(part);
		}
		return builder.toString();
	}

	public int length() {
		return length;
	}

	public int partCount() {
		return parts.length;
	}

	public int partIndexOf(int index) {
		requireValidIndex(index);
		int partIndex = 0;
		for (int i = 0; i < parts.length; i++) {
			partIndex += parts[i].length();
			if (index < partIndex) {
				return i;
			}
		}
		return -1;
	}

	private void requireValidIndex(int index) {
		if (index < 0 || index > length - 1) {
			throw new IllegalArgumentException("index out of bounds: " + index);
		}
	}

	private void requireValidPartIndex(int index) {
		if (index < 0 || index > parts.length - 1) {
			throw new IllegalArgumentException("part index out of bounds: " + index);
		}
	}

	public void set(int partIndex, String value) {
		requireValidPartIndex(partIndex);
		Objects.requireNonNull(value, "value cannot be null");
		length += (value.length() - parts[partIndex].length());
		parts[partIndex] = value;
	}

	private void setParts(String[] parts) {
		this.parts = parts;
		this.length = lengthOf(parts);
	}

	@Override
	public String toString() {
		return join("");
	}

	/**
	 * <strong>Note:</strong>Does not guarantee that the casing will remain the
	 * same.
	 * 
	 * @param limit the maximum amount of duplicate, consecutive characters to
	 *            remain untouched
	 * @param checkChars the characters to check for (must be lower case)
	 */
	public void trimDuplicateConsecutive(int limit, String checkChars) {
		if (limit < 1) {
			throw new IllegalArgumentException("the limit cannot be less than one");
		}
		Objects.requireNonNull(checkChars, "string of characters to check cannot be null");

		limit--;
		StringBuilder[] newResult = new StringBuilder[partCount()];
		for (int i = 0; i < newResult.length; i++) {
			newResult[i] = new StringBuilder(parts[i].length());
		}
		for (int i = 0; i < length(); i++) {
			char currChar = Character.toLowerCase(charAt(i));
			int count = 0;

			for (int j = 1; j < length() - i; j++) {
				if (currChar != Character.toLowerCase(charAt(i + j))) {
					break;
				}
				if (count > limit) {
					break;
				}
				if (checkChars.indexOf(currChar) == -1) {
					break;
				}
				count++;
			}
			if (count <= limit) {
				newResult[partIndexOf(i)].append(currChar);
			}
		}
		for (int i = 0; i < newResult.length; i++) {
			set(i, newResult[i].toString());
		}
	}
}