package homestrifeeditor.windows;

import homestrifeeditor.HSStage;
import homestrifeeditor.HSVect2D;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.ScrollPaneConstants;

public class TextureObjectPane  extends JPanel implements ActionListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	public static int textureObjectPaneWidth = 1920 * 8;
    public static int textureObjectPaneHeight = 1080 * 8;
    
    public EditorWindow parent;
    public HSStage stage;
    
    public TextureObjectLayeredPane textureObjectLayeredPane;
    public JScrollPane textureObjectScrollPane;
    public JToolBar textureObjectToolBar;
    
    public ArrayList<JLabel> clipboard;
    
    public TextureObjectPane(EditorWindow theParent) {
    	parent = theParent;
    	stage = null;
    	clipboard = new ArrayList<JLabel>();
    	
    	createPaneContents();
    	resetScrollBars();
    }
    
    public HSVect2D getHSOffset(int x, int y)
    {
        HSVect2D point = new HSVect2D();
        point.x = x - textureObjectPaneWidth/2;
        point.y = y - textureObjectPaneHeight/2;
        
        return point;
    }
	
    public Point getSwingOffset(float x, float y)
    {
        Point point = new Point(textureObjectPaneWidth/2 + (int)x, textureObjectPaneHeight/2 + (int)y);
        
        return point;
    }
    
    public void resetScrollBars()
    {
        textureObjectScrollPane.revalidate();
        
        float horMin = textureObjectScrollPane.getHorizontalScrollBar().getMinimum();
        float horMax = textureObjectScrollPane.getHorizontalScrollBar().getMaximum();
        float horMid = (horMax - horMin) / (float)2.5;
        float verMin = textureObjectScrollPane.getVerticalScrollBar().getMinimum();
        float verMax = textureObjectScrollPane.getVerticalScrollBar().getMaximum();
        float verMid = (verMax - verMin) / (float)2.5;
        textureObjectScrollPane.getHorizontalScrollBar().setValue((int)horMid);
        textureObjectScrollPane.getVerticalScrollBar().setValue((int)verMid);
    }

	private void createPaneContents() {
		textureObjectLayeredPane = new TextureObjectLayeredPane(this);
		textureObjectLayeredPane.setOpaque(true);
		textureObjectLayeredPane.setBackground(Color.magenta);
		textureObjectLayeredPane.setPreferredSize(new Dimension(textureObjectPaneWidth, textureObjectPaneHeight));
		textureObjectScrollPane = new JScrollPane(textureObjectLayeredPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		textureObjectScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		
		setLayout(new BorderLayout());
		add(textureObjectScrollPane);
	}

	public void setStage(HSStage stage) {
		textureObjectLayeredPane.setStage(stage);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
