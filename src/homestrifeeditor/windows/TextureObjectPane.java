package homestrifeeditor.windows;

import homestrifeeditor.HSStage;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

public class TextureObjectPane  extends JPanel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	public static int textureObjectPaneWidth = 1920 * 4;
    public static int textureObjectPaneHeight = 1080 * 4;
    
    public EditorWindow parent;
    public HSStage stage;
    
    public TextureObjectLayeredPane textureObjectLayeredPane;
    public JScrollPane textureObjectScrollPane;
    public JToolBar textureObjectToolBar;
    
    //TODO: Copy/Paste
    public ArrayList<JLabel> clipboard;
    
    public TextureObjectPane(EditorWindow theParent) {
    	parent = theParent;
    	stage = null;
    	clipboard = new ArrayList<JLabel>();
    	
    	createPaneContents();
    }
    

	private void createPaneContents() {
		
	}
	
    public Point getSwingOffset(float x, float y)
    {
        Point point = new Point(textureObjectPaneWidth/2 + (int)x, textureObjectPaneHeight/2 + (int)y);
        
        return point;
    }

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
