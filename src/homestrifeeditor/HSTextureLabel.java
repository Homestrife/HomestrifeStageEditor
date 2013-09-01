package homestrifeeditor;

import homestrifeeditor.windows.TextureObjectLayeredPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class HSTextureLabel extends JLabel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	public TextureObjectLayeredPane parent;
	public HSTexture texture;
	public ImageIcon icon;
	
	private int mouseStartX;
	private int mouseStartY;
	
	private int mouseMoveThreshold;
	private boolean moveBox;
	
	public HSTextureLabel(TextureObjectLayeredPane theParent, HSTexture theTexture) {
		super();
		setName("texture");
		parent = theParent;
		texture = theTexture;
		mouseStartX = 0;
		mouseStartY = 0;
		mouseMoveThreshold = 0;
		moveBox = false;
		loadIcon();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public HSTextureLabel(HSTextureLabel l) {
    	//Woo deep copy
    	this(l.parent, l.texture);
    	setBounds(l.getBounds());
    	texture = new HSTexture(texture);
    }
    
    private void loadIcon() {
    	icon = TGAReader.loadTGA(texture.filePath, "");
    		
        setIcon(icon);
        setText("");
        setName("texture");
        setMinimumSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        setMaximumSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        Point pos = parent.parent.getSwingOffset(texture.offset.x, texture.offset.y);
        setBounds(pos.x, pos.y, icon.getIconWidth(), icon.getIconHeight());		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(!parent.selectedItems.contains(this)) { return; }
        //if(locked) return;
        Component c = e.getComponent();
        
        if(c == null) { return ; }
        
        int xDiff = e.getX() - mouseStartX;
        int yDiff = e.getY() - mouseStartY;
        
        if(Math.abs(xDiff) >= mouseMoveThreshold || Math.abs(yDiff) >= mouseMoveThreshold)
        {
            moveBox = true;
        }
        
        if(!moveBox) { return; }
        
        texture.offset.x = texture.offset.x + xDiff;
        texture.offset.y = texture.offset.y + yDiff;
        
        Point pos = parent.parent.getSwingOffset(texture.offset.x, texture.offset.y);
        setBounds(pos.x, pos.y, icon.getIconWidth(), icon.getIconHeight());
        
        parent.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
        if(e.getClickCount() == 2)
        {
            //Double click
        }
        else if(e.getClickCount() == 1)
        {
            boolean multiSelect = false;
            if(e.isControlDown())
            {
                if(parent.selectedItems.contains(this))
                {
                    //parent.unselect(this);
                    return;
                }
                multiSelect = true;
            }
            //parent.setSelected(this, multiSelect);
            mouseStartX = e.getX();
            mouseStartY = e.getY();
        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		moveBox = false;
	}

}
