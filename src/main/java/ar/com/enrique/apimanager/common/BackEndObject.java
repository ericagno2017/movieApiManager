package ar.com.enrique.apimanager.common;


import ar.com.enrique.apimanager.json.JSONObjectConverter;

import java.io.Serializable;

public abstract class BackEndObject implements Serializable {

    private static final long serialVersionUID = -3704224041413428471L;

    @Override
    public String toString() {
        return JSONObjectConverter.convertToJSON(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return true;
    }

}
