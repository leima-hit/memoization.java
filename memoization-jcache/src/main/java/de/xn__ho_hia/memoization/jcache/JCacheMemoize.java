/*
 * This file is part of memoization.java. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of memoization.java,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.memoization.jcache;

import static de.xn__ho_hia.memoization.shared.MemoizationDefaults.defaultKeySupplier;
import static de.xn__ho_hia.memoization.shared.MemoizationDefaults.hashCodeKeyFunction;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

import de.xn__ho_hia.memoization.shared.MemoizationDefaults;

/**
 * <p>
 * Factory for lightweight wrappers that store the result of a potentially expensive function call. Each method of this
 * class exposes zero or more of the following features:
 * </p>
 * <strong>Default cache</strong>
 * <p>
 * The memoizer uses the default cache of this factory. Current implementation creates a new {@link Cache} per memoizer.
 * </p>
 * <strong>Default cache key</strong>
 * <p>
 * The memoizer uses the default {@link BiFunction} or {@link Supplier} to calculate the cache key for each call. Either
 * uses the natural key (e.g. the input itself) or one of the methods in {@link MemoizationDefaults}.
 * </p>
 * <strong>Custom cache</strong>
 * <p>
 * The memoizer uses a user-provided {@link Cache} as its cache. It is possible to add values to the cache both before
 * and after the memoizer was created.
 * </p>
 * <strong>Custom cache key</strong>
 * <p>
 * The memoizer uses a user-defined {@link BiFunction} or {@link Supplier} to calculate the cache key for each call.
 * Take a look at {@link MemoizationDefaults} for a possible key functions and suppliers.
 * </p>
 *
 * @see BiFunction
 * @see Consumer
 * @see Function
 * @see Predicate
 * @see Supplier
 * @see <a href="https://en.wikipedia.org/wiki/Memoization">Wikipedia: Memoization</a>
 */
public final class JCacheMemoize {

    private static final AtomicLong   CACHE_COUNTER = new AtomicLong(0);
    private static final CacheManager CACHE_MANAGER = Caching.getCachingProvider().getCacheManager();

    private JCacheMemoize() {
        // factory class
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a JCache {@link Cache}.
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
    public static final <VALUE> Supplier<VALUE> supplier(final Supplier<VALUE> supplier) {
        return supplier(supplier, defaultKeySupplier());
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a JCache {@link Cache}.
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
    public static final <KEY, VALUE> Supplier<VALUE> supplier(
            final Supplier<VALUE> supplier,
            final Supplier<KEY> keySupplier) {
        return supplier(supplier, keySupplier, createCache(Supplier.class.getSimpleName()));
    }

    /**
     * <p>
     * Memoizes a {@link Supplier} in a previously constructed JCache {@link Cache}.
     * </p>
     * <h3>Features</h3>
     * <ul>
     * <li>Custom cache</li>
     * <li>Custom cache key</li>
     * </ul>
     *
     * @param supplier
     *            The {@link Supplier} to memoize.
     * @param keySupplier
     *            The {@link Supplier} for the cache key.
     * @param cache
     *            The {@link Cache} to use.
     * @return The wrapped {@link Supplier}.
     */
    public static final <KEY, VALUE> Supplier<VALUE> supplier(
            final Supplier<VALUE> supplier,
            final Supplier<KEY> keySupplier,
            final Cache<KEY, VALUE> cache) {
        return new JCacheBasedSupplierMemoizer<>(cache, keySupplier, supplier);
    }

    /**
     * <p>
     * Memoizes a {@link Function} in a JCache {@link Cache}.
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
    public static final <KEY, VALUE> Function<KEY, VALUE> function(final Function<KEY, VALUE> function) {
        return function(function, createCache(Function.class.getSimpleName()));
    }

    /**
     * <p>
     * Memoizes a {@link Function} in a previously constructed JCache {@link Cache}.
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
     *            The {@link Cache} to use.
     * @return The wrapped {@link Function}.
     */
    public static final <KEY, VALUE> Function<KEY, VALUE> function(
            final Function<KEY, VALUE> function,
            final Cache<KEY, VALUE> cache) {
        return new JCacheBasedFunctionMemoizer<>(cache, function);
    }

    /**
     * <p>
     * Memoizes a {@link Predicate} in a JCache {@link Cache}.
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
    public static final <VALUE> Predicate<VALUE> predicate(final Predicate<VALUE> predicate) {
        return predicate(predicate, createCache(Predicate.class.getSimpleName()));
    }

    /**
     * <p>
     * Memoizes a {@link Predicate} in a previously constructed JCache {@link Cache}.
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
     *            The {@link Cache} to use.
     * @return The wrapped {@link Predicate}.
     */
    public static final <VALUE> Predicate<VALUE> predicate(
            final Predicate<VALUE> predicate,
            final Cache<VALUE, Boolean> cache) {
        return new JCacheBasedPredicateMemoizer<>(cache, predicate);
    }

    /**
     * <p>
     * Memoizes a {@link Consumer} in a JCache {@link Cache}.
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
    public static final <VALUE> Consumer<VALUE> consumer(final Consumer<VALUE> consumer) {
        return consumer(consumer, createCache(Consumer.class.getSimpleName()));
    }

    /**
     * <p>
     * Memoizes a {@link Consumer} in a previously constructed JCache {@link Cache}.
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
     *            The {@link Cache} to use.
     * @return The wrapped {@link Consumer}.
     */
    public static final <VALUE> Consumer<VALUE> consumer(
            final Consumer<VALUE> consumer,
            final Cache<VALUE, VALUE> cache) {
        return new JCacheBasedConsumerMemoizer<>(cache, consumer);
    }

    /**
     * <p>
     * Memoizes a {@link BiFunction} in a JCache {@link Cache}.
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
    public static final <FIRST, SECOND, VALUE> BiFunction<FIRST, SECOND, VALUE> biFunction(
            final BiFunction<FIRST, SECOND, VALUE> biFunction) {
        return biFunction(biFunction, hashCodeKeyFunction());
    }

    /**
     * <p>
     * Memoizes a {@link BiFunction} in a JCache {@link Cache}.
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
    public static final <FIRST, SECOND, KEY, VALUE> BiFunction<FIRST, SECOND, VALUE> biFunction(
            final BiFunction<FIRST, SECOND, VALUE> biFunction,
            final BiFunction<FIRST, SECOND, KEY> keyFunction) {
        return biFunction(biFunction, keyFunction, createCache(BiFunction.class.getSimpleName()));
    }

    /**
     * <p>
     * Memoizes a {@link BiFunction} in a previously constructed JCache {@link Cache}.
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
     *            The {@link Cache} to use.
     * @return The wrapped {@link BiFunction}.
     */
    public static final <FIRST, SECOND, KEY, VALUE> BiFunction<FIRST, SECOND, VALUE> biFunction(
            final BiFunction<FIRST, SECOND, VALUE> biFunction,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final Cache<KEY, VALUE> cache) {
        return new JCacheBasedBiFunctionMemoizer<>(cache, keyFunction, biFunction);
    }

    static <KEY, VALUE> Cache<KEY, VALUE> createCache(final String typeName) {
        final MutableConfiguration<KEY, VALUE> configuration = new MutableConfiguration<>();
        return CACHE_MANAGER.createCache(generateCacheName(typeName), configuration);
    }

    static String generateCacheName(final String type) {
        return JCacheMemoize.class.getSimpleName() + type + CACHE_COUNTER.getAndIncrement();
    }

}