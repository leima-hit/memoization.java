/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.jcache;

import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

import javax.cache.Cache;

final class JCacheBasedDoubleUnaryOperatorMemoizer<KEY>
        extends AbstractJCacheBasedMemoizer<KEY, Double>
        implements DoubleUnaryOperator {

    private final DoubleFunction<KEY> keyFunction;
    private final DoubleUnaryOperator biFunction;

    JCacheBasedDoubleUnaryOperatorMemoizer(
            final Cache<KEY, Double> cache,
            final DoubleFunction<KEY> keyFunction,
            final DoubleUnaryOperator biFunction) {
        super(cache);
        this.keyFunction = keyFunction;
        this.biFunction = biFunction;
    }

    @Override
    public double applyAsDouble(final double value) {
        final KEY key = keyFunction.apply(value);
        return invoke(key, givenKey -> Double.valueOf(biFunction.applyAsDouble(value))).doubleValue();
    }

}
