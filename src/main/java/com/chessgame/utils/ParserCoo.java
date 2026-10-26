package com.chessgame.utils;

import static java.lang.String.valueOf;

public class ParserCoo {

    public String parseColIntToString(int col) {
        return String.valueOf((char) ('A' + col));
    }

    public String parseRowIntToString(int row){
        return String.valueOf(((row + 8)%8)+1);
    }


}

