/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.guava;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.LoadingCache;

import de.xn__ho_hia.memoization.shared.MemoizationException;

abstract class AbstractGuavaLoadingCacheBasedMemoizer<KEY, VALUE> {

    private final LoadingCache<KEY, VALUE> cache;

    AbstractGuavaLoadingCacheBasedMemoizer(final LoadingCache<KEY, VALUE> cache) {
        this.cache = cache;
    }

    protected final VALUE get(final KEY key) {
        try {
            return cache.get(key);
        } catch (final ExecutionException exception) {
            throw new MemoizationException(exception);
        }
    }

}
