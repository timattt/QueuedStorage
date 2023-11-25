package org.timattt.storage.structure;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class SerializationBasedKeyValueStorage<K extends Comparable<K>, V> implements KeyValueStorage<K, V> {

    private final File rootFolder;
    private final Set<Iterator<K>> givenIterators;
    private boolean closed;

    public SerializationBasedKeyValueStorage(String rootPath) {
        this.rootFolder = new File(rootPath);
        this.closed = false;
        this.givenIterators = new HashSet<Iterator<K>>();

        boolean exists = this.rootFolder.exists();

        if (exists) {
            try {
                readAll();
            } catch (Exception e) {
                throw new MalformedDataException();
            }
        } else {
            this.rootFolder.mkdir();
        }
    }

    public synchronized Map<K, V> readAll() {
        Map<K, V> result = new TreeMap<K, V>();

        for (File file : Objects.requireNonNull(rootFolder.listFiles())) {
            iterateHashFolder(file, (ObjectRepresentation item) -> {
                result.put(item.key, item.value);
            });
        }

        return result;
    }

    private void iterateHashFolder(File hashFolder, Consumer<ObjectRepresentation> consumer) {
        for (File file : Objects.requireNonNull(hashFolder.listFiles())) {
            String name = file.getName();
            if (name.contains("key")) {
                String keyFileName = file.getName();;
                String valueFileName = keyFileName.replace("key", "value");

                File keyFile = new File(hashFolder, keyFileName);
                File valueFile = new File(hashFolder, valueFileName);

                K newKey = (K) Utils.deserializeObject(keyFile);
                V newValue = (V) Utils.deserializeObject(valueFile);

                consumer.accept(new ObjectRepresentation(keyFile, valueFile, newKey, newValue));
            }
        }
    }

    private File getHashFolder(K key) {
        final int hash = Math.abs(key.hashCode());
        return new File(this.rootFolder, Integer.toString(hash));
    }

    @Override
    public synchronized V read(K key) {
        assertOpen();
        final File hashFolder = getHashFolder(key);

        if (!hashFolder.exists()) {
            return null;
        }

        AtomicReference<V> result = new AtomicReference<V>();

        iterateHashFolder(hashFolder, (ObjectRepresentation item) -> {
            if (item.key.equals(key)) {
                result.set(item.value);
            }
        });

        return result.get();
    }

    @Override
    public synchronized boolean exists(K key) {
        return read(key) != null;
    }

    private void assertNoIteratorsExists() {
        for (Iterator<K> iter : givenIterators) {
            if (iter.hasNext()) {
                throw new ConcurrentModificationException();
            }
        }

        givenIterators.clear();
    }

    private void replace(K key, V value) {
        final File hashFolder = getHashFolder(key);

        iterateHashFolder(hashFolder, (ObjectRepresentation it) -> {
            if (key.equals(it.key)) {
                it.valueFile.delete();
                Utils.serializeObject(value, it.valueFile);
            }
        });
    }

    @Override
    public synchronized void write(K key, V value) {
        assertOpen();
        if (exists(key)) {
            replace(key, value);
            return;
        }
        assertNoIteratorsExists();

        final File hashFolder = getHashFolder(key);

        if (!hashFolder.exists()) {
            hashFolder.mkdir();
        }

        File keyFile = null;
        File valueFile = null;

        Random rand = new Random();

        do {
            int id = Math.abs(rand.nextInt());
            keyFile = new File(hashFolder, "key_" + Long.toString(id));
            valueFile = new File(hashFolder, "value_" + Long.toString(id));
        } while (keyFile.exists() || valueFile.exists());

        Utils.serializeObject(key, keyFile);
        Utils.serializeObject(value, valueFile);
    }

    @Override
    public synchronized void delete(K key) {
        assertOpen();
        assertNoIteratorsExists();
        final File hashFolder = getHashFolder(key);

        iterateHashFolder(hashFolder, (ObjectRepresentation item) -> {
            if (item.key.equals(key)) {
                item.keyFile.delete();
                item.valueFile.delete();
            }
        });
    }

    @Override
    public synchronized Iterator<K> readKeys() {
        assertOpen();
        Iterator<K> res = readAll().keySet().iterator();
        givenIterators.add(res);
        return res;
    }

    @Override
    public synchronized int size() {
        assertOpen();
        return readAll().size();
    }

    @Override
    public synchronized void flush() {
    }

    private void assertOpen() {
        if (closed) {
            throw new RuntimeException("Writing to closed");
        }
    }

    @Override
    public synchronized void close() throws IOException {
        this.closed = true;
    }

    @Value
    @AllArgsConstructor
    private class ObjectRepresentation {
        File keyFile;
        File valueFile;
        K key;
        V value;
    }
}
