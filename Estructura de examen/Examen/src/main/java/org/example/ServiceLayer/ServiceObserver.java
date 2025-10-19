package org.example.ServiceLayer;

import org.example.Utilities.ChangeType;

public interface ServiceObserver<T>{
    void DataChanged(ChangeType type, T entity);
}

