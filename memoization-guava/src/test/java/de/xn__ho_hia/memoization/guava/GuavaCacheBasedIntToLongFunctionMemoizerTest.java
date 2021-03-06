/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.guava;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.concurrent.ExecutionException;
import java.util.function.IntFunction;
import java.util.function.IntToLongFunction;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import de.xn__ho_hia.memoization.shared.MemoizationException;
import de.xn__ho_hia.quality.suppression.CompilerWarnings;

/**
 *
 *
 */
@SuppressWarnings({ CompilerWarnings.NLS, CompilerWarnings.STATIC_METHOD })
public class GuavaCacheBasedIntToLongFunctionMemoizerTest {

    /** Captures expected exceptions. */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
    *
    */
    @Test
    public void shouldAcceptLoadingCache() {
        // given
        final IntToLongFunction function = a -> 123L;
        final IntFunction<Integer> keyFunction = Integer::valueOf;
        final Cache<Integer, Long> cache = CacheBuilder.newBuilder().build();

        // when
        final GuavaCacheBasedIntToLongFunctionMemoizer<Integer> memoizer = new GuavaCacheBasedIntToLongFunctionMemoizer<>(
                cache, keyFunction, function);

        // then
        Assert.assertNotNull(memoizer);
    }

    /**
    *
    */
    @Test
    public void shouldTransformInput() {
        // given
        final IntToLongFunction function = a -> 123L;
        final IntFunction<Integer> keyFunction = Integer::valueOf;
        final Cache<Integer, Long> cache = CacheBuilder.newBuilder().build();

        // when
        final GuavaCacheBasedIntToLongFunctionMemoizer<Integer> memoizer = new GuavaCacheBasedIntToLongFunctionMemoizer<>(
                cache, keyFunction, function);

        // then
        Assert.assertEquals("Memoized value does not match expectation", 123L, memoizer.applyAsLong(789));
    }

    /**
     * @throws ExecutionException
     *             Added for the call to 'cache.get(..)'.
     */
    @Test
    @SuppressWarnings(CompilerWarnings.UNCHECKED)
    public void shouldWrapExecutionExceptionInMemoizationException() throws ExecutionException {
        // given
        final IntFunction<Integer> keyFunction = Integer::valueOf;
        final Cache<Integer, Long> cache = Mockito.mock(Cache.class);
        given(cache.get(any(), any())).willThrow(ExecutionException.class);
        final GuavaCacheBasedIntToLongFunctionMemoizer<Integer> memoizer = new GuavaCacheBasedIntToLongFunctionMemoizer<>(
                cache, keyFunction, null);

        // when
        thrown.expect(MemoizationException.class);

        // then
        memoizer.applyAsLong(789);
    }

}
