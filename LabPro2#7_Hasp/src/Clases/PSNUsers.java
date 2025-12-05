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
    private ImageIcon lastTrophyImage;

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

            long PosUser = rusers.getFilePointer();
            String username = rusers.readUTF();
            rusers.readInt();
            rusers.readInt();
            boolean activo = rusers.readBoolean();

            if (activo) {
                ListUsers.add(username, PosUser);
            }
        }
    }

    public void addUser(String username) throws IOException {

        if (ListUsers.search(username) != -1) {
            return;
        }

        rusers.seek(rusers.length());
        long singuinte = rusers.getFilePointer();

        rusers.writeUTF(username);
        rusers.writeInt(0);
        rusers.writeInt(0);
        rusers.writeBoolean(true);

        ListUsers.add(username, singuinte);
    }

    public void deactivateUser(String username) throws IOException {

        long pos = ListUsers.search(username);
        if (pos == -1) {
            return;
        }

        rusers.seek(pos);
        rusers.readUTF();
        rusers.readInt();
        rusers.readInt();
        rusers.writeBoolean(false);

        ListUsers.remove(username);
    }

    public void addTrophieTo(String username, String game, String NameTrf, Trophy type) throws IOException {

        long PosUser = ListUsers.search(username);
        if (PosUser == -1) {
            return;
        }

        String TrofeoName;

        switch (type) {
            case PLATINO:
                TrofeoName = "imgtrofeos/PLATINO.png";
                break;
            case ORO:
                TrofeoName = "imgtrofeos/ORO.png";
                break;
            case PLATA:
                TrofeoName = "imgtrofeos/PLATA.png";
                break;
            default:
                TrofeoName = "imgtrofeos/BRONCE.png";
        }

        RandomAccessFile trophies = new RandomAccessFile("trophies.psn", "rw");
        trophies.seek(trophies.length());

        trophies.writeUTF(username);
        trophies.writeUTF(type.name());
        trophies.writeUTF(game);
        trophies.writeUTF(NameTrf);
        trophies.writeUTF(LocalDate.now().toString());

        byte[] img = readImage(TrofeoName);
        trophies.writeInt(img.length);
        trophies.write(img);

        trophies.close();

        rusers.seek(PosUser);
        rusers.readUTF();

        int puntos = rusers.readInt();
        int cantidad = rusers.readInt();

        puntos += type.puntos;
        cantidad++;

        rusers.seek(PosUser);
        rusers.readUTF();
        rusers.writeInt(puntos);
        rusers.writeInt(cantidad);
    }

    private byte[] readImage(String NameImg) throws IOException {
        File fileimg = new File(NameImg);

        if (!fileimg.exists()) {
            return new byte[0];
        }

        byte[] img = new byte[(int) fileimg.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(fileimg));
        dis.readFully(img);
        dis.close();
        return img;
    }

    public String playerInfo(String username) throws IOException {

        lastTrophyImage = null; // SE RESETEA CADA VEZ

        long pos = ListUsers.search(username);
        if (pos == -1) {
            return "Usuario no existe";
        }

        rusers.seek(pos);

        String user = rusers.readUTF();
        int puntos = rusers.readInt();
        int trofeos = rusers.readInt();
        boolean activo = rusers.readBoolean();

        String info = "Usuario: " + user + "\n"
                + "Puntos: " + puntos + "\n"
                + "Trofeos: " + trofeos + "\n"
                + "Activo: " + activo + "\n\n"
                + "Trofeos obtenidos:\n";

        RandomAccessFile trophies = new RandomAccessFile("trophies.psn", "r");

        while (trophies.getFilePointer() < trophies.length()) {

            String usrtrf = trophies.readUTF();
            String tipo = trophies.readUTF();
            String game = trophies.readUTF();
            String desc = trophies.readUTF();
            String fecha = trophies.readUTF();

            int size = trophies.readInt();
            byte[] img = new byte[size];
            trophies.readFully(img);

            if (usrtrf.equals(username)) {
                info += "Fecha: " + fecha
                        + " | Tipo: " + tipo
                        + " | Juego: " + game
                        + " | Desc: " + desc + "\n";
                if (img.length > 0) {
                    lastTrophyImage = new ImageIcon(img);
                }
            }
        }

        trophies.close();
        return info;
    }

    public ImageIcon getLastTrophyImage() {
        return lastTrophyImage;
    }
}
