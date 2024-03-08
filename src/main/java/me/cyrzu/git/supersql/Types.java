package me.cyrzu.git.supersql;

import me.cyrzu.git.supersql.column.AbstractColumn;
import me.cyrzu.git.supersql.column.AbstractKeyColumn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public enum Types {

    MYSQL {
        @Override
        @NotNull String insertAndUpdate(@NotNull SQLTable table, int valueAmount) {
            StringBuilder builder = new StringBuilder("INSERT INTO ").append(table.getName()).append(" (");
            StringBuilder questMark = new StringBuilder();

            Iterator<AbstractColumn> iterator = table.getColumnList().iterator();
            while (iterator.hasNext()) {
                AbstractColumn next = iterator.next();
                builder.append(next.getName());
                questMark.append("?");

                if(iterator.hasNext()) {
                    builder.append(", ");
                    questMark.append(", ");
                }
            }

            builder.append(") VALUES ");

            for (int i = 0; i < valueAmount; i++) {
                builder.append("(").append(questMark).append(")");

                if (i < valueAmount - 1) {
                    builder.append(", ");
                }
            }

            if(table.getKey() != null) {
                builder.append(" ON DUPLICATE KEY UPDATE " );

                List<AbstractColumn> values = new ArrayList<>(table.getColumns().values());
                values.sort(Comparator.comparing(colum -> !(colum instanceof AbstractKeyColumn key) || !key.isPrimaryKey()));

                Iterator<AbstractColumn> iterator1 = values.iterator();


                while (iterator1.hasNext()) {
                    AbstractColumn next = iterator1.next();
                    if(next instanceof AbstractKeyColumn nextKey && nextKey.isPrimaryKey()) {
                        iterator1.remove();
                        continue;
                    }

                    builder.append(next.getName()).append(" = VALUES(").append(next.getName()).append(")");
                    if(iterator1.hasNext()) {
                        builder.append(", ");
                    }
                }
            }

            return builder.append(";").toString();
        }

        @Override
        @NotNull String createTable(@NotNull SQLTable table) {
            StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.getName() + " (");
            Iterator<AbstractColumn> iterator = table.getColumnList().iterator();

            while (iterator.hasNext()) {
                AbstractColumn column = iterator.next();
                builder.append(column.create(this));

                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }

            builder.append(");");
            return builder.toString();
        }
    },
    SQLITE {
        @Override
        @NotNull String insertAndUpdate(@NotNull SQLTable table, int valueAmount) {
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

            if(valueAmount <= 0) {
                return builder.append(");").toString();
            }

            // Replicating the values and placeholders based on valueAmount
            StringBuilder values = new StringBuilder();
            for (int i = 0; i < valueAmount; i++) {
                values.append("(").append(questMark).append(")");
                if (i < valueAmount - 1) {
                    values.append(", ");
                }
            }

            builder.append(") VALUES ").append(values).append(";");
            return builder.toString();
        }

        @Override
        @NotNull String createTable(@NotNull SQLTable table) {
            StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.getName() + " (");
            final List<AbstractColumn> values = List.copyOf(table.getColumns().values());
            for (int i = 0; i < values.size(); i++) {
                final AbstractColumn column = values.get(i);
                builder.append(column.create(this));

                if(i != values.size() - 1) {
                    builder.append(", ");
                }
            }

            builder.append(");");
            return builder.toString();
        }
    };

    @NotNull
    abstract String insertAndUpdate(@NotNull SQLTable table, int valueAmount);

    @NotNull
    abstract String createTable(@NotNull SQLTable table);

}
