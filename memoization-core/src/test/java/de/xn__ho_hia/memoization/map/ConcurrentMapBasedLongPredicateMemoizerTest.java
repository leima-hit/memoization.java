/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.xn__ho_hia.quality.suppression.CompilerWarnings;

/**
 *
 */
@SuppressWarnings({ CompilerWarnings.NLS, CompilerWarnings.STATIC_METHOD })
public class ConcurrentMapBasedLongPredicateMemoizerTest {

    /**
    *
    */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
    *
    */
    @Test
    public void shouldAcceptCacheAndPredicate() {
        // given
        final ConcurrentMap<Long, Boolean> cache = new ConcurrentHashMap<>();
        final LongPredicate predicate = input -> true;
        final LongFunction<Long> keyFunction = Long::valueOf;

        // when
        final ConcurrentMapBasedLongPredicateMemoizer<Long> memoizer = new ConcurrentMapBasedLongPredicateMemoizer<>(
                cache, keyFunction, predicate);

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
        final ConcurrentMap<Long, Boolean> cache = null;
        final LongPredicate predicate = input -> true;
        final LongFunction<Long> keyFunction = Long::valueOf;

        // when
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Provide an empty map instead of NULL.");

        // then
        new ConcurrentMapBasedLongPredicateMemoizer<>(cache, keyFunction, predicate);
    }

    /**
    *
    */
    @Test
    @SuppressWarnings(CompilerWarnings.UNUSED)
    public void shouldRequireNonNullPredicate() {
        // given
        final ConcurrentMap<Long, Boolean> cache = new ConcurrentHashMap<>();
        final LongPredicate predicate = null;
        final LongFunction<Long> keyFunction = Long::valueOf;

        // when
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Cannot memoize a NULL Predicate - provide an actual Predicate to fix this.");

        // then
        new ConcurrentMapBasedLongPredicateMemoizer<>(cache, keyFunction, predicate);
    }

    /**
    *
    */
    @Test
    public void shouldMemoizePredicateCall() {
        // given
        final ConcurrentMap<Long, Boolean> cache = new ConcurrentHashMap<>();
        final LongPredicate predicate = input -> true;
        final LongFunction<Long> keyFunction = Long::valueOf;

        // when
        final ConcurrentMapBasedLongPredicateMemoizer<Long> memoizer = new ConcurrentMapBasedLongPredicateMemoizer<>(
                cache, keyFunction, predicate);

        // then
        Assert.assertTrue("Memoized value does not match expectations", memoizer.test(123L));
    }

}
