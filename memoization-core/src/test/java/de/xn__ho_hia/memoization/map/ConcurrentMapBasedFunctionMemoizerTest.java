/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.xn__ho_hia.quality.suppression.CompilerWarnings;

/**
 *
 */
@SuppressWarnings({ CompilerWarnings.NLS, CompilerWarnings.STATIC_METHOD })
public class ConcurrentMapBasedFunctionMemoizerTest {

    /**
     *
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     *
     */
    @Test
    public void shouldAcceptCacheAndFunction() {
        // given
        final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();
        final Function<String, String> function = input -> "output";

        // when
        final ConcurrentMapBasedFunctionMemoizer<String, String> memoizer = new ConcurrentMapBasedFunctionMemoizer<>(
                cache, function);

        // then
        Assert.assertNotNull("Memoizer is NULL", memoizer);
    }

    /**
     *
     */
    @Test
    @SuppressWarnings(CompilerWarnings.UNUSED)
    public void shouldRequireNonNullCache() {
        // given
        final ConcurrentMap<String, String> cache = null;
        final Function<String, String> function = input -> "output";

        // when
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Provide an empty map instead of NULL.");

        // then
        new ConcurrentMapBasedFunctionMemoizer<>(cache, function);
    }

    /**
     *
     */
    @Test
    @SuppressWarnings(CompilerWarnings.UNUSED)
    public void shouldRequireNonNullFunction() {
        // given
        final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();
        final Function<String, String> function = null;

        // when
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Cannot memoize a NULL Function - provide an actual Function to fix this.");

        // then
        new ConcurrentMapBasedFunctionMemoizer<>(cache, function);
    }

    /**
     *
     */
    @Test
    public void shouldMemoizeFunctionCall() {
        // given
        final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();
        final Function<String, String> function = input -> "output";

        // when
        final ConcurrentMapBasedFunctionMemoizer<String, String> memoizer = new ConcurrentMapBasedFunctionMemoizer<>(
                cache, function);

        // then
        Assert.assertEquals("Memoized value does not match expectations", "output", memoizer.apply("test"));
    }

}