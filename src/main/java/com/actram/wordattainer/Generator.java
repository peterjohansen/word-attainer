package com.actram.wordattainer;

import java.io.IOException;

/**
 * Generates words (as {@link String}s) based on {@link GeneratorSettings}.
 *
 * @author Peter André Johansen
 */
public interface Generator {

	/**
	 * Queries the generator for a result.
	 * <p>
	 * <strong>Note:</strong>Changes to the generator settings results in
	 * undefined behavior. If the settings need to be changed, call
	 * {@link #update(GeneratorSettings)} again with the new settings.
	 * 
	 * @return the result
	 * @throws IllegalStateException if the generator hasn't been initialized
	 */
	public String query();

	/**
	 * Initializes this generator.
	 * <p>
	 * <strong>Note:</strong>Changes to the generator settings results in
	 * undefined behavior when {@link #query()} is called. If the settings need
	 * to be changed, call this method again with the new settings.
	 * 
	 * @param settings the generator settings
	 */
	public void update(GeneratorSettings settings) throws IOException;

}