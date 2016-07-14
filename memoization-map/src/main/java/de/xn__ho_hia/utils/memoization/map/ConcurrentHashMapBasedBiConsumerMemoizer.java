package de.xn__ho_hia.utils.memoization.map;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import de.xn__ho_hia.quality.suppression.CompilerWarnings;

final class ConcurrentHashMapBasedBiConsumerMemoizer<FIRST, SECOND, KEY>
        extends ConcurrentHashMapBasedMemoizer<KEY, KEY>
        implements BiConsumer<FIRST, SECOND> {

    private final BiFunction<FIRST, SECOND, KEY> keyFunction;
    private final BiConsumer<FIRST, SECOND>      biConsumer;

    @SuppressWarnings(CompilerWarnings.NLS)
    public ConcurrentHashMapBasedBiConsumerMemoizer(
            final Map<KEY, KEY> preComputedValues,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final BiConsumer<FIRST, SECOND> biConsumer) {
        super(preComputedValues);
        this.keyFunction = requireNonNull(keyFunction,
                "Provide a key function, might just be 'MemoizationDefaults.hashCodeKeyFunction()'.");
        this.biConsumer = requireNonNull(biConsumer,
                "Cannot memoize a NULL BiConsumer - provide an actual BiConsumer to fix this.");
    }

    @Override
    public void accept(final FIRST second, final SECOND first) {
        final KEY key = keyFunction.apply(second, first);
        computeIfAbsent(key, givenKey -> {
            biConsumer.accept(second, first);
            return givenKey;
        });
    }

}
