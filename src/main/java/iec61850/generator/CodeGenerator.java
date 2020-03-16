package iec61850.generator;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import java.io.*;
import java.util.ArrayList;

/**
 * @author Александр Холодов
 * @created 03/2020
 * @project OpenIEDconfigurator
 * @description Генерация объектов МЭК61850
 */
public class CodeGenerator {

    public static void main(String[] args) {

        File projectPath = new File("src\\main\\java\\");
        File objectsPath = new File("src\\main\\java\\iec61850\\objects");
        objectsPath.delete();

        PowerShell powerShell = PowerShell.openSession(); PowerShellResponse response;
        System.out.println("PowerShell is started \nExecuting: xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/... xsd");

        response = powerShell.executeCommand(String.format("cd %s", projectPath.getAbsolutePath()));
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_BaseSimpleTypes.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_BaseTypes.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_Communication.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_Coordinates.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_DataTypeTemplates.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_Enums.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_IED.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_Maintenance.xsd");
        response = powerShell.executeCommand("xjc -npa -no-header -p iec61850.objects iec61850/xsdSource/SCL_Substation.xsd");

        if(objectsPath.exists()) System.out.println("\nObjects is generated:");
        for(File file:objectsPath.listFiles()){  System.out.println(file); }

        for(File file:objectsPath.listFiles()){ deleteComments(file); }

        System.exit(0);
    }

    private static void deleteComments(File file){
        System.out.println("Delete commenting from: " + file);
        ArrayList<String> fileData = readFile(file);
        ArrayList<String> clearData = new ArrayList<String>();

        boolean drop = false;
        for(String line:fileData){
            if(line.contains("/**")) drop = true;
            if(!drop) {
                if(line.contains("XmlSchemaType")) continue;
                clearData.add(line);
            }
            if(line.contains("*/")) drop = false;
        }
        writeFile(file, clearData);
    }

    private static void writeFile(File file, ArrayList<String> data){
        try {
            if(file.exists()) file.delete();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(String line:data) writer.write(line+"\n");
            writer.close();
            data.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static ArrayList<String> readFile(File file){
        ArrayList<String> temp = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) temp.add(line);
            br.close();
            return temp;
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

}
