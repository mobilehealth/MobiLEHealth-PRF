package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import database.CriaConexao;
import domain.Person;

public class PersonDAO {

	// Cria uma variavel do tipo Conexao
	private Connection conexao;

	// Contrutor usado para iniicar a variavel conexao com tipo CriaConexao
	public PersonDAO() throws SQLException {
		this.conexao = (Connection) CriaConexao.getConexao();
	}

	public void adiciona(int friendTo, int possiblesFriend) throws SQLException {

		if (getAmizade(friendTo, possiblesFriend)) {
			// Prepara o comando sql
			String sql = "insert into app.possiblefriends (id, friendto, possiblefriend) values (nextval('app.possiblefriends_id_seq'),?,?)";

			// Armazena o objeto conexao.prepareStatement(sql) dentro da
			// variavel stmt
			PreparedStatement stmt = conexao.prepareStatement(sql);

			// Substitui os valores adicionados na variavel sql, assim sendo 1,
			// a posiзгo do campo nome
			// sendo substituido pelo retorno do metodo getNome() da class
			// Contato
			stmt.setInt(1, friendTo);
			stmt.setInt(2, possiblesFriend);

			// Executa e para o stmt
			// executa o comando sql
			stmt.execute();
			stmt.close();

			System.out.println("friendTo: " + friendTo + " - "
					+ "possiblesFriend: " + possiblesFriend);

		}
	}

	public boolean getAmizade(int friendTo, int possiblesFriend)
			throws SQLException {

		// Prepara o comando sql
		String sql = "select * from app.possiblefriends where friendto=? and possiblefriend=?";
		boolean flag = true;
		// Armazena o objeto conexao.prepareStatement(sql) dentro da variavel
		// stmt
		PreparedStatement stmt = this.conexao.prepareStatement(sql);
		stmt.setInt(1, friendTo);
		stmt.setInt(2, possiblesFriend);

		// Cria o objeto ResultSet
		// Onde sera armazenado os valores buscados no banco de dados
		ResultSet rs = stmt.executeQuery();

		// Enquanto existir valores no ResultSet
		// o loop sera efetuado
		while (rs.next()) {
			flag = false;
		}

		rs.close();
		stmt.close();
		return flag;
	}

	public ArrayList<Person> getPreencharOntologia() throws SQLException {

		// Prepara o comando sql
		String sql = "select p.id, p.name_first, p.name_last, p.disease, p.gender, p.date_birth, u.tempodoenca, u.afinidade from public.person p inner join app.users u on p.id = u.person_id";

		// Armazena o objeto conexao.prepareStatement(sql) dentro da variavel
		// stmt
		PreparedStatement stmt = this.conexao.prepareStatement(sql);

		// Cria o objeto ResultSet
		// Onde sera armazenado os valores buscados no banco de dados
		ResultSet rs = stmt.executeQuery();
		ArrayList lista = new ArrayList<Person>();
		String aux_firstName;
		String aux_lastName;
		// Enquanto existir valores no ResultSet
		// o loop sera efetuado
		while (rs.next()) {

			try {
				
				
				
				
				
				aux_firstName = rs.getString("name_first");
				aux_firstName = aux_firstName.toLowerCase();
				
				aux_firstName = aux_firstName.replace("б", "a");
				aux_firstName = aux_firstName.replace("й", "e");
				aux_firstName = aux_firstName.replace("н", "i");
				aux_firstName = aux_firstName.replace("у", "o");
				aux_firstName = aux_firstName.replace("ъ", "u");
				
				aux_firstName = aux_firstName.replace("в", "a");
				aux_firstName = aux_firstName.replace("к", "e");
				aux_firstName = aux_firstName.replace("о", "i");
				aux_firstName = aux_firstName.replace("ф", "o");
				aux_firstName = aux_firstName.replace("ы", "u");
				
				aux_firstName = aux_firstName.replace("г", "a");
				aux_firstName = aux_firstName.replace("х", "o");
				aux_firstName = aux_firstName.replace("з", "c");
				
				aux_firstName = aux_firstName.replace("а", "a");
				aux_firstName = aux_firstName.replace("и", "e");
				aux_firstName = aux_firstName.replace("м", "i");
				aux_firstName = aux_firstName.replace("т", "o");
				aux_firstName = aux_firstName.replace("щ", "u");
				
				
				
				
				aux_lastName = rs.getString("name_last");
				aux_lastName = aux_lastName.toLowerCase();
				
				aux_lastName = aux_lastName.replace("б", "a");
				aux_lastName = aux_lastName.replace("й", "e");
				aux_lastName = aux_lastName.replace("н", "i");
				aux_lastName = aux_lastName.replace("у", "o");
				aux_lastName = aux_lastName.replace("ъ", "u");
				
				aux_lastName = aux_lastName.replace("в", "a");
				aux_lastName = aux_lastName.replace("к", "e");
				aux_lastName = aux_lastName.replace("о", "i");
				aux_lastName = aux_lastName.replace("ф", "o");
				aux_lastName = aux_lastName.replace("ы", "u");
				
				aux_lastName = aux_lastName.replace("г", "a");
				aux_lastName = aux_lastName.replace("х", "o");
				aux_lastName = aux_lastName.replace("з", "c");
				
				aux_lastName = aux_lastName.replace("а", "a");
				aux_lastName = aux_lastName.replace("и", "e");
				aux_lastName = aux_lastName.replace("м", "i");
				aux_lastName = aux_lastName.replace("т", "o");
				aux_lastName = aux_lastName.replace("щ", "u");
				

				Person temp = new Person();
				// pega todos os atributos da pessoa
				temp.setId(Integer.parseInt(rs.getString("id")));
				temp.setName_first(aux_firstName);
				temp.setName_last(aux_lastName);
				temp.setDisease(rs.getString("disease"));
				temp.setGender(rs.getString("gender"));

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar date = Calendar.getInstance();
				date.setTime(sdf.parse(rs.getString("date_birth")));
				temp.setAge(date);

				temp.setTempodoenca(rs.getString("tempodoenca"));
				temp.setAfinidade(rs.getString("afinidade"));

				lista.add(temp);

			} catch (ParseException e) {
				System.out.println("Erro ao pegar dados: " + e);
			}

		}

		rs.close();
		stmt.close();
		return lista;
	}

