package cl.cencosud.filtro;

import java.io.File;
import java.io.FilenameFilter;

public class Filtro implements FilenameFilter {
    private String extension;

    private String prefijo;

    public Filtro(String prefijo, String extension) {
        this.prefijo = prefijo;
        this.extension = extension;
    }

    public boolean accept(File dir, String name) {
        return name.endsWith(extension) && name.startsWith(prefijo);
    }
}