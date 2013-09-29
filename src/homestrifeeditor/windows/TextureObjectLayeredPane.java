package homestrifeeditor.windows;

import homestrifeeditor.HSObject;
import homestrifeeditor.HSStage;
import homestrifeeditor.HSTexture;
import homestrifeeditor.HSTextureLabel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
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
    
    public void removeSelected() {
    	for(JLabel sel : selectedItems) {
    		remove(sel);
    		parent.parent.currentlyLoadedStage.objects.remove(((HSTextureLabel)sel).parentObject);
    		parent.parent.objectListPane.removeObjectFromList(((HSTextureLabel)sel).parentObject);
    		parent.parent.objectListPane.repaint();
    	}
    	selectedItems.clear();
    	repaint();
    }

	public void setStage(HSStage stage) {
		removeAll();
		for(HSObject obj : stage.objects) {
			HSTextureLabel texLabel = new HSTextureLabel(obj, this, new HSTexture(obj.texturePath, -obj.depth, obj.pos));
			if(texLabel.getIcon() == null) return;
			texLabel.setVisible(true);

			add(texLabel, new Integer(-obj.depth));
		}
		repaint();
	}
    
    public void unselect(JLabel label)
    {
        if(label.getName().compareTo("texture") == 0)
        {
            label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
        
        selectedItems.remove(label);
        
        repaint();
    }
    
    public void unselectAll()
    {
        for(Component c : getComponents())
        {
            if(c.getName().compareTo("texture") == 0)
            {
                ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            }
        }
        
        selectedItems.clear();
        
        repaint();
    }
    
    public void setSelected(JLabel selectedLabel, boolean multiSelect)
    {
        if(!selectedLabel.isVisible()) { return; }
        
        if(!multiSelect)
        {
            unselectAll();
        }
        
        selectedItems.add(selectedLabel);
        selectedLabel.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
        g.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
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
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
