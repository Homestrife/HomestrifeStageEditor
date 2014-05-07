package homestrifeeditor.windows;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import homestrifeeditor.HSStage;

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

public class StagePropertiesWindow extends JFrame implements ChangeListener {
	private static final long serialVersionUID = 1L;
	
	private static int windowWidth = 200;
    private static int windowHeight = 100;
    private static int windowBorderBuffer = 10;
    
    private static int gridWidth = 650;
    private static int gridRowHeight = 45;
    private static int gridColumns = 2;
    private static int gridHorizontalGap = 10;
    private static int gridVerticalGap = 5;
	
	private EditorWindow parent;
	private HSStage stage;
    
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
	
	public StagePropertiesWindow(EditorWindow editorWindow, HSStage currentlyLoadedStage) {
		parent = editorWindow;
		stage = currentlyLoadedStage;

        setTitle("Stage Properties");
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
		
		createWindowContents();
	}

	private void createWindowContents() {
        JLabel widthLabel = new JLabel("Width");
        widthSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        widthSpinner.setValue(stage.width);
        widthSpinner.addChangeListener(this);
        
        JLabel heightLabel = new JLabel("Height");
        heightSpinner = new JSpinner(new SpinnerNumberModel(0, -99999, 99999, 1));
        heightSpinner.setValue(stage.height);
        heightSpinner.addChangeListener(this);
        
        JPanel valueInterface = new JPanel(new GridLayout(2, gridColumns, gridHorizontalGap, gridVerticalGap));
        valueInterface.setSize(gridWidth, gridRowHeight * 2);
        valueInterface.add(widthLabel);
        valueInterface.add(widthSpinner);
        valueInterface.add(heightLabel);
        valueInterface.add(heightSpinner);
        
        JPanel propertiesPane = new JPanel();
        propertiesPane.setLayout(new BoxLayout(propertiesPane, BoxLayout.Y_AXIS));
        propertiesPane.setBorder(new EmptyBorder(windowBorderBuffer, windowBorderBuffer, windowBorderBuffer, windowBorderBuffer));
        propertiesPane.add(valueInterface);
		
		add(propertiesPane);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(widthSpinner != null) {
			stage.width = (int) widthSpinner.getValue();
		}
		if(heightSpinner != null) {
			stage.height = (int) heightSpinner.getValue();
		}
		parent.textureObjectPane.textureObjectLayeredPane.repaint();
	}
}
