package homestrifeeditor.windows;

import homestrifeeditor.HSObject;
import homestrifeeditor.HSStage;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EditorWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public static String BaseWindowTitle = "Homestrife Stage Editor - ";
    public static int windowWidth = 1000;
    public static int windowHeight = 600;
    
    public String workingDirectory;
    
    public ObjectListPane holdListPane;
    public TextureObjectPane textureObjectPane;
    
    //File chooser is declared at the class stage so that it remembers last folder location..
    public static JFileChooser fileChooser;
    
    public EditorWindow() {
        workingDirectory = "";
        
        fileChooser = new JFileChooser("..");
        
        setTitle(BaseWindowTitle + "No Stage Loaded");
        setSize(windowWidth, windowHeight);
        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    	createMenuBar();
    	createWindowContents();
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
    }
    
    private void createMenuBar()
    {
        JMenuBar menuBar;
        JMenu file;
        JMenu newObject;
        JMenuItem newGraphic;
        JMenuItem newTerrain;
        JMenuItem newPhysicsObject;
        JMenuItem newFighter;
        JMenuItem generate;
        JMenuItem open;
        JMenuItem save;
        JMenuItem saveAs;
        JMenuItem importAnimation;
        JMenu edit;
        JMenuItem undo;
        JMenuItem redo;
        JMenuItem cut;
        JMenuItem copy;
        JMenuItem paste;
        JMenuItem delete;
        JMenuItem selectAll;
        JMenu object;
        JMenuItem objectAttributes;
        JMenuItem eventHolds;
        JMenu help;
        JMenuItem helpContent;
        JMenuItem about;
        
        menuBar = new JMenuBar();
        
        file = new JMenu("File");
        newObject = new JMenu("New");
        //
        newGraphic = new JMenuItem("Graphic");
        newGraphic.setActionCommand("newGraphic");
        newGraphic.addActionListener(this);
        //
        newTerrain = new JMenuItem("Terrain");
        newTerrain.setActionCommand("newTerrain");
        newTerrain.addActionListener(this);
        //
        newPhysicsObject = new JMenuItem("Physics Object");
        newPhysicsObject.setActionCommand("newPhysicsObject");
        newPhysicsObject.addActionListener(this);
        //
        newFighter = new JMenuItem("Fighter");
        newFighter.setActionCommand("newFighter");
        newFighter.addActionListener(this);
        //
        generate = new JMenuItem("Generate...");
        generate.setActionCommand("generate");
        generate.addActionListener(this);
        //
        open = new JMenuItem("Open...");
        open.setActionCommand("open");
        open.addActionListener(this);
        //
        save = new JMenuItem("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        //
        saveAs = new JMenuItem("Save As...");
        saveAs.setActionCommand("saveAs");
        saveAs.addActionListener(this);
        //
        importAnimation = new JMenuItem("Import Animation");
        importAnimation.setActionCommand("importAnimation");
        importAnimation.addActionListener(this);
        //
        newObject.add(newGraphic);
        newObject.add(newTerrain);
        newObject.add(newPhysicsObject);
        newObject.add(newFighter);
        file.add(newObject);
        file.add(generate);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(new JSeparator());
        file.add(importAnimation);
        menuBar.add(file);
        
        edit = new JMenu("Edit");
        //
        undo = new JMenuItem("Undo");
        undo.setActionCommand("undo");
        undo.addActionListener(this);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        //
        redo = new JMenuItem("Redo");
        redo.setActionCommand("redo");
        redo.addActionListener(this);
        //
        cut = new JMenuItem("Cut");
        cut.setActionCommand("cut");
        cut.addActionListener(this);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        //
        copy = new JMenuItem("Copy");
        copy.setActionCommand("copy");
        copy.addActionListener(this);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        //
        paste = new JMenuItem("Paste");
        paste.setActionCommand("paste");
        paste.addActionListener(this);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        //
        delete = new JMenuItem("Delete");
        delete.setActionCommand("delete");
        delete.addActionListener(this);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        //
        selectAll = new JMenuItem("Select All");
        selectAll.setActionCommand("selectAll");
        selectAll.addActionListener(this);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));
        //
        
        edit.add(undo);
        edit.add(redo);
        edit.add(new JSeparator());
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.add(selectAll);
        menuBar.add(edit);
        
        object = new JMenu("Object");
        objectAttributes = new JMenuItem("Attributes");
        eventHolds = new JMenuItem("Event Holds");
        objectAttributes.addActionListener(this);
        eventHolds.addActionListener(this);
        objectAttributes.setActionCommand("objectAttributes");
        eventHolds.setActionCommand("eventHolds");
        object.add(objectAttributes);
        object.add(eventHolds);
        menuBar.add(object);     
        
        help = new JMenu("Help");
        helpContent = new JMenuItem("Help Content");
        about = new JMenuItem("About");
        help.add(helpContent);
        help.add(about);
        menuBar.add(help);
        
        setJMenuBar(menuBar);
    }

    private void createWindowContents() {
    	JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createObjectListPane(), createObjectDataPane());
    	sp.setResizeWeight(.2);
        this.setContentPane(sp);
    }

	private JComponent createObjectListPane() {
        holdListPane = new ObjectListPane(this);
        return holdListPane;
	}

	private JComponent createObjectDataPane() {
		textureObjectPane = new TextureObjectPane(this);
		return textureObjectPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		switch(e.getActionCommand()) {
			case "open": open(); break;
		}
	}


	private void open() {
        int returnVal = fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            return;
        }
        
        try {
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(file);
        	doc.getDocumentElement().normalize();
        	
        	if(doc.getDocumentElement().getNodeName().compareTo("HSStage") != 0) {
            	JOptionPane.showMessageDialog(this, "Root node of Stage is not 'HSStage'", "Error", JOptionPane.ERROR_MESSAGE);  
            	return;
        	}
        	
        	HSStage loadStage = new HSStage();
        	
        	NamedNodeMap levelAttributes = doc.getDocumentElement().getAttributes();
        	if(levelAttributes.getNamedItem("gravity") != null) loadStage.gravity = Float.parseFloat(levelAttributes.getNamedItem("gravity").getNodeValue());
        	if(levelAttributes.getNamedItem("width") != null) loadStage.width = Integer.parseInt(levelAttributes.getNamedItem("width").getNodeValue());
        	if(levelAttributes.getNamedItem("height") != null) loadStage.height = Integer.parseInt(levelAttributes.getNamedItem("height").getNodeValue());
        	
        	NodeList children = doc.getDocumentElement().getChildNodes();
        	for(int i=0; i < children.getLength(); i++) {
        		Node child = children.item(i);
        		switch(child.getNodeName()) {
        		case "SpawnPoints": break; //TODO: this
        		case "Objects": 
        			NodeList objects = child.getChildNodes();
                	for(int j=0; j < objects.getLength(); j++) {
                		//Go through the objects
                		NamedNodeMap objectAttributes = objects.item(j).getAttributes();
                		if(objectAttributes == null) continue;
                		if(objectAttributes.getNamedItem("defFilePath") == null) continue;
                		HSObject hsobject = HSObject.ObjectFromDefinition(file.getParent() + File.separator, objectAttributes.getNamedItem("defFilePath").getNodeValue(), objectAttributes, this);
                		if(hsobject != null)
                			loadStage.objects.add(hsobject);
                	}
        			break;
        		}
        	}
        }
        catch(ParserConfigurationException e) {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);  
        }
        catch(SAXException e) {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "SAX Exception", JOptionPane.ERROR_MESSAGE);              
        }
        catch(IOException e) {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);              
        }
	}


	private class KeyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getID() == KeyEvent.KEY_PRESSED) {
				if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					
		        }
				else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					
		        }
				else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					
		        }
				else {
					
				}
				/*
				 * TODO: Fix Control + A
				else if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					selectAll();
				}
				*/
			}
			return false;
		}
	}
}
