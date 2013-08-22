package homestrifeeditor;

import homestrifeeditor.windows.TextureObjectLayeredPane;

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
		moveBox =false;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
