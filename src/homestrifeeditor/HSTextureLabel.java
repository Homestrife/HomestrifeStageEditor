package homestrifeeditor;

import homestrifeeditor.windows.EditorWindow;
import homestrifeeditor.windows.panes.TextureObjectLayeredPane;

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
    public HSObject parentObject;
	public ImageIcon icon = null;
	public ImageIcon originalIcon = null;
	
	private int mouseStartX;
	private int mouseStartY;
	
	private int mouseMoveThreshold;
	private boolean moveBox;
	
	public HSTextureLabel(HSObject theParentObject, TextureObjectLayeredPane theParent, HSTexture theTexture) {
		super();
		setName("texture");
		parent = theParent;
		parentObject = theParentObject;
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
    	this(l.parentObject, l.parent, l.texture);
    	setBounds(l.getBounds());
    	texture = new HSTexture(texture);
    }
    
    public void loadIcon() {
    	if(originalIcon == null) {
    		originalIcon = TGAReader.loadTGA(texture.filePath, "");
    	}
    	
    	if(originalIcon == null) 
    		return;
    	
    	icon = EditorWindow.resize(originalIcon);
    	if(icon == null) {
    		return;
    	}

        setIcon(icon);
        setText("");
        setName("texture");
        
    	Point pos = parent.parent.getSwingOffset(texture.offset.x * EditorWindow.scale, texture.offset.y * EditorWindow.scale);
    	setMinimumSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        setMaximumSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        setBounds(pos.x, pos.y, icon.getIconWidth(), icon.getIconHeight());	
	}
    
    public void updatePos() {
    	updatePos((int)parentObject.pos.x, (int)parentObject.pos.y);
    }
    
    public void updatePos(int nx, int ny) {
    	parentObject.pos.x = nx;
    	parentObject.pos.y = ny;
    	Point pos = parent.parent.getSwingOffset(parentObject.pos.x * EditorWindow.scale, parentObject.pos.y * EditorWindow.scale);
   
        setBounds(pos.x, pos.y, icon.getIconWidth(), icon.getIconHeight());	
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		if(!parent.selectedItems.contains(this)) { return; }
        //if(locked) return;
        Component c = e.getComponent();
        
        if(c == null) { return ; }
        
        float xDiff = e.getX() - mouseStartX;
        float yDiff = e.getY() - mouseStartY;
        
        if(Math.abs(xDiff) >= mouseMoveThreshold || Math.abs(yDiff) >= mouseMoveThreshold)
        {
            moveBox = true;
        }
        
        if(!moveBox) { return; }
        
        parentObject.pos.x = parentObject.pos.x + xDiff;
        parentObject.pos.y = parentObject.pos.y + yDiff;

        Point pos = parent.parent.getSwingOffset(parentObject.pos.x * EditorWindow.scale, parentObject.pos.y * EditorWindow.scale);
        setBounds(pos.x, pos.y, icon.getIconWidth(), icon.getIconHeight());
        
        parent.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
        parent.parent.parent.objectListPane.objectList.clearSelection();
        if(e.getClickCount() == 1)
        {
            boolean multiSelect = false;
            boolean shouldSelect = true;
            if(e.isControlDown())
            {
                if(parent.selectedItems.contains(this))
                {
                    parent.unselect(this);
                    shouldSelect = false;
                }
                else {
                	multiSelect = true;
                }
            }
            if(shouldSelect) {
	            parent.setSelected(this, multiSelect);
	            mouseStartX = e.getX();
	            mouseStartY = e.getY();
            }
        }
        
        parent.parent.parent.objectListPane.setSelectedFromTexturePane();
        
    	if(e.getClickCount() == 2)
        {
        	parent.parent.parent.objectListPane.editObjectButtonPressed();
        }
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		moveBox = false;
	}

}
