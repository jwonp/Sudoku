package com.example.sudoku;

public class Location {
    private int row; // 0 ~ 8
    private int col; // 0 ~ 8
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row;}

    public int getCol() { return col;}
    public int getSquare() {
        return (((row/3) * 3) + (col/3));
    }

    public void setRow(int row) { this.row = row;}

    public void setCol(int col) { this.col = col;}

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isSameLocationWith(Location location) {
        if(location.getRow() == row && location.getCol() == col) return true;
        else return false;
    }



}
