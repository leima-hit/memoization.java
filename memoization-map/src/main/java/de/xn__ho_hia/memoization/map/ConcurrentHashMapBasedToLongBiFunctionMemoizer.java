package de.xn__ho_hia.memoization.map;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.ToLongBiFunction;

import de.xn__ho_hia.quality.suppression.CompilerWarnings;

final class ConcurrentHashMapBasedToLongBiFunctionMemoizer<FIRST, SECOND, KEY>
        extends ConcurrentHashMapBasedMemoizer<KEY, Long>
        implements ToLongBiFunction<FIRST, SECOND> {

    private final BiFunction<FIRST, SECOND, KEY>  keyFunction;
    private final ToLongBiFunction<FIRST, SECOND> function;

    @SuppressWarnings(CompilerWarnings.NLS)
    ConcurrentHashMapBasedToLongBiFunctionMemoizer(
            final Map<KEY, Long> preComputedValues,
            final BiFunction<FIRST, SECOND, KEY> keyFunction,
            final ToLongBiFunction<FIRST, SECOND> function) {
        super(preComputedValues);
        this.keyFunction = requireNonNull(keyFunction,
                "Provide a key function, might just be 'MemoizationDefaults.hashCodeKeyFunction()'.");
        this.function = requireNonNull(function,
                "Cannot memoize a NULL ToLongBiFunction - provide an actual ToLongBiFunction to fix this.");
    }

    @Override
    public long applyAsLong(final FIRST t, final SECOND u) {
        final KEY key = keyFunction.apply(t, u);
        return computeIfAbsent(key, givenKey -> Long.valueOf(function.applyAsLong(t, u))).longValue();
    }

}