package org.timattt.storage.structure;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class Utils {

    @SneakyThrows
    public static void serializeObject(Object obj, File dest) {
        @Cleanup
        FileOutputStream fileOutputStream = new FileOutputStream(dest);
        @Cleanup
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(obj);
    }

    @SneakyThrows
    public static Object deserializeObject(File fileName) {
        @Cleanup
        FileInputStream fileInputStream = new FileInputStream(fileName);
        @Cleanup
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        return objectInputStream.readObject();
    }

}
