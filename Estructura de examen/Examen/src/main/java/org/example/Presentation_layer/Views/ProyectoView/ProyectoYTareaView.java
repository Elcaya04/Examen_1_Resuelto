package org.example.Presentation_layer.Views.ProyectoView;

import org.example.Domain_Layer.Proyecto;
import org.example.Domain_Layer.Tarea;
import org.example.Domain_Layer.Usuario;
import org.example.Presentation_layer.Controllers.ProyectoController;
import org.example.Presentation_layer.Controllers.TareaController;
import org.example.Presentation_layer.Controllers.UsuarioController;
import org.example.Presentation_layer.Models.ProyectoTableModel;
import org.example.Presentation_layer.Models.TareaTableModel;
import org.example.Presentation_layer.Views.EditarTareaDialog.EditarTareaDialog;
import org.example.Utilities.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ProyectoYTareaView extends JFrame {
    // Componentes de Proyectos
    private JPanel panelProyectos;
    private JTextField txtDescripcionProyecto;
    private JComboBox<Usuario> cmbEncargadoProyecto;
    private JButton btnAgregarProyecto;
    private JTable tablaProyectos;
    private JScrollPane scrollProyectos;

    // Componentes de Tareas
    private JPanel panelTareas;
    private JLabel lblProyectoSeleccionado;
    private JLabel lblDescripcionProyecto;
    private JTextField txtDescripcionTarea;
    private JTextField txtFechaVencimiento;
    private JComboBox<PrioridadTarea> cmbPrioridad;
    private JComboBox<Usuario> cmbResponsableTarea;
    private JButton btnAgregarTarea;
    private JTable tablaTareas;
    private JScrollPane scrollTareas;
    private JPanel panelFormularioTareas;

    // Controllers
    private ProyectoController proyectoController;
    private TareaController tareaController;
    private UsuarioController usuarioController;

    // Models
    private ProyectoTableModel proyectoTableModel;
    private TareaTableModel tareaTableModel;

    // Datos
    private List<Usuario> usuarios;
    private Proyecto proyectoSeleccionado;

    public ProyectoYTareaView(ProyectoController proyectoController, TareaController tareaController,
                              UsuarioController usuarioController) {
        this.proyectoController = proyectoController;
        this.tareaController = tareaController;
        this.usuarioController = usuarioController;

        this.proyectoTableModel = new ProyectoTableModel();
        this.tareaTableModel = new TareaTableModel();
// Registrar observadores ANTES de cargar datos
        proyectoController.Observer((type, entity) -> {
            if (type == ChangeType.CREATE) {
                Usuario u = buscarUsuarioPorId(entity.getEncargadoId());
                entity.setEncargado(u);
                proyectoTableModel.agregar(entity);
            } else if (type == ChangeType.UPDATE) {
                Usuario u = buscarUsuarioPorId(entity.getEncargadoId());
                entity.setEncargado(u);
                proyectoTableModel.actualizar(entity);
            }
        });

        tareaController.Observer((type, entity) -> {
            if (type == ChangeType.CREATE) {
                Usuario u = buscarUsuarioPorId(entity.getResponsableId());
                entity.setResponsable(u);
                tareaTableModel.agregar(entity);
            } else if (type == ChangeType.UPDATE) {
                Usuario u = buscarUsuarioPorId(entity.getResponsableId());
                entity.setResponsable(u);
                tareaTableModel.actualizar(entity);
            }
        });
        initComponents();
        cargarDatos();
        agregarListeners();

        setTitle("Sistema de Gestión de Proyectos y Tareas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior - Proyectos
        panelProyectos = crearPanelProyectos();

        // Panel inferior - Tareas
        panelTareas = crearPanelTareas();

        // Dividir pantalla
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelProyectos, panelTareas);
        splitPane.setDividerLocation(350);

        contentPane.add(splitPane, BorderLayout.CENTER);
        setContentPane(contentPane);
    }

    private JPanel crearPanelProyectos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Proyectos"));

        // Formulario horizontal
        JPanel formularioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        formularioPanel.add(new JLabel("Descripción:"));
        txtDescripcionProyecto = new JTextField(20);
        formularioPanel.add(txtDescripcionProyecto);

        formularioPanel.add(new JLabel("Encargado:"));
        cmbEncargadoProyecto = new JComboBox<>();
        cmbEncargadoProyecto.setPreferredSize(new Dimension(150, 25));
        formularioPanel.add(cmbEncargadoProyecto);

        btnAgregarProyecto = new JButton("Crear Proyecto");
        formularioPanel.add(btnAgregarProyecto);

        panel.add(formularioPanel, BorderLayout.NORTH);

        // Tabla
        tablaProyectos = new JTable(proyectoTableModel);
        tablaProyectos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProyectos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaProyectos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablaProyectos.getColumnModel().getColumn(2).setPreferredWidth(150);

        scrollProyectos = new JScrollPane(tablaProyectos);
        panel.add(scrollProyectos, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelTareas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Tareas"));

        // Label de proyecto seleccionado
        lblProyectoSeleccionado = new JLabel("No hay proyecto seleccionado");
        lblProyectoSeleccionado.setFont(new Font("Arial", Font.BOLD, 12));
        lblProyectoSeleccionado.setForeground(new Color(100, 100, 100));
        panel.add(lblProyectoSeleccionado, BorderLayout.NORTH);

        // Panel de contenido de tareas
        JPanel panelContenidoTareas = new JPanel(new BorderLayout(10, 10));

        // Formulario de tareas (inicialmente oculto)
        panelFormularioTareas = crearFormularioTareas();
        panelContenidoTareas.add(panelFormularioTareas, BorderLayout.NORTH);

        // Tabla de tareas
        tablaTareas = new JTable(tareaTableModel);
        tablaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaTareas.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaTareas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaTareas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaTareas.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaTareas.getColumnModel().getColumn(4).setPreferredWidth(100);
        tablaTareas.getColumnModel().getColumn(5).setPreferredWidth(100);

        scrollTareas = new JScrollPane(tablaTareas);
        panelContenidoTareas.add(scrollTareas, BorderLayout.CENTER);

        panel.add(panelContenidoTareas, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearFormularioTareas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        panel.add(new JLabel("Descripción:"));
        txtDescripcionTarea = new JTextField(15);
        panel.add(txtDescripcionTarea);

        panel.add(new JLabel("Fecha:"));
        txtFechaVencimiento = new JTextField(12);
        panel.add(txtFechaVencimiento);

        panel.add(new JLabel("Prioridad:"));
        cmbPrioridad = new JComboBox<>(PrioridadTarea.values());
        panel.add(cmbPrioridad);

        panel.add(new JLabel("Responsable:"));
        cmbResponsableTarea = new JComboBox<>();
        cmbResponsableTarea.setPreferredSize(new Dimension(120, 25));
        panel.add(cmbResponsableTarea);

        btnAgregarTarea = new JButton("Crear Tarea");
        panel.add(btnAgregarTarea);

        panel.setVisible(false);
        return panel;
    }

    private void cargarDatos() {
        usuarios = usuarioController.leerTodos();

        System.out.println("[DEBUG] Usuarios cargados: " + usuarios.size());
        for (Usuario u : usuarios) {
            System.out.println("  - " + u.getId() + ": " + u.getNombre());
        }

        // Cargar combos de usuarios
        cmbEncargadoProyecto.removeAllItems();
        cmbResponsableTarea.removeAllItems();

        for (Usuario u : usuarios) {
            cmbEncargadoProyecto.addItem(u);
            cmbResponsableTarea.addItem(u);
        }

        // Cargar proyectos con sus usuarios asociados
        List<Proyecto> proyectos = proyectoController.leerTodos();
        for (Proyecto p : proyectos) {
            Usuario u = buscarUsuarioPorId(p.getEncargadoId());
            p.setEncargado(u);
            System.out.println("[DEBUG] Proyecto: " + p.getCodigo() + " -> Encargado: " +
                    (u != null ? u.getNombre() : "NO ENCONTRADO"));
        }
        proyectoTableModel.setRows(proyectos);

        // Cargar tareas con sus usuarios asociados
        List<Tarea> tareas = tareaController.leerTodas();
        for (Tarea t : tareas) {
            Usuario u = buscarUsuarioPorId(t.getResponsableId());
            t.setResponsable(u);
            System.out.println("[DEBUG] Tarea: " + t.getNumero() + " -> Responsable: " +
                    (u != null ? u.getNombre() : "NO ENCONTRADO"));
        }
    }private Usuario buscarUsuarioPorId(String id) {
        if (id == null || id.isEmpty()) return null;
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }

    private void agregarListeners() {
        // Crear proyecto
        btnAgregarProyecto.addActionListener(e -> crearProyecto());

        // Seleccionar proyecto
        tablaProyectos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarProyecto();
            }
        });

        // Crear tarea
        btnAgregarTarea.addActionListener(e -> crearTarea());

        // Doble click en tarea
        tablaTareas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarTarea();
                }
            }
        });
    }

    private void crearProyecto() {
        try {
            String descripcion = txtDescripcionProyecto.getText().trim();
            Usuario encargado = (Usuario) cmbEncargadoProyecto.getSelectedItem();

            if (descripcion.isEmpty()) {
                mostrarError("La descripción del proyecto es obligatoria");
                return;
            }

            if (encargado == null) {
                mostrarError("Debe seleccionar un encargado");
                return;
            }

            proyectoController.agregar(descripcion, encargado.getId());
            limpiarFormularioProyectos();

        } catch (Exception ex) {
            mostrarError("Error al crear proyecto: " + ex.getMessage());
        }
    }

    private void seleccionarProyecto() {
        int row = tablaProyectos.getSelectedRow();

        if (row < 0) {
            proyectoSeleccionado = null;
            lblProyectoSeleccionado.setText("No hay proyecto seleccionado");
            panelFormularioTareas.setVisible(false);
            tareaTableModel.setRows(new ArrayList<>());
            return;
        }

        proyectoSeleccionado = proyectoTableModel.getAt(row);

        if (proyectoSeleccionado != null) {
            lblProyectoSeleccionado.setText("Proyecto: " + proyectoSeleccionado.getDescripcion());
            panelFormularioTareas.setVisible(true);

            List<Tarea> tareas = tareaController.leerPorProyecto(proyectoSeleccionado.getCodigo());

            // Asegurar que todas las tareas tienen su usuario resuelto
            for (Tarea t : tareas) {
                if (t.getResponsable() == null) {
                    Usuario u = buscarUsuarioPorId(t.getResponsableId());
                    t.setResponsable(u);
                }
            }

            tareaTableModel.setRows(tareas);
        }
    }

    private void crearTarea() {
        try {
            if (proyectoSeleccionado == null) {
                mostrarError("Debe seleccionar un proyecto");
                return;
            }

            String descripcion = txtDescripcionTarea.getText().trim();
            String fecha = txtFechaVencimiento.getText().trim();
            PrioridadTarea prioridad = (PrioridadTarea) cmbPrioridad.getSelectedItem();
            Usuario responsable = (Usuario) cmbResponsableTarea.getSelectedItem();

            if (descripcion.isEmpty()) {
                mostrarError("La descripción de la tarea es obligatoria");
                return;
            }

            if (fecha.isEmpty()) {
                mostrarError("La fecha de finalización es obligatoria");
                return;
            }

            if (responsable == null) {
                mostrarError("Debe seleccionar un responsable");
                return;
            }

            tareaController.agregar(descripcion, fecha, prioridad, responsable.getId(),
                    proyectoSeleccionado.getCodigo());
            limpiarFormularioTareas();

        } catch (Exception ex) {
            mostrarError("Error al crear tarea: " + ex.getMessage());
        }
    }

    private void editarTarea() {
        int row = tablaTareas.getSelectedRow();

        if (row < 0) {
            mostrarError("Debe seleccionar una tarea");
            return;
        }

        Tarea tarea = tareaTableModel.getAt(row);

        if (tarea != null) {
            EditarTareaDialog dialog = new EditarTareaDialog(this, tarea);
            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                try {
                    tareaController.actualizarTarea(dialog.getTarea());
                    tareaTableModel.actualizar(dialog.getTarea());
                } catch (Exception ex) {
                    mostrarError("Error al actualizar tarea: " + ex.getMessage());
                }
            }
        }
    }

    private void limpiarFormularioProyectos() {
        txtDescripcionProyecto.setText("");
        txtDescripcionProyecto.requestFocus();
    }

    private void limpiarFormularioTareas() {
        txtDescripcionTarea.setText("");
        txtFechaVencimiento.setText("");
        cmbPrioridad.setSelectedIndex(0);
        txtDescripcionTarea.requestFocus();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

