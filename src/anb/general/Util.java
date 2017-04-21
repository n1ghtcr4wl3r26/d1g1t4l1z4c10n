package anb.general;


import anb.entidades.Tramite;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;


/*
*   Nombre de la clase: Util, metodos generales que se utilizaran en todo el sistema
*
*   Fecha creación, Fecha Modificación
*
*   Autor creador, Autor Modificador
*/

public class Util {
    
    public static void main(String args[]) {
        Calendar calendario = GregorianCalendar.getInstance();
        Date fecha = calendario.getTime();
        SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyHHmmss");
        byte[] encodedBytes = Base64.encodeBase64(String.valueOf(Util.randomWord(5) + formato.format(fecha)).getBytes());
        System.out.println(new String(encodedBytes));
    }
    
    
    
    public static List<Tramite> adicionar_a_lista(List<Tramite> listaini , List<Tramite> listades ) {
        List<Tramite> res = new ArrayList<Tramite>();
        res = listades;
        Tramite tram = new Tramite(); 
        for (int i = 0; i < listaini.size() ; i++){
            tram = listaini.get(i);  
            listades.add(tram);
            tram = null;
        }        
        
        return res;
    }
    
    public static String completarCeros6(String numero) {
        String res = "";
        
        if(numero.length() == 6)
            res = numero;    
        if(numero.length() == 5)
            res = "0"+numero;    
        if(numero.length() == 4)
            res = "00"+numero;    
        if(numero.length() == 3)
            res = "000"+numero;    
        if(numero.length() == 2)
            res = "0000"+numero;    
        if(numero.length() == 1)
            res = "00000"+numero;   
        
        return res;
    }
    
    public static String completarCeros7(String numero) {
        String res = "";
        if(numero.length() == 7)
            res = numero;
        if(numero.length() == 6)
            res = "0"+numero;    
        if(numero.length() == 5)
            res = "00"+numero;    
        if(numero.length() == 4)
            res = "000"+numero;    
        if(numero.length() == 3)
            res = "0000"+numero;    
        if(numero.length() == 2)
            res = "00000"+numero;    
        if(numero.length() == 1)
            res = "000000"+numero;   
        
        return res;
    }
    
    public static String completarCeros8(String numero) {
        String res = "";
        
        if(numero.length() == 8)
            res = numero;    
        if(numero.length() == 7)
            res = "0"+numero;    
        if(numero.length() == 6)
            res = "00"+numero;    
        if(numero.length() == 5)
            res = "000"+numero;    
        if(numero.length() == 4)
            res = "0000"+numero;    
        if(numero.length() == 3)
            res = "00000"+numero;    
        if(numero.length() == 2)
            res = "000000"+numero;    
        if(numero.length() == 1)
            res = "0000000"+numero;   
        
        return res;
    }
    
    public static String randomWord(int length) {
        Random random = new Random();
        StringBuilder word = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            word.append((char)('a' + random.nextInt(26)));
        }

        return word.toString();
    }
    
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' ||
                       chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
    
    public static void isAjax(HttpServletRequest request, HttpServletResponse response) {
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            Util.redirect(request, response, "/index.do");
        }
    }
    
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
               
    public static String join(String elements[], String separator) {
        String text = "";
        for (String el : elements) {
            text += el + separator;
        }
        return text.replaceAll("\\" + separator + "+$","");
    }
    
    public static void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
    }
           
}