package me.cyrzu.git.supersql;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Pair<K, V> {

    @Getter
    @NotNull
    private final K key;

    @Getter
    @NotNull
    private final V value;

    public Pair(@NotNull K key, @NotNull V value) {
        this.key = key;
        this.value = value;
    }

}
