/*
 * #%L
 * SciJava mathematical expression parser.
 * %%
 * Copyright (C) 2015 Board of Regents of the University of
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

package org.scijava.sjep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

/**
 * Tests {@link ExpressionParser}.
 *
 * @author Curtis Rueden
 */
public class ExpressionParserTest extends AbstractTest {

	@Test
	public void testStrings() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("'hello'*\"world\"");

		assertNotNull(queue);
		// ["hello", "world", *]
		assertEquals(3, queue.size());
		assertString("hello", queue.get(0));
		assertString("world", queue.get(1));
	}

	@Test
	public void testNumbers() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("1.1+2L+3f+4-+5d-6");

		assertNotNull(queue);
		// [1.1, 2L, +, 3f, +, 4, +, +5d, -, 6, -]
		assertEquals(11, queue.size());
		assertNumber(1.1, queue.get(0));
		assertNumber(2L, queue.get(1));
		assertNumber(3f, queue.get(3));
		assertNumber(4, queue.get(5));
		assertNumber(+5d, queue.get(7));
		assertNumber(6, queue.get(9));
	}

	/** Tests each individual standard operator. */
	@Test
	public void testOperatorsIndividual() {
		final ExpressionParser p = new ExpressionParser();
		assertUnary(p, "a", Operators.TRANSPOSE, "a'");
		assertUnary(p, "a", Operators.DOT_TRANSPOSE, "a.'");
		assertBinary(p, "a", Operators.POW, "b", "a^b");
		assertBinary(p, "a", Operators.DOT_POW, "b", "a.^b");
		assertUnary(p, "a", Operators.PRE_INC, "++a");
		assertUnary(p, "a", Operators.POST_INC, "a++");
		assertUnary(p, "a", Operators.PRE_DEC, "--a");
		assertUnary(p, "a", Operators.POST_DEC, "a--");
		assertUnary(p, "a", Operators.POS, "+a");
		assertUnary(p, "a", Operators.NEG, "-a");
		assertUnary(p, "a", Operators.COMPLEMENT, "~a");
		assertUnary(p, "a", Operators.NOT, "!a");
		assertBinary(p, "a", Operators.MUL, "b", "a*b");
		assertBinary(p, "a", Operators.DIV, "b", "a/b");
		assertBinary(p, "a", Operators.MOD, "b", "a%b");
		assertBinary(p, "a", Operators.ADD, "b", "a+b");
		assertBinary(p, "a", Operators.SUB, "b", "a-b");
		assertBinary(p, "a", Operators.LEFT_SHIFT, "b", "a<<b");
		assertBinary(p, "a", Operators.RIGHT_SHIFT, "b", "a>>b");
		assertBinary(p, "a", Operators.UNSIGNED_RIGHT_SHIFT, "b", "a>>>b");
		assertBinary(p, "a", Operators.COLON, "b", "a:b");
		assertBinary(p, "a", Operators.LESS_THAN, "b", "a<b");
		assertBinary(p, "a", Operators.GREATER_THAN, "b", "a>b");
		assertBinary(p, "a", Operators.LESS_THAN_OR_EQUAL, "b", "a<=b");
		assertBinary(p, "a", Operators.GREATER_THAN_OR_EQUAL, "b", "a>=b");
		assertBinary(p, "a", Operators.INSTANCEOF, "b", "a instanceof b");
		assertBinary(p, "a", Operators.EQUAL, "b", "a==b");
		assertBinary(p, "a", Operators.NOT_EQUAL, "b", "a!=b");
		assertBinary(p, "a", Operators.BITWISE_AND, "b", "a&b");
		assertBinary(p, "a", Operators.BITWISE_OR, "b", "a|b");
		assertBinary(p, "a", Operators.LOGICAL_AND, "b", "a&&b");
		assertBinary(p, "a", Operators.LOGICAL_OR, "b", "a||b");
		assertBinary(p, "a", Operators.ASSIGN, "b", "a=b");
		assertBinary(p, "a", Operators.POW_ASSIGN, "b", "a^=b");
		assertBinary(p, "a", Operators.DOT_POW_ASSIGN, "b", "a.^=b");
		assertBinary(p, "a", Operators.MUL_ASSIGN, "b", "a*=b");
		assertBinary(p, "a", Operators.DIV_ASSIGN, "b", "a/=b");
		assertBinary(p, "a", Operators.MOD_ASSIGN, "b", "a%=b");
		assertBinary(p, "a", Operators.RIGHT_DIV_ASSIGN, "b", "a\\=b");
		assertBinary(p, "a", Operators.DOT_DIV_ASSIGN, "b", "a./=b");
		assertBinary(p, "a", Operators.DOT_RIGHT_DIV_ASSIGN, "b", "a.\\=b");
		assertBinary(p, "a", Operators.ADD_ASSIGN, "b", "a+=b");
		assertBinary(p, "a", Operators.SUB_ASSIGN, "b", "a-=b");
		assertBinary(p, "a", Operators.AND_ASSIGN, "b", "a&=b");
		assertBinary(p, "a", Operators.OR_ASSIGN, "b", "a|=b");
		assertBinary(p, "a", Operators.LEFT_SHIFT_ASSIGN, "b", "a<<=b");
		assertBinary(p, "a", Operators.RIGHT_SHIFT_ASSIGN, "b", "a>>=b");
		assertBinary(p, "a", Operators.UNSIGNED_RIGHT_SHIFT_ASSIGN, "b", "a>>>=b");
	}

	@Test
	public void testUnaryOperator1() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("-a");

		assertNotNull(queue);
		// [a, -]
		assertVariable("a", queue.get(0));
		assertSame(Operators.NEG, queue.get(1));
	}

	@Test
	public void testUnaryOperator2() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("-+a");

		assertNotNull(queue);
		// [a, +, -]
		assertVariable("a", queue.get(0));
		assertSame(Operators.POS, queue.get(1));
		assertSame(Operators.NEG, queue.get(2));
	}

	@Test
	public void testUnaryOperator3() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("a+-b");

		assertNotNull(queue);
		// [a, b, -, +]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertSame(Operators.NEG, queue.get(2));
		assertSame(Operators.ADD, queue.get(3));
	}

	@Test
	public void testUnaryOperator4() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("a-+-b");

		assertNotNull(queue);
		// [a, b, -, +, -]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertSame(Operators.NEG, queue.get(2));
		assertSame(Operators.POS, queue.get(3));
		assertSame(Operators.SUB, queue.get(4));
	}

	@Test
	public void testUnaryOperator5() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("-+a-+-b");

		assertNotNull(queue);
		// [a, +, -, b, -, +, -]
		assertVariable("a", queue.get(0));
		assertSame(Operators.POS, queue.get(1));
		assertSame(Operators.NEG, queue.get(2));
		assertVariable("b", queue.get(3));
		assertSame(Operators.NEG, queue.get(4));
		assertSame(Operators.POS, queue.get(5));
		assertSame(Operators.SUB, queue.get(6));
	}

	@Test
	public void testBinaryOperator() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("a-b");

		assertNotNull(queue);
		// [a, b, -]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertSame(Operators.SUB, queue.get(2));
	}

	@Test
	public void testNullaryFunction() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("f()");

		assertNotNull(queue);
		// [f]
		assertFunction("f", 0, queue.get(0));
	}

	@Test
	public void testUnaryFunction() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("f(a)");

		assertNotNull(queue);
		// [a, f]
		assertVariable("a", queue.get(0));
		assertFunction("f", 1, queue.get(1));
	}

	@Test
	public void testBinaryFunction() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("f(a,b)");

		assertNotNull(queue);
		// [a, b, f]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertFunction("f", 2, queue.get(2));
	}

	@Test
	public void testTernaryFunction() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("f(a,b,c)");

		assertNotNull(queue);
		// [a, b, c, f]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertVariable("c", queue.get(2));
		assertFunction("f", 3, queue.get(3));
	}

	@Test
	public void testNestedFunctions() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue =
			parser.parsePostfix("f(g(),a,h(b),i(c,d))");

		assertNotNull(queue);
		// [g, a, b, h, c, d, i, f]
		assertFunction("g", 0, queue.get(0));
		assertVariable("a", queue.get(1));
		assertVariable("b", queue.get(2));
		assertFunction("h", 1, queue.get(3));
		assertVariable("c", queue.get(4));
		assertVariable("d", queue.get(5));
		assertFunction("i", 2, queue.get(6));
		assertFunction("f", 4, queue.get(7));
	}

	@Test
	public void testOperatorPrecedence() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("a+b*c");

		assertNotNull(queue);
		// [a, b, c, *, +]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertVariable("c", queue.get(2));
		assertSame(Operators.MUL, queue.get(3));
		assertSame(Operators.ADD, queue.get(4));
	}

	@Test
	public void testOperatorAssociativity() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue = parser.parsePostfix("(a/b/c)+(a^b^c)");

		assertNotNull(queue);
		// [a, b, /, c, /, a, b, c, ^, ^, +]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertSame(Operators.DIV, queue.get(2));
		assertVariable("c", queue.get(3));
		assertSame(Operators.DIV, queue.get(4));
		assertVariable("a", queue.get(5));
		assertVariable("b", queue.get(6));
		assertVariable("c", queue.get(7));
		assertSame(Operators.POW, queue.get(8));
		assertSame(Operators.POW, queue.get(9));
		assertSame(Operators.ADD, queue.get(10));
	}

	/** A more complex test of {@link ExpressionParser#parsePostfix}. */
	@Test
	public void testParsePostfix() {
		final ExpressionParser parser = new ExpressionParser();
		final LinkedList<Object> queue =
			parser.parsePostfix("a*b-c*a/func(quick,brown,fox)^foo^bar");

		assertNotNull(queue);
		assertEquals(16, queue.size());
		// [a, b, *, c, a, *, quick, brown, fox, func, foo, bar, ^, ^, /, -]
		assertVariable("a", queue.get(0));
		assertVariable("b", queue.get(1));
		assertSame(Operators.MUL, queue.get(2));
		assertVariable("c", queue.get(3));
		assertVariable("a", queue.get(4));
		assertSame(Operators.MUL, queue.get(5));
		assertVariable("quick", queue.get(6));
		assertVariable("brown", queue.get(7));
		assertVariable("fox", queue.get(8));
		assertFunction("func", 3, queue.get(9));
		assertVariable("foo", queue.get(10));
		assertVariable("bar", queue.get(11));
		assertSame(Operators.POW, queue.get(12));
		assertSame(Operators.POW, queue.get(13));
		assertSame(Operators.DIV, queue.get(14));
		assertSame(Operators.SUB, queue.get(15));
	}

	/** Tests empty expressions. */
	@Test
	public void testEmpty() {
		final ExpressionParser parser = new ExpressionParser();
		assertEquals(0, parser.parsePostfix("").size());
		assertEquals(0, parser.parsePostfix("()").size());
		assertEquals(0, parser.parsePostfix("(())").size());
	}

	/** Tests that parsing an invalid expression fails appropriately. */
	@Test
	public void testInvalid() {
		final ExpressionParser parser = new ExpressionParser();
		assertInvalid(parser, "a a", "Invalid character at index 2");
		assertInvalid(parser, "func(,)", "Invalid character at index 5");
		assertInvalid(parser, "foo,bar",
			"Misplaced separator or mismatched parentheses at index 4");
		assertInvalid(parser, "(", "Mismatched parentheses at index 1");
		assertInvalid(parser, ")", "Mismatched parentheses at index 1");
		assertInvalid(parser, ")(", "Mismatched parentheses at index 1");
		assertInvalid(parser, "(()", "Mismatched parentheses at index 3");
	}

	// -- Helper

	private void assertUnary(final ExpressionParser parser, final String var,
		final Operator op, final String expression)
	{
		final LinkedList<Object> queue = parser.parsePostfix(expression);
		assertNotNull(queue);
		assertEquals(2, queue.size());
		assertVariable(var, queue.get(0));
		assertSame(op, queue.get(1));
	}

	private void assertBinary(final ExpressionParser parser, final String var1,
		final Operator op, final String var2, final String expression)
	{
		final LinkedList<Object> queue = parser.parsePostfix(expression);
		assertNotNull(queue);
		assertEquals(3, queue.size());
		assertVariable(var1, queue.get(0));
		assertVariable(var2, queue.get(1));
		assertSame(op, queue.get(2));
	}

	private void assertInvalid(final ExpressionParser parser,
		final String expression, final String message)
	{
		try {
			parser.parsePostfix(expression);
			fail("Expected IllegalArgumentException");
		}
		catch (final IllegalArgumentException exc) {
			assertEquals(message, exc.getMessage());
		}
	}

}
