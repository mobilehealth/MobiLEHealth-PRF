package recommendation;

import java.util.ArrayList;

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

}
