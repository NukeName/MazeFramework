/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework;

import java.awt.Point;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javax.swing.JOptionPane;
import mazeframework.interfaces.MazeCellType;

/**
 *
 * @author Heisenberg
 */
public class MazeGrid {

    private EventHandler<MouseEvent> mouseCanvasPressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            onMouseCanvasPressed(event);
        }
    };
    private EventHandler<MouseEvent> mouseCanvasDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            onMouseCanvasDragged(event);
        }
    };
    private double singleSide;
    private int sideCount;
    private Rectangle oldGoal;
    private Rectangle oldStart;
    private Rectangle[][] refGrid;
    public static final Color BORDERCOLOR = Color.DARKGRAY;
    public static final Color STARTCOLOR = Color.DARKVIOLET;
    public static final Color GOALCOLOR = Color.YELLOWGREEN;
    public static final Color EMPTYCOLOR = Color.TRANSPARENT;
    public static final Color PATHCOLOR = new Color(1, 0.56, 0.07, 0.6);
    private Group localGroup;

    public MazeGrid(Group root, int sides, double width) {
        refGrid = new Rectangle[sides][sides];
        this.singleSide = width / sides;
        this.sideCount = sides;
        Group localRoot = new Group();
        this.localGroup = localRoot;
        Group grid = new Group();
        for (int x = 0; x < sides; x++) {
            for (int y = 0; y < sides; y++) {
                Rectangle cur = createRect(x, y);
                refGrid[x][y] = cur;
                grid.getChildren().add(cur);
            }
        }


        Rectangle sceneBounds = new Rectangle();
        sceneBounds.setWidth(width);
        sceneBounds.setHeight(width);
        sceneBounds.setFill(Color.TRANSPARENT);
        sceneBounds.setOnMouseDragged(mouseCanvasDragged);
        sceneBounds.setOnMousePressed(mouseCanvasPressed);

        localRoot.getChildren().add(grid);
        localRoot.getChildren().add(sceneBounds);
        localRoot.getChildren().add(solution);

//        root.getChildren().add(grid);
//        root.getChildren().add(sceneBounds);
    }

    public MazeGrid(Rectangle og, Rectangle os, Group rectangleGroup, Rectangle[][] grid, double width) {
        sideCount = grid.length;
        oldGoal = og;
        oldStart = os;
        singleSide = width;
        this.refGrid = grid;
        Rectangle sceneBounds = new Rectangle();
        sceneBounds.setWidth(width * sideCount);
        sceneBounds.setHeight(width * sideCount);
        sceneBounds.setFill(Color.TRANSPARENT);
        sceneBounds.setOnMouseDragged(mouseCanvasDragged);
        sceneBounds.setOnMousePressed(mouseCanvasPressed);
        this.localGroup = new Group();
        localGroup.getChildren().add(rectangleGroup);
        localGroup.getChildren().add(sceneBounds);
        localGroup.getChildren().add(solution);
    }

    private Rectangle createRect(int x, int y) {
        Rectangle r = new Rectangle();
        r.setWidth(singleSide);
        r.setHeight(singleSide);
        r.setX(x * singleSide);
        r.setY(y * singleSide);
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.LIGHTGRAY);
        r.setStrokeWidth(2);
        r.setStrokeType(StrokeType.INSIDE);
        return r;
    }

    private void onMouseCanvasDragged(MouseEvent t) {
        mouseAction(t);
    }

    private void onMouseCanvasPressed(MouseEvent t) {
        mouseAction(t);
    }

    private void mouseAction(MouseEvent t) {
        Rectangle src = findByCoords(t.getX(), t.getY());
        if (src != null) {
            if (t.isShiftDown()) {
                if (t.getButton() == MouseButton.PRIMARY) {
                    if (oldStart != null) {
                        if (((Color) oldStart.getFill()).equals(STARTCOLOR)) {
                            oldStart.setFill(EMPTYCOLOR);
                        }
                    }
                    oldStart = src;
                    src.setFill(STARTCOLOR);
                } else if (t.getButton() == MouseButton.SECONDARY) {
                    if (oldGoal != null) {
                        if (((Color) oldGoal.getFill()).equals(GOALCOLOR)) {
                            oldGoal.setFill(EMPTYCOLOR);
                        }
                    }
                    oldGoal = src;
                    src.setFill(GOALCOLOR);
                }
            } else {
                if (t.getButton() == MouseButton.PRIMARY) {
                    src.setFill(BORDERCOLOR);
                } else if (t.getButton() == MouseButton.SECONDARY) {
                    src.setFill(EMPTYCOLOR);
                }
            }
        }
    }

    private Rectangle findByCoords(double x, double y) {
        int ix = (int) (x / singleSide);
        int iy = (int) (y / singleSide);
        if (inRange(0, ix, sideCount - 1) && inRange(0, iy, sideCount - 1)) {
            return refGrid[ix][iy];
        }
        return null;
    }

    private Point getGridCoords(Rectangle r) {
        for (int x = 0; x < refGrid.length; x++) {
            for (int y = 0; y < refGrid.length; y++) {
                if (refGrid[x][y].equals(r)) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private boolean inRange(int min, int n, int max) {
        return ((min <= n) && (n <= max));
    }

    public static boolean compareColors(Color c1, Color c2) {
        return ((c1.getRed() == c2.getRed()) && (c1.getBlue() == c2.getBlue()) && (c1.getGreen() == c2.getGreen()) && (c1.getOpacity() == c2.getOpacity()));
    }

    public static boolean compareColors(Rectangle r1, Color c2) {
        Color c1 = (Color) r1.getFill();
        return ((c1.getRed() == c2.getRed()) && (c1.getBlue() == c2.getBlue()) && (c1.getGreen() == c2.getGreen()) && (c1.getOpacity() == c2.getOpacity()));
    }

    public Group getGridGroup() {
        return localGroup;
    }

    public int getSideCount() {
        return sideCount;
    }

    public MazeGridDataContainer toDataContainer() {
        MazeGridDataContainer m = new MazeGridDataContainer();
        m.setRefGrid(refGrid);
        m.setSingleSide(singleSide);
        return m;
    }

    public boolean hasStartAndEnd() {
        if (oldGoal != null && oldStart != null) {
            if (compareColors(oldGoal, GOALCOLOR)) {
                if (compareColors(oldStart, STARTCOLOR)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Point getStart() {
        return getGridCoords(oldStart);
    }

    public Point getGoal() {
        return getGridCoords(oldGoal);
    }

    public MazeCellType[][] getConvertedGrid() {
        MazeCellType[][] cGrid = new MazeCellType[refGrid.length][refGrid.length];
        for (int x = 0; x < refGrid.length; x++) {
            for (int y = 0; y < refGrid.length; y++) {
                cGrid[x][y] = considerColor(refGrid[x][y]);
            }
        }
        return cGrid;
    }

    private MazeCellType considerColor(Rectangle r) {
        if (compareColors(r, EMPTYCOLOR)) {
            return MazeCellType.EMPTY;
        } else if (compareColors(r, BORDERCOLOR)) {
            return MazeCellType.BORDER;
        } else if (compareColors(r, GOALCOLOR)) {
            return MazeCellType.GOAL;
        } else {
            //STARTCOLOR
            return MazeCellType.START;
        }
    }
    
    private Group solution = new Group();
    public void overlaySolution(List<Point> res, long timeStamp) {
        if (res != null) {
            solution.getChildren().clear();
            double iterations = 0;
            Iterator<Point> iter = res.iterator();
            while (iter.hasNext()) {
                Point p = iter.next();
                Rectangle r = new Rectangle();
                r.setWidth(singleSide);
                r.setHeight(singleSide);
                r.setX(p.getX() * singleSide);
                r.setY(p.getY() * singleSide);
                Color c = new Color(STARTCOLOR.getRed(),STARTCOLOR.getGreen(),STARTCOLOR.getBlue(),STARTCOLOR.getOpacity());
                Color inter = c.interpolate(GOALCOLOR, iterations/res.size());
                Color mod  = new Color(inter.getRed(),inter.getGreen(), inter.getBlue(), 0.6);
                r.setFill(mod);
                r.setMouseTransparent(true);
                solution.getChildren().add(r);
                iterations++;
            }
            JOptionPane.showMessageDialog(null, "Path Point Length: "+res.size()+"\n Timediff: "+(System.currentTimeMillis()-timeStamp)
                    , "Successful Completion!", JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            JOptionPane.showMessageDialog(null, "Presumably no path was found", "Null Algorithm Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
}
