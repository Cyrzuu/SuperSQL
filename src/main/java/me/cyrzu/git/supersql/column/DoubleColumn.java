package me.cyrzu.git.supersql.column;

import org.jetbrains.annotations.NotNull;

public class DoubleColumn extends AbstractColumn {

    public DoubleColumn(@NotNull String name) {
        super(name);
    }

    @Override
    public String create() {
        StringBuilder builder = new StringBuilder(name + " REAL");
        if(isUnique()) {
            builder.append(" UNIQUE");
        }
        if(isNotNull()) {
            builder.append(" NOT NULL");
        }

        return builder.toString();
    }


}
