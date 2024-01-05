package me.cyrzu.git.supersql.column;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKeyColumn extends AbstractColumn {

    @Getter
    private boolean primaryKey = false;

    public AbstractKeyColumn(@NotNull String name) {
        super(name);
    }

    public final AbstractKeyColumn primaryKey() {
        this.primaryKey = true;
        this.unique = false;
        this.notNull = false;
        return this;
    }

    @Override
    public final AbstractKeyColumn unique() {
        this.unique = true;
        this.primaryKey = false;
        return this;
    }

    @Override
    public final AbstractKeyColumn notNull() {
        this.notNull = true;
        this.primaryKey = false;
        return this;
    }

    @Override
    public String toString() {
        return "AbstractKeyColumn{" +
                "primaryKey=" + primaryKey +
                ", name='" + name + '\'' +
                ", unique=" + unique +
                ", notNull=" + notNull +
                '}';
    }
}
