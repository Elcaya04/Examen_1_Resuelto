package org.example.Data_Acess_Layer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.Domain_Layer.Tarea;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TareaFileStore implements FileStore<Tarea> {
    private final File xmlFile;

    public TareaFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Tarea> Leer() {
        List<Tarea> tareas = new ArrayList<>();
        if (!xmlFile.exists() || xmlFile.length() == 0) return tareas;

        try (FileInputStream in = new FileInputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Tarea.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xr = xif.createXMLStreamReader(in);

            while (xr.hasNext()) {
                int eventType = xr.getEventType();

                if (eventType == XMLStreamConstants.START_ELEMENT && "tarea".equals(xr.getLocalName())) {
                    Unmarshaller u = ctx.createUnmarshaller();
                    Tarea t = u.unmarshal(xr, Tarea.class).getValue();
                    tareas.add(t);
                    System.out.println("[DEBUG] Tarea cargada: " + t.getNumero() + " - " + t.getDescripcion());
                } else {
                    xr.next();
                }
            }
            xr.close();
            System.out.println("[DEBUG] Total de tareas cargadas: " + tareas.size());
        } catch (Exception ex) {
            System.err.println("[ERROR] Leyendo tareas: " + ex.getMessage());
            ex.printStackTrace();
        }
        return tareas;
    }

    @Override
    public void Escribir(List<Tarea> lista) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Tarea.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("tareas");

            if (lista != null) {
                for (Tarea t : lista) m.marshal(t, xw);
            }

            xw.writeEndElement();
            xw.writeEndDocument();
            xw.flush();
            xw.close();
        } catch (Exception ex) {
            System.err.println("[ERROR] Escribiendo tareas: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void ensureFile() {
        try {
            if (!xmlFile.exists()) {
                xmlFile.getParentFile().mkdirs();
                xmlFile.createNewFile();
                Escribir(new ArrayList<>());
            }
        } catch (Exception ignored) {}
    }
}
