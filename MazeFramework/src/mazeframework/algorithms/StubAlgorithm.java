/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import mazeframework.interfaces.MazeCellType;
import mazeframework.interfaces.Pathfinder;

/**
 *
 * @author Gintoki
 */
public class StubAlgorithm implements Pathfinder {

    @Override
    public void initPathfinder(MazeCellType[][] maze, Point start, Point goal) {
       
    }

    @Override
    public List<Point> startPathfinder(boolean diagonalAllowed) {
        ArrayList<Point> arr = new ArrayList<>();
        arr.add(new Point(4, 3));
        arr.add(new Point(5,3));
        arr.add(new Point(5,4));
        return arr;
    }
    
}
