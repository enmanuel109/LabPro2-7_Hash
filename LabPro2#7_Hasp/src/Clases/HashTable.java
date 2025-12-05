/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

/**
 *
 * @author HP
 */
public class HashTable {

    private Entry head;

    public HashTable() {
        this.head = null;
    }

    //metodo de add
    public void add(String username, long posicion) {

        Entry nuevo = new Entry(username, posicion);

        if (head == null) {
            head = nuevo;
            return;
        }

        Entry actual = head;

        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }

        actual.setSiguiente(nuevo);
    }

    //metodo de buscar
    public long search(String username) {

        Entry actual = head;

        while (actual != null) {

            if (actual.getUsername().equals(username)) {
                return actual.getPosicion();
            }

            actual = actual.getSiguiente();
        }

        return -1;
    }

    //metood de remove
    public void remove(String username) {

        if (head == null) {
            return;
        }

        // ✅ SI ES EL PRIMERO
        if (head.getUsername().equals(username)) {
            head = head.getSiguiente();
            return;
        }

        Entry actual = head;

        while (actual.getSiguiente() != null) {

            if (actual.getSiguiente().getUsername().equals(username)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return;
            }

            actual = actual.getSiguiente();
        }
    }
}
