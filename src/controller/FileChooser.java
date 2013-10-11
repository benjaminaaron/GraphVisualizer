package controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileChooser extends JPanel{

	private static final long serialVersionUID = 1L;
	private JFileChooser fc = new JFileChooser();
	public FileChooser(){}	
	
	public String[] chooseFile() throws IOException{	
        int returnVal = fc.showOpenDialog(this);       
        if (returnVal == JFileChooser.APPROVE_OPTION){        	
            Path filepath = fc.getSelectedFile().toPath();
            Charset charset = Charset.defaultCharset();        
            List<String> stringList = Files.readAllLines(filepath, charset);
            String[] stringArray = stringList.toArray(new String[]{});       
            return stringArray;
        }
            return null; 
        }
}
