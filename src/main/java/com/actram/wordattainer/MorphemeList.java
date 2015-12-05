package com.actram.wordattainer;

import java.util.ArrayList;
import java.util.Objects;

public class MorphemeList extends ArrayList<String> {
	private static final long serialVersionUID = -2082355773577858672L;

	public static void swap(Integer[] indices, int index1, int index2) {
		Objects.requireNonNull(indices, "indices array cannot be null");
		if (indices.length < 2) {
			throw new IllegalArgumentException("indices array must contain at least two elements");
		}
		if (index1 < 0 || index1 > indices.length - 1) {
			throw new IllegalArgumentException("index1 is out of bounds: " + index1);
		}
		if (index2 < 0 || index2 > indices.length - 1) {
			throw new IllegalArgumentException("index2 is out of bounds: " + index2);
		}

		int temp = indices[index1];
		indices[index1] = index2;
		indices[index2] = temp;
	}

	public void move(int[] indices, boolean down) {
		Objects.requireNonNull(indices, "indices array cannot be null");
		if (down) {
			int last = indices[indices.length - 1];
			for (int index = indices.length - 2; index >= 0; index--) {
				indices[index + 1] = indices[index];
			}
			indices[0] = last;
		} else {
			int first = indices[0];
			for (int index = indices.length - 1; index > 0; index--) {
				indices[index - 1] = indices[index];
			}
			indices[indices.length - 1] = first;
		}
	}
}