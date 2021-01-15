package com.a02.game;

import com.a02.users.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    /**
     * Devuelve la posición relativa a una cámara (320 * 180 píxeles en todos los casos) en un Vector3.
     * @return Vector3 posición
     */
    public static Vector3 getRelativeMousePos() {
        return MainGame.cursorCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(),0));
    }

    private static boolean isPressed;

    public static boolean mouseJustClicked() {
        if (Gdx.input.isTouched() && !isPressed) {
            isPressed = true;
            return true;
        }
        else if (!Gdx.input.isTouched() && isPressed) {
            isPressed = false;
            return false;
        }
        else {
            return false;
        }
    }

    /**
     * Crea una animación a partir de una sprite sheet.
     * @param path Ruta del archivo.
     * @param frameCols Columnas de la sprite sheet.
     * @param frameRows Filas de la sprite sheet.
     * @param frameDuration Duración de cada fotograma de la animación.
     * @return Objeto Animation con duración concreta.
     */
    public static Animation<TextureRegion> createAnimation(String path, int frameCols, int frameRows, float frameDuration) {
        //Cargar el sprite sheet
        Texture tempTexture = new Texture(Gdx.files.internal(path));

        //Divide el sprite sheet en una TextureRegion[][] bidimensional
        TextureRegion[][] tempTR = TextureRegion.split(tempTexture,
                tempTexture.getWidth() / frameCols,
                tempTexture.getHeight() / frameRows);

        //Coloca los frames de la animación en un array 1D de TextureRegion
        TextureRegion[] animationFrames = new TextureRegion[frameCols * frameRows];
        int index = 0;
        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                animationFrames[index++] = tempTR[i][j];
            }
        }

        //Crea y devuelve la animación
        return new Animation<>(frameDuration, animationFrames);
    }

    public static HashMap<String, User> readSer(String path) throws IOException, ClassNotFoundException {
        HashMap<String, User> map;
        try{
            FileInputStream fs = new FileInputStream(path);
            ObjectInputStream os = new ObjectInputStream(fs);
            map = (HashMap<String, User>) os.readObject();
            os.close();
            return map;
        } catch (EOFException e) {
            map = new HashMap<>();
            writeSer(path, map);
            return map;
        } catch (FileNotFoundException e) {
            PrintWriter writer = new PrintWriter(path, "UTF-8"); //Crea el archivo, aunque no se utilize después
            map = new HashMap<>();
            writeSer(path, map);
            return map;
        }
    }

    public static void writeSer(String path, HashMap<String,User> map) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(map);
        oos.close();
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

    public static void printMap(HashMap<String, User> mp) {
        Iterator<Map.Entry<String, User>> it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, User> pair = it.next();
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

    /**
     * Redondea con n decimales.
     * @param value Valor a redondear
     * @param precision Número de decimales
     * @return Número redondeado
     */
    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
