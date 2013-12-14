/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Javid
 */
public class WaveAlgorithm implements Pathfinder {
    
    Cell[][] cells;
    Cell begin, end;
    
    @Override
    public void initPathfinder(MazeCellType[][] maze, Point start, Point goal) {
        cells = new Cell[maze.length][maze.length];
        this.begin = new Cell(start.x, start.y);
        this.end = new Cell(goal.x, goal.y);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                if (maze[i][j] == MazeCellType.EMPTY) {
                    cells[i][j] = new Cell(i, j);
                }
            }
        }
        cells[start.x][start.y] = begin;
        cells[goal.x][goal.y] = end;
    }
    
    @Override
    public List<Point> startPathfinder(boolean diagonalAllowed) {
        begin.setWaveNum(0);
        setWaveNums(begin, 0);
        System.out.println("----------------");
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                if (begin.getX() == j && begin.getY() == i) {
                    System.out.print("<" + cells[j][i].getWavenum() + ">");
                    continue;
                }
                if (end.getX() == j && end.getY() == i) {
                    System.out.print(">" + cells[j][i].getWavenum() + "<");
                    continue;
                }
                if (cells[j][i] != null) {
                    System.out.print(cells[j][i].getWavenum() + " ");
                }
                
            }
            System.out.println("");
        }
        //End of marking

        //bactracking
        ArrayList<Point> arr = new ArrayList<>();
        int dx[] = {1, 0, -1, 0};
        int dy[] = {0, 1, 0, -1};
        int d = end.getWavenum();
        Cell curCell = end;
        while (d > 0) {
            for (int i = 0; i < 4; ++i) {
                if (curCell.getX() + dx[i] < cells.length
                        && curCell.getY() + dy[i] < cells.length
                        && curCell.getX() + dx[i] >= 0
                        && curCell.getY() + dy[i] >= 0
                        && cells[curCell.getX() + dx[i]][curCell.getY() + dy[i]] != null
                        && d - 1 == cells[curCell.getX() + dx[i]][curCell.getY() + dy[i]].getWavenum()) {
                    arr.add(new Point(cells[curCell.getX() + dx[i]][curCell.getY() + dy[i]].getX(), cells[curCell.getX() + dx[i]][curCell.getY() + dy[i]].getY()));
                    curCell = cells[curCell.getX() + dx[i]][curCell.getY() + dy[i]];
                    break;
                }
            }
            d--;
        }
        return arr;
    }
    
    private void setWaveNums(Cell c, int curWaveNum) {
        int dx[] = {1, 0, -1, 0};
        int dy[] = {0, 1, 0, -1};
        boolean stop = true;
        int lastMarked = 1;
        Cell[] markedCells = new Cell[cells.length * cells.length];
        markedCells[0] = begin;
        do {
            stop = true;
            for (Cell cell : markedCells) {
                for (int i = 0; i < 4; ++i) {
                    if (c.getX() + dx[i] < cells.length
                            && c.getY() + dy[i] < cells.length
                            && c.getX() + dx[i] >= 0
                            && c.getY() + dy[i] >= 0
                            && cells[c.getX() + dx[i]][c.getY() + dy[i]] != null
                            && cells[c.getX() + dx[i]][c.getY() + dy[i]].getWavenum() == -1) {
                        
                    }
                }
            }
        } while (stop);

//        int dx[] = {1, 0, -1, 0};
//        int dy[] = {0, 1, 0, -1};
//        System.out.println(c.getX() + " " + c.getY());
//        for (int i = 0; i < 4; ++i) {
//            if (c.getX() + dx[i] < cells.length
//                    && c.getY() + dy[i] < cells.length
//                    && c.getX() + dx[i] >= 0
//                    && c.getY() + dy[i] >= 0
//                    && cells[c.getX() + dx[i]][c.getY() + dy[i]] != null
//                    && cells[c.getX() + dx[i]][c.getY() + dy[i]].getWavenum() == -1) {
//                cells[c.getX() + dx[i]][c.getY() + dy[i]].setWaveNum(curWaveNum + 1);
//                setWaveNums(cells[c.getX() + dx[i]][c.getY() + dy[i]], curWaveNum + 1);
//            }
//        }
    }
}

class Cell {
    
    private int x, y;
    private int wavenum;
    
    public Cell(int inx, int iny) {
        this.x = inx;
        this.y = iny;
        this.wavenum = -1;
    }
    
    public void setX(int in) {
        this.x = in;
    }
    
    public void setY(int in) {
        this.y = in;
    }
    
    public void setWaveNum(int in) {
        this.wavenum = in;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the wavenum
     */
    public int getWavenum() {
        return wavenum;
    }
}
