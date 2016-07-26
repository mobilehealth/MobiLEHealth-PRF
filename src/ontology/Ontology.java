package ontology;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

import DAO.PersonDAO;
import domain.Person;

public class Ontology {
	
	public static ArrayList<Person> getAllPersons(){
		
		try {
			
			
			gerarArquivoOWL();
			
			
			FileManager.get().addLocatorClassLoader(Ontology.class.getClassLoader());
			Model model = FileManager.get().loadModel("RelateUsers.owl");
			
			// Obtem atributos de todos os usuários
			String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
					"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
					"PREFIX prop: <http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#>" +
					"SELECT * WHERE { "
					+ " ?person foaf:firstName ?firstName ."
					+ " ?person foaf:familyName ?familyName ."
					+ " ?person foaf:age ?age ."
					+ " ?person foaf:gender ?gender ."
					+ " ?person prop:disease ?disease ."
					+ " ?person prop:afinidade ?afinidade ."
					+ " ?person prop:tempodoenca ?tempodoenca ."
					+ " ?person prop:Id ?id"
					+ "}";
			
			
			Query query = QueryFactory.create(q);
			QueryExecution qexec = QueryExecutionFactory.create(query, model);
			ResultSet r = qexec.execSelect();
			QuerySolution s;
			
			
			Query query2 ;
			QueryExecution qexec2;
			QuerySolution s2;
			ResultSet r2;
			
			
			ArrayList persons = new ArrayList<Person>();
			Person person = new Person();
			
			while (r.hasNext()){

				s = r.nextSolution();
				
				
				
				person.setId(Integer.parseInt(s.getLiteral("id").toString()));
				person.setName_first(s.getLiteral("firstName").toString());
				person.setName_last(s.getLiteral("familyName").toString());
				
				
				person.setIdade(Integer.parseInt(s.getLiteral("age").toString()));
				
				
				person.setDisease(s.getLiteral("disease").toString());
				person.setAfinidade(s.getLiteral("afinidade").toString());
				person.setTempodoenca(s.getLiteral("tempodoenca").toString());
				person.setGender(s.getLiteral("gender").toString());
				
				// Obtem os amigos do usuarios r
				String q2 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
						"PREFIX prop: <http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#>" +
						"SELECT * WHERE {?person prop:Id ?id . ?person foaf:firstName ?name . ?person foaf:knows ?person2 ."
						+ " ?person2 foaf:firstName ?name2 . ?person2 prop:Id ?id2 FILTER (?name = \""+person.getName_first()+"\")}";
				
				query2 = QueryFactory.create(q2);
				qexec2 = QueryExecutionFactory.create(query2, model);
				r2 = qexec2.execSelect();
			
				
				
				
				
				while (r2.hasNext()){
					s2 = r2.nextSolution();
					person.setKnows(Integer.parseInt(s2.getLiteral("id2").toString()));
					
				}
				
				qexec2.close();
				persons.add(person);
				person = new Person();

			}
			
			qexec.close();
			return persons;
			
		} catch (Exception e){
			
			JOptionPane.showMessageDialog(null, "Erro ao obter dados da ontologia:\n" + e);
			return null;
		}
		
		
		
	}
	
	
	
