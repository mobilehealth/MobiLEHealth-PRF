package PRF;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import DAO.PersonDAO;
import ontology.Ontology;
import domain.Person;

public class AlgoritmoGenetico {
	
	
	
	private ArrayList<ArrayList<Person>> populacao;
    private int tamCromossomo = 3;
    private int tamPopulacao = 1000;
    private double avaliacao[];
    private ArrayList<Person> melhorRepresentacao;
    private double melhorAvaliacao = -100000;
    private int geracao = 0;
    ArrayList<Person> persons;
    Ontology ontologia = new Ontology();
    
    private void iniciarPopulacao(Person person) {
    	
    	
    	
    	/* Retorno uma lista com:
    	 
    		- Os amigos dos meus amigos e
    		- Demais usuários
    		
    	*/
    	ArrayList<Integer> IdPossibleFriends = ontologia.possibleriendsOf(person);
    	
        melhorRepresentacao = new ArrayList<Person>();
        melhorAvaliacao = -100000;
        geracao = 0;
    	
        this.populacao = new ArrayList<ArrayList<Person>>();
        Random random = new Random();	
        
        for(int i = 0; i < tamPopulacao; i++){
        	
        	ArrayList<Person> cromoTemporario = new ArrayList<Person>();
        	
            for(int j = 0; j < tamCromossomo; j++){

            	// Adiciona ao cromossomo a pessoa que tenha o id sorteado
            	cromoTemporario.add(find(IdPossibleFriends.get(random.nextInt(IdPossibleFriends.size()))));
	
            }
            this.populacao.add(cromoTemporario);
        }
        
    }
	
