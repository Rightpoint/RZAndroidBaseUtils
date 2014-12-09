package com.raizlabs.functions;

/**
 * Interface for a delegate which can be executed on given parameters.
 *
 * @param <Params> The type of parameters that this {@link Delegate} executes
 * upon.
 */
public interface Delegate<Params> {
	/**
	 * Executes this {@link Delegate} on the given parameters.
	 * @param params The parameters to the delegate.
	 */
	public void execute(Params params);
}
