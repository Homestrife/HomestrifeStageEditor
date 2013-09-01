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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EditorWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	public static String BaseWindowTitle = "Homestrife Stage Editor - ";
    public static int windowWidth = 1000;
    public static int windowHeight = 600;
    
    public File currentFile;
    
    public ObjectListPane objectListPane;
    public TextureObjectPane textureObjectPane;
    
    public HSStage currentlyLoadedStage;
    
    //File chooser is declared at the class stage so that it remembers last folder location..
    public static JFileChooser fileChooser;
    
    public EditorWindow() {
        currentFile = null;
        
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
        JMenuItem newStage;
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
        newStage = new JMenuItem("New");
        newStage.setActionCommand("new");
        newStage.addActionListener(this);
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
        file.add(newStage);
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
    	sp.setResizeWeight(.05);
        this.setContentPane(sp);
    }
    
    public void setCurrentlyLoadedStage(HSStage newStage)
    {
        currentlyLoadedStage = newStage;
        setTitle(BaseWindowTitle + currentlyLoadedStage.name);
        textureObjectPane.setStage(currentlyLoadedStage);
        objectListPane.loadStageObjects(currentlyLoadedStage);
    }

	private JComponent createObjectListPane() {
		objectListPane = new ObjectListPane(this);
        return objectListPane;
	}

	private JComponent createObjectDataPane() {
		textureObjectPane = new TextureObjectPane(this);
		return textureObjectPane;
	}

	public void newObject() {
		textureObjectPane.setStage(currentlyLoadedStage);
		textureObjectPane.resetScrollBars();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		switch(e.getActionCommand()) {
		case "new": newStage(); break;
		case "open": open(); break;
		case "save": save(); break;
		case "saveAs": saveAs(); break;
		}
	}

	private void newStage() {
		currentlyLoadedStage = new HSStage();
		currentFile = null;
		setCurrentlyLoadedStage(currentlyLoadedStage);
	}

	private void saveAs() {
        if(currentlyLoadedStage == null) {
        	JOptionPane.showMessageDialog(this, "No Stage loaded", "Whoops", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showSaveDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        } else {
            return;
        }

        currentFile = file;
        
        //Now that we have a working directory, we can save
        save();
	}

	private void save() {
        //If we don't have a working directory or a loaded object, we should save as instead (save as can handle the lack of a loaded object as well)
        if(currentlyLoadedStage == null || currentFile == null) { saveAs(); }
        
        //if(!currentFile.exists()) { return; }
        
        createDefinitionFile();
	}

	private void createDefinitionFile() {
		if(currentlyLoadedStage == null) { return; }
		
		try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            Element root = doc.createElement("HSStage");
            root.setAttribute("gravity", "" + currentlyLoadedStage.gravity);
            root.setAttribute("width", "" + currentlyLoadedStage.width);
            root.setAttribute("height", "" + currentlyLoadedStage.height);
            
            //TODO spawn points
            
            Element objects = doc.createElement("Objects");
            for(HSObject obj : currentlyLoadedStage.objects) {
            	Element object = doc.createElement("Object");
            	object.setAttribute("defFilePath", obj.defPath);
            	object.setAttribute("posX", "" + obj.pos.x);
            	object.setAttribute("posY", "" + obj.pos.y);
            	object.setAttribute("depth", "" + obj.depth);
            	objects.appendChild(object);
            }
            root.appendChild(objects);
            doc.appendChild(root);
            
            //Save the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(currentFile);
            transformer.transform(source, result);
		}
        catch(ParserConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);  
            
        }
        catch(TransformerConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Transformer Configuration Exception", JOptionPane.ERROR_MESSAGE);
        }
        catch(TransformerException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Transformer Exception", JOptionPane.ERROR_MESSAGE);   
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
        	
        	setCurrentlyLoadedStage(loadStage);
            currentFile = file;
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
