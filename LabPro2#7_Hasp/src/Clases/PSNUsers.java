/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import javax.swing.ImageIcon;

/**
 *
 * @author Cantarero
 */
public class PSNUsers {

    private RandomAccessFile rusers;
    private HashTable ListUsers;

    public PSNUsers() {
        try {
            rusers = new RandomAccessFile("ArchivosUsers.psn", "rw");
            ListUsers = new HashTable();
            reloadHashTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadHashTable() throws IOException {
        rusers.seek(0);
        while (rusers.getFilePointer() < rusers.length()) {

            long pos = rusers.getFilePointer();
            String username = rusers.readUTF();
            rusers.readInt();
            rusers.readInt();
            boolean activo = rusers.readBoolean();

            if (activo) {
                ListUsers.add(username, pos);
            }
        }
    }

    public void addUser(String username) throws IOException {
        username = username.trim().toLowerCase();

        if (ListUsers.search(username) != -1) {
            throw new IOException("Usuario ya existe");
        }

        rusers.seek(rusers.length());
        long pos = rusers.getFilePointer();

        rusers.writeUTF(username);
        rusers.writeInt(0);
        rusers.writeInt(0);
        rusers.writeBoolean(true);

        ListUsers.add(username, pos);
    }

    public void deactivateUser(String username) throws IOException {
        username = username.trim().toLowerCase();

        long pos = ListUsers.search(username);
        if (pos == -1) {
            throw new IOException("Usuario no existe");
        }

        rusers.seek(pos);
        rusers.readUTF();
        rusers.readInt();
        rusers.readInt();
        rusers.writeBoolean(false);
    }

    public void addTrophieTo(String username, String game, String nameTrf, Trophy type) throws IOException {
        username = username.trim().toLowerCase();

        long pos = ListUsers.search(username);
        if (pos == -1) {
            throw new IOException("Usuario no existe");
        }

        rusers.seek(pos);
        rusers.readUTF();
        rusers.readInt();
        rusers.readInt();
        boolean activo = rusers.readBoolean();

        if (!activo) {
            throw new IOException("Usuario desactivado");
        }

        File tf = new File("trophies.psn");
        if (!tf.exists()) {
            tf.createNewFile();
        }

        String imgPath;
        switch (type) {
            case PLATINO:
                imgPath = "src/imgtrofeos/PLATINO.png";
                break;
            case ORO:
                imgPath = "src/imgtrofeos/ORO.png";
                break;
            case PLATA:
                imgPath = "src/imgtrofeos/PLATA.png";
                break;
            default:
                imgPath = "src/imgtrofeos/BRONCE.png";
        }

        RandomAccessFile trophies = new RandomAccessFile(tf, "rw");
        trophies.seek(trophies.length());

        trophies.writeUTF(username);
        trophies.writeUTF(type.name());
        trophies.writeUTF(game);
        trophies.writeUTF(nameTrf);
        trophies.writeUTF(LocalDate.now().toString());

        byte[] img = readImage(imgPath);
        trophies.writeInt(img.length);
        trophies.write(img);
        trophies.close();

        rusers.seek(pos);
        rusers.readUTF();
        int puntos = rusers.readInt();
        int cantidad = rusers.readInt();

        puntos += type.puntos;
        cantidad++;

        rusers.seek(pos);
        rusers.readUTF();
        rusers.writeInt(puntos);
        rusers.writeInt(cantidad);
    }

    private byte[] readImage(String ruta) throws IOException {

        File file = new File(ruta);

        if (!file.exists()) {
            throw new IOException("No existe la imagen: " + ruta);
        }

        return java.nio.file.Files.readAllBytes(file.toPath());
    }

    public String playerInfo(String username) throws IOException {

        username = username.trim().toLowerCase();

        long pos = ListUsers.search(username);
        if (pos == -1) {
            return "Usuario no existe";
        }

        rusers.seek(pos);

        String user = rusers.readUTF();
        int puntos = rusers.readInt();
        int trofeos = rusers.readInt();
        boolean activo = rusers.readBoolean();

        String estado = activo ? "ACTIVO" : "DESACTIVADO";

        String info = "Usuario: " + user + "\n"
                + "Estado: " + estado + "\n"
                + "Puntos: " + puntos + "\n"
                + "Trofeos: " + trofeos + "\n\n"
                + "Trofeos obtenidos:\n";

        File tf = new File("trophies.psn");
        if (!tf.exists()) {
            tf.createNewFile();
        }

        RandomAccessFile trophies = new RandomAccessFile(tf, "r");

        while (trophies.getFilePointer() < trophies.length()) {

            String usrtrf = trophies.readUTF();
            String tipo = trophies.readUTF();
            String game = trophies.readUTF();
            String desc = trophies.readUTF();
            String fecha = trophies.readUTF();

            int size = trophies.readInt();
            trophies.skipBytes(size);

            if (usrtrf.equals(username)) {
                info += "Fecha: " + fecha
                        + " | Tipo: " + tipo
                        + " | Juego: " + game
                        + " | Desc: " + desc + "\n";
            }
        }

        trophies.close();
        return info;
    }
}
