package homestrifeeditor.windows.panes;

import homestrifeeditor.HSObject;
import homestrifeeditor.HSStage;
import homestrifeeditor.HSTextureLabel;
import homestrifeeditor.ObjectListCellRenderer;
import homestrifeeditor.windows.EditorWindow;
import homestrifeeditor.windows.MassShiftWindow;
import homestrifeeditor.windows.ObjectAttributesWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ObjectListPane extends JPanel implements ActionListener, ListSelectionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	
	public EditorWindow parent;
	
	public DefaultListModel<HSObject> objectListModel;
	public JList<HSObject> objectList;
	
	private JToolBar objectListToolbar;
	
	public ObjectListPane(EditorWindow theParent) {
		parent = theParent;
		createPaneContents();
	}

	private void createPaneContents() {
        JLabel objectListLabel = new JLabel("Object List");
        objectListModel = new DefaultListModel<HSObject>();
        objectList = new JList<HSObject>(objectListModel);
        objectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        objectList.setName("objectList");
        objectList.setCellRenderer(new ObjectListCellRenderer());
        objectList.addListSelectionListener(this);
        objectList.addMouseListener(this);
        
        
        JScrollPane objectListScrollPane = new JScrollPane(objectList);
        
        JButton addObjectButton = new JButton("+");
        addObjectButton.setActionCommand("addObject");
        addObjectButton.setToolTipText("Create New Object");
        addObjectButton.addActionListener(this);
        
        JButton removeObjectsButton = new JButton("-");
        removeObjectsButton.setActionCommand("removeObjects");
        removeObjectsButton.setToolTipText("Delete Selected Object(s)");
        removeObjectsButton.addActionListener(this);
        
        JButton moveObjectUpButton = new JButton("/\\");
        moveObjectUpButton.setActionCommand("moveObjectUp");
        moveObjectUpButton.setToolTipText("Move Selected Object Up");
        moveObjectUpButton.addActionListener(this);
        
        JButton moveObjectDownButton = new JButton("\\/");
        moveObjectDownButton.setActionCommand("moveObjectDown");
        moveObjectDownButton.setToolTipText("Move Selected Object Down");
        moveObjectDownButton.addActionListener(this);
        
        JButton editObjectButton = new JButton("Edit");
        editObjectButton.setActionCommand("editObject");
        editObjectButton.setToolTipText("Edit Object Attributes");
        editObjectButton.addActionListener(this);
        
        JButton massShiftObjectButton = new JButton("Mass Shift");
        massShiftObjectButton.setActionCommand("massShift");
        massShiftObjectButton.setToolTipText("Mass Shift Attributes");
        massShiftObjectButton.addActionListener(this);
        
        objectListToolbar = new JToolBar();
        objectListToolbar.setFloatable(false);
        objectListToolbar.add(addObjectButton);
        objectListToolbar.add(removeObjectsButton);
        objectListToolbar.add(moveObjectUpButton);
        objectListToolbar.add(moveObjectDownButton);
        objectListToolbar.add(editObjectButton);
        objectListToolbar.add(massShiftObjectButton);
        setToolBarEnabled(true);
        
        setLayout(new BorderLayout());
        add(objectListLabel, BorderLayout.PAGE_START);
        add(objectListScrollPane, BorderLayout.CENTER);
        add(objectListToolbar, BorderLayout.PAGE_END);
	}
    
    public void setToolBarEnabled(boolean enable)
    {
        for(Component c : objectListToolbar.getComponents())
        {
            c.setEnabled(enable);
        }
    }
    
    public void addObjectToList(HSObject obj, int index)
    {
        if(obj == null) { return; }
        
        if(index >= 0)
        {
            objectListModel.add(index + 1, obj);
        }
        else
        {
            objectListModel.addElement(obj);
        }
        moveIndexDown();
    }
    
    public void addObjectToList(HSObject obj)
    {
        addObjectToList(obj, objectList.getSelectedIndex());
    }
    
    public void addObjectsToList(ArrayList<HSObject> objs)
    {
        for (HSObject obj : objs)
        {
        	addObjectToList(obj);
        }
    }
    
    public int getIndexOfObject(HSObject obj) {
    	HSObject[] objs = getAllObjects();
    	for(int i=0; i < objs.length; i++) {
    		if(objs[i] == obj) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public void removeObjectFromList(HSObject obj) {
    	int i = getIndexOfObject(obj);
    	if(i >= 0 && i < parent.currentlyLoadedStage.objects.size())
    		removeObjectFromList(i);
    	
    	System.out.println(i);
    }
    
    public HSObject removeObjectFromList(int index)
    {
        return (HSObject)objectListModel.remove(index);
    }
    
    public ArrayList<HSObject> removeObjectsFromList(int[] indices)
    {
        ArrayList<HSObject> removedObjects = new ArrayList<>();
        
        Arrays.sort(indices, 0, indices.length - 1);
        for(int i = indices.length - 1; i >= 0; i--)
        {
            removedObjects.add(0, removeObjectFromList(indices[i]));
        }
        
        return removedObjects;
    }
    
    public ArrayList<HSObject> removeSelectedObjects()
    {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected object(s)?", "Delete Object(s)", JOptionPane.YES_NO_OPTION);
        if(n != JOptionPane.OK_OPTION) return null;
        ArrayList<HSObject> removeObjs = removeObjectsFromList(objectList.getSelectedIndices());
        parent.currentlyLoadedStage.objects.remove(removeObjs);
        for(Component c : parent.textureObjectPane.textureObjectLayeredPane.getComponents()) {
        	HSTextureLabel texLabel = ((HSTextureLabel)c);
        	if(removeObjs.contains(texLabel.parentObject)) {
        		parent.textureObjectPane.textureObjectLayeredPane.remove(c);
        	}
        }
        parent.textureObjectPane.textureObjectLayeredPane.repaint();
        return n == 0 ? removeObjs : null;
    }
    
    public void removeAllObjectsFromList()
    {
        setToolBarEnabled(false);
        objectListModel.removeAllElements();
    }
    
    public int getCurrentlySelectedIndex()
    {
        return objectList.getSelectedIndex();
    }
    
    public HSObject getCurrentlySelectedObject()
    {
        if(objectList.getSelectedIndex() < 0) { return null; }
        
        return (HSObject)objectListModel.get(objectList.getSelectedIndex());
    }
    
    public HSObject[] getAllObjects()
    {
        HSObject[] objs = new HSObject[objectListModel.getSize()];
        
        for (int i = 0; i < objectListModel.getSize(); i++)
        {
            HSObject obj = (HSObject)objectListModel.get(i);
            objs[i] = obj;
        }
        
        return objs;
    }
    
    public void moveSelectedObjectUp()
    {
        int index = objectList.getSelectedIndex();
        
        if(index <= 0) { return; }
        
        HSObject objectToMove = removeObjectFromList(index);
        
        objectListModel.add(index - 1, objectToMove);
        
        objectList.setSelectedIndex(index - 1);
    }
    
    public void moveSelectedObjectDown()
    {
        int index = objectList.getSelectedIndex();
        
        if(index < 0 || index >= objectListModel.getSize() - 1) { return; }
        
        HSObject objectToMove = removeObjectFromList(index);
        
        objectListModel.add(index + 1, objectToMove);
        
        objectList.setSelectedIndex(index + 1);
    }
    
    public void loadStageObjects(HSStage currentlyLoadedStage)
    {
        removeAllObjectsFromList();
        
        if(currentlyLoadedStage == null) { return; }
        
        setToolBarEnabled(true);
        
        addObjectsToList(currentlyLoadedStage.objects);
    }
    
    public void applyObjectChanges(HSObject obj, int index)
    {
        objectListModel.setElementAt(obj, index);
    }
    
    public void editObjectButtonPressed()
    {
    	if(getCurrentlySelectedObject() != null)
    		createObjectAttributesWindow(getCurrentlySelectedObject());
    }
    
    private void createObjectAttributesWindow(HSObject hsobj) {
        ObjectAttributesWindow window = new ObjectAttributesWindow(this, hsobj);
        window.setVisible(true);
	}

	public void massShift(int shiftX, int shiftY)
    {
        int[] indices = objectList.getSelectedIndices();
        
        for(int i : indices)
        {
            HSObject obj = (HSObject)objectListModel.get(i);

            obj.pos.x += shiftX;
            obj.pos.y += shiftY;
            
            for(Component c : parent.textureObjectPane.textureObjectLayeredPane.getComponents()) {
            	if(!(c instanceof HSTextureLabel)) continue;
            	((HSTextureLabel)c).updatePos();
            }
        }
     
    }
    
    public void massShiftButtonPressed()
    {
        MassShiftWindow window = new MassShiftWindow(this);
        window.setVisible(true);
    }
    
    public void moveIndexUp() {
        int index = objectList.getSelectedIndex();
       //if(index - 1 < objectList.getMinSelectionIndex()) return;
    	objectList.setSelectedIndex(index - 1);
    	objectList.ensureIndexIsVisible(index - 1);
    }
    
    public void moveIndexDown() {
        int index = objectList.getSelectedIndex();
        //if(index + 1 > objectList.getMaxSelectionIndex()) return;
    	objectList.setSelectedIndex(index + 1);
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getClickCount() == 1) {
			if(e.getButton() == MouseEvent.BUTTON3) {
				int[] selected = objectList.getSelectedIndices();
				objectList.setSelectedIndex(objectList.locationToIndex(e.getPoint()));
				editObjectButtonPressed();
				objectList.setSelectedIndices(selected);
			}
			//TODO bring to front on click (maybe?)
		}
		else if(e.getClickCount() == 2) {
			HSObject sel = objectList.getSelectedValue();
			if(sel != null) {
				JScrollPane scrollPane = parent.textureObjectPane.textureObjectScrollPane;
				scrollPane.revalidate();
				scrollPane.getHorizontalScrollBar().setValue((int) (sel.pos.x * EditorWindow.scale) + (scrollPane.getHorizontalScrollBar().getMinimum() + scrollPane.getHorizontalScrollBar().getMaximum()) / 2 - scrollPane.getWidth() / 2);
				scrollPane.getVerticalScrollBar().setValue((int) (sel.pos.y * EditorWindow.scale) + (scrollPane.getVerticalScrollBar().getMinimum() + scrollPane.getVerticalScrollBar().getMaximum()) / 2 - scrollPane.getHeight() / 2);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void setSelectedFromTexturePane() {
        //Now here we want to make it so we select multiple objects in the list if we have selected multiple textures
        ArrayList<Integer> toSelectList = new ArrayList<>();
        for(JLabel sel : parent.textureObjectPane.textureObjectLayeredPane.selectedItems) {
        	if(!(sel instanceof HSTextureLabel)) continue;
        	for(int i=0; i < objectListModel.getSize(); i++) {
        		if(((HSTextureLabel)sel).parentObject.equals(objectListModel.get(i))) {
        			toSelectList.add(i);
        		}
        	}
        }
        int[] toSelectArray = new int[toSelectList.size()];
        for(int i=0; i < toSelectList.size(); i++) {
        	toSelectArray[i] = toSelectList.get(i);
        }
        objectList.setSelectedIndices(toSelectArray);
	}

	private void addObject() {
		if(parent.currentlyLoadedStage == null) return;
		
        int returnVal = EditorWindow.fileChooser.showOpenDialog(this);
        File file;
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = EditorWindow.fileChooser.getSelectedFile();
        } else {
            return;
        }
		
        HSObject obj = HSObject.ObjectFromDefinition(file, parent);
        parent.textureObjectPane.textureObjectLayeredPane.addObject(obj);
        addObjectToList(obj);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand())
        {
            case "addObject": addObject(); break;
            case "removeObjects": removeSelectedObjects(); break;
            case "moveObjectUp": moveSelectedObjectUp(); break;
            case "moveObjectDown": moveSelectedObjectDown(); break;
            case "editObject": editObjectButtonPressed(); break;
            case "massShift": massShiftButtonPressed(); break;
        }
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		parent.textureObjectPane.textureObjectLayeredPane.setSelectedFromListPane();
	}
	
}
