package me.cyrzu.git.supersql.column;

import org.jetbrains.annotations.NotNull;

public class BytesColumn extends AbstractColumn {

    public BytesColumn(@NotNull String name) {
        super(name);
    }

    @Override
    public String create() {
        StringBuilder builder = new StringBuilder(name + " BLOB");
        if(isUnique()) {
            builder.append(" UNIQUE");
        }
        if(isNotNull()) {
            builder.append(" NOT NULL");
        }

        return builder.toString();
    }


}
