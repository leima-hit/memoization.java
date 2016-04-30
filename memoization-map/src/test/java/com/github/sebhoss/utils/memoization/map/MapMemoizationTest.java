/*
 * Copyright © 2016 Sebastian Hoß <mail@shoss.de>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.github.sebhoss.utils.memoization.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 */
public class MapMemoizationTest {

    /**
    *
    */
    @SuppressWarnings("static-method")
    @Test
    public void shouldMemoizeSupplier() {
        // given
        final Supplier<String> supplier = () -> "test"; //$NON-NLS-1$

        // when
        final Supplier<String> memoize = MapMemoization.memoize(supplier);

        // then
        Assert.assertNotNull(memoize);
    }

    /**
    *
    */
    @SuppressWarnings("static-method")
    @Test
    public void shouldMemoizeFunction() {
        // given
        final Function<String, String> function = a -> "test"; //$NON-NLS-1$

        // when
        final Function<String, String> memoize = MapMemoization.memoize(function);

        // then
        Assert.assertNotNull(memoize);
    }

    /**
    *
    */
    @SuppressWarnings("static-method")
    @Test
    public void shouldMemoizeBiFunction() {
        // given
        final BiFunction<String, String, String> function = (a, b) -> "test"; //$NON-NLS-1$

        // when
        final BiFunction<String, String, String> memoize = MapMemoization.memoize(function);

        // then
        Assert.assertNotNull(memoize);
    }

    /**
    *
    */
    @SuppressWarnings("static-method")
    @Test
    public void shouldMemoizeConsumer() {
        // given
        final Consumer<String> consumer = System.out::println;

        // when
        final Consumer<String> memoize = MapMemoization.memoize(consumer);

        // then
        Assert.assertNotNull(memoize);
    }

    /**
     * @throws NoSuchMethodException
     *             Reflection problemt
     * @throws IllegalAccessException
     *             Reflection problemt
     * @throws InvocationTargetException
     *             Reflection problemt
     * @throws InstantiationException
     *             Reflection problemt
     */
    @SuppressWarnings("static-method")
    @Test
    public void shouldDeclarePrivateConstructor()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // given
        final Constructor<MapMemoization> constructor = MapMemoization.class.getDeclaredConstructor();

        // when
        final boolean isPrivate = Modifier.isPrivate(constructor.getModifiers());

        // then
        Assert.assertTrue("Constructor is not private", isPrivate); //$NON-NLS-1$
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}
