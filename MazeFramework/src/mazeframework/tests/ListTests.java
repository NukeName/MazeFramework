/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeframework.tests;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Gintoki
 */
public class ListTests {
    public ListTests() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(6);
        
        Iterator<Integer> iter = list.iterator();
        while(iter.hasNext()) {
            int a = iter.next();
            System.out.println(a);
        }
        
        
    }
}
