package me.cyrzu.git.supersql.column;

import me.cyrzu.git.supersql.Types;
import org.jetbrains.annotations.NotNull;

public class VarcharColumn extends AbstractKeyColumn {

    private final int max;

    public VarcharColumn(@NotNull String name, int max) {
        super(name);
        this.max = Math.max(1, max);
    }

    @Override
    public String create(@NotNull Types types) {
        StringBuilder builder = new StringBuilder(name + " VARCHAR(%s)%s".formatted(max, types == Types.MYSQL ? " CHARSET utf8" : ""));
        if(!isPrimaryKey()) {
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
