package be;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class PlayerFunctions {
    public void playVideo(String filePath){
        try {
            Runtime.getRuntime().exec( "open -a /Applications/VLC.app/Contents/MacOS/VLC/ " + filePath + " ");

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
