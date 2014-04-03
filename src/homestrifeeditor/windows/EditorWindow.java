package homestrifeeditor.windows;

import homestrifeeditor.HSObject;
import homestrifeeditor.HSStage;
import homestrifeeditor.HSTextureLabel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EditorWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
    //0 - current
	public static final int XML_FORMAT_VERSION = 0;
	
	public static String BaseWindowTitle = "Homestrife Stage Editor - ";
    public static int windowWidth = 1000;
    public static int windowHeight = 600;
    
    public static float scale = .1f;
    
    public String workingDirectory = "";
    public String exeDirectory = "";
    public String fileChooserDirectory = "";
    public File curFile;
    
    public ObjectListPane objectListPane;
    public TextureObjectPane textureObjectPane;
    
    public HSStage currentlyLoadedStage;
    
    public static JFileChooser fileChooser;
    
    public EditorWindow() {    
    	curFile = null;
    	
        setTitle(BaseWindowTitle + "No Stage Loaded");
        setSize(windowWidth, windowHeight);
        setMinimumSize(new Dimension(windowWidth, windowHeight));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    	createMenuBar();
    	createWindowContents();
    	
    	loadSettings();
    	
    	addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
    			saveSettings();
    			super.windowClosing(e);
    		}
    	});
    	
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
    }
    
    private void createMenuBar()
    {
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenuItem newStageItem, generateItem, openItem, saveItem, saveAsItem, importAnimationItem, setExeLocationItem;
        JMenu editMenu;
        JMenuItem undoItem, redoItem, cutItem, copyItem, pasteItem, deleteItem, selectAllItem;
        JMenu scaleMenu;
        JMenuItem tenPercentItem, twentyFivePercentItem, fiftyPercentItem, hundredPercentItem;
        JMenu helpMenu;
        JMenuItem helpContentItem, aboutItem;
        
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        newStageItem = new JMenuItem("New");
        newStageItem.setActionCommand("new");
        newStageItem.addActionListener(this);
        //
        generateItem = new JMenuItem("Generate...");
        generateItem.setActionCommand("generate");
        generateItem.addActionListener(this);
        //
        openItem = new JMenuItem("Open...");
        openItem.setActionCommand("open");
        openItem.addActionListener(this);
        //
        saveItem = new JMenuItem("Save");
        saveItem.setActionCommand("save");
        saveItem.addActionListener(this);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        //
        saveAsItem = new JMenuItem("Save As...");
        saveAsItem.setActionCommand("saveAs");
        saveAsItem.addActionListener(this);
        //
        importAnimationItem = new JMenuItem("Import Animation");
        importAnimationItem.setActionCommand("importAnimation");
        importAnimationItem.addActionListener(this);
        //
        setExeLocationItem = new JMenuItem("Set Game Location");
        setExeLocationItem.setActionCommand("exeLocation");
        setExeLocationItem.addActionListener(this);
        //
        fileMenu.add(newStageItem);
        fileMenu.add(generateItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(setExeLocationItem);
        menuBar.add(fileMenu);
        
        editMenu = new JMenu("Edit");
        //
        undoItem = new JMenuItem("Undo");
        undoItem.setActionCommand("undo");
        undoItem.addActionListener(this);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        //
        redoItem = new JMenuItem("Redo");
        redoItem.setActionCommand("redo");
        redoItem.addActionListener(this);
        //
        cutItem = new JMenuItem("Cut");
        cutItem.setActionCommand("cut");
        cutItem.addActionListener(this);
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        //
        copyItem = new JMenuItem("Copy");
        copyItem.setActionCommand("copy");
        copyItem.addActionListener(this);
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        //
        pasteItem = new JMenuItem("Paste");
        pasteItem.setActionCommand("paste");
        pasteItem.addActionListener(this);
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        //
        deleteItem = new JMenuItem("Delete");
        deleteItem.setActionCommand("delete");
        deleteItem.addActionListener(this);
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        //
        selectAllItem = new JMenuItem("Select All");
        selectAllItem.setActionCommand("selectAll");
        selectAllItem.addActionListener(this);
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));
        //
        
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.add(new JSeparator());
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(deleteItem);
        editMenu.add(selectAllItem);
        menuBar.add(editMenu);
        
        scaleMenu = new JMenu("Scale");
        //
        tenPercentItem = new JMenuItem("10%");
        tenPercentItem.setActionCommand("10% Scale");
        tenPercentItem.addActionListener(this);
        //
        twentyFivePercentItem = new JMenuItem("25%");
        twentyFivePercentItem.setActionCommand("25% Scale");
        twentyFivePercentItem.addActionListener(this); 
        //
        fiftyPercentItem = new JMenuItem("50%");
        fiftyPercentItem.setActionCommand("50% Scale");
        fiftyPercentItem.addActionListener(this);
        //
        hundredPercentItem = new JMenuItem("100%");
        hundredPercentItem.setActionCommand("100% Scale");
        hundredPercentItem.addActionListener(this);
        //

        scaleMenu.add(tenPercentItem);
        scaleMenu.add(twentyFivePercentItem);
        scaleMenu.add(fiftyPercentItem);
        scaleMenu.add(hundredPercentItem);
        menuBar.add(scaleMenu);
        
        helpMenu = new JMenu("Help");
        helpContentItem = new JMenuItem("Help Content");
        aboutItem = new JMenuItem("About");
        helpMenu.add(helpContentItem);
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private void createWindowContents() {
    	JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createObjectListPane(), createObjectDataPane());
    	sp.setResizeWeight(.05);
        this.setContentPane(sp);
    }
    
    private void loadSettings() {
    	File file = new File("settings.xml");
        
        fileChooser = new JFileChooser(".");
        exeDirectory = "";
        fileChooserDirectory = ".";
        
    	System.out.println("Loading Settings...");
        
		try {
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(file);
	        doc.getDocumentElement().normalize();
	        
	        Node root = doc.getDocumentElement();
	        if(root.getNodeName().compareTo("Settings") != 0)
	        {
	        	JOptionPane.showMessageDialog(this, "Settings file has invalid root, expected <Settings>", "Error loading settings", JOptionPane.ERROR_MESSAGE); 
	        	return; 
	        }
	        
	        NodeList nodes = root.getChildNodes();
	        for(int i=0; i < nodes.getLength(); i++) {
	        	Node node = nodes.item(i);
	        	System.out.println(node.getNodeName() + ": " + node.getTextContent());
	        	switch(node.getNodeName()) {
	        	case "FileChooserDir":
	        		if(node.getTextContent() != null)
	        			fileChooserDirectory = node.getTextContent();
	        		fileChooser = new JFileChooser(fileChooserDirectory);
	        		break;
	        	case "ExeDir":
	        		if(node.getTextContent() != null)
	        			exeDirectory = node.getTextContent();
	        		break;
	        	case "Scale":
	        		if(node.getTextContent() != null)
	        			scale = Float.parseFloat(node.getTextContent());
	        		break;
	        	}
	        }
		} 
        catch(ParserConfigurationException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage() + " | Using default settings", "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE); 
        	return; 
        }
        catch(SAXException e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage() + " | Using default settings", "SAX Exception", JOptionPane.ERROR_MESSAGE);  
        	return;            
        }
        catch(IOException e)
        {
        	//JOptionPane.showMessageDialog(this, e.getMessage() + " | Using default settings", "IO Exception", JOptionPane.ERROR_MESSAGE); 
        }
    	System.out.println("Finished Loading Settings\n");
		
		if(exeDirectory.isEmpty()) {
        	if(JOptionPane.showConfirmDialog(this, "Set game .exe directory now?", ".exe Directory Not Set", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        		setExeLocation();
        	}
        	else {
        		
        	}
		}
	}
    
    private void saveSettings() {
    	
    	System.out.println("\nSaving Settings...");
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            Element root = doc.createElement("Settings");
            
            Element chooserDirSetting = doc.createElement("FileChooserDir");
            chooserDirSetting.setTextContent(fileChooser.getCurrentDirectory().getPath());
        	System.out.println("FileChooserDir: " + fileChooser.getCurrentDirectory().getPath());
            
            Element exeDirSetting = doc.createElement("ExeDir");
            exeDirSetting.setTextContent(exeDirectory);
        	System.out.println("ExeDir: " + exeDirectory);
        	
        	Element scaleSetting = doc.createElement("Scale");
        	scaleSetting.setTextContent("" + scale);
        	System.out.println("Scale: " + scale);
            
            root.appendChild(chooserDirSetting);
            root.appendChild(exeDirSetting);
            root.appendChild(scaleSetting);
            doc.appendChild(root);
            
            //finally, save the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("settings.xml"));
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
        catch(Exception e)
        {
        	JOptionPane.showMessageDialog(this, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setExeLocation() {
    	File lastFile = fileChooser.getCurrentDirectory();
    	
    	fileChooser = new JFileChooser(exeDirectory);
    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	int returnVal = fileChooser.showDialog(this, "Choose .exe Location");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            exeDirectory = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.println("Set .exe Directory: " + exeDirectory);
        } else {
            
        }
        fileChooser = new JFileChooser(lastFile);
    }
    
    
    public void setCurrentlyLoadedStage(HSStage newStage)
    {
        currentlyLoadedStage = newStage;
        setTitle(BaseWindowTitle + "Stage Loaded");
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
    
    public String createAbsolutePath(String relPath)
    {
    	return createAbsolutePathFrom(relPath, workingDirectory);
    }
    
    public String createAbsolutePathFrom(String relPath, String fromPath)
    {
    	relPath = relPath.replace('\\', File.separatorChar);
    	relPath = relPath.replace('/', File.separatorChar);
    	if(!fromPath.endsWith(File.separator)) fromPath += File.separator;
    	File a = new File(fromPath);
	    File b = new File(a, relPath);
	    String absolute = "";
		try {
			absolute = b.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	    return absolute;
    }
    
    public String createRelativePath(String absPath)
    {
    	return createPathRelativeTo(absPath, workingDirectory);
    }
    
    public String createPathRelativeTo(String absPath, String relativeTo)
    {
    	String[] relativeToPieces = relativeTo.split(File.separator.compareTo("\\") == 0 ? "\\\\" : "/");
        String[] absPathPieces = absPath.split(File.separator.compareTo("\\") == 0 ? "\\\\" : "/");
        
        //first, make sure they share the same drive
        if(!relativeToPieces[0].equals(absPathPieces[0]))
        {
            return "";
        }
        
        //compare each until either one ends or a point of divergence is found
        int end = relativeToPieces.length > absPathPieces.length ? absPathPieces.length : relativeToPieces.length;
        int divergeancePoint = end;
        for(int i = 0; i < end; i++)
        {
            if(!relativeToPieces[i].equals(absPathPieces[i]))
            {
                divergeancePoint = i;
                break;
            }
        }

        //add double periods to signify parent directories
        String relativePath = "";
        for(int i = 0; i < end - divergeancePoint; i++)
        {
            relativePath += ".." + File.separator;
        }
        
        //add the absolute path starting with the divergence point
        for(int i = divergeancePoint; i < absPathPieces.length; i++)
        {
            if(i > divergeancePoint)
            {
                relativePath += File.separator;
            }
            relativePath += absPathPieces[i];
        }
        
        return relativePath;
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action Preformed: " + e.getActionCommand());
		switch(e.getActionCommand()) {
		case "new": newStage(); break;
		case "open": open(); break;
		case "save": save(); break;
		case "saveAs": saveAs(); break;
		case "delete": delete(); break;
        case "exeLocation": setExeLocation(); break;
        case "10% Scale": adjustScale(.1f); break;
        case "25% Scale": adjustScale(.25f); break;
        case "50% Scale": adjustScale(.5f); break;
        case "100% Scale": adjustScale(1f); break;
		}
	}

	private void adjustScale(float d) {
		scale = d;
		for(Component comp : textureObjectPane.textureObjectLayeredPane.getComponents()) {
			if(comp instanceof HSTextureLabel)
				((HSTextureLabel)comp).loadIcon();
		}
	}

	private void newStage() {
		currentlyLoadedStage = new HSStage();
		workingDirectory = "";
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

        workingDirectory = file.getParent();
        curFile = file;
        
        //Now that we have a working directory, we can save
        save();
	}

	private void save() {
        //If we don't have a working directory or a loaded object, we should save as instead (saveAs as can handle the lack of a loaded stage as well)
        if(currentlyLoadedStage == null || workingDirectory == "") { saveAs(); }
        
        createDefinitionFile();
	}

	private void createDefinitionFile() {
		if(currentlyLoadedStage == null) { return; }
		
		try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            
            Element root = doc.createElement("HSStage");
            root.setAttribute("version", "" + XML_FORMAT_VERSION);
            root.setAttribute("gravity", "" + currentlyLoadedStage.gravity);
            root.setAttribute("width", "" + currentlyLoadedStage.width);
            root.setAttribute("height", "" + currentlyLoadedStage.height);
            
            //TODO spawn points
            
            Element objects = doc.createElement("Objects");
            for(HSObject obj : currentlyLoadedStage.objects) {
            	Element object = doc.createElement("Object");
            	object.setAttribute("defFilePath", createPathRelativeTo(obj.defPath, exeDirectory));
            	object.setAttribute("posX", "" + (obj.pos.x - obj.offsetX));
            	object.setAttribute("posY", "" + (obj.pos.y - obj.offsetY));
            	object.setAttribute("depth", "" + obj.depth);
            	objects.appendChild(object);
            }
            root.appendChild(objects);
            doc.appendChild(root);
            
            //Save the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(curFile.getAbsolutePath());
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

        workingDirectory = file.getParent();
        curFile = file;
        
        try {
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(file);
        	doc.getDocumentElement().normalize();
        	removeWhitespaceNodes(doc);
        	
        	int version = 0;
        	
        	if(doc.getDocumentElement().getNodeName().compareTo("HSStage") != 0) {
            	JOptionPane.showMessageDialog(this, "Root node of Stage is not 'HSStage'", "Error", JOptionPane.ERROR_MESSAGE);  
            	return;
        	}
            
            if(!doc.getDocumentElement().getAttribute("version").isEmpty()) version = Integer.parseInt(doc.getDocumentElement().getAttribute("version"));
            
            System.out.println("Loading object with xml format version: " + version);
        	
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
                		HSObject hsobject = HSObject.ObjectFromDefinition(exeDirectory.isEmpty() ? workingDirectory : exeDirectory, objectAttributes.getNamedItem("defFilePath").getNodeValue(), objectAttributes, this);
                		if(hsobject != null)
                			loadStage.objects.add(hsobject);
                	}
        			break;
        		}
        	}
        	
        	setCurrentlyLoadedStage(loadStage);
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

	private void delete() {
		textureObjectPane.textureObjectLayeredPane.removeSelected();
	}
    
	//Found on Google
    private static void removeWhitespaceNodes(Document doc) {
    	XPath xp = XPathFactory.newInstance().newXPath();
	    NodeList nl;
		try {
			nl = (NodeList) xp.evaluate("//text()[normalize-space(.)='']", doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return;
		}
	
	    for (int i=0; i < nl.getLength(); ++i) {
	        Node node = nl.item(i);
	        node.getParentNode().removeChild(node);
	    }
    }
    
    public static ImageIcon resize(ImageIcon icon) {
    	int width = (int) (icon.getIconWidth() * scale);
    	int height = (int) (icon.getIconHeight() * scale);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(icon.getImage(), 0, 0, width, height, null);
        g2d.dispose();
        return new ImageIcon(bi);
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