    private void avaliarPopulacao(Person person){
        
        avaliacao = new double[tamPopulacao];
        
      //Percorre todos os individuos da população e os avalia
        for(int i = 0; i < tamPopulacao; i++){
        	//System.out.println("\tAnalisando indiviudo: " + i + " de 300");
        	double fatorIdade = 0;
			int fatorAmigos = 0;
			int fatorTempo = 0;
			int fatorAfinidade = 0;
			int fatorDoenca = 0;
			int fatorGenero = 0;
			
			double auxIdade = 1;

			for(int j = 0; j < tamCromossomo; j++){
				if(person.getDisease().equalsIgnoreCase(populacao.get(i).get(j).getDisease())){
					fatorDoenca++;
				}
			}

			
			for(int j = 0; j < tamCromossomo; j++){
				if(person.getAfinidade().equalsIgnoreCase(populacao.get(i).get(j).getAfinidade())){
					fatorAfinidade++;
				}
			}

			
			for(int j = 0; j < tamCromossomo; j++){
				if(person.getTempodoenca().equalsIgnoreCase(populacao.get(i).get(j).getTempodoenca())){
					fatorTempo++;
				}
			}
			
			for(int j = 0; j < tamCromossomo; j++){
				
				if(populacao.get(i).get(j).getSameFriends() == null){
					populacao.get(i).get(j).setSameFriends(ontologia.sameFriends(person.getId(), populacao.get(i).get(j).getId()));
					fatorAmigos += populacao.get(i).get(j).getSameFriends();
					//fatorAmigos += ontologia.sameFriends(person.getId(), populacao.get(i).get(j).getId());
				} else {
					
					fatorAmigos += populacao.get(i).get(j).getSameFriends();
					
				}
				
				
			}
			
			for(int j = 0; j < tamCromossomo; j++){
				if(person.getGender().equalsIgnoreCase(populacao.get(i).get(j).getGender())){
					fatorGenero++;
				}
			}
			
			for(int j = 0; j < tamCromossomo; j++){
				
				if(person.getAge() > populacao.get(i).get(j).getAge()){
					auxIdade += (person.getAge() - populacao.get(i).get(j).getAge());
				} else {
					auxIdade += (populacao.get(i).get(j).getAge() - person.getAge());
				}
			}
			
			fatorIdade = person.getAge()/auxIdade;
			
			for(int j = 0; j < tamCromossomo; j++){
				int count = 0;
				for(int k = 0; k < tamCromossomo; k++){
					if(populacao.get(i).get(j).getId() == populacao.get(i).get(k).getId()){
						count++;
						
						
						/*
						if(person.getDisease().equalsIgnoreCase(populacao.get(i).get(j).getDisease())){
							System.out.println("\t\t****Fador doença: " + fatorDoenca + "--");
							fatorDoenca--;
						}
						
						if(person.getAfinidade().equalsIgnoreCase(populacao.get(i).get(j).getAfinidade())){
							fatorAfinidade--;
						}
						
						if(person.getTempodoenca().equalsIgnoreCase(populacao.get(i).get(j).getTempodoenca())){
							fatorTempo--;
						}
						
						if(person.getGender().equalsIgnoreCase(populacao.get(i).get(j).getGender())){
							fatorGenero--;
						}
						

						*/
					}
					
				}
				
				if(count >=2){
					fatorIdade = 0;
					fatorAmigos = 0;
					fatorTempo = 0;
					fatorAfinidade = 0;
					fatorDoenca = 0;
					fatorGenero = 0;
					
				}
			}
			

			//System.out.println("\t\tfator Idade: " + fatorIdade);
			// Função Fitness
			avaliacao[i] = 0.2*(fatorDoenca) + 0.1*(fatorAfinidade) + 0.3*(fatorTempo) + 0.2*(fatorAmigos) + 0.1*(person.getAge()/ auxIdade) + 0.1*fatorGenero;
			
			//System.out.println("\t0.2*" + fatorDoenca + " + 0.1*" + fatorAfinidade + " + 0.3*"+ fatorTempo + " + 0.2*" +fatorAmigos + " + 0.1*"+ "(" + person.getAge()+ "/" +auxIdade + ")" + " + 0.1*" + fatorGenero + " = " + avaliacao[i]);
			//System.out.println("\tAvaliação: " + avaliacao[i]);
			if(avaliacao[i] > melhorAvaliacao){
                melhorAvaliacao = avaliacao[i];
                melhorRepresentacao = populacao.get(i);
            }
			
        }
        
        System.out.println("melhor avaliação: " + melhorAvaliacao);
        
	}

    
    
    
    private void selecionarPais(){
        
    	int numeroDePais = tamPopulacao/2;    	
    	ArrayList<ArrayList<Person>> novosPais = new ArrayList<ArrayList<Person>>();
        
        int melhorPai = 0;
        double melhorAvaliacao = 0; 
        
        for(int k = 0; k < numeroDePais; k++){
        	for(int i = 0; i < tamPopulacao; i++){
        		if(avaliacao[i] >= melhorAvaliacao){
        			melhorAvaliacao = avaliacao[i];
        			melhorPai = i;
        		}
        	}
        	novosPais.add(populacao.get(melhorPai));         	
        }
        this.populacao = novosPais;
    }
    
    
    private void cruzar(Person person){
        
        Random random = new Random();
        int metadePopulacao = tamPopulacao/2;
        int a;
        int b;
        for(int i = 0; i < metadePopulacao; i++){
        	//System.out.println("\tcruzamento: " + i + " de " + metadePopulacao);
        	
        	a = random.nextInt(metadePopulacao);
        	b = random.nextInt(metadePopulacao);
        	
        	ArrayList<Person> cromo1 = populacao.get(a);
        	ArrayList<Person> cromo2 = populacao.get(b);
        	ArrayList<Person> cromoFilho = new ArrayList<Person>();

            for(int j = 0; j < tamCromossomo; j++){
            	
            	if(cromo1.get(j).getSameFriends() == null){
            		
					populacao.get(a).get(j).setSameFriends(ontologia.sameFriends(person.getId(), cromo1.get(j).getId()));
					cromo1 = populacao.get(a);
					//fatorAmigos += populacao.get(i).get(j).getSameFriends();
					//fatorAmigos += ontologia.sameFriends(person.getId(), populacao.get(i).get(j).getId());
					
				} 
            	
            	
            	if(cromo2.get(j).getSameFriends() == null){
            		
					populacao.get(b).get(j).setSameFriends(ontologia.sameFriends(person.getId(), cromo2.get(j).getId()));
					cromo2 = populacao.get(b);
					//fatorAmigos += populacao.get(i).get(j).getSameFriends();
					//fatorAmigos += ontologia.sameFriends(person.getId(), populacao.get(i).get(j).getId());
					
				} 
            	
            	
            	// Quem tiver mais amigos em comum será selecionado
            	if(cromo1.get(j).getSameFriends() > cromo2.get(j).getSameFriends()){
            		cromoFilho.add(cromo1.get(j));
            	} else {
            		cromoFilho.add(cromo2.get(j));
            	}           
            }
            populacao.add(cromoFilho);
        }
    }
    
    
    public void mutar(Person person){
    	
    	// Porcentagem de individuos que devem sofre mutação
    	int taxa = 10;

    	
    	for(int a = 0; a < (populacao.size()*(taxa/100.0)); a++){
	    	
    		Random random = new Random();
	    	
	    	int x = random.nextInt(100);
	    	random = new Random();
	    	int maiorMenor = random.nextInt(2);
	    	random = new Random();
	    	
	    	int y = 0;
	    	if(maiorMenor == 0 && x > 0){
	    		// Gera números entre 1 e x 
	    		y = (random.nextInt(x)+1);
	    		
	    	} else {
	    		//Gera números entre x e 100
	    		y =  (random.nextInt((100-x)) + x);
	    		
	    	}
	    	
	    	random = new Random();
	    	int z = random.nextInt(100);
	
	    		
	    	if(((y <= z) && (z <= x)) || ((x <= z) && (z <= y))){
	    			
	    		//System.out.println("\t >>>>>>>> Houve mutaçao <<<<<<<<<<<<<<");
	    		random = new Random();
	    		// Escolhe qual membro da população vai sofrer a mutação
	        	int p1 = random.nextInt(tamPopulacao);
	        	
	        	random = new Random();
	        	// Em qual posição do cromossomo desse mebro
	        	int c1 = random.nextInt(tamCromossomo);
	    		
	        	random = new Random();
	    		// Escolhe qual membro da população vai ser usado na mutação
	        	int p2;
	        	Person cromoTemp;
	        	
	        	do{
	        		
	        		p2 = random.nextInt(this.persons.size());
	        		cromoTemp = this.persons.get(p2);
			
	        	} while (this.persons.get(p2).getId() == person.getId());
	        	
	        	populacao.get(p1).remove(c1);
	        	populacao.get(p1).add(cromoTemp);
	    		
	    	} else {
	    		//System.out.println("\t >>>>>>>> Não Houve mutaçao <<<<<<<<<<<<<<");
	    	}

    	}
    	
    }
    
    
	private Person find(int id) {
		
		// Procura a pessoa com o Id recebido por parametro
		for(int i = 0; i < this.persons.size(); i++ ){
			
			if(id == this.persons.get(i).getId()){
				return this.persons.get(i);
			}
			
		}
		
		return null;
	}


