/*
 * #%L
 * Parsington: the SciJava mathematical expression parser.
 * %%
 * Copyright (C) 2015 - 2021 Board of Regents of the University of
 * Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package org.scijava.parsington;

import java.io.IOException;

import org.scijava.parsington.eval.DefaultEvaluator;
import org.scijava.parsington.eval.EvaluatorConsole;

/**
 * Launches the console-driven expression evaluator.
 *
 * @author Curtis Rueden
 * @see EvaluatorConsole
 */
public final class Main {

	private Main() {
		// Prevent instantiation of utility class.
	}

	// -- Main method --

	public static void main(final String[] args) throws IOException {
		final DefaultEvaluator evaluator = new DefaultEvaluator();
		if (args.length > 0) {
			// Evaluate the given expressions.
			for (final String expression : args) {
				Object result = evaluator.evaluate(expression);
				if (result instanceof Variable) {
					// Unwrap the variable.
					result = evaluator.get((Variable) result);
				}
				System.out.println(result);
			}
		}
		else {
			// Show the REPL.
			new EvaluatorConsole(evaluator).showConsole();
		}
	}

}
