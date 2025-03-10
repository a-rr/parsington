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

package org.scijava.parsington.eval;

import java.util.LinkedList;
import java.util.Map;

import org.scijava.parsington.ExpressionParser;
import org.scijava.parsington.SyntaxTree;
import org.scijava.parsington.Variable;

/**
 * Interface for expression evaluators.
 *
 * @author Curtis Rueden
 */
public interface Evaluator {

	/** Gets the parser used when evaluating expressions. */
	ExpressionParser getParser();

	/**
	 * Gets whether the evaluator is operating in strict mode.
	 * 
	 * @see #setStrict(boolean)
	 */
	boolean isStrict();

	/**
	 * Sets whether the evaluator is operating in strict mode. Evaluators operate
	 * in strict mode by default.
	 * <p>
	 * When evaluating strictly, usage of an unassigned variable token in a place
	 * where its value is needed will generate an {@link IllegalArgumentException}
	 * with an "Unknown variable" message; in non-strict mode, such a variable
	 * will instead be resolved to an object of type {@link Unresolved} with the
	 * same name as the original variable.
	 * </p>
	 * <p>
	 * In cases such as assignment, this may be sufficient to complete the
	 * evaluation; for example, the expression {@code foo=bar} will complete
	 * successfully in non-strict mode, with the variable {@code foo} containing
	 * an object of type {@link Unresolved} and token value {@code "bar"}. But in
	 * cases where the unresolved value is needed as an input for additional
	 * operations, the evaluation may still ultimately fail of the operation in
	 * question is not defined for unresolved values. For example, the
	 * {@link DefaultEvaluator} will fail with an "Unsupported binary operator"
	 * exception when given the expression {@code foo+bar}, since {@code foo} and
	 * {@code bar} are unresolved variables, and the {@code +} operator cannot
	 * handle such objects.
	 * </p>
	 */
	void setStrict(boolean strict);

	/** Evaluates the given infix expression, returning the result. */
	Object evaluate(final String expression);

	/** Evaluates the given postfix token queue, returning the result. */
	Object evaluate(final LinkedList<Object> queue);

	/** Evaluates the given syntax tree, returning the result. */
	Object evaluate(final SyntaxTree syntaxTree);

	/**
	 * Gets the value of the given token. For variables, returns the value of the
	 * variable, throwing an exception if the variable is not set. For literals,
	 * returns the token itself.
	 */
	Object value(Object token);

	/** Gets the value of the given variable. */
	Object get(Variable v);

	/** Sets the value of the given variable. */
	void set(Variable v, Object value);

	/** Assigns variables en masse. */
	void setAll(Map<? extends String, ? extends Object> map);

}