	public ArrayList<Person> algortimoGentico(ArrayList<Person> persons){
		this.persons = persons;
		try {
			
			PersonDAO personDao = new PersonDAO();
		
			for(int i = 0; i < this.persons.size(); i++){
				
				//System.out.println("\n\nAnalisando: " + this.persons.get(i).getName_first());
				iniciarPopulacao(this.persons.get(i));
				//System.out.println("População para: " + this.persons.get(i).getName_first() + " Gerada com sucesso");
				
				
				for(int geracao =0; geracao < 120; geracao++){
					
					//System.out.println("\nGeração: " + geracao + " Para: " + this.persons.get(i).getName_first());
					
					avaliarPopulacao(this.persons.get(i));
					/*
					for(int h =0; h < populacao.size(); h++){
						//System.out.println("individuo: " + h);
						for(int k =0; k < populacao.get(h).size(); k++){
							//System.out.println("\nNome: " + populacao.get(h).get(k).getName_first() + " - " + populacao.get(h).get(k).getId());
						}
					}
					*/
					//System.out.println("Geração: " + geracao + " Avaliada com sucesso");
					
					
					//System.out.println("\nSelecionado pais da geração: " + geracao + " Para: " + this.persons.get(i).getName_first());
					selecionarPais();
					//System.out.println("Pais da geração: " + geracao + " selecionados com sucesso");
					
					//System.out.println("\nCruzando individuos da geração: " + geracao);
					cruzar(this.persons.get(i));
					//System.out.println("Cruzando dos individuos da geração: " + geracao + " realizado com sucesso");
					
					mutar(this.persons.get(i));
					
				}
				
				avaliarPopulacao(this.persons.get(i));
				System.out.println("\n\nAnali de : " + this.persons.get(i).getName_first() + "Foi finalisada com sucesso");
				System.out.println("Recomendação para: " + this.persons.get(i).getName_first() + " - " + this.persons.get(i).getId() + " foi:\n");
				for(int w =0; w < this.melhorRepresentacao.size(); w++){
					personDao.adiciona(this.persons.get(i).getId(), melhorRepresentacao.get(w).getId());
					System.out.println(melhorRepresentacao.get(w).getName_first() + " - " + melhorRepresentacao.get(w).getId());
				}
				
			}
			
			return this.melhorRepresentacao;
		
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro ao armazenar informações na base de dados: " + e);
			return null;
		}
	}
	

}
