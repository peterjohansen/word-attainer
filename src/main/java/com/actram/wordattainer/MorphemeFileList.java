package com.actram.wordattainer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 *
 * @author Peter André Johansen
 */
public class MorphemeFileList implements Serializable {
	private static final long serialVersionUID = -1321200231491594320L;

	private final List<String> fileList = new ArrayList<>();

	public void add(String morphemeFile) {
		Objects.requireNonNull(morphemeFile, "morpheme file cannot be null");
		fileList.add(morphemeFile);
	}

	public void clear() {
		fileList.clear();
	}

	public boolean contains(int index) {
		return (index < size() && index >= 0);
	}

	public String get(int index) {
		if (!contains(index)) {
			throw new IllegalArgumentException("index is out of bounds: " + index);
		}
		return fileList.get(index);
	}

	public List<String> getFileList() {
		return Collections.unmodifiableList(fileList);
	}

	public boolean isEmpty() {
		return fileList.isEmpty();
	}

	public List<String> load(int index) throws IOException {
		if (!contains(index)) {
			throw new IllegalArgumentException("index is out of bounds: " + index);
		}
		return load(fileList.get(index));
	}

	public List<String> load(String file) throws IOException {
		if (!fileList.contains(file)) {
			throw new IllegalArgumentException("file is not in this list");
		}
		return Files.readAllLines(Paths.get(file));
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

	public String nextMorpheme(Random random) {
		Objects.requireNonNull(random, "the random object cannot be null");
		return get(random.nextInt(size()));
	}

	public void removeAll(List<String> selection) {
		Objects.requireNonNull(selection, "selection cannot be null");
		fileList.removeAll(selection);
	}

	public MorphemeFileList setTo(MorphemeFileList morphemeFileList) {
		Objects.requireNonNull(morphemeFileList, "morpheme file list cannot be null");
		fileList.clear();
		fileList.addAll(morphemeFileList.fileList);
		return this;
	}

	public int size() {
		return fileList.size();
	}

	public void swap(int index1, int index2) {
		if (contains(index1) && contains(index2)) {
			Collections.swap(fileList, index1, index2);
		}
	}

	public void swap(String element1, String element2) {
		if (size() < 2) {
			throw new IllegalArgumentException("list must contain at least two elements to swap");
		}
		Objects.requireNonNull(element1, "element 1 cannot be null");
		int indexElement1 = fileList.indexOf(element1);
		if (indexElement1 == -1) {
			throw new IllegalArgumentException("element 1 is not in this list");
		}
		Objects.requireNonNull(element2, "element 2 cannot be null");
		int indexElement2 = fileList.indexOf(element2);
		if (indexElement2 == -1) {
			throw new IllegalArgumentException("element 2 is not in this list");
		}
		swap(indexElement1, indexElement2);
	}
}