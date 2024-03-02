package me.cyrzu.git.supersql.column;

import lombok.Getter;
import me.cyrzu.git.supersql.Types;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractColumn {

    @Getter
    @NotNull
    protected final String name;

    @Getter
    protected boolean unique = false;

    @Getter
    protected boolean notNull = false;

    public AbstractColumn(@NotNull String name) {
        this.name = name;
    }

    public AbstractColumn unique() {
        this.unique = true;
        return this;
    }

    public AbstractColumn notNull() {
        this.notNull = true;
        return this;
    }

    public abstract String create(@NotNull Types types);

}
