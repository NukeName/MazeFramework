/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework.interfaces;

import java.awt.Point;
import java.util.List;

/**
 *
 * @author Gintoki
 */
public interface Pathfinder {
    public void initPathfinder(MazeCellType[][] maze, Point start, Point goal);
    
    public List<Point> startPathfinder(boolean diagonalAllowed);
}
