package org.example.Data_Acess_Layer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.example.Domain_Layer.Usuario;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class UsuarioFileStore implements FileStore<Usuario> {
    private final File xmlFile;

    public UsuarioFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Usuario> Leer() {
        List<Usuario> usuarios = new ArrayList<>();
        if (!xmlFile.exists() || xmlFile.length() == 0) return usuarios;

        try (FileInputStream in = new FileInputStream(xmlFile)) {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xr = xif.createXMLStreamReader(in);

            String id = null;
            String nombre = null;
            String email = null;

            while (xr.hasNext()) {
                int eventType = xr.getEventType();

                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    String localName = xr.getLocalName();

                    if ("id".equals(localName)) {
                        xr.next();
                        if (xr.getEventType() == XMLStreamConstants.CHARACTERS) {
                            id = xr.getText();
                        }
                    } else if ("name".equals(localName)) {
                        xr.next();
                        if (xr.getEventType() == XMLStreamConstants.CHARACTERS) {
                            nombre = xr.getText();
                        }
                    } else if ("email".equals(localName)) {
                        xr.next();
                        if (xr.getEventType() == XMLStreamConstants.CHARACTERS) {
                            email = xr.getText();
                        }
                    }
                } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                    if ("user".equals(xr.getLocalName())) {
                        if (id != null && nombre != null) {
                            usuarios.add(new Usuario(id, nombre, email != null ? email : ""));
                        }
                        id = null;
                        nombre = null;
                        email = null;
                    }
                }

                xr.next();
            }
            xr.close();

            System.out.println("[INFO] Se cargaron " + usuarios.size() + " usuarios del archivo XML");

        } catch (Exception ex) {
            System.err.println("[ERROR] Leyendo usuarios: " + ex.getMessage());
            ex.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public void Escribir(List<Usuario> lista) {

    }

    private void ensureFile() {
        try {
            if (!xmlFile.exists()) {
                xmlFile.getParentFile().mkdirs();
                xmlFile.createNewFile();
            }
        } catch (Exception ignored) {}
    }
}
