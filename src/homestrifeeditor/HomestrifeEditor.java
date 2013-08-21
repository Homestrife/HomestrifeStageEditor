/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package homestrifeeditor;

import homestrifeeditor.windows.EditorWindow;

import javax.swing.SwingUtilities;

/**
 *
 * @author Darlos9D
 */
public class HomestrifeEditor
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EditorWindow editorWindow = new EditorWindow();
                editorWindow.setVisible(true);
            }
        });
    }
}
