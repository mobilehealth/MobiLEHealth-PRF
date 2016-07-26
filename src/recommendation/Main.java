package recommendation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import PRF.AlgoritmoGenetico;
import domain.Person;
import ontology.Ontology;

public class Main {

	public static void main(String[] args) {


		Ontology ontologia = new Ontology();
		AlgoritmoGenetico algoritmo = new AlgoritmoGenetico();
		ArrayList<Person> persons;
		ArrayList<Person> resultado;
		
		
		persons = ontologia.getAllPersons();
		
		algoritmo.algortimoGentico(persons);
		
		
		

	}
	
	public void Gravar(String texto){  
		
		String textoQueSeraEscrito = "Texto que sera escrito.";
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File("Arquivo.txt"));
			arquivo.write(textoQueSeraEscrito);
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}  
	
	
	
	
	
	
}
