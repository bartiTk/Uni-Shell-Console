package terminal.fileController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//This class returns public variables which are used in the
//FileManager class

public class VariableFile implements Serializable {
    private final List<VariableFile> files = new ArrayList<>();
    private String name;
    private VariableFile parent;
    private boolean dir = false;
    
    
//used to make files
    public VariableFile(String name, VariableFile parent) {
        this.parent = parent;
        this.name = name;
        
    }
//used to make directories
    public VariableFile(String name, boolean dir, VariableFile parent) {
        this.parent = parent;
        this.name = name;
        this.dir = dir;
        
    }
//these are used in FileManager
    
    public void setDirectory(boolean dir) {
        this.dir = dir;
    }
    public String nameGet() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//checks if need file or directory
    public boolean isDirectory() {
        return dir;
    }


//stores the list of files
    public List<VariableFile> fileGet() {
        return files;
    }

    public VariableFile parentGet() {
        return parent;
    }

    @Override
    public String toString() {
        return String.format("%5s OF %-8s", nameGet(), dir ? "Folder":"File" );
    }
}
