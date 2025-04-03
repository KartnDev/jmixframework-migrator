package io.kartondev.cache;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public final class CacheService implements Closeable {
    private static final CacheService INSTANCE = new CacheService();
    private final DB db;
    private final HTreeMap<Object, Object> cache;

    @SuppressWarnings({"unchecked"})
    private CacheService() {
        db = DBMaker
                .fileDB(new File("cache.db"))
                .make();
        cache = (HTreeMap<Object, Object>) db.hashSet("cache")
                .expireAfterCreate(60, TimeUnit.SECONDS)
                .make()
                .getMap();
    }


    public static CacheService getInstance() {
        return INSTANCE;
    }

    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Object key) {
        return (T) cache.get(key);
    }

    public Boolean exists(Object key) {
        return cache.getOrDefault(key, null) == null;
    }

    @Override
    public void close() throws IOException {
        db.close();
    }
}
