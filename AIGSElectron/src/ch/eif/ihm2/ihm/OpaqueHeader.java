package ch.eif.ihm2.ihm;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class OpaqueHeader extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable arg0, Object ob,
            boolean arg2, boolean arg3, int arg4, int arg5) {
        JLabel t = new JLabel(ob.toString());
        t.setOpaque(false);
        t.setForeground(Color.black);
        return t;
    }
}
