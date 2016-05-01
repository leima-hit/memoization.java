package com.github.sebhoss.utils.memoization.map;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.sebhoss.utils.memoization.shared.MemoizingConsumer;

final class ConcurrentHashMapBasedConsumerMemoizer<KEY, VALUE>
        extends ConcurrentHashMapBasedMemoizer<KEY, VALUE>
        implements MemoizingConsumer<KEY, VALUE> {

    private final Function<VALUE, KEY> keyFunction;
    private final Consumer<VALUE>      consumer;

    @SuppressWarnings("nls")
    ConcurrentHashMapBasedConsumerMemoizer(
            final Map<KEY, VALUE> preComputedValues,
            final Function<VALUE, KEY> keyFunction,
            final Consumer<VALUE> consumer) {
        super(requireNonNull(preComputedValues,
                "Provide an empty map instead of NULL in case you don't have any precomputed values."));
        this.keyFunction = requireNonNull(keyFunction, "Provide a key function, might just be 'Function.identity()'.");
        this.consumer = requireNonNull(consumer,
                "Cannot memoize a NULL consumer - provide an actual consumer to fix this.");
    }

    @Override
    public Function<VALUE, KEY> getKeyFunction() {
        return keyFunction;
    }

    @Override
    public Consumer<VALUE> getConsumer() {
        return consumer;
    }

    @Override
    public BiFunction<KEY, Function<KEY, VALUE>, VALUE> getMemoizingFunction() {
        return this::computeIfAbsent;
    }

}
