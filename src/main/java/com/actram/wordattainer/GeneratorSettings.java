package com.actram.wordattainer;

/**
 * 
 *
 * @author Peter André Johansen
 */
public class GeneratorSettings {

	private final MorphemeList morphemes;
	private boolean retainMorphemesOrder;

	public GeneratorSettings() {
		this.morphemes = new MorphemeList();
		setRetainMorphemesOrder(false);
	}

	public MorphemeList getMorphemes() {
		return morphemes;
	}

	public boolean retainMorphemesOrder() {
		return retainMorphemesOrder;
	}

	public void setRetainMorphemesOrder(boolean retainMorphemesOrder) {
		this.retainMorphemesOrder = retainMorphemesOrder;
	}
}