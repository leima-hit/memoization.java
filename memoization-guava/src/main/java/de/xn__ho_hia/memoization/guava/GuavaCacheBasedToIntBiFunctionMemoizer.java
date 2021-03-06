/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.guava;

import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;

import com.google.common.cache.Cache;

final class GuavaCacheBasedToIntBiFunctionMemoizer<FIRST, SECOND, KEY>
        extends AbstractGuavaCacheBasedMemoizer<KEY, Integer>
        implements ToIntBiFunction<FIRST, SECOND> {

    private final BiFunction<FIRST, SECOND, KEY> keyFunction;
    private final ToIntBiFunction<FIRST, SECOND> biFunction;

    GuavaCacheBasedToIntBiFunctionMemoizer(
            final Cache<KEY, Integer> cache,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ToIntBiFunction<FIRST, SECOND> biFunction) {
        super(cache);
        this.keyFunction = keyFunction;
        this.biFunction = biFunction;
    }

    @Override
    public int applyAsInt(final FIRST first, final SECOND second) {
        final KEY key = keyFunction.apply(first, second);
        return get(key, givenKey -> Integer.valueOf(biFunction.applyAsInt(first, second))).intValue();
    }

}
