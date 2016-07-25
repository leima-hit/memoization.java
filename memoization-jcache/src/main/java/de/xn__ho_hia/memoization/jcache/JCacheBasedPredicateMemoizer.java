/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.jcache;

import java.util.function.Predicate;

import javax.cache.Cache;

final class JCacheBasedPredicateMemoizer<VALUE>
        extends AbstractJCacheBasedMemoizer<VALUE, Boolean>
        implements Predicate<VALUE> {

    private final Predicate<VALUE> predicate;

    JCacheBasedPredicateMemoizer(
            final Cache<VALUE, Boolean> cache,
            final Predicate<VALUE> predicate) {
        super(cache);
        this.predicate = predicate;
    }

    @Override
    public boolean test(final VALUE input) {
        return invoke(input, key -> Boolean.valueOf(predicate.test(key))).booleanValue();
    }

}