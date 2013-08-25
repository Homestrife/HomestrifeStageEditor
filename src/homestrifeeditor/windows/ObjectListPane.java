package homestrifeeditor.windows;

import homestrifeeditor.HSBox;
import homestrifeeditor.HSObject;
import homestrifeeditor.HSStage;
import homestrifeeditor.HSTexture;
import homestrifeeditor.ObjectListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
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
    
    public HSObject removeObjectFromObjectList(int index)
    {
        return (HSObject)objectListModel.remove(index);
    }
    
    public ArrayList<HSObject> removeObjectsFromList(int[] indices)
    {
        ArrayList<HSObject> removedObjects = new ArrayList<>();
        
        Arrays.sort(indices, 0, indices.length - 1);
        for(int i = indices.length - 1; i >= 0; i--)
        {
            removedObjects.add(0, removeObjectFromObjectList(indices[i]));
        }
        
        return removedObjects;
    }
    
    public ArrayList<HSObject> removeSelectedObjects()
    {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected object(s)?", "Delete Object(s)", JOptionPane.YES_NO_OPTION);
        ArrayList<HSObject> removeObjs = removeObjectsFromList(objectList.getSelectedIndices());
        parent.currentlyLoadedStage.objects.remove(removeObjs);
        parent.repaint();
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
        
        HSObject objectToMove = removeObjectFromObjectList(index);
        
        objectListModel.add(index - 1, objectToMove);
        
        objectList.setSelectedIndex(index - 1);
    }
    
    public void moveSelectedObjectDown()
    {
        int index = objectList.getSelectedIndex();
        
        if(index < 0 || index >= objectListModel.getSize() - 1) { return; }
        
        HSObject objectToMove = removeObjectFromObjectList(index);
        
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
        //createObjectAttributesWindow(getCurrentlySelectedObject());
    }
    
    public void massShift(int shiftX, int shiftY)
    {
        int[] indices = objectList.getSelectedIndices();
        
        for(int i : indices)
        {
            HSObject obj = (HSObject)objectListModel.get(i);
            
            obj.pos.x += shiftX;
            obj.pos.y += shiftY;
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
    	objectList.ensureIndexIsVisible(index + 1);
    }

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand())
        {
            //case "addObject": addObjectToList(); break;
            case "removeObjects": removeSelectedObjects(); break;
            case "moveObjectUp": moveSelectedObjectUp(); break;
            case "moveObjectDown": moveSelectedObjectDown(); break;
            //case "editObject": editObjectButtonPressed(); break;
            case "massShift": massShiftButtonPressed(); break;
        }
		
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