	public static ArrayList<Integer> possibleriendsOf(Person person){
		
		try {
			
			
			ArrayList possibleFriends = new ArrayList<Integer>();
			
			FileManager.get().addLocatorClassLoader(Ontology.class.getClassLoader());
			Model model = FileManager.get().loadModel("RelateUsers.owl");
			
			String filter = "?id != ";
			for(int i = 0; i < person.getKnows().size(); i++){
				
				filter += "\""+person.getKnows().get(i)+"\"" + " && ?id != ";
				
			}
			
			filter += "\"" + person.getId() + "\"";
			
			// Obtem atributos de todos os usuários
			String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
						"PREFIX prop: <http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#>" +
						"SELECT * WHERE {"
						+ "?person prop:Id ?id FILTER ("+ filter +")"
						+ " }";
			
			Query query = QueryFactory.create(q);
			QueryExecution qexec = QueryExecutionFactory.create(query, model);
			ResultSet r = qexec.execSelect();
			QuerySolution s;
			
			while (r.hasNext()){

				s = r.nextSolution();
				
				possibleFriends.add(Integer.parseInt(s.getLiteral("id").toString()));
			}
			
			qexec.close();
			return possibleFriends;
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao buscar a lista de possiveis amigos: \n" + e);
		}
		
		return null;
		
	}
	
	
	public static int sameFriends(int idPerson1, int idPerson2){
		
		
		FileManager.get().addLocatorClassLoader(Ontology.class.getClassLoader());
		Model model = FileManager.get().loadModel("RelateUsers.owl");
		int count = 0;
		
		// Os campos nameX estão ai para facilitar o gerenciamente		
		String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
				"PREFIX prop: <http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#>" +
				"SELECT * WHERE {"
				+ "?person1 prop:Id ?id1 . ?person1 foaf:knows ?friends1 . ?friends1 foaf:firstName ?name1 . "
				+ "?person2 prop:Id ?id2 . ?person2 foaf:knows ?friends2 . ?friends2 foaf:firstName ?name2 ."
				+ "FILTER (?id1 = \""+ idPerson1 +"\" && ?id2 = \""+idPerson2+"\" && ?name1 = ?name2)"
				+ " }";
		
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet r = qexec.execSelect();
		QuerySolution s;
		
		while (r.hasNext()){

			s = r.nextSolution();
			count++;
			
		}
		qexec.close();
		return count;
		
	}



