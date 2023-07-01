package terminal.fileController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import terminal.cmds.CMDProcess;

//
//

public class FileManager {
    
    private static final String DATA_FILE = "data.txt";
    private static FileManager fileManager = null;
    private List<VariableFile> files;
    //sets currentfile to be nothing
    private VariableFile fileCurrent = null;

    private FileManager(){
        
        try {
            //loads the contents of the file into files
            files = loadData();
            fileCurrent = files.get(0);
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            
            e.printStackTrace();
        }
    }

    public static FileManager getInstance() {
        //if filemanager == null then make one
        if (fileManager == null) fileManager = new FileManager();
        return fileManager;
    }


    public void createFile(String name){
        //Uses the first File class in File to create a file as dir is false on default
        VariableFile file = new VariableFile(name, fileCurrent);
        fileCurrent.fileGet().add(file);
        
        try {
            //writes the data
            writeData();
            
        }
        catch (IOException e) {
            
            e.printStackTrace();
        }
    }
    
        public void makeDirectory(String name){
        // uses the second file class, same as create file but instead marks dir as true from the File class
        VariableFile file = new VariableFile(name, true, fileCurrent);
        //gets the file name from user and adds as a file
        fileCurrent.fileGet().add(file);
        try {           
            writeData();
            
        }
        catch (IOException e) {
            e.printStackTrace();
            
        }
    }

    public String listFiles(){
        
        StringBuilder strbuild = new StringBuilder();
        //Cycles through the files and grabs its dir and the file name
        for (VariableFile file:fileCurrent.fileGet()){
            
            String fileType = file.isDirectory() ? "Folder": "File";
            //appends filetype for every file found
            strbuild.append(fileType);
            for (int i=0;i< (file.isDirectory()? 15:20);i++){
                //adds a space to the string builder
                strbuild.append(" ");
            }
            //creates a new file for every file found so it displays in a list
            strbuild.append(file.nameGet()).append("\n");
        }
        return strbuild.toString();
    }

    private List<VariableFile> loadData() throws IOException, ClassNotFoundException {
        
        List<VariableFile> files = new ArrayList<>();
        java.io.File file = new java.io.File(DATA_FILE);
        //checks if the file already exists and if it doesnt will create a new file
        if (!file.exists()){
            //creating a new directory
            VariableFile fileNew = new VariableFile("Home", true, null);
            files.add(fileNew);
            //loads the cmdproccess class from cmdprocess
            CMDProcess process = new CMDProcess("0","mkdir","Home", getClass().getClassLoader().getResource("").getPath());
            process.start();
            
            try {

                process.join();
            }
            catch (InterruptedException e) {

                e.printStackTrace();
            }
            fileCurrent = fileNew;
            return files;
        }
        
        //catches the data from oos and reads it
        ObjectInputStream OIS = new ObjectInputStream(new FileInputStream(file));
        Object read = null;
        //checks if there is data to be read and will add it to files,  if not will close the ois
        while ((read = OIS.readObject()) != null){
            
            VariableFile fileRead = (VariableFile) read;
            files.add(fileRead);
        }
        OIS.close();
        //returns the data that was found from ois into the List<File>
        return files;
    }

    private void writeData() throws IOException {
        
        java.io.File file = new java.io.File(DATA_FILE);
        //checks if the file exists and if it doesnt will create a new file.
        if (!file.exists()){
            boolean newFile = file.createNewFile();
        }
        //converting streams so that they can be written
        ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(file));
        for (VariableFile f:files){
            
            OOS.writeObject(f);
        }
        //cleans and exits oos
        OOS.writeObject(null);
        OOS.flush();
        //close
        OOS.close();
    }
    
        public String cd(String filename){
        VariableFile file = null;
        if (filename.equals("..")){
            //grabs current parent into the curretfile
            if (fileCurrent.parentGet() != null){
                //if .parentGet() gives a result
                fileCurrent = fileCurrent.parentGet();
            }
            //otherwise returns nothing
            return "";
        }
        for (VariableFile fc:fileCurrent.fileGet()){
            //if input is the same as filename
            if (fc.nameGet().equals(filename)){
                
                file = fc;
                break;
            }
        }
        // This part of the code checks if file containes anything
        // and if so will return the filecurrent
        if (file != null){
            
            if (file.isDirectory()) {
                
                fileCurrent = file;
                return fileCurrent.nameGet();
            }
            else{
                //give error
                return "Cd into file invalid";
            }
        }
        //if file doesnt contain anything it is then invalid
        else{
            return "Invalid directory : "+filename+" Is missing.";
        }

    }

    public String getCurrentFileName() {
        
        return fileCurrent.nameGet();
    }
}
