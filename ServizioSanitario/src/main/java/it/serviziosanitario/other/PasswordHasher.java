/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.other;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.javatuples.Pair;

/**
 *
 * @author Alessio Gottardi
 */
public class PasswordHasher {
        private static final String ALGORITMO = "SHA-256";
        
        /**
        * Ritorna l'hash della password con il salt indicato
        * @param password la password.
        * @param salt il salt.
        * @return l' hash.
        */
	public static String hashPassword(String password, String salt) {
            try {
                MessageDigest md = MessageDigest.getInstance(ALGORITMO);
                md.update(salt.getBytes(StandardCharsets.UTF_8));
                return toString(md.digest(password.getBytes(StandardCharsets.UTF_8)));
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
                return null;
            }
	}
        
        /**
        * Controlla se la password combacia con l'hash e il salt
        * @param password la password.
        * @param hash l' hash.
        * @param salt il salt.
        * @return il risultato dell'autenticazione.
        */
	public static boolean authenticate(String password, String hash, String salt) {
            return hash.equals(hashPassword(password, salt));
	}
        
        /**
        * Genera un hash con la password data e un salt generato in modo casuale
        * @param password la password.
        * @return un Pair che contiene l'hash e il salt.
        */
        public static Pair<String, String> generateHashAndSalt(String password) {
            MessageDigest md;
            try
            {
                md = MessageDigest.getInstance(ALGORITMO);

                SecureRandom random = new SecureRandom();
                byte[] randSalt = new byte[16];
                random.nextBytes(randSalt);
                
                String salt = toString(randSalt);
                md.update(salt.getBytes(StandardCharsets.UTF_8));

                byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
                
                return Pair.with(toString(hashedPassword), salt);
                
            } catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
                return null;
            }
	}
        
        /**
        * Trasforma un array di bytes in una String
        * @param data l'array di bytes.
        * @return la String corrispondente.
        */
	public static String toString(byte[] data)  {
            StringBuilder sb = new StringBuilder();
            for (byte b : data)
                sb.append(String.format("%02x", b));
            return sb.toString();
	}
}
