package homestrifeeditor;

import homestrifeeditor.windows.EditorWindow;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HSObject {
	public String name = "HSObject";
	public String texturePath = "";
	public String defPath = "";
	public HSVect2D pos = new HSVect2D();
	public double depth = 0.0;

	public HSObject() {
		
	}
	
	public HSObject(HSObject o) {
		name = new String(o.name);
		texturePath = new String(o.texturePath);
		defPath = new String(o.defPath);
		pos = new HSVect2D(o.pos);
		depth = o.depth;
	}
	
	public String toString() {
		return "HSObject at position (" + pos.x + ", " + pos.y + ") at depth=" + depth + " with texture at " + texturePath;
	}
	
	public static HSObject ObjectFromDefinition(File def, EditorWindow window) {
		HSObject hsobject = new HSObject();
		hsobject.defPath = def.getPath();
		String texPath = "";
		//All this work for the freakin texure
		try {
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(def);
        	doc.getDocumentElement().normalize();
        	
        	if(doc.getDocumentElement().getNodeName().compareTo("HSObjects") != 0) {
        		JOptionPane.showMessageDialog(null, "Root node of Object is not 'HSObjects'", "Error", JOptionPane.ERROR_MESSAGE);  
            	return null;
        	}
        	
        	//Basically we just need the texture
        	NodeList textureList = doc.getElementsByTagName("Texture");
        	for(int i=0; i < textureList.getLength(); i++) {
        		Node n = textureList.item(i);
        		if(n.getNodeType() == Node.ELEMENT_NODE) {
        			NamedNodeMap textureAttr = n.getAttributes();
        			if(textureAttr.getNamedItem("textureFilePath") != null) { 
        				texPath = window.createAbsolutePathFrom(textureAttr.getNamedItem("textureFilePath").getNodeValue(), window.exeDirectory);
        				break;
        			}
        		}
        	}
        	System.out.println(texPath);
        	hsobject.texturePath = texPath;
        	hsobject.name = def.getName();
		} 
        catch(ParserConfigurationException e) {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);  
        }
        catch(SAXException e) {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "SAX Exception", JOptionPane.ERROR_MESSAGE);              
        }
        catch(IOException e) {
        	JOptionPane.showMessageDialog(null, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);              
        }
		return hsobject;
	}
	
	public static HSObject ObjectFromDefinition(String dir, String defPath, NamedNodeMap attributes, EditorWindow window) {
		HSObject hsobject = null;
		String texPath = "";
		try {
			File file = new File(dir + File.separator + defPath);
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	Document doc = dBuilder.parse(file);
        	doc.getDocumentElement().normalize();
        	
        	if(doc.getDocumentElement().getNodeName().compareTo("HSObjects") != 0) {
        		JOptionPane.showMessageDialog(window, "Root node of Object is not 'HSObjects'", "Error", JOptionPane.ERROR_MESSAGE);  
            	return null;
        	}
        	
        	//Basically we just need the texture
        	NodeList textureList = doc.getElementsByTagName("Texture");
        	for(int i=0; i < textureList.getLength(); i++) {
        		Node n = textureList.item(i);
        		if(n.getNodeType() == Node.ELEMENT_NODE) {
        			NamedNodeMap textureAttr = n.getAttributes();
        			if(textureAttr.getNamedItem("textureFilePath") != null) { 
        				texPath = window.createAbsolutePathFrom(textureAttr.getNamedItem("textureFilePath").getNodeValue(), dir);
        				break;
        			}
        		}
        	}
        	hsobject = new HSObject();
        	hsobject.texturePath = texPath;
        	hsobject.defPath = window.createAbsolutePathFrom(defPath, dir);
        	hsobject.name = defPath;

        	if(attributes.getNamedItem("posX") != null) hsobject.pos.x = Float.parseFloat(attributes.getNamedItem("posX").getNodeValue());
        	if(attributes.getNamedItem("posY") != null) hsobject.pos.y = Float.parseFloat(attributes.getNamedItem("posY").getNodeValue());
        	if(attributes.getNamedItem("depth") != null) hsobject.depth = Double.parseDouble(attributes.getNamedItem("depth").getNodeValue());
        	
        	System.out.println(hsobject);
		} 
        catch(ParserConfigurationException e) {
        	JOptionPane.showMessageDialog(window, e.getMessage(), "Parser Configuration Exception", JOptionPane.ERROR_MESSAGE);  
        }
        catch(SAXException e) {
        	JOptionPane.showMessageDialog(window, e.getMessage(), "SAX Exception", JOptionPane.ERROR_MESSAGE);              
        }
        catch(IOException e) {
        	JOptionPane.showMessageDialog(window, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);              
        }
		return hsobject;
	}
}
