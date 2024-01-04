package me.cyrzu.git.supersql;

import org.jetbrains.annotations.NotNull;

public interface SQLObject {

    @NotNull
    UpdateBuilder updateObject(@NotNull UpdateBuilder builder);

}
