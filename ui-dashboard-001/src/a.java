
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author phamh
 */
public class a {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JTable Popup Menu Example");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create a sample JTable
            String[][] data = {
                {"John", "Doe", "25"},
                {"Jane", "Doe", "30"},
                {"Bob", "Smith", "40"}
            };
            String[] columnNames = {"First Name", "Last Name", "Age"};
            JTable table = new JTable(data, columnNames);

            // Create a popup menu
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem deleteItem = new JMenuItem("Delete Row");
            deleteItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Delete the selected row from the table
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.removeRow(selectedRow);
                    }
                }
            });
            popupMenu.add(deleteItem);

            // Attach the popup menu to the JTable
            table.setComponentPopupMenu(popupMenu);

            JScrollPane scrollPane = new JScrollPane(table);
            frame.add(scrollPane);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
