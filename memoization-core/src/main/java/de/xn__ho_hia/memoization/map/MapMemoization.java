/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.map;

import static de.xn__ho_hia.memoization.map.ConcurrentMaps.asConcurrentMap;
import static de.xn__ho_hia.memoization.shared.MemoizationDefaults.defaultKeySupplier;
import static de.xn__ho_hia.memoization.shared.MemoizationDefaults.hashCodeKeyFunction;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongBiFunction;
import java.util.function.ToLongFunction;

import de.xn__ho_hia.memoization.shared.MemoizationDefaults;

/**
 * <p>
 * Factory for lightweight wrappers that store the result of a potentially expensive function call. Each method of this
 * class exposes zero or more of the following features:
 * </p>
 * <strong>Default cache</strong>
 * <p>
 * The memoizer uses the default cache of this factory. Current implementation creates a new {@link ConcurrentMap} per
 * memoizer.
 * </p>
 * <strong>Default cache key</strong>
 * <p>
 * The memoizer uses the default {@link BiFunction} or {@link Supplier} to calculate the cache key for each call. Either
 * uses the natural key (e.g. the input itself) or one of the methods in {@link MemoizationDefaults}.
 * </p>
 * <strong>Custom cache</strong>
 * <p>
 * The memoizer uses a user-provided {@link ConcurrentMap} as its cache. It is possible to add values to the cache both
 * before and after the memoizer was created.
 * </p>
 * <strong>Custom cache key</strong>
 * <p>
 * The memoizer uses a user-defined {@link BiFunction} or {@link Supplier} to calculate the cache key for each call.
 * Take a look at {@link MemoizationDefaults} for a possible key functions and suppliers.
 * </p>
 * <strong>Skips previously computed values</strong>
 * <p>
 * The memoizer will skip calls for previously computed values. The provided {@link Map} will undergo a defensive copy
 * and thus cannot be used to add more values to the memoizer. However this might be useful in case you want to warm-up
 * your memoizer with some values.
 * </p>
 *
 * @see BiConsumer
 * @see BiFunction
 * @see BiPredicate
 * @see BooleanSupplier
 * @see Consumer
 * @see DoubleBinaryOperator
 * @see DoubleConsumer
 * @see DoublePredicate
 * @see DoubleSupplier
 * @see DoubleToIntFunction
 * @see DoubleToLongFunction
 * @see DoubleUnaryOperator
 * @see Function
 * @see IntBinaryOperator
 * @see IntConsumer
 * @see IntPredicate
 * @see IntSupplier
 * @see IntToDoubleFunction
 * @see IntToLongFunction
 * @see IntUnaryOperator
 * @see LongBinaryOperator
 * @see LongConsumer
 * @see LongPredicate
 * @see LongSupplier
 * @see LongToDoubleFunction
 * @see LongToIntFunction
 * @see LongUnaryOperator
 * @see ObjDoubleConsumer
 * @see ObjIntConsumer
 * @see ObjLongConsumer
 * @see Predicate
 * @see Supplier
 * @see ToDoubleBiFunction
 * @see ToIntBiFunction
 * @see ToLongBiFunction
 * @see ToDoubleFunction
 * @see ToIntFunction
 * @see ToLongFunction
 * @see <a href="https://en.wikipedia.org/wiki/Memoization">Wikipedia: Memoization</a>
 */
public final class MapMemoization {

