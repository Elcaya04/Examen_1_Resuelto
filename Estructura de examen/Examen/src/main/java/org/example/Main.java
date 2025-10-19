package org.example;

import org.example.Data_Acess_Layer.FileStore;
import org.example.Data_Acess_Layer.ProyectoFileStore;
import org.example.Data_Acess_Layer.TareaFileStore;
import org.example.Data_Acess_Layer.UsuarioFileStore;
import org.example.Domain_Layer.Proyecto;
import org.example.Domain_Layer.Tarea;
import org.example.Domain_Layer.Usuario;
import org.example.Presentation_layer.Controllers.ProyectoController;
import org.example.Presentation_layer.Controllers.TareaController;
import org.example.Presentation_layer.Controllers.UsuarioController;
import org.example.Presentation_layer.Models.ProyectoTableModel;
import org.example.Presentation_layer.Models.TareaTableModel;
import org.example.Presentation_layer.Views.ProyectoView.ProyectoYTareaView;
import org.example.ServiceLayer.ProyectoService;
import org.example.ServiceLayer.Service;
import org.example.ServiceLayer.TareaService;
import org.example.ServiceLayer.UsuarioService;

import javax.swing.*;
import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            // Crear directorios
            File dataDir = new File("data");
            dataDir.mkdirs();

            // Rutas de los archivos XML
            // IMPORTANTE: Asegúrate que data.xml esté en la carpeta "data"
            File dataFile = new File(dataDir, "data.xml");
            File proyectoFile = new File(dataDir, "proyectos.xml");
            File tareaFile = new File(dataDir, "tareas.xml");

            // Validar que data.xml existe
            if (!dataFile.exists()) {
                System.err.println("[ERROR] No se encontró el archivo: " + dataFile.getAbsolutePath());
                System.err.println("Por favor, copia el archivo data.xml a la carpeta 'data'");
                JOptionPane.showMessageDialog(null,
                        "Error: No se encontró data.xml en la carpeta 'data'.\nPor favor, copia el archivo XML proporcionado.",
                        "Error de configuración",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            FileStore<Usuario> usuarioFileStore = new UsuarioFileStore(dataFile);
            FileStore<Proyecto> proyectoFileStore = new ProyectoFileStore(proyectoFile);
            FileStore<Tarea> tareaFileStore = new TareaFileStore(tareaFile);

            // Crear services
            Service<Usuario> usuarioService = new UsuarioService(usuarioFileStore);
            Service<Proyecto> proyectoService = new ProyectoService(proyectoFileStore);
            Service<Tarea> tareaService = new TareaService(tareaFileStore);

            // Crear controllers
            UsuarioController usuarioController = new UsuarioController(usuarioService);
            ProyectoController proyectoController = new ProyectoController(proyectoService, usuarioService);
            TareaController tareaController = new TareaController(tareaService, usuarioService);

            // Crear vista
            SwingUtilities.invokeLater(() -> {
                new ProyectoYTareaView(proyectoController, tareaController, usuarioController);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Error iniciando la aplicación: " + ex.getMessage());
        }
    }
}