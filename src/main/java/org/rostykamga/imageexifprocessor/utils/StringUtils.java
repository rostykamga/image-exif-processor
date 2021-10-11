package org.rostykamga.imageexifprocessor.utils;

public class StringUtils {

    /**
     * This method splits the input argument and return its nth (0 based) chunk.
     * @param str the string we want to split. The chunks are separated by space
     * @param n the index of the chunk to take
     * @return the nth (0-based) chunk of  the string, or null if the string to not have n chunks
     */
    public static String takeNthChunk(int n, String str){

        //return null if empty string or
        if(str.isEmpty())
            throw  new IllegalArgumentException("String must not be empty");
        if(n < 0)
            throw new IllegalArgumentException("Out of range index: "+n);

        String[] chunks = str.trim().split(" ");

        if(chunks.length <= n || chunks[n].isEmpty())
            return null;

        return chunks[n];
    }
}
