package org.example.Presentation_layer.Models;

import org.example.Domain_Layer.Tarea;
import org.example.ServiceLayer.ServiceObserver;
import org.example.Utilities.ChangeType;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TareaTableModel extends AbstractTableModel {
    private final String[] cols = {"Número", "Descripción", "Fecha Vencimiento", "Prioridad", "Estado", "Responsable"};
    private final Class<?>[] types = {String.class, String.class, String.class, String.class, String.class, String.class};
    private final List<Tarea> rows = new ArrayList<>();

    public void setRows(List<Tarea> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public Tarea getAt(int row) {
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
    }

    public void agregar(Tarea t) {
        rows.add(t);
        fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
    }

    public void actualizar(Tarea t) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getNumero().equals(t.getNumero())) {
                rows.set(i, t);
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
        Tarea t = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> t.getNumero();
            case 1 -> t.getDescripcion();
            case 2 -> t.getFechaFinalizacion();
            case 3 -> t.getPrioridad().getDescripcion();
            case 4 -> t.getEstado().getDescripcion();
            case 5 -> t.getResponsable() != null ? t.getResponsable().getNombre() : "N/A";
            default -> null;
        };
    }
}
