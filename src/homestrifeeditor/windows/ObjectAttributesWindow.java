/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.HSObject;
import homestrifeeditor.HSTextureLabel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreePath;

/**
 * The attributes of the currently selected object
 * @author Darlos9D
 */
public class ObjectAttributesWindow extends JFrame implements ActionListener, ChangeListener, DocumentListener, ItemListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 400;
    private static int windowHeightGeneral = 200;
    private static int windowBorderBuffer = 10;
    
    public ObjectListPane parent;
    private HSObject object;
    
    private JButton applyButton;
    
    private JLabel parallaxDepthLabel;
    private JSpinner parallaxDepthSpinner; private static String parallaxDepthTooltip = "<html>The higher the number, the farther away from the camera it is</html>";
    
    public ObjectAttributesWindow(ObjectListPane theParent, HSObject theObject)
    {
        parent = theParent;
        object = theObject;
        
        setTitle("Object Attributes - " + object.name);
        setSize(windowWidth, windowHeightGeneral);
        setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
       
        createWindowContents();
    }
    
    private void createWindowContents()
    {
    	parallaxDepthLabel = new JLabel("Parallax Depth");
    	parallaxDepthLabel.setToolTipText(parallaxDepthTooltip);
    	parallaxDepthSpinner = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 1.0));
    	parallaxDepthSpinner.setToolTipText(parallaxDepthTooltip);
    	parallaxDepthSpinner.addChangeListener(this);
    	parallaxDepthSpinner.setValue(object.depth);
    	parallaxDepthSpinner.setEnabled(true);
    	
    	JPanel dataPane = new JPanel(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	
    	gbc.weightx = .5;
    	
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	dataPane.add(parallaxDepthLabel, gbc);
    	
    	gbc.gridx = 1;
    	gbc.gridy = 0;
    	dataPane.add(parallaxDepthSpinner, gbc);
    	
    	setContentPane(dataPane);
    }
    
    public void fieldChanged()
    {
    	object.depth = (double) parallaxDepthSpinner.getValue();

    	for(Component c : parent.parent.textureObjectPane.textureObjectLayeredPane.getComponents()) {
    		HSTextureLabel texLabel = ((HSTextureLabel)c);
    		if(texLabel.parentObject == object) {
    			parent.parent.textureObjectPane.textureObjectLayeredPane.remove(texLabel);
    			texLabel.texture.depth = -object.depth;
    			parent.parent.textureObjectPane.textureObjectLayeredPane.add(texLabel, object.depth);
    			parent.parent.textureObjectPane.textureObjectLayeredPane.repaint();
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
