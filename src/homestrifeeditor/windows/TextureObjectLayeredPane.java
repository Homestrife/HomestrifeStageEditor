package homestrifeeditor.windows;

import homestrifeeditor.HSTexture;
import homestrifeeditor.HSTextureLabel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class TextureObjectLayeredPane extends JLayeredPane implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	public TextureObjectPane parent;
	public ArrayList<JLabel> selectedItems;
	
	public TextureObjectLayeredPane(TextureObjectPane theParent) {
		parent = theParent;
		selectedItems = new ArrayList<JLabel>();
	}
    
    public void addTexture(HSTexture tex, boolean addToHold)
    {
        HSTextureLabel texLabel = new HSTextureLabel(this, tex);
        if(texLabel.getIcon() == null) { return; }
        //texLabel.setVisible(showTextures);
        
        add(texLabel, new Integer(tex.depth));
        
        //if(addToHold)
            //parent.hold.textures.add(tex);
        
        //setSelected(texLabel, false);
    }
    
    public void addTexture(String path) {        
        HSTexture newTex = new HSTexture(path);
        
        //moveAllTextureDepthsDown();
        newTex.depth = 0;
        newTex.offset.x = 0;
        newTex.offset.y = 0;
        
        //addTexture(newTex);
    }

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
