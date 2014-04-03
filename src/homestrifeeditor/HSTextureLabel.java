package homestrifeeditor;

import homestrifeeditor.windows.EditorWindow;
import homestrifeeditor.windows.TextureObjectLayeredPane;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

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
        
        texture.offset.x = texture.offset.x + xDiff;
        texture.offset.y = texture.offset.y + yDiff;

        Point pos = parent.parent.getSwingOffset(texture.offset.x * EditorWindow.scale, texture.offset.y * EditorWindow.scale);
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
        
        //Now here we want to make it so we select multiple objects in the list if we have selected multiple textures
        ArrayList<Integer> toSelectList = new ArrayList<>();
        DefaultListModel<HSObject> model = parent.parent.parent.objectListPane.objectListModel;
        JList<HSObject> list = parent.parent.parent.objectListPane.objectList;
        for(JLabel sel : parent.selectedItems) {
        	if(!(sel instanceof HSTextureLabel)) continue;
        	for(int i=0; i < model.getSize(); i++) {
        		if(((HSTextureLabel)sel).parentObject.equals(model.get(i))) {
        			toSelectList.add(i);
        		}
        	}
        }
        int[] toSelectArray = new int[toSelectList.size()];
        for(int i=0; i < toSelectList.size(); i++) {
        	toSelectArray[i] = toSelectList.get(i);
        }
        list.setSelectedIndices(toSelectArray);
        
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
