package org.rostykamga.imageexifprocessor;

import com.drew.metadata.MetadataException;

public class MainClass {

    public static void main(String[] args) throws MetadataException {
        ExifProcessor processor = new ExifProcessor();

        System.out.println(processor.extractExif("Apple iPhone 4.jpg"));
    }
}
