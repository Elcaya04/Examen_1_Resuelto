package org.example.Data_Acess_Layer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.Domain_Layer.Proyecto;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProyectoFileStore implements FileStore<Proyecto> {
    private final File xmlFile;

    public ProyectoFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Proyecto> Leer() {
        List<Proyecto> proyectos = new ArrayList<>();
        if (!xmlFile.exists() || xmlFile.length() == 0) return proyectos;

        try (FileInputStream in = new FileInputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Proyecto.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xr = xif.createXMLStreamReader(in);

            while (xr.hasNext()) {
                int eventType = xr.getEventType();

                if (eventType == XMLStreamConstants.START_ELEMENT && "proyecto".equals(xr.getLocalName())) {
                    Unmarshaller u = ctx.createUnmarshaller();
                    Proyecto p = u.unmarshal(xr, Proyecto.class).getValue();
                    proyectos.add(p);
                    System.out.println("[DEBUG] Proyecto cargado: " + p.getCodigo() + " - " + p.getDescripcion());
                } else {
                    xr.next();
                }
            }
            xr.close();
            System.out.println("[DEBUG] Total de proyectos cargados: " + proyectos.size());
        } catch (Exception ex) {
            System.err.println("[ERROR] Leyendo proyectos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return proyectos;
    }

    @Override
    public void Escribir(List<Proyecto> lista) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Proyecto.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("proyectos");

            if (lista != null) {
                for (Proyecto p : lista) m.marshal(p, xw);
            }

            xw.writeEndElement();
            xw.writeEndDocument();
            xw.flush();
            xw.close();
        } catch (Exception ex) {
            System.err.println("[ERROR] Escribiendo proyectos: " + ex.getMessage());
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
