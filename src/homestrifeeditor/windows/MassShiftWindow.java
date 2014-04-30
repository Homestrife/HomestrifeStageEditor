/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor.windows;

import homestrifeeditor.windows.panes.ObjectListPane;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Darlos9D
 */
public class MassShiftWindow extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 150;
    private static int windowHeight = 140;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 650;
    private static int gridRowHeight = 45;
    private static int gridColumns = 2;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
    
    private ObjectListPane parent;
    
    private JSpinner offsetXSpinner;
    private JSpinner offsetYSpinner;
    
    private int prevOffsetX;
    private int prevOffsetY;
    
    public MassShiftWindow(ObjectListPane theParent)
    {
        parent = theParent;
        prevOffsetX = 0;
        prevOffsetY = 0;
        
        setTitle("Multi-Object Shift");
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        
        createWindowContents();
    }
    
    private void createWindowContents()
    {
        JLabel offsetXLabel = new JLabel("Offset X");
        offsetXSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        offsetXSpinner.setValue(0);
        offsetXSpinner.addChangeListener(this);
        
        JLabel offsetYLabel = new JLabel("Offset Y");
        offsetYSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        offsetYSpinner.setValue(0);
        offsetYSpinner.addChangeListener(this);
        
        JPanel valueInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
        valueInterface.setSize(gridWidth, gridRowHeight * 2);
        valueInterface.add(offsetXLabel);
        valueInterface.add(offsetXSpinner);
        valueInterface.add(offsetYLabel);
        valueInterface.add(offsetYSpinner);
        
        JButton closeButton = new JButton("Close");
        closeButton.setActionCommand("closeButton");
        closeButton.addActionListener(this);
        
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonPane.add(closeButton);
        
        JPanel massShiftPane = new JPanel();
        massShiftPane.setLayout(new BoxLayout(massShiftPane, BoxLayout.Y_AXIS));
        massShiftPane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        massShiftPane.add(valueInterface);
        massShiftPane.add(buttonPane);
        
        add(massShiftPane);
    }
    
    private void closeWindow()
    {
        this.dispose();
    }
    
    private void fieldChanged()
    {
        int xDiff = (int)offsetXSpinner.getValue() - prevOffsetX;
        int yDiff = (int)offsetYSpinner.getValue() - prevOffsetY;
        
        parent.massShift(xDiff, yDiff);
        
        prevOffsetX += xDiff;
        prevOffsetY += yDiff;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "closeButton": closeWindow(); break;
        }
    }
    
    @Override
    public void stateChanged(ChangeEvent e)
    {
        fieldChanged();
    }
}
