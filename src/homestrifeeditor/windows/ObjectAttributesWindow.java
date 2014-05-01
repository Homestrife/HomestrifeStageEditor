/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.HSObject;
import homestrifeeditor.HSTextureLabel;
import homestrifeeditor.windows.panes.ObjectListPane;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * The attributes of the currently selected object
 * @author Darlos9D
 */
public class ObjectAttributesWindow extends JDialog implements ActionListener, ChangeListener, DocumentListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 400;
    private static int windowHeightGeneral = 200;
    
    public ObjectListPane parent;
    private HSObject object;
    
    //private JButton applyButton;
    
    private JLabel parallaxDepthLabel;
    private JSpinner parallaxDepthSpinner;
    private static String parallaxDepthTooltip = "<html>The higher the number, the farther away from the camera it is</html>";
    
    private JLabel xLabel, yLabel;
    private JSpinner xSpinner, ySpinner;
    private static String positionTooltip = "<html>Set the position of the object precisely</html>";
    
    
    public ObjectAttributesWindow(ObjectListPane theParent, HSObject theObject)
    {
        parent = theParent;
        object = theObject;
        
        setTitle("Object Attributes - " + object.name);
        setSize(windowWidth, windowHeightGeneral);
        setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        
        // Now you can't click past the window
        this.setModal(true);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
       
        createWindowContents();
    }
    
    private void createWindowContents()
    {
    	JPanel dataPane = new JPanel(new GridLayout(0, 2, 10, 10));
    	
    	xLabel = new JLabel("X Position");
    	xLabel.setToolTipText(positionTooltip);
    	dataPane.add(xLabel);

    	xSpinner = new JSpinner(new SpinnerNumberModel(0.0, -10000.0, 10000.0, 1.0));
    	xSpinner.setToolTipText(positionTooltip);
    	xSpinner.addChangeListener(this);
    	xSpinner.setValue((double)object.pos.x);
    	xSpinner.setEnabled(true);
    	dataPane.add(xSpinner);
    	
    	yLabel = new JLabel("Y Position");
    	yLabel.setToolTipText(positionTooltip);
    	dataPane.add(yLabel);

    	ySpinner = new JSpinner(new SpinnerNumberModel(0.0, -10000.0, 10000.0, 1.0));
    	ySpinner.setToolTipText(positionTooltip);
    	ySpinner.addChangeListener(this);
    	ySpinner.setValue((double)object.pos.y);
    	ySpinner.setEnabled(true);
    	dataPane.add(ySpinner);
    	
    	parallaxDepthLabel = new JLabel("Parallax Depth");
    	parallaxDepthLabel.setToolTipText(parallaxDepthTooltip);
    	dataPane.add(parallaxDepthLabel);
    	
    	parallaxDepthSpinner = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 1.0));
    	parallaxDepthSpinner.setToolTipText(parallaxDepthTooltip);
    	parallaxDepthSpinner.addChangeListener(this);
    	parallaxDepthSpinner.setValue(object.depth);
    	parallaxDepthSpinner.setEnabled(true);
    	dataPane.add(parallaxDepthSpinner);
    	
    	setContentPane(dataPane);
    }
    
    public void fieldChanged()
    {
    	if(parallaxDepthSpinner != null)
    		object.depth = (double) parallaxDepthSpinner.getValue();

    	if(xSpinner != null)
    		object.pos.x = ((Double)xSpinner.getValue()).floatValue();
    	if(ySpinner != null)
    		object.pos.y = ((Double)ySpinner.getValue()).floatValue();

    	for(Component c : parent.parent.textureObjectPane.textureObjectLayeredPane.getComponents()) {
    		HSTextureLabel texLabel = ((HSTextureLabel)c);
    		if(texLabel.parentObject == object) {
    			parent.parent.textureObjectPane.textureObjectLayeredPane.remove(texLabel);
    			texLabel.texture.depth = -object.depth;
    			//texLabel.texture.offset.x = object.offsetX;
    			//texLabel.texture.offset.y = object.offsetY;
    			//texLabel.setBounds((int)(object.offsetX * EditorWindow.scale), (int)(object.offsetY * EditorWindow.scale), (int)(texLabel.getWidth() * EditorWindow.scale), (int)(texLabel.getHeight() * EditorWindow.scale));
    			parent.parent.textureObjectPane.textureObjectLayeredPane.add(texLabel, object.depth);
    			parent.parent.textureObjectPane.textureObjectLayeredPane.repaint();
    			texLabel.updatePos();
    			break;
    		}
    	}
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "fieldChanged": fieldChanged(); break;
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void removeUpdate(DocumentEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void insertUpdate(DocumentEvent e)
    {
        fieldChanged();
    }
    
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        fieldChanged();
    }
}
