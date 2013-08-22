/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class HSVect2D {
    public float x;
    public float y;
    
    public HSVect2D ()
    {
        x = 0;
        y = 0;
    }
    
    public HSVect2D(HSVect2D v) {
    	//Thats one deep copy
    	x = v.x;
    	y = v.y;
    }
}
