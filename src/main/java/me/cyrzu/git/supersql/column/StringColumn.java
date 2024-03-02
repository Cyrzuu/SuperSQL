package me.cyrzu.git.supersql.column;

import me.cyrzu.git.supersql.Types;
import org.jetbrains.annotations.NotNull;

public class StringColumn extends AbstractColumn {

    public StringColumn(@NotNull String name) {
        super(name);
    }

    @Override
    public String create(@NotNull Types types) {
        StringBuilder builder = new StringBuilder(name + " TEXT");
        if(isUnique()) {
            builder.append(" UNIQUE");
        }
        if(isNotNull()) {
            builder.append(" NOT NULL");
        }

        return builder.toString();
    }


}
