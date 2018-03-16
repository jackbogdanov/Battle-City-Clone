package com.bestproger.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;


public class Utils {

    private static final int LENGTH_MAS = 26;
    public static BufferedImage resize(BufferedImage image, int width, int height){
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        newImage.getGraphics().drawImage(image, 0, 0, width, height, null);
        return newImage;
    }

    public static BufferedImage resize(BufferedImage image, float width, float height){
        BufferedImage newImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);
        newImage.getGraphics().drawImage(image, 0, 0,(int) width, (int) height, null);
        return newImage;
    }



    public static Integer[][] levelParser(String filePath){

        Integer[][] result = new Integer[26][26];

        BufferedImage image = ResourceLoader.loadImage(filePath);
        image = Utils.resize(image, image.getWidth(), image.getHeight());
        int[] imageDate = ((DataBufferInt) image.getAlphaRaster().getDataBuffer()).getData();
        int k = 0;

        for (int i = 0; i < imageDate.length; i++) {
            switch (imageDate[i]){
                case 0xff000000: imageDate[i] = 0;
                    break;
                case 0xff663333: imageDate[i] = 1;
                    break;
                case 0xff666666: imageDate[i] = 2;
                    break;
                case 0xff00cccc: imageDate[i] = 3;
                    break;
                case 0xff006600: imageDate[i] = 4;
                    break;
                case 0xff999999: imageDate[i] = 5;
                    break;
            }
        }

        for (int i = 0; i < imageDate.length; i++) {
            result[i - LENGTH_MAS*k][k] = imageDate[i];
            if ((i + 1) % LENGTH_MAS == 0) k++;
        }

        return result;
    }

    public static void removeBlack(int[] imageData){
        for (int i = 0; i < imageData.length; i++) {
            if (imageData[i] == 0xff000001 || imageData[i] == 0xff000000){
                imageData[i] = 0x00000000;
            }
        }
    }

    public static final Integer[] strToInt_arrays(String[] array){
        Integer[] result = new Integer[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = Integer.parseInt(array[i]);
        }
        
        return result;
    }
}
