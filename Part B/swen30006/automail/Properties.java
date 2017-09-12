/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class Properties {
    private HashMap<String, String> properties = new HashMap<>();
    
    /**
     *
     * @param file
     */
    public void load(FileReader file) throws IOException{
        BufferedReader br = new BufferedReader(file);
        String line;
        while ((line = br.readLine()) != null) {
           // process the line.
           // ignore if comment
           if (!line.startsWith("#")){
               String[] line_a = line.split("=");
               properties.put(line_a[0], line_a[1]);
           }
        }
    }
        
    public String getProperty(String name){
        return properties.get(name);
    }
}
