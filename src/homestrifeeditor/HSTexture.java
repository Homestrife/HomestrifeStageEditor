/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

/**
 *
 * @author Darlos9D
 */
public class HSTexture {
    public HSVect2D offset;
    public int depth;
    public String filePath;
    
    public HSTexture (HSObject theParent, String theFilePath)
    {
        offset = new HSVect2D();
        depth = 0;
        filePath = theFilePath;
    }
    
    public HSTexture(String theFilePath, int theDepth, HSVect2D theOffset) {
    	offset = theOffset;
    	depth = theDepth;
    	filePath = theFilePath;
    }
    
    public HSTexture(HSTexture t) {
    	//Deep copy
    	offset = new HSVect2D(t.offset);
    	depth = t.depth;
    	filePath = new String(t.filePath);
    }
}
