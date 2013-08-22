/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import homestrifeeditor.HSVect2D;

/**
 *
 * @author Darlos9D
 */
public class HSBox {
    public float height;
    public float width;
    public HSVect2D offset;
    public int depth;
    
    public HSBox()
    {
        height = 0;
        width = 0;
        offset = new HSVect2D();
        depth = 0;
    }
    
    public HSBox(HSBox b) {
    	//deep copy
    	height = b.height;
    	width = b.width;
    	offset = new HSVect2D(b.offset);
    	depth = b.depth;
    }
}
