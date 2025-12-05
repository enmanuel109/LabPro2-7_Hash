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
        Entry nuevoEntry = new Entry(username, posicion);

        if (this.head == null) {
            this.head = nuevoEntry;
            return;
        }

        Entry actual = this.head;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }

        actual.setSiguiente(nuevoEntry);
    }

    //metodo de buscar
    public long search(String username) {
        Entry actual = this.head;

        while (actual != null) {
            if (actual.getUsername().equals(username)) {
                return actual.getPosicion();
            }
            actual = actual.getSiguiente();
        }
        return -1;
    }

    //metood de remove
    public boolean remove(String username) {
        if (this.head == null) {
            return false;
        }

        if (this.head.getUsername().equals(username)) {
            this.head = this.head.getSiguiente();
            return true;
        }

        Entry actual = this.head;
        while (actual.getSiguiente() != null) {
            if (actual.getSiguiente().getUsername().equals(username)) {
                actual.setSiguiente(actual.getSiguiente().getSiguiente());
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
}
