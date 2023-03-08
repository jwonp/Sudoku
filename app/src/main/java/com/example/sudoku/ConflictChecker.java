package com.example.sudoku;
import com.example.sudoku.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class ConflictChecker {
    HashMap<Integer, ArrayList<Location>> locations;

    public ConflictChecker(){
        locations = getNewLocations();
    }


//    public void resetConflictChecker() {
//        locations = getNewLocations();
//    }

    public HashMap<Integer, ArrayList<Location>> getNewLocations() {
        HashMap<Integer, ArrayList<Location>> tmp = new HashMap<Integer, ArrayList<Location>>();

        tmp.put(1, new ArrayList<Location>());
        tmp.put(2, new ArrayList<Location>());
        tmp.put(3, new ArrayList<Location>());
        tmp.put(4, new ArrayList<Location>());
        tmp.put(5, new ArrayList<Location>());
        tmp.put(6, new ArrayList<Location>());
        tmp.put(7, new ArrayList<Location>());
        tmp.put(8, new ArrayList<Location>());
        tmp.put(9, new ArrayList<Location>());

        return  tmp;
    }

    public void addInputNumberLocation(int num, Location location) {
        Objects.requireNonNull(locations.get(num)).add(location);
    }
    public void removeInputNumberLocation(int num, Location location) {
        if( locations.get(num) == null) return;
        for(Location item : locations.get(num)) {
            if(item.isSameLocationWith(location)) {
                locations.get(num).remove(item);
                return;
            }
        }
    }
    public ArrayList<Location> getLocationsByNumber(int number) {
        return locations.get(number);
    }

    public ArrayList<Location> getConflictingNumbers(int target) {
        ArrayList<Location> locationList = locations.get(target);
        ArrayList<Location> result = new ArrayList<Location>();

        for(Location item : Objects.requireNonNull(locationList)) {
            if(isConflicting(item, locationList))
                result.add(item);
        }
        return result;
    }

    public boolean isConflicting(Location location, ArrayList<Location> list) {
        boolean conflict = false;
        int row = location.getRow();
        int col = location.getCol();
        int square = location.getSquare();
        for(Location item : list) {
            if(location.isSameLocationWith(item)) continue;
            if(row == item.getRow() || col == item.getCol() || square == item.getSquare()) {
                conflict = true;
            }
        }
        return conflict;
    }






//    static final int One = 0;
//    static final int Two = 1;
//    static final int Three = 2;
//    static final int Four = 3;
//    static final int Five = 4;
//    static final int Six = 5;
//    static final int Seven = 6;
//    static final int Eight = 7;
//    static final int Nine = 8;
}
