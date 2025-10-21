package org.example.Presentation_layer.Views.EditarTareaDialog;

import org.example.Domain_Layer.Tarea;
import org.example.Utilities.EstadoTarea;
import org.example.Utilities.PrioridadTarea;

import javax.swing.*;
import java.awt.*;

public class EditarTareaDialog extends JDialog {
    private JTextField txtDescripcion;
    private JComboBox<PrioridadTarea> prioridadCombo;
    private JComboBox<EstadoTarea> estadoCombo;
    private JButton guardarButton;
    private JButton cancelarButton;
    private Tarea tarea;
    private boolean confirmado = false;

    public EditarTareaDialog(JFrame parent, Tarea tarea) {
        super(parent, "Editar Tarea", true);
        this.tarea = tarea;
        initComponents();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Descripción
        panel.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextField(tarea.getDescripcion(), 20);
        panel.add(txtDescripcion);

        // Prioridad
        panel.add(new JLabel("Prioridad:"));
        prioridadCombo = new JComboBox<>(PrioridadTarea.values());
        prioridadCombo.setSelectedItem(tarea.getPrioridad());
        panel.add(prioridadCombo);

        // Estado
        panel.add(new JLabel("Estado:"));
        estadoCombo = new JComboBox<>(EstadoTarea.values());
        estadoCombo.setSelectedItem(tarea.getEstado());
        panel.add(estadoCombo);

        // Número de tarea (solo lectura)
        panel.add(new JLabel("Número:"));
        JLabel numeroLabel = new JLabel(tarea.getNumero());
        numeroLabel.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(numeroLabel);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");

        guardarButton.addActionListener(e -> guardar());
        cancelarButton.addActionListener(e -> cancelar());

        botonesPanel.add(guardarButton);
        botonesPanel.add(cancelarButton);

        add(panel, BorderLayout.CENTER);
        add(botonesPanel, BorderLayout.SOUTH);

        pack();
        setSize(450, 250);
    }

    private void guardar() {
        String descripcion = txtDescripcion.getText().trim();

        if (descripcion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "La descripción es obligatoria",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        tarea.setDescripcion(descripcion);
        tarea.setPrioridad((PrioridadTarea) prioridadCombo.getSelectedItem());
        tarea.setEstado((EstadoTarea) estadoCombo.getSelectedItem());
        confirmado = true;
        dispose();
    }

    private void cancelar() {
        confirmado = false;
        dispose();
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Tarea getTarea() {
        return tarea;
    }
}