	public static void gerarArquivoOWL() {
		
		// Armazena as informações basícas dos individuos
		String individuos = " ";
		ArrayList<Person> persons = new ArrayList<Person>();
		ArrayList<Person> personsFrindns;

		
		PersonDAO dao;
		
		try {
			
			dao = new PersonDAO();
			persons = dao.getPreencharOntologia();
		
		
			for (int i =0; i < persons.size(); i++) {
				

				personsFrindns = new ArrayList<Person>();
				personsFrindns = dao.getPreencharOntologiaAmigos(persons.get(i).getId());
				individuos += 
						"\n\n\n<!-- http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#"+persons.get(i).getName_first()+"_"+persons.get(i).getName_last()+"-->\n\n\n"+
	
							"<owl:NamedIndividual rdf:about=\"&untitled-ontology-10;"+persons.get(i).getName_first()+"_"+persons.get(i).getName_last()+"\">\n"+
										
								        "<rdf:type rdf:resource=\"http://schema.org/Person\"/>\n"+
								        "<Id>"+persons.get(i).getId()+"</Id>\n"+
								        "<foaf:firstName>"+persons.get(i).getName_first()+"</foaf:firstName>\n"+
								        "<foaf:familyName>"+persons.get(i).getName_last()+"</foaf:familyName>\n"+
								       
								        "<disease>"+persons.get(i).getDisease()+"</disease>\n"+
								        
								        "<tempodoenca>"+persons.get(i).getTempodoenca()+"</tempodoenca>\n"+
								        "<foaf:gender>"+persons.get(i).getGender()+"</foaf:gender>\n"+
								        "<afinidade>"+persons.get(i).getAfinidade()+"</afinidade>\n"+
								        "<foaf:age>"+persons.get(i).getIdade()+"</foaf:age>\n";
				
										for(int j = 0; j < personsFrindns.size(); j++){
											
											individuos += "<foaf:knows rdf:resource=\"&untitled-ontology-10;"+personsFrindns.get(j).getName_first()+"_"+personsFrindns.get(j).getName_last()+"\"/>\n";
											
										}
										
										individuos += "</owl:NamedIndividual>";
				
			}
		
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
						        		
						      
		
		// String que contém as informações basícas do arquivo da ontologia
		// basta concatenar e escrever no arquivo
		String arquivoOWLbase = 
				"<?xml version=\"1.0\"?>\n"+
					"<!DOCTYPE rdf:RDF [\n"+
				    "<!ENTITY foaf \"http://xmlns.com/foaf/0.1/\">\n"+
				    "<!ENTITY owl \"http://www.w3.org/2002/07/owl#\" >\n"+
				    "<!ENTITY xsd \"http://www.w3.org/2001/XMLSchema#\" >\n"+
				    "<!ENTITY rdfs \"http://www.w3.org/2000/01/rdf-schema#\" >\n"+
				    "<!ENTITY rdf \"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" >\n"+
				    "<!ENTITY untitled-ontology-10 \"http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#\" >\n"+
				    "]>\n\n\n"+
				    
				    "<rdf:RDF xmlns=\"http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#\"\n"+
						"xml:base=\"http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10\"\n"+
						"xmlns:untitled-ontology-10=\"http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#\"\n"+
					    "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n"+
					    "xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\n"+
					    "xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n"+
					    "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\n"+
					    "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n"+
					    "<owl:Ontology rdf:about=\"http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10\">\n"+
					    "    <owl:imports rdf:resource=\"http://xmlns.com/foaf/0.1/\"/>\n"+
					    "</owl:Ontology>\n\n\n\n"+
    


					    "<!-- \n"+
					    "///////////////////////////////////////////////////////////////////////////////////////\n"+
					    "//\n"+
					    "// Object Properties\n"+
					    "//\n"+
					    "///////////////////////////////////////////////////////////////////////////////////////\n"+
					     "-->\n\n\n\n"+
					
					    "<!-- http://xmlns.com/foaf/0.1/knows -->\n\n"+
					
					    "<owl:ObjectProperty rdf:about=\"&foaf;knows\">\n"+
					        "<rdf:type rdf:resource=\"&owl;SymmetricProperty\"/>\n"+
					    "</owl:ObjectProperty>\n\n\n"+
					    
					
					
					    "<!-- \n"+
					    "///////////////////////////////////////////////////////////////////////////////////////\n"+
					    "//\n"+
					    "// Data properties\n"+
					    "//\n"+
					    "///////////////////////////////////////////////////////////////////////////////////////\n"+
					    "-->\n\n\n"+
					
					    
					
					
					    "<!-- http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#Id -->\n\n"+
					
					    "<owl:DatatypeProperty rdf:about=\"&untitled-ontology-10;Id\"/>\n\n\n"+
					    
					
					
					    "<!-- http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#afinidade -->\n\n"+
					
					    "<owl:DatatypeProperty rdf:about=\"&untitled-ontology-10;afinidade\"/>\n\n\n"+
					    
					
					
					    "<!-- http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#disease -->\n\n"+
					
					    "<owl:DatatypeProperty rdf:about=\"&untitled-ontology-10;disease\">\n"+
					        "<rdf:type rdf:resource=\"&owl;FunctionalProperty\"/>\n"+
					        "<rdfs:domain rdf:resource=\"http://purl.org/dc/terms/Agent\"/>\n"+
					        "<rdfs:range rdf:resource=\"&rdfs;Literal\"/>\n"+
					    "</owl:DatatypeProperty>\n\n\n\n"+
					    
					
					
					    "<!-- http://www.semanticweb.org/jerffeson/ontologies/2016/5/untitled-ontology-10#tempodoenca -->\n\n"+
					
					    "<owl:DatatypeProperty rdf:about=\"&untitled-ontology-10;tempodoenca\"/>\n\n\n\n"+
					    
					
					
					    "<!-- \n"+
					    "///////////////////////////////////////////////////////////////////////////////////////\n"+
					    "//\n"+
					    "// Individuals\n"+
					    "//\n"+
					    "///////////////////////////////////////////////////////////////////////////////////////\n"+
					    "-->" + 
					    
					    individuos 
					    
					    + "</rdf:RDF>";
		
		
		
		
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File("RelateUsers.owl"));
			arquivo.write(arquivoOWLbase);
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
	}
	
	
	

}
