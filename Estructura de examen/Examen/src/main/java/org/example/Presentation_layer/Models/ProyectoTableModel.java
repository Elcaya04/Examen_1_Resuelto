package org.example.Presentation_layer.Models;

import org.example.Domain_Layer.Proyecto;
import org.example.ServiceLayer.ServiceObserver;
import org.example.Utilities.ChangeType;


import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;


public class ProyectoTableModel extends AbstractTableModel {
    private final String[] cols = {"Código", "Descripción", "Encargado"};
    private final Class<?>[] types = {String.class, String.class, String.class};
    private final List<Proyecto> rows = new ArrayList<>();

    public void setRows(List<Proyecto> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Proyecto getAt(int row) {
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
    }

    public void agregar(Proyecto p) {
        rows.add(p);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }

    public void actualizar(Proyecto p) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getCodigo().equals(p.getCodigo())) {
                rows.set(i, p);
                fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    @Override
    public int getRowCount() { return rows.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int c) { return cols[c]; }

    @Override
    public Class<?> getColumnClass(int c) { return types[c]; }

    @Override
    public boolean isCellEditable(int r, int c) { return false; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Proyecto p = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> p.getCodigo();
            case 1 -> p.getDescripcion();
            case 2 -> p.getEncargado() != null ? p.getEncargado().getNombre() : "N/A";
            default -> null;
        };
    }
}