	public ArrayList<Person> getPreencharOntologiaAmigos(int id)
			throws SQLException {

		// Prepara o comando sql
		String sql = "select p.name_last, p.name_first from public.person p where id in (select f.id_following from public.person p inner join app.follow f on p.id = f.id_follower and f.id_follower = ?)";

		// Armazena o objeto conexao.prepareStatement(sql) dentro da variavel
		// stmt
		PreparedStatement stmt = this.conexao.prepareStatement(sql);
		stmt.setInt(1, id);
		// Cria o objeto ResultSet
		// Onde sera armazenado os valores buscados no banco de dados
		ResultSet rs = stmt.executeQuery();
		ArrayList lista = new ArrayList<Person>();
		String aux_firstName;
		String aux_lastName;
		
		// Enquanto existir valores no ResultSet
		// o loop sera efetuado
		while (rs.next()) {

			
			
			aux_firstName = rs.getString("name_first");
			aux_firstName = aux_firstName.toLowerCase();
			
			aux_firstName = aux_firstName.replace("б", "a");
			aux_firstName = aux_firstName.replace("й", "e");
			aux_firstName = aux_firstName.replace("н", "i");
			aux_firstName = aux_firstName.replace("у", "o");
			aux_firstName = aux_firstName.replace("ъ", "u");
			
			aux_firstName = aux_firstName.replace("в", "a");
			aux_firstName = aux_firstName.replace("к", "e");
			aux_firstName = aux_firstName.replace("о", "i");
			aux_firstName = aux_firstName.replace("ф", "o");
			aux_firstName = aux_firstName.replace("ы", "u");
			
			aux_firstName = aux_firstName.replace("г", "a");
			aux_firstName = aux_firstName.replace("х", "o");
			aux_firstName = aux_firstName.replace("з", "c");
			
			aux_firstName = aux_firstName.replace("а", "a");
			aux_firstName = aux_firstName.replace("и", "e");
			aux_firstName = aux_firstName.replace("м", "i");
			aux_firstName = aux_firstName.replace("т", "o");
			aux_firstName = aux_firstName.replace("щ", "u");
			
			
			
			
			aux_lastName = rs.getString("name_last");
			aux_lastName = aux_lastName.toLowerCase();
			
			aux_lastName = aux_lastName.replace("б", "a");
			aux_lastName = aux_lastName.replace("й", "e");
			aux_lastName = aux_lastName.replace("н", "i");
			aux_lastName = aux_lastName.replace("у", "o");
			aux_lastName = aux_lastName.replace("ъ", "u");
			
			aux_lastName = aux_lastName.replace("в", "a");
			aux_lastName = aux_lastName.replace("к", "e");
			aux_lastName = aux_lastName.replace("о", "i");
			aux_lastName = aux_lastName.replace("ф", "o");
			aux_lastName = aux_lastName.replace("ы", "u");
			
			aux_lastName = aux_lastName.replace("г", "a");
			aux_lastName = aux_lastName.replace("х", "o");
			aux_lastName = aux_lastName.replace("з", "c");
			
			aux_lastName = aux_lastName.replace("а", "a");
			aux_lastName = aux_lastName.replace("и", "e");
			aux_lastName = aux_lastName.replace("м", "i");
			aux_lastName = aux_lastName.replace("т", "o");
			aux_lastName = aux_lastName.replace("щ", "u");
			
			
			
			
			
			
			
			Person temp = new Person();

			temp.setName_first(aux_firstName);
			temp.setName_last(aux_lastName);

			lista.add(temp);

		}

		rs.close();
		stmt.close();
		return lista;
	}

}
