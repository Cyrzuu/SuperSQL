package me.cyrzu.git.supersql;

import me.cyrzu.git.supersql.column.AbstractColumn;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public enum Types {

    MYSQL {
        @Override
        @NotNull String insertAndUpdate(@NotNull SQLTable table) {
            StringBuilder builder = new StringBuilder("INSERT INTO ").append(table.getName()).append(" (");
            StringBuilder questMark = new StringBuilder();

            Iterator<AbstractColumn> iterator = table.getColumns().values().iterator();
            while (iterator.hasNext()) {
                AbstractColumn next = iterator.next();
                builder.append(next.getName());
                questMark.append("?");

                if(iterator.hasNext()) {
                    builder.append(", ");
                    questMark.append(", ");
                }
            }

            builder.append(") VALUES (").append(questMark).append(")");

            if(table.getKey() != null) {
                builder.append(" ON DUPLICATE KEY UPDATE " );

                Iterator<AbstractColumn> iterator1 = table.getColumns().values().iterator();
                while (iterator1.hasNext()) {
                    AbstractColumn next = iterator1.next();
                    if(next.isPrimaryKey()) continue;

                    builder.append(next.getName()).append(" = VALUES(").append(next.getName()).append(")");
                    if(iterator1.hasNext()) {
                        builder.append(", ");
                    }
                }
            }

            return builder.append(";").toString();
        }
    },
    SQLITE {
        @Override
        @NotNull String insertAndUpdate(@NotNull SQLTable table) {
            StringBuilder builder = new StringBuilder("INSERT OR REPLACE INTO ").append(table.getName()).append(" (");
            StringBuilder questMark = new StringBuilder();

            Iterator<AbstractColumn> iterator = table.getColumns().values().iterator();
            while (iterator.hasNext()) {
                AbstractColumn next = iterator.next();
                builder.append(next.getName());
                questMark.append("?");

                if(iterator.hasNext()) {
                    builder.append(", ");
                    questMark.append(", ");
                }
            }

            builder.append(") VALUES (").append(questMark).append(");");
            return builder.toString();
        }
    };

    @NotNull
    abstract String insertAndUpdate(@NotNull SQLTable table);

}
