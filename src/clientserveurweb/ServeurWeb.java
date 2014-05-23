/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserveurweb;

import clientserveurweb.serveur.Core.Serveur;

/**
 *
 * @author Antoine
 */
public class ServeurWeb {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Serveur srv = new Serveur(80, 6);
    }

}
