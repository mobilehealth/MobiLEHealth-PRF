package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import database.CriaConexao;
import domain.Person;

public class PersonDAO {
	
	
	// Cria uma variavel do tipo Conexao
    private Connection conexao;

    // Contrutor usado para iniicar a variavel conexao com tipo CriaConexao
    public PersonDAO () throws SQLException {
        this.conexao = (Connection) CriaConexao.getConexao();
    }

    public void adiciona(int friendTo, int possiblesFriend) throws SQLException {
    	
    	if(getAmizade(friendTo, possiblesFriend)){
	        // Prepara o comando sql
	        String sql = "insert into app.possiblefriends (id, friendto, possiblefriend) values (nextval('app.possiblefriends_id_seq'),?,?)";
	
	        // Armazena o objeto conexao.prepareStatement(sql) dentro da variavel stmt
	        PreparedStatement stmt = conexao.prepareStatement(sql);
	
	        // Substitui os valores adicionados na variavel sql, assim sendo 1, a posição do campo nome
	        // sendo substituido pelo retorno do metodo getNome() da class Contato
	        stmt.setInt(1, friendTo);
	        stmt.setInt(2, possiblesFriend);
	
	        // Executa e para o stmt
	        //executa o comando sql
	        stmt.execute();
	        stmt.close();
	        
	        System.out.println("friendTo: " + friendTo + " - " + "possiblesFriend: " + possiblesFriend);
	        
    	}
    }
    
    
    
    
    
    
    public boolean getAmizade(int friendTo, int possiblesFriend) throws SQLException {

        // Prepara o comando sql
        String sql = "select * from app.possiblefriends where friendto=? and possiblefriend=?";
        boolean flag = true;
        // Armazena o objeto conexao.prepareStatement(sql) dentro da variavel stmt
        PreparedStatement stmt = this.conexao.prepareStatement(sql);
        stmt.setInt(1, friendTo);
        stmt.setInt(2, possiblesFriend);
        
        // Cria o objeto ResultSet
        // Onde sera armazenado os valores buscados no banco de dados
        ResultSet rs = stmt.executeQuery();

        //Enquanto existir valores no ResultSet
        // o loop sera efetuado
        while (rs.next()) {
        	flag = false;
        }

        rs.close();
        stmt.close();
        return flag;
    }
    

}
