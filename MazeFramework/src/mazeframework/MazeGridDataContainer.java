/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework;

import java.io.Serializable;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author Gintoki
 */
public class MazeGridDataContainer implements Serializable {

    private double singleSide;
    private ColorRef[][] colRef;

    public void setSingleSide(double width) {
        singleSide = width;
    }

    public void setRefGrid(Rectangle[][] ref) {
        colRef = new ColorRef[ref.length][ref.length];
        for (int i = 0; i < ref.length; i++) {
            for (int k = 0; k < ref.length; k++) {
                colRef[i][k] = new ColorRef((Color) ref[i][k].getFill());
            }
        }
    }

    public MazeGrid reconstructMazeGrid() {
        Group grid = new Group();
        Rectangle oS = null;
        Rectangle oG = null;
        Rectangle[][] rects = new Rectangle[colRef.length][colRef.length];
        for (int i = 0; i < colRef.length; i++) {
            for (int k = 0; k < colRef.length; k++) {
                Rectangle r = new Rectangle();
                Color c = colRef[i][k].reconstructColor();
                r.setWidth(singleSide);
                r.setHeight(singleSide);
                r.setX(i * singleSide);
                r.setY(k * singleSide);
                r.setFill(c);
                r.setStroke(Color.LIGHTGRAY);
                r.setStrokeWidth(2);
                r.setStrokeType(StrokeType.INSIDE);
                rects[i][k] = r;
                if(MazeGrid.compareColors(c, MazeGrid.GOALCOLOR)) {
                    oG = r;
                } else if (MazeGrid.compareColors(c, MazeGrid.STARTCOLOR)) {
                    oS = r;
                }
                grid.getChildren().add(r);
            }
        }
        return new MazeGrid(oG, oS, grid, rects, singleSide);
    }
}

class ColorRef implements Serializable {

    private double red;
    private double green;
    private double blue;
    private double alpha;

    public ColorRef(Color col) {
        setColor(col);
    }

    public final void setColor(Color col) {
        red = col.getRed();
        green = col.getGreen();
        blue = col.getBlue();
        alpha = col.getOpacity();
    }

    public Color reconstructColor() {
        return new Color(red, green, blue, alpha);
    }
}
