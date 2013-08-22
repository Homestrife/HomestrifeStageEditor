package homestrifeeditor.windows;

import homestrifeeditor.HSLevel;

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
    public HSLevel level;
    
    public TextureObjectLayeredPane textureObjectLayeredPane;
    public JScrollPane textureObjectScrollPane;
    public JToolBar textureObjectToolBar;
    
    //TODO: Copy/Paste
    public ArrayList<JLabel> clipboard;
    
    public TextureObjectPane(EditorWindow theParent) {
    	parent = theParent;
    	level = null;
    	clipboard = new ArrayList<JLabel>();
    	
    	createPaneContents();
    }
    

	private void createPaneContents() {
		
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
