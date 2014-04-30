package homestrifeeditor.windows.panes;

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
	
	public void addObject(HSObject obj) {
		HSTextureLabel texLabel = new HSTextureLabel(obj, this, new HSTexture(obj.texturePath, -obj.depth, obj.pos));
		if(texLabel.getIcon() == null) return;
		texLabel.setVisible(true);

		add(texLabel, new Double(-obj.depth));
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
			addObject(obj);
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
    
    public void setSelectedFromListPane() {
		// Update the selected textures in the pane to what is selected in the list
		int[] curSelected = parent.parent.objectListPane.objectList.getSelectedIndices();
		unselectAll();
		for(Component c : getComponents()) {
			if(!(c instanceof HSTextureLabel)) continue;
			for(int i = 0; i < curSelected.length; i++) {
				if(((HSTextureLabel)c).parentObject.equals(parent.parent.objectListPane.objectListModel.get(curSelected[i]))) {
					setSelected((HSTextureLabel) c, i != 0);
				}
			}
		}
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
        g.drawLine(this.getWidth() / 2, 0, this.getWidth() / 2, this.getHeight());
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		
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
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
