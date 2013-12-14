/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework.interfaces;

import mazeframework.algorithms.AStarAlgorithm;
import mazeframework.algorithms.StubAlgorithm;

/**
 *
 * @author Gintoki
 */
public class MazeGlobals {
    public static Pathfinder algorithm = new AStarAlgorithm();
}
