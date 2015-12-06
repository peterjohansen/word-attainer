package com.actram.wordattainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 *
 * @author Peter André Johansen
 */
public class MorphemeList extends ArrayList<String> {
	private static final long serialVersionUID = -2082355773577858672L;

	public boolean contains(int index) {
		return (index < size() && index >= 0);
	}

	public void moveDown(List<String> selection) {
		Objects.requireNonNull(selection, "selection cannot be null");

		// Don't move elements further down if
		// one of them has reached the end
		if (selection.get(selection.size() - 1).equals(get(size() - 1))) {
			return;
		}

		// Move selection down by one
		for (int i = size() - 2; i >= 0; i--) {
			String e1 = get(i);
			String e2 = get(i + 1);
			if (selection.contains(e1)) {
				swap(e1, e2);
			}
		}
	}

	public void moveUp(List<String> selection) {
		Objects.requireNonNull(selection, "selection cannot be null");

		// Don't move elements further up if
		// one of them has reached the start
		if (selection.get(0).equals(get(0))) {
			return;
		}

		// Move selection up by one
		for (int i = 1; i < size(); i++) {
			String e1 = get(i);
			String e2 = get(i - 1);
			if (selection.contains(e1)) {
				swap(e1, e2);
			}
		}
	}

	public void swap(int index1, int index2) {
		if (contains(index1) && contains(index2)) {
			Collections.swap(this, index1, index2);
		}
	}

	public void swap(String element1, String element2) {
		if (size() < 2) {
			throw new IllegalArgumentException("list must contain at least two elements to swap");
		}
		Objects.requireNonNull(element1, "element 1 cannot be null");
		int indexElement1 = indexOf(element1);
		if (indexElement1 == -1) {
			throw new IllegalArgumentException("element 1 is not in this list");
		}
		Objects.requireNonNull(element2, "element 2 cannot be null");
		int indexElement2 = indexOf(element2);
		if (indexElement2 == -1) {
			throw new IllegalArgumentException("element 2 is not in this list");
		}
		swap(indexElement1, indexElement2);
	}
}