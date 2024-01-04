package me.cyrzu.git.supersql.column;

import org.jetbrains.annotations.NotNull;

public class IntegerColumn extends AbstractKeyColumn {

    public IntegerColumn(@NotNull String name) {
        super(name);
    }

    @Override
    public String create() {
        StringBuilder builder = new StringBuilder(name + " INT");
        if(isPrimaryKey()) {
            builder.append(" PRIMARY KEY");
        } else {
            if(isUnique()) {
                builder.append(" UNIQUE");
            }
            if(isNotNull()) {
                builder.append(" NOT NULL");
            }
        }

        return builder.toString();
    }


}
