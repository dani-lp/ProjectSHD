package com.a02.game.components;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.a02.game.User;

public class Utils {
    public static HashMap<String, User> readSer(String path) throws IOException, ClassNotFoundException {
        FileInputStream fs = new FileInputStream(path);
        ObjectInputStream os = new ObjectInputStream(fs);

        return (HashMap<String, User>) os.readObject();
    }

    public static void writeSer(String path, HashMap<String,User> map) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(map);
    }

    public static void deleteUser(String path, String key) {
        HashMap<String, User> map = null;
        try {
            map = readSer(path);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (map != null) map.remove(key);

        try {
            writeSer(path, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }

    public static boolean validateMail(String emailStr) {
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find() || emailStr.equals("");
    }
}
