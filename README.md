Wow 🤣🤣

[![](https://jitpack.io/v/Cyrzuu/SuperSQL.svg)](https://jitpack.io/#Cyrzuu/SuperSQL)

**Maven:**
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.Cyrzuu</groupId>
    <artifactId>SuperSQL</artifactId>
    <version>1.1.5</version>
</dependency>
```

**Gradle:**
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
        implementation 'com.github.Cyrzuu:SuperSQL:1.1.5'
}
```

**Example**
```java
SuperSQL superSQL = new SuperMySQL(plugin, DATABASE_HOST, DATABASE_PORT, DATABASE_BASE, DATABASE_USERNAME, DATABASE_PASSWORD);

SQLTable table = SQLTable.builder(superSQL, "table_name")
        .add(new VarcharColumn("uuid", 36).primaryKey())
        .add(new StringColumn("username").unique().notNull())
        .add(new IntegerColumn("kills").notNull())
        .add(new BytesColumn("bytes").notNull())
        .build();

superSQL.createTable(table, Throwable::printStackTrace);

PlayerObject playerObject = new PlayerObject();
table.createUpdate(playerObject);

SQLResult result = table.selectBuilder().where("uuid", UUID.randomUUID().toString()).execute();
while (result.next()) {
    getLogger().info(result.getString("username"));
}

table.deleteBuilder().where("username", "Notch").execute();
```

```java
public class PlayerObject implements SQLObject {

    private final UUID uuid = UUID.randomUUID();

    private final String name = UUID.randomUUID().toString().substring(0, 8);

    private final int kills = new Random().nextInt(25);

    private final byte[] bytes = new byte[0];

    @Override
    public @NotNull UpdateBuilder updateObject(@NotNull UpdateBuilder updateBuilder) {
        return updateBuilder
                .put(uuid.toString())
                .put(name)
                .put(kills)
                .put(bytes);
    }

}
```
