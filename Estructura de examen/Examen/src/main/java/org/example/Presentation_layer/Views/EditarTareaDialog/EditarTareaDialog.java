package org.example.Presentation_layer.Views.EditarTareaDialog;

import org.example.Domain_Layer.Tarea;
import org.example.Utilities.EstadoTarea;
import org.example.Utilities.PrioridadTarea;

import javax.swing.*;
import java.awt.*;

public class EditarTareaDialog extends JDialog {
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
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Prioridad:"));
        prioridadCombo = new JComboBox<>(PrioridadTarea.values());
        prioridadCombo.setSelectedItem(tarea.getPrioridad());
        panel.add(prioridadCombo);

        panel.add(new JLabel("Estado:"));
        estadoCombo = new JComboBox<>(EstadoTarea.values());
        estadoCombo.setSelectedItem(tarea.getEstado());
        panel.add(estadoCombo);

        panel.add(new JLabel("Descripción:"));
        JLabel descLabel = new JLabel(tarea.getDescripcion());
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(descLabel);

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
        setSize(400, 200);
    }

    private void guardar() {
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



