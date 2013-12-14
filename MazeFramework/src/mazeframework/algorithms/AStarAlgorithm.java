/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import mazeframework.interfaces.MazeCellType;
import mazeframework.interfaces.Pathfinder;

/**
 *
 * @author Gintoki
 */
public class AStarAlgorithm implements Pathfinder {

    // MazeCellType[][] maze;
    MazeCell[][] allCells;
//    Point start;
//    Point goal;
    MazeCell start;
    MazeCell goal;

    @Override
    public void initPathfinder(MazeCellType[][] maze, Point start, Point goal) {
        //    this.maze = maze;
//        this.start = start;
//        this.goal = goal;

        MazeCell strt = new MazeCell(start.x, start.y);
        MazeCell gl = new MazeCell(goal.x, goal.y);
        strt.setG(0);
        strt.setH(distance(strt, gl));
        strt.setF(strt.getG() + strt.getH());
        gl.setG(distance(strt, gl));
        gl.setH(0);
        gl.setF(gl.getG() + gl.getH());


        allCells = new MazeCell[maze.length][maze.length];
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze.length; y++) {
                if (maze[x][y] == MazeCellType.EMPTY) {
                    MazeCell mc = new MazeCell(x, y);
                    mc.setG(distance(strt, mc));
                    mc.setH(distance(mc, gl));
                    mc.setF(mc.getH() + mc.getG());
                    allCells[x][y] = mc;
                } else {
                    allCells[x][y] = null;
                }
            }
        }
        allCells[start.x][start.y] = strt;
        allCells[goal.x][goal.y] = gl;
        this.start = strt;
        this.goal = gl;
    }

    @Override
    public List<Point> startPathfinder(boolean diagonalAllowed) {
        ArrayList<MazeCell> closedSet = new ArrayList<>();
        ArrayList<MazeCell> openSet = new ArrayList<>();
        openSet.add(start);
        ArrayList<MazeCell> pathMap = new ArrayList<>();

        while (!openSet.isEmpty()) {
            MazeCell min = getMinF(openSet);
            if (min.equals(goal)) {
                return reconstructPath(start, goal);
            }

            openSet.remove(min);
            closedSet.add(min);
            List<MazeCell> ngbr = getNeighbourNodes(min, diagonalAllowed);
            Iterator<MazeCell> iter = ngbr.iterator();
            boolean tentativeIsBetter = false;
            while (iter.hasNext()) {
                MazeCell cr = iter.next();
                if (!closedSet.contains(cr)) {
                    double tentativeGscore = min.getG() + distance(min, cr);
                    if (!openSet.contains(cr)) {
                        openSet.add(cr);
                        tentativeIsBetter = true;
                    } else {
                        if (tentativeGscore < cr.getG()) {
                            tentativeIsBetter = true;
                        } else {
                            tentativeIsBetter = false;
                        }
                    }

                    if (tentativeIsBetter) {
                        cr.setCameFrom(min);
                        cr.setG(tentativeGscore);
                        cr.setH(distance(cr, goal));
                        cr.setF(cr.getG() + cr.getH());
                    }
                }
            }
        }
        return null;
    }

    private double distance(MazeCell m1, MazeCell m2) {
        int vx = m1.getX() - m2.getX();
        int vy = m1.getY() - m2.getY();
        return (Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2)));
    }

    private List<Point> reconstructPath(MazeCell strt, MazeCell gl) {
        ArrayList<Point> pathMap = new ArrayList<>();
        MazeCell cur = gl;
        while (cur != null) {
            pathMap.add(new Point(cur.getX(), cur.getY()));
            cur = cur.getCameFrom();
        }
        Collections.reverse(pathMap);
        return pathMap;
    }

    private MazeCell getMinF(List<MazeCell> l) {
        Iterator<MazeCell> iter = l.iterator();
        MazeCell curMin = null;
        while (iter.hasNext()) {
            MazeCell cur = iter.next();
            if (curMin == null) {
                curMin = cur;
            } else {
                if (cur.getF() < curMin.getF()) {
                    curMin = cur;
                }
            }
        }
        return curMin;
    }

    private List<MazeCell> getNeighbourNodes(MazeCell node, boolean diagonal) {
        if (diagonal) {
            return getNNodesDiagonal(node); 
        }
        return getNNodesNoDiagonal(node);
    }

    private List<MazeCell> getNNodesNoDiagonal(MazeCell node) {
        ArrayList<MazeCell> nbrs = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();
        int xmin = x;
        int xmax = x;
        int ymin = y;
        int ymax = y;

        if (x - 1 >= 0) {
            xmin = x - 1;
        }
        if (x + 1 < allCells.length) {
            xmax = x + 1;
        }
        if (y - 1 >= 0) {
            ymin = y - 1;
        }
        if (y + 1 < allCells.length) {
            ymax = y + 1;
        }

        int[] crds = {xmin, y, x, ymax, x, ymin, xmax, y};

        for (int i = 0; i < crds.length; i += 2) {
            MazeCell mc = allCells[crds[i]][crds[i + 1]];
            if (mc != null) {
                if (!mc.equals(node)) { //Not the node istelf
                    if (!nbrs.contains(mc)) {
                        nbrs.add(mc);
                    }
                }
            }
        }

        return nbrs;
    }

    private List<MazeCell> getNNodesDiagonal(MazeCell node) {
        ArrayList<MazeCell> nbrs = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();
        int xmin = x;
        int xmax = x;
        int ymin = y;
        int ymax = y;

        if (x - 1 >= 0) {
            xmin = x - 1;
        }
        if (x + 1 < allCells.length) {
            xmax = x + 1;
        }
        if (y - 1 >= 0) {
            ymin = y - 1;
        }
        if (y + 1 < allCells.length) {
            ymax = y + 1;
        }

        for (int i = xmin; i <= xmax; i++) {
            for (int k = ymin; k <= ymax; k++) {
                if (allCells[i][k] != null) {
                    if (!allCells[i][k].equals(node)) { //Not the node istelf
                        nbrs.add(allCells[i][k]);
                    }
                }
            }
        }
        return nbrs;
    }
}

class MazeCell {

    private MazeCell cameFrom = null;
    private int x;
    private int y;
    private double g; //Cost from start
    private double h; //Heuristic distance estimate (start,goal)
    private double f; //g+f

    public MazeCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the g
     */
    public double getG() {
        return g;
    }

    /**
     * @param g the g to set
     */
    public void setG(double g) {
        this.g = g;
    }

    /**
     * @return the h
     */
    public double getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(double h) {
        this.h = h;
    }

    /**
     * @return the f
     */
    public double getF() {
        return f;
    }

    /**
     * @param f the f to set
     */
    public void setF(double f) {
        this.f = f;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    public Point getPoint() {
        return new Point(x, y);
    }

    /**
     * @return the cameFrom
     */
    public MazeCell getCameFrom() {
        return cameFrom;
    }

    /**
     * @param cameFrom the cameFrom to set
     */
    public void setCameFrom(MazeCell cameFrom) {
        this.cameFrom = cameFrom;
    }
}