    private MapMemoization() {
        // factory class
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @return The wrapped {@link Supplier}.
     */
    public static <VALUE> Supplier<VALUE> memoize(final Supplier<VALUE> supplier) {
        return memoize(supplier, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link Supplier}.
     */
    public static <VALUE> Supplier<VALUE> memoize(
            final Supplier<VALUE> supplier,
            final Map<String, VALUE> preComputedValues) {
        return memoize(supplier, defaultKeySupplier(), asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @return The wrapped {@link Supplier}.
     */
    public static <KEY, VALUE> Supplier<VALUE> memoize(
            final Supplier<VALUE> supplier,
            final Supplier<KEY> keySupplier) {
        return memoize(supplier, keySupplier, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link Supplier}.
     */
    public static <KEY, VALUE> Supplier<VALUE> memoize(
            final Supplier<VALUE> supplier,
            final Supplier<KEY> keySupplier,
            final Map<KEY, VALUE> preComputedValues) {
        return memoize(supplier, keySupplier, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache key</li>
     * <li>Custom cache</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link Supplier}.
     */
    public static <KEY, VALUE> Supplier<VALUE> memoize(
            final Supplier<VALUE> supplier,
            final Supplier<KEY> keySupplier,
            final ConcurrentMap<KEY, VALUE> cache) {
        return new ConcurrentMapBasedSupplierMemoizer<>(cache, keySupplier, supplier);
    }

    /**
     * <p>
     * Memoizes a {@link BooleanSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @return The wrapped {@link BooleanSupplier}.
     */
    public static BooleanSupplier memoize(final BooleanSupplier supplier) {
        return memoize(supplier, defaultKeySupplier());
    }

    /**
     * <p>
     * Memoizes a {@link BooleanSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link BooleanSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @return The wrapped {@link BooleanSupplier}.
     */
    public static <KEY> BooleanSupplier memoize(
            final BooleanSupplier supplier,
            final Supplier<KEY> keySupplier) {
        return memoize(supplier, keySupplier, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link BooleanSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param supplier
     *            The {@link BooleanSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link BooleanSupplier}.
     */
    public static <KEY> BooleanSupplier memoize(
            final BooleanSupplier supplier,
            final Supplier<KEY> keySupplier,
            final Map<KEY, Boolean> preComputedValues) {
        return memoize(supplier, keySupplier, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link BooleanSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link BooleanSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link BooleanSupplier}.
     */
    public static <KEY> BooleanSupplier memoize(
            final BooleanSupplier supplier,
            final Supplier<KEY> keySupplier,
            final ConcurrentMap<KEY, Boolean> cache) {
        return new ConcurrentMapBasedBooleanSupplierMemoizer<>(cache, keySupplier, supplier);
    }

    /**
     * <p>
     * Memoizes a {@link DoubleSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @return The wrapped {@link DoubleSupplier}.
     */
    public static DoubleSupplier memoize(final DoubleSupplier supplier) {
        return memoize(supplier, defaultKeySupplier());
    }

    /**
     * <p>
     * Memoizes a {@link DoubleSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link DoubleSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @return The wrapped {@link DoubleSupplier}.
     */
    public static <KEY> DoubleSupplier memoize(
            final DoubleSupplier supplier,
            final Supplier<KEY> keySupplier) {
        return memoize(supplier, keySupplier, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link DoubleSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param supplier
     *            The {@link DoubleSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link DoubleSupplier}.
     */
    public static <KEY> DoubleSupplier memoize(
            final DoubleSupplier supplier,
            final Supplier<KEY> keySupplier,
            final Map<KEY, Double> preComputedValues) {
        return memoize(supplier, keySupplier, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link DoubleSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link DoubleSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link DoubleSupplier}.
     */
    public static <KEY> DoubleSupplier memoize(
            final DoubleSupplier supplier,
            final Supplier<KEY> keySupplier,
            final ConcurrentMap<KEY, Double> cache) {
        return new ConcurrentMapBasedDoubleSupplierMemoizer<>(cache, keySupplier, supplier);
    }

    /**
     * <p>
     * Memoizes a {@link IntSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @return The wrapped {@link IntSupplier}.
     */
    public static IntSupplier memoize(final IntSupplier supplier) {
        return memoize(supplier, defaultKeySupplier());
    }

    /**
     * <p>
     * Memoizes a {@link IntSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link IntSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @return The wrapped {@link IntSupplier}.
     */
    public static <KEY> IntSupplier memoize(
            final IntSupplier supplier,
            final Supplier<KEY> keySupplier) {
        return memoize(supplier, keySupplier, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link IntSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param supplier
     *            The {@link IntSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link IntSupplier}.
     */
    public static <KEY> IntSupplier memoize(
            final IntSupplier supplier,
            final Supplier<KEY> keySupplier,
            final Map<KEY, Integer> preComputedValues) {
        return memoize(supplier, keySupplier, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link IntSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link IntSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link IntSupplier}.
     */
    public static <KEY> IntSupplier memoize(
            final IntSupplier supplier,
            final Supplier<KEY> keySupplier,
            final ConcurrentMap<KEY, Integer> cache) {
        return new ConcurrentMapBasedIntSupplierMemoizer<>(cache, keySupplier, supplier);
    }

    /**
     * <p>
     * Memoizes a {@link LongSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @return The wrapped {@link LongSupplier}.
     */
    public static LongSupplier memoize(final LongSupplier supplier) {
        return memoize(supplier, defaultKeySupplier());
    }

    /**
     * <p>
     * Memoizes a {@link LongSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link LongSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @return The wrapped {@link LongSupplier}.
     */
    public static <KEY> LongSupplier memoize(
            final LongSupplier supplier,
            final Supplier<KEY> keySupplier) {
        return memoize(supplier, keySupplier, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link LongSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param supplier
     *            The {@link LongSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link LongSupplier}.
     */
    public static <KEY> LongSupplier memoize(
            final LongSupplier supplier,
            final Supplier<KEY> keySupplier,
            final Map<KEY, Long> preComputedValues) {
        return memoize(supplier, keySupplier, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link LongSupplier} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link LongSupplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link LongSupplier}.
     */
    public static <KEY> LongSupplier memoize(
            final LongSupplier supplier,
            final Supplier<KEY> keySupplier,
            final ConcurrentMap<KEY, Long> cache) {
        return new ConcurrentMapBasedLongSupplierMemoizer<>(cache, keySupplier, supplier);
    }

    /**
     * <p>
     * Memoizes a {@link Function} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link Function} to memoize.
     * @return The wrapped {@link Function}.
     */
    public static <KEY, VALUE> Function<KEY, VALUE> memoize(final Function<KEY, VALUE> function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link Function} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link Function} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link Function}.
     */
    public static <KEY, VALUE> Function<KEY, VALUE> memoize(
            final Function<KEY, VALUE> function,
            final Map<KEY, VALUE> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link Function} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link Function} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link Function}.
     */
    public static <KEY, VALUE> Function<KEY, VALUE> memoize(
            final Function<KEY, VALUE> function,
            final ConcurrentMap<KEY, VALUE> cache) {
        return new ConcurrentMapBasedFunctionMemoizer<>(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link BiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param biFunction
     *            The {@link BiFunction} to memoize.
     * @return The wrapped {@link BiFunction}.
     */
    public static <FIRST, SECOND, VALUE> BiFunction<FIRST, SECOND, VALUE> memoize(
            final BiFunction<FIRST, SECOND, VALUE> biFunction) {
        return memoize(biFunction, hashCodeKeyFunction());
    }

    /**
     * <p>
     * Memoizes a {@link BiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param biFunction
     *            The {@link BiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @return The wrapped {@link BiFunction}.
     */
    public static <FIRST, SECOND, KEY, VALUE> BiFunction<FIRST, SECOND, VALUE> memoize(
            final BiFunction<FIRST, SECOND, VALUE> biFunction,
            final BiFunction<FIRST, SECOND, KEY> keyFunction) {
        return memoize(biFunction, keyFunction, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link BiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param biFunction
     *            The {@link BiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link BiFunction}.
     */
    public static <FIRST, SECOND, KEY, VALUE> BiFunction<FIRST, SECOND, VALUE> memoize(
            final BiFunction<FIRST, SECOND, VALUE> biFunction,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final Map<KEY, VALUE> preComputedValues) {
        return memoize(biFunction, keyFunction, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link BiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param biFunction
     *            The {@link BiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link BiFunction}.
     */
    public static <FIRST, SECOND, KEY, VALUE> BiFunction<FIRST, SECOND, VALUE> memoize(
            final BiFunction<FIRST, SECOND, VALUE> biFunction,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ConcurrentMap<KEY, VALUE> cache) {
        return new ConcurrentMapBasedBiFunctionMemoizer<>(cache, keyFunction, biFunction);
    }

    /**
     * <p>
     * Memoizes a {@link Consumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link Consumer} to memoize.
     * @return The wrapped {@link Consumer}.
     */
    public static <VALUE> Consumer<VALUE> memoize(final Consumer<VALUE> consumer) {
        return memoize(consumer, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link Consumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param consumer
     *            The {@link Consumer} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link Consumer}.
     */
    public static <VALUE> Consumer<VALUE> memoize(
            final Consumer<VALUE> consumer,
            final Map<VALUE, VALUE> preComputedValues) {
        return memoize(consumer, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link Consumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link Consumer} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link Consumer}.
     */
    public static <VALUE> Consumer<VALUE> memoize(
            final Consumer<VALUE> consumer,
            final ConcurrentMap<VALUE, VALUE> cache) {
        return new ConcurrentMapBasedConsumerMemoizer<>(cache, identity(), consumer);
    }

    /**
     * <p>
     * Memoizes a {@link DoubleConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link DoubleConsumer} to memoize.
     * @return The wrapped {@link DoubleConsumer}.
     */
    public static DoubleConsumer memoize(final DoubleConsumer consumer) {
        return memoize(consumer, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link DoubleConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param consumer
     *            The {@link DoubleConsumer} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link DoubleConsumer}.
     */
    public static DoubleConsumer memoize(
            final DoubleConsumer consumer,
            final Map<Double, Double> preComputedValues) {
        return memoize(consumer, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link DoubleConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link DoubleConsumer} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link DoubleConsumer}.
     */
    public static DoubleConsumer memoize(
            final DoubleConsumer consumer,
            final ConcurrentMap<Double, Double> cache) {
        return new ConcurrentMapBasedDoubleConsumerMemoizer(cache, consumer);
    }

    /**
     * <p>
     * Memoizes a {@link IntConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link IntConsumer} to memoize.
     * @return The wrapped {@link IntConsumer}.
     */
    public static IntConsumer memoize(final IntConsumer consumer) {
        return memoize(consumer, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link IntConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param consumer
     *            The {@link IntConsumer} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link IntConsumer}.
     */
    public static IntConsumer memoize(
            final IntConsumer consumer,
            final Map<Integer, Integer> preComputedValues) {
        return memoize(consumer, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link IntConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link IntConsumer} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link IntConsumer}.
     */
    public static IntConsumer memoize(
            final IntConsumer consumer,
            final ConcurrentMap<Integer, Integer> cache) {
        return new ConcurrentMapBasedIntConsumerMemoizer(cache, consumer);
    }

    /**
     * <p>
     * Memoizes a {@link LongConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link LongConsumer} to memoize.
     * @return The wrapped {@link LongConsumer}.
     */
    public static LongConsumer memoize(final LongConsumer consumer) {
        return memoize(consumer, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link LongConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param consumer
     *            The {@link LongConsumer} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link LongConsumer}.
     */
    public static LongConsumer memoize(
            final LongConsumer consumer,
            final Map<Long, Long> preComputedValues) {
        return memoize(consumer, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link LongConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link LongConsumer} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link LongConsumer}.
     */
    public static LongConsumer memoize(
            final LongConsumer consumer,
            final ConcurrentMap<Long, Long> cache) {
        return new ConcurrentMapBasedLongConsumerMemoizer(cache, consumer);
    }

    /**
     * <p>
     * Memoizes a {@link ObjDoubleConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjDoubleConsumer} to memoize.
     * @return The wrapped {@link ObjDoubleConsumer}.
     */
    public static <VALUE> ObjDoubleConsumer<VALUE> memoize(final ObjDoubleConsumer<VALUE> consumer) {
        return memoize(consumer, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ObjDoubleConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjDoubleConsumer} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ObjDoubleConsumer}.
     */
    public static <VALUE> ObjDoubleConsumer<VALUE> memoize(
            final ObjDoubleConsumer<VALUE> consumer,
            final Map<String, String> preComputedValues) {
        return memoize(consumer, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ObjDoubleConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjDoubleConsumer} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ObjDoubleConsumer}.
     */
    public static <VALUE> ObjDoubleConsumer<VALUE> memoize(
            final ObjDoubleConsumer<VALUE> consumer,
            final ConcurrentMap<String, String> cache) {
        return new ConcurrentMapBasedObjDoubleConsumerMemoizer<>(cache,
                MemoizationDefaults.objDoubleConsumerHashCodeKeyFunction(), consumer);
    }

    /**
     * <p>
     * Memoizes a {@link ObjIntConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjIntConsumer} to memoize.
     * @return The wrapped {@link ObjIntConsumer}.
     */
    public static <VALUE> ObjIntConsumer<VALUE> memoize(final ObjIntConsumer<VALUE> consumer) {
        return memoize(consumer, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ObjIntConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjIntConsumer} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ObjIntConsumer}.
     */
    public static <VALUE> ObjIntConsumer<VALUE> memoize(
            final ObjIntConsumer<VALUE> consumer,
            final Map<String, String> preComputedValues) {
        return memoize(consumer, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ObjIntConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjIntConsumer} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ObjIntConsumer}.
     */
    public static <VALUE> ObjIntConsumer<VALUE> memoize(
            final ObjIntConsumer<VALUE> consumer,
            final ConcurrentMap<String, String> cache) {
        return new ConcurrentMapBasedObjIntConsumerMemoizer<>(cache,
                MemoizationDefaults.objIntConsumerHashCodeKeyFunction(), consumer);
    }

    /**
     * <p>
     * Memoizes a {@link ObjLongConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjLongConsumer} to memoize.
     * @return The wrapped {@link ObjLongConsumer}.
     */
    public static <VALUE> ObjLongConsumer<VALUE> memoize(final ObjLongConsumer<VALUE> consumer) {
        return memoize(consumer, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ObjLongConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjLongConsumer} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ObjLongConsumer}.
     */
    public static <VALUE> ObjLongConsumer<VALUE> memoize(
            final ObjLongConsumer<VALUE> consumer,
            final Map<String, String> preComputedValues) {
        return memoize(consumer, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ObjLongConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param consumer
     *            The {@link ObjLongConsumer} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ObjLongConsumer}.
     */
    public static <VALUE> ObjLongConsumer<VALUE> memoize(
            final ObjLongConsumer<VALUE> consumer,
            final ConcurrentMap<String, String> cache) {
        return new ConcurrentMapBasedObjLongConsumerMemoizer<>(cache,
                MemoizationDefaults.objLongConsumerHashCodeKeyFunction(), consumer);
    }

    /**
     * <p>
     * Memoizes a {@link BiConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param biConsumer
     *            The {@link BiConsumer} to memoize.
     * @return The wrapped {@link BiConsumer}.
     */
    public static <FIRST, SECOND> BiConsumer<FIRST, SECOND> memoize(final BiConsumer<FIRST, SECOND> biConsumer) {
        return memoize(biConsumer, hashCodeKeyFunction(), emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link BiConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param biConsumer
     *            The {@link BiConsumer} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link BiConsumer}.
     */
    public static <FIRST, SECOND, KEY> BiConsumer<FIRST, SECOND> memoize(
            final BiConsumer<FIRST, SECOND> biConsumer,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final Map<KEY, KEY> preComputedValues) {
        return memoize(biConsumer, keyFunction, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link BiConsumer} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param biConsumer
     *            The {@link BiConsumer} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link BiConsumer}.
     */
    public static <FIRST, SECOND, KEY> BiConsumer<FIRST, SECOND> memoize(
            final BiConsumer<FIRST, SECOND> biConsumer,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ConcurrentMap<KEY, KEY> cache) {
        return new ConcurrentMapBasedBiConsumerMemoizer<>(cache, keyFunction, biConsumer);
    }

    /**
     * <p>
     * Memoizes a {@link Predicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link Predicate} to memoize.
     * @return The wrapped {@link Predicate}.
     */
    public static <VALUE> Predicate<VALUE> memoize(final Predicate<VALUE> predicate) {
        return memoize(predicate, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link Predicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param predicate
     *            The {@link Predicate} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link Predicate}.
     */
    public static <VALUE> Predicate<VALUE> memoize(
            final Predicate<VALUE> predicate,
            final Map<VALUE, Boolean> preComputedValues) {
        return memoize(predicate, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link Predicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link Predicate} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link Predicate}.
     */
    public static <VALUE> Predicate<VALUE> memoize(
            final Predicate<VALUE> predicate,
            final ConcurrentMap<VALUE, Boolean> cache) {
        return new ConcurrentMapBasedPredicateMemoizer<>(cache, predicate);
    }

    /**
     * <p>
     * Memoizes a {@link BiPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link BiPredicate} to memoize.
     * @return The wrapped {@link BiPredicate}.
     */
    public static <FIRST, SECOND> BiPredicate<FIRST, SECOND> memoize(final BiPredicate<FIRST, SECOND> predicate) {
        return memoize(predicate, hashCodeKeyFunction(), emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link BiPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param predicate
     *            The {@link BiPredicate} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link BiPredicate}.
     */
    public static <FIRST, SECOND, KEY> BiPredicate<FIRST, SECOND> memoize(
            final BiPredicate<FIRST, SECOND> predicate,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final Map<KEY, Boolean> preComputedValues) {
        return memoize(predicate, keyFunction, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link BiPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link BiPredicate} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link BiPredicate}.
     */
    public static <FIRST, SECOND, KEY> BiPredicate<FIRST, SECOND> memoize(
            final BiPredicate<FIRST, SECOND> predicate,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ConcurrentMap<KEY, Boolean> cache) {
        return new ConcurrentMapBasedBiPredicateMemoizer<>(cache, keyFunction, predicate);
    }

    /**
     * <p>
     * Memoizes a {@link IntPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link IntPredicate} to memoize.
     * @return The wrapped {@link IntPredicate}.
     */
    public static IntPredicate memoize(final IntPredicate predicate) {
        return memoize(predicate, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link IntPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param predicate
     *            The {@link IntPredicate} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link IntPredicate}.
     */
    public static IntPredicate memoize(
            final IntPredicate predicate,
            final Map<Integer, Boolean> preComputedValues) {
        return memoize(predicate, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link IntPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link IntPredicate} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link IntPredicate}.
     */
    public static IntPredicate memoize(
            final IntPredicate predicate,
            final ConcurrentMap<Integer, Boolean> cache) {
        return new ConcurrentMapBasedIntPredicateMemoizer(cache, predicate);
    }

    /**
     * <p>
     * Memoizes a {@link LongPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link LongPredicate} to memoize.
     * @return The wrapped {@link DoublePredicate}.
     */
    public static LongPredicate memoize(final LongPredicate predicate) {
        return memoize(predicate, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link LongPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param predicate
     *            The {@link LongPredicate} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link LongPredicate}.
     */
    public static LongPredicate memoize(
            final LongPredicate predicate,
            final Map<Long, Boolean> preComputedValues) {
        return memoize(predicate, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link LongPredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link LongPredicate} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link LongPredicate}.
     */
    public static LongPredicate memoize(
            final LongPredicate predicate,
            final ConcurrentMap<Long, Boolean> cache) {
        return new ConcurrentMapBasedLongPredicateMemoizer(cache, predicate);
    }

    /**
     * <p>
     * Memoizes a {@link DoublePredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link DoublePredicate} to memoize.
     * @return The wrapped {@link DoublePredicate}.
     */
    public static DoublePredicate memoize(final DoublePredicate predicate) {
        return memoize(predicate, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link DoublePredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param predicate
     *            The {@link DoublePredicate} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link DoublePredicate}.
     */
    public static DoublePredicate memoize(
            final DoublePredicate predicate,
            final Map<Double, Boolean> preComputedValues) {
        return memoize(predicate, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link DoublePredicate} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param predicate
     *            The {@link DoublePredicate} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link DoublePredicate}.
     */
    public static DoublePredicate memoize(
            final DoublePredicate predicate,
            final ConcurrentMap<Double, Boolean> cache) {
        return new ConcurrentMapBasedDoublePredicateMemoizer(cache, predicate);
    }

    /**
     * <p>
     * Memoizes a {@link DoubleBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link DoubleBinaryOperator} to memoize.
     * @return The wrapped {@link DoubleBinaryOperator}.
     */
    public static DoubleBinaryOperator memoize(final DoubleBinaryOperator operator) {
        return memoize(operator, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link DoubleBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param operator
     *            The {@link DoubleBinaryOperator} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link DoubleBinaryOperator}.
     */
    public static DoubleBinaryOperator memoize(
            final DoubleBinaryOperator operator,
            final Map<String, Double> preComputedValues) {
        return memoize(operator, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link DoubleBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link DoubleBinaryOperator} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link DoubleBinaryOperator}.
     */
    public static DoubleBinaryOperator memoize(
            final DoubleBinaryOperator operator,
            final ConcurrentMap<String, Double> cache) {
        return new ConcurrentMapBasedDoubleBinaryOperatorMemoizer<>(cache,
                MemoizationDefaults.doubleBinaryOperatorHashCodeKeyFunction(), operator);
    }

    /**
     * <p>
     * Memoizes a {@link IntBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link IntBinaryOperator} to memoize.
     * @return The wrapped {@link IntBinaryOperator}.
     */
    public static IntBinaryOperator memoize(final IntBinaryOperator operator) {
        return memoize(operator, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link IntBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param operator
     *            The {@link IntBinaryOperator} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link IntBinaryOperator}.
     */
    public static IntBinaryOperator memoize(
            final IntBinaryOperator operator,
            final Map<String, Integer> preComputedValues) {
        return memoize(operator, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link IntBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link IntBinaryOperator} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link IntBinaryOperator}.
     */
    public static IntBinaryOperator memoize(
            final IntBinaryOperator operator,
            final ConcurrentMap<String, Integer> cache) {
        return new ConcurrentMapBasedIntBinaryOperatorMemoizer<>(cache,
                MemoizationDefaults.intBinaryOperatorHashCodeKeyFunction(), operator);
    }

    /**
     * <p>
     * Memoizes a {@link LongBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link LongBinaryOperator} to memoize.
     * @return The wrapped {@link LongBinaryOperator}.
     */
    public static LongBinaryOperator memoize(final LongBinaryOperator operator) {
        return memoize(operator, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link LongBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param operator
     *            The {@link LongBinaryOperator} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link LongBinaryOperator}.
     */
    public static LongBinaryOperator memoize(
            final LongBinaryOperator operator,
            final Map<String, Long> preComputedValues) {
        return memoize(operator, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link LongBinaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link LongBinaryOperator} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link LongBinaryOperator}.
     */
    public static LongBinaryOperator memoize(
            final LongBinaryOperator operator,
            final ConcurrentMap<String, Long> cache) {
        return new ConcurrentMapBasedLongBinaryOperatorMemoizer<>(cache,
                MemoizationDefaults.longBinaryOperatorHashCodeKeyFunction(), operator);
    }

    /**
     * <p>
     * Memoizes a {@link DoubleUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link DoubleUnaryOperator} to memoize.
     * @return The wrapped {@link DoubleUnaryOperator}.
     */
    public static DoubleUnaryOperator memoize(final DoubleUnaryOperator operator) {
        return memoize(operator, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link DoubleUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param operator
     *            The {@link DoubleUnaryOperator} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link DoubleUnaryOperator}.
     */
    public static DoubleUnaryOperator memoize(
            final DoubleUnaryOperator operator,
            final Map<Double, Double> preComputedValues) {
        return memoize(operator, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link DoubleUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link DoubleUnaryOperator} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link DoubleUnaryOperator}.
     */
    public static DoubleUnaryOperator memoize(
            final DoubleUnaryOperator operator,
            final ConcurrentMap<Double, Double> cache) {
        return new ConcurrentMapBasedDoubleUnaryOperatorMemoizer(cache, operator);
    }

    /**
     * <p>
     * Memoizes a {@link IntUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link IntUnaryOperator} to memoize.
     * @return The wrapped {@link IntUnaryOperator}.
     */
    public static IntUnaryOperator memoize(final IntUnaryOperator operator) {
        return memoize(operator, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link IntUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param operator
     *            The {@link IntUnaryOperator} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link IntUnaryOperator}.
     */
    public static IntUnaryOperator memoize(
            final IntUnaryOperator operator,
            final Map<Integer, Integer> preComputedValues) {
        return memoize(operator, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link IntUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link IntUnaryOperator} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link IntUnaryOperator}.
     */
    public static IntUnaryOperator memoize(
            final IntUnaryOperator operator,
            final ConcurrentMap<Integer, Integer> cache) {
        return new ConcurrentMapBasedIntUnaryOperatorMemoizer(cache, operator);
    }

    /**
     * <p>
     * Memoizes a {@link LongUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link LongUnaryOperator} to memoize.
     * @return The wrapped {@link LongUnaryOperator}.
     */
    public static LongUnaryOperator memoize(final LongUnaryOperator operator) {
        return memoize(operator, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link LongUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param operator
     *            The {@link LongUnaryOperator} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link LongUnaryOperator}.
     */
    public static LongUnaryOperator memoize(
            final LongUnaryOperator operator,
            final Map<Long, Long> preComputedValues) {
        return memoize(operator, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link LongUnaryOperator} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param operator
     *            The {@link LongUnaryOperator} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link LongUnaryOperator}.
     */
    public static LongUnaryOperator memoize(
            final LongUnaryOperator operator,
            final ConcurrentMap<Long, Long> cache) {
        return new ConcurrentMapBasedLongUnaryOperatorMemoizer(cache, operator);
    }

    /**
     * <p>
     * Memoizes a {@link DoubleToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link DoubleToIntFunction} to memoize.
     * @return The wrapped {@link DoubleToIntFunction}.
     */
    public static DoubleToIntFunction memoize(final DoubleToIntFunction function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link DoubleToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link DoubleToIntFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link DoubleToIntFunction}.
     */
    public static DoubleToIntFunction memoize(
            final DoubleToIntFunction function,
            final Map<Double, Integer> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link DoubleToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link DoubleToIntFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link DoubleToIntFunction}.
     */
    public static DoubleToIntFunction memoize(
            final DoubleToIntFunction function,
            final ConcurrentMap<Double, Integer> cache) {
        return new ConcurrentMapBasedDoubleToIntFunctionMemoizer(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link DoubleToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link DoubleToLongFunction} to memoize.
     * @return The wrapped {@link DoubleToLongFunction}.
     */
    public static DoubleToLongFunction memoize(final DoubleToLongFunction function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link DoubleToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link DoubleToLongFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link DoubleToLongFunction}.
     */
    public static DoubleToLongFunction memoize(
            final DoubleToLongFunction function,
            final Map<Double, Long> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link DoubleToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link DoubleToLongFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link DoubleToLongFunction}.
     */
    public static DoubleToLongFunction memoize(
            final DoubleToLongFunction function,
            final ConcurrentMap<Double, Long> cache) {
        return new ConcurrentMapBasedDoubleToLongFunctionMemoizer(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link IntToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link IntToDoubleFunction} to memoize.
     * @return The wrapped {@link IntToDoubleFunction}.
     */
    public static IntToDoubleFunction memoize(final IntToDoubleFunction function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link IntToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link IntToDoubleFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link IntToDoubleFunction}.
     */
    public static IntToDoubleFunction memoize(
            final IntToDoubleFunction function,
            final Map<Integer, Double> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link IntToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link IntToDoubleFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link IntToDoubleFunction}.
     */
    public static IntToDoubleFunction memoize(
            final IntToDoubleFunction function,
            final ConcurrentMap<Integer, Double> cache) {
        return new ConcurrentMapBasedIntToDoubleFunctionMemoizer(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link IntToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link IntToLongFunction} to memoize.
     * @return The wrapped {@link IntToLongFunction}.
     */
    public static IntToLongFunction memoize(final IntToLongFunction function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link IntToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link IntToLongFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link IntToLongFunction}.
     */
    public static IntToLongFunction memoize(
            final IntToLongFunction function,
            final Map<Integer, Long> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link IntToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link IntToLongFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link IntToLongFunction}.
     */
    public static IntToLongFunction memoize(
            final IntToLongFunction function,
            final ConcurrentMap<Integer, Long> cache) {
        return new ConcurrentMapBasedIntToLongFunctionMemoizer(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link LongToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link LongToDoubleFunction} to memoize.
     * @return The wrapped {@link LongToDoubleFunction}.
     */
    public static LongToDoubleFunction memoize(final LongToDoubleFunction function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link LongToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link LongToDoubleFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link LongToDoubleFunction}.
     */
    public static LongToDoubleFunction memoize(
            final LongToDoubleFunction function,
            final Map<Long, Double> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link LongToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link LongToDoubleFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link LongToDoubleFunction}.
     */
    public static LongToDoubleFunction memoize(
            final LongToDoubleFunction function,
            final ConcurrentMap<Long, Double> cache) {
        return new ConcurrentMapBasedLongToDoubleFunctionMemoizer(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link LongToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link LongToIntFunction} to memoize.
     * @return The wrapped {@link LongToIntFunction}.
     */
    public static LongToIntFunction memoize(final LongToIntFunction function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link LongToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link LongToIntFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link LongToIntFunction}.
     */
    public static LongToIntFunction memoize(
            final LongToIntFunction function,
            final Map<Long, Integer> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link LongToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link LongToIntFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link LongToIntFunction}.
     */
    public static LongToIntFunction memoize(
            final LongToIntFunction function,
            final ConcurrentMap<Long, Integer> cache) {
        return new ConcurrentMapBasedLongToIntFunctionMemoizer(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link ToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToDoubleFunction} to memoize.
     * @return The wrapped {@link ToDoubleFunction}.
     */
    public static <VALUE> ToDoubleFunction<VALUE> memoize(final ToDoubleFunction<VALUE> function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link ToDoubleFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ToDoubleFunction}.
     */
    public static <VALUE> ToDoubleFunction<VALUE> memoize(
            final ToDoubleFunction<VALUE> function,
            final Map<VALUE, Double> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ToDoubleFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToDoubleFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ToDoubleFunction}.
     */
    public static <VALUE> ToDoubleFunction<VALUE> memoize(
            final ToDoubleFunction<VALUE> function,
            final ConcurrentMap<VALUE, Double> cache) {
        return new ConcurrentMapBasedToDoubleFunctionMemoizer<>(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link ToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToIntFunction} to memoize.
     * @return The wrapped {@link ToIntFunction}.
     */
    public static <VALUE> ToIntFunction<VALUE> memoize(final ToIntFunction<VALUE> function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link ToIntFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ToIntFunction}.
     */
    public static <VALUE> ToIntFunction<VALUE> memoize(
            final ToIntFunction<VALUE> function,
            final Map<VALUE, Integer> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ToIntFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToIntFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ToIntFunction}.
     */
    public static <VALUE> ToIntFunction<VALUE> memoize(
            final ToIntFunction<VALUE> function,
            final ConcurrentMap<VALUE, Integer> cache) {
        return new ConcurrentMapBasedToIntFunctionMemoizer<>(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link ToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToLongFunction} to memoize.
     * @return The wrapped {@link ToLongFunction}.
     */
    public static <VALUE> ToLongFunction<VALUE> memoize(final ToLongFunction<VALUE> function) {
        return memoize(function, emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link ToLongFunction} to memoize.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ToLongFunction}.
     */
    public static <VALUE> ToLongFunction<VALUE> memoize(
            final ToLongFunction<VALUE> function,
            final Map<VALUE, Long> preComputedValues) {
        return memoize(function, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ToLongFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToLongFunction} to memoize.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ToLongFunction}.
     */
    public static <VALUE> ToLongFunction<VALUE> memoize(
            final ToLongFunction<VALUE> function,
            final ConcurrentMap<VALUE, Long> cache) {
        return new ConcurrentMapBasedToLongFunctionMemoizer<>(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link ToDoubleBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToDoubleBiFunction} to memoize.
     * @return The wrapped {@link ToDoubleBiFunction}.
     */
    public static <FIRST, SECOND> ToDoubleBiFunction<FIRST, SECOND> memoize(
            final ToDoubleBiFunction<FIRST, SECOND> function) {
        return memoize(function, hashCodeKeyFunction(), emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ToDoubleBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link ToDoubleBiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ToDoubleBiFunction}.
     */
    public static <FIRST, SECOND, KEY> ToDoubleBiFunction<FIRST, SECOND> memoize(
            final ToDoubleBiFunction<FIRST, SECOND> function,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final Map<KEY, Double> preComputedValues) {
        return memoize(function, keyFunction, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ToDoubleBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToDoubleBiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ToDoubleBiFunction}.
     */
    public static <FIRST, SECOND, KEY> ToDoubleBiFunction<FIRST, SECOND> memoize(
            final ToDoubleBiFunction<FIRST, SECOND> function,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ConcurrentMap<KEY, Double> cache) {
        return new ConcurrentMapBasedToDoubleBiFunctionMemoizer<>(cache, keyFunction, function);
    }

    /**
     * <p>
     * Memoizes a {@link ToIntBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToIntBiFunction} to memoize.
     * @return The wrapped {@link ToIntBiFunction}.
     */
    public static <FIRST, SECOND> ToIntBiFunction<FIRST, SECOND> memoize(
            final ToIntBiFunction<FIRST, SECOND> function) {
        return memoize(function, hashCodeKeyFunction(), emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ToIntBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link ToIntBiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ToIntBiFunction}.
     */
    public static <FIRST, SECOND, KEY> ToIntBiFunction<FIRST, SECOND> memoize(
            final ToIntBiFunction<FIRST, SECOND> function,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final Map<KEY, Integer> preComputedValues) {
        return memoize(function, keyFunction, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ToIntBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToIntBiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param cache
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ToIntBiFunction}.
     */
    public static <FIRST, SECOND, KEY> ToIntBiFunction<FIRST, SECOND> memoize(
            final ToIntBiFunction<FIRST, SECOND> function,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ConcurrentMap<KEY, Integer> cache) {
        return new ConcurrentMapBasedToIntBiFunctionMemoizer<>(cache, keyFunction, function);
    }

    /**
     * <p>
     * Memoizes a {@link ToLongBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Default cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToLongBiFunction} to memoize.
     * @return The wrapped {@link ToLongBiFunction}.
     */
    public static <FIRST, SECOND> ToLongBiFunction<FIRST, SECOND> memoize(
            final ToLongBiFunction<FIRST, SECOND> function) {
        return memoize(function, hashCodeKeyFunction(), emptyMap());
    }

    /**
     * <p>
     * Memoizes a {@link ToLongBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Default cache</li>
     * <li>Custom cache key</li>
     * <li>Skips previously computed values</li>
     * </ul>
     *
     * @param function
     *            The {@link ToLongBiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param preComputedValues
     *            {@link Map} of already computed values.
     * @return The wrapped {@link ToLongBiFunction}.
     */
    public static <FIRST, SECOND, KEY> ToLongBiFunction<FIRST, SECOND> memoize(
            final ToLongBiFunction<FIRST, SECOND> function,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final Map<KEY, Long> preComputedValues) {
        return memoize(function, keyFunction, asConcurrentMap(preComputedValues));
    }

    /**
     * <p>
     * Memoizes a {@link ToLongBiFunction} in a {@link ConcurrentMap}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param function
     *            The {@link ToLongBiFunction} to memoize.
     * @param keyFunction
     *            The {@link BiFunction} to compute the cache key.
     * @param cache
     *            The {@link ConcurrentMap} based cache to use.
     * @return The wrapped {@link ToLongBiFunction}.
     */
    public static <FIRST, SECOND, KEY> ToLongBiFunction<FIRST, SECOND> memoize(
            final ToLongBiFunction<FIRST, SECOND> function,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ConcurrentMap<KEY, Long> cache) {
        return new ConcurrentMapBasedToLongBiFunctionMemoizer<>(cache, keyFunction, function);
    }

}
