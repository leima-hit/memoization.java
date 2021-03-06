/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.guava;

import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.cache.Cache;

final class GuavaCacheBasedPredicateMemoizer<INPUT, KEY>
        extends AbstractGuavaCacheBasedMemoizer<KEY, Boolean>
        implements Predicate<INPUT> {

    private final Function<INPUT, KEY> keyFunction;
    private final Predicate<INPUT>     predicate;

    GuavaCacheBasedPredicateMemoizer(
            final Cache<KEY, Boolean> cache,
            final Function<INPUT, KEY> keyFunction,
            final Predicate<INPUT> predicate) {
        super(cache);
        this.keyFunction = keyFunction;
        this.predicate = predicate;
    }

    @Override
    public boolean test(final INPUT input) {
        final KEY key = keyFunction.apply(input);
        return get(key, givenKey -> Boolean.valueOf(predicate.test(input))).booleanValue();
    }

}
