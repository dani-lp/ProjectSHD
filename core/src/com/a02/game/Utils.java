package com.a02.game;

import com.a02.entity.Entity;
import com.a02.users.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.io.*;
import java.util.ArrayList;
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

    /**
     * Comprueba si el mouse ha hecho click una sóla vez.
     * @return Click del ratón
     */
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

    /**
     * Lee el archivo de usuario serizalizado parámetro.
     * @param path Ruta del archivo
     * @return Mapa extraído del archivo
     * @throws IOException IOException
     * @throws ClassNotFoundException ClassNotFoundException
     */
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

    /**
     * Escribe el archivo con los datos de usuario parámetro.
     * @param path Ruta del archivo
     * @param map Datos de usuario
     * @throws IOException Error de acceso a archivo
     */
    public static void writeSer(String path, HashMap<String,User> map) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(map);
        oos.close();
    }

    /**
     * Borra el usuario parámetro del archivo de usuarios.
     * @param path Ruta del archivo
     * @param key Nombre de usuario
     */
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

    /**
     * Imprime el mapa parámetro por consola.
     * @param mp Mapa parámetro
     */
    public static void printMap(HashMap<String, User> mp) {
        Iterator<Map.Entry<String, User>> it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, User> pair = it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }

    /**
     * Comprueba con un regex si el email introducido es de forma válida.
     * @param emailStr Email a comprobar
     * @return Comprobación de mapa
     */
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

    /**
     * Devuelve la distancia entre dos entidades.
     * @param e1 Entidad 1
     * @param e2 Entidad 2
     * @return Distancia entre entidades
     */
    public static double distanceBetweenEntities(Entity e1, Entity e2) {
        return Math.sqrt(Math.pow(e1.getX() - e2.getX(), 2) + Math.pow(e1.getY() - e2.getY(), 2));
    }

    /**
     * Devuelve el índice de la entidad más cercana a la entity parámetro de una lista parámetro.
     * @param entity Entidad 'base'
     * @param entityList Lista de entidades 'externas'
     * @return Índice de la entidad más cercana
     */
    public static int getNearestEntityIndex(Entity entity, ArrayList<? extends Entity> entityList) {
        int minDistanceIndex = 0;
        double minDistance = 400; //Distancia mayor a la diagonal máxima (368 aprox.)
        for (int entL = 0; entL < entityList.size(); entL++) {
            double distance = distanceBetweenEntities(entity, entityList.get(entL));
            if (distance < minDistance ) {
                minDistance = distance;
                minDistanceIndex = entL;
            }
        }
        return minDistanceIndex;
    }
    /**
     * Guarda el usuario actulizado en el HashMap
     * @param username Nombre del usuario que esta jugando
     * @param points Puntuacion que ha obtenido
     */
    public static void saveMaxScore(String username, int points) {
        HashMap<String, User> map = null;
        try {
            map = readSer("data/users.ser");        //Deserializar el HashMap de usuarios
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        assert map != null;
        User tempUser = map.get(username);
        //Encontrar el usuario que esta jugando y actualizarlo si ha obtenido mayor puntuacion
        if (tempUser.getScoreRecord() < points) tempUser.setScoreRecord(points);
        map.put(tempUser.getUsername(), tempUser);

        try {
            writeSer("data/users.ser", map);    //Serializar el usuario actualizado
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
