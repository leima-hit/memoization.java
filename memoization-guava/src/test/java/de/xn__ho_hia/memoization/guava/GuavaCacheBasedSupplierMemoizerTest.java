/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.guava;

import java.util.function.Supplier;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 *
 */
public class GuavaCacheBasedSupplierMemoizerTest {

    /** Captures expected exceptions. */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
    *
    */
    @Test
    @SuppressWarnings("static-method")
    public void shouldAcceptLoadingCacheAndKeySupplier() {
        // given
        final Supplier<String> keySupplier = () -> "key"; //$NON-NLS-1$
        final Supplier<String> supplier = () -> "value"; //$NON-NLS-1$
        final LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .build(new SupplierBasedCacheLoader<>(supplier));

        // when
        final GuavaCacheBasedSupplierMemoizer<String, String> memoizer = new GuavaCacheBasedSupplierMemoizer<>(cache,
                keySupplier);

        // then
        Assert.assertNotNull(memoizer);
    }

    /**
    *
    */
    @Test
    @SuppressWarnings({ "static-method", "nls" })
    public void shouldReturnSuppliedValue() {
        // given
        final Supplier<String> keySupplier = () -> "key"; //$NON-NLS-1$
        final Supplier<String> supplier = () -> "value"; //$NON-NLS-1$
        final LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .build(new SupplierBasedCacheLoader<>(supplier));

        // when
        final GuavaCacheBasedSupplierMemoizer<String, String> memoizer = new GuavaCacheBasedSupplierMemoizer<>(cache,
                keySupplier);

        // then
        Assert.assertEquals("value", memoizer.get());
    }

}
