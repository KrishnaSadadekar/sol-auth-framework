package com.nmims.lms.utilities;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntitySyncUtil {

    public static <F, B, K> void sync(
            List<F> frontendList,
            List<B> backendList,
            Function<F, K> frontendKeyFn,
            Function<B, K> backendKeyFn,
            BiConsumer<F, B> updateFn,
            Function<F, B> createFn,
            Consumer<B> deactivateFn,
            Consumer<List<B>> saveFn
    ) {
        Map<K, B> backendMap = backendList.stream()
                .collect(Collectors.toMap(backendKeyFn, b -> b));

        Set<K> frontendKeys = frontendList.stream()
                .map(frontendKeyFn)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<B> toSave = new ArrayList<>();

        for (F frontend : frontendList) {
            K key = frontendKeyFn.apply(frontend);
            B backend = key != null ? backendMap.get(key) : null;

            if (backend != null) {
                updateFn.accept(frontend, backend);
                toSave.add(backend);
            } else {
                B created = createFn.apply(frontend);
                toSave.add(created);
            }
        }

        for (B backend : backendList) {
            K key = backendKeyFn.apply(backend);
            if (!frontendKeys.contains(key)) {
                deactivateFn.accept(backend);
                toSave.add(backend);
            }
        }

        saveFn.accept(toSave);
    }
}

