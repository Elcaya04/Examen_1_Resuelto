package org.example.Data_Acess_Layer;

import java.util.List;

public interface FileStore<T> {
    List<T> Leer();
    void Escribir(List<T> lista);
}
