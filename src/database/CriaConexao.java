package database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class CriaConexao {
	
	
	// Metodo para criar a conexao com o Banco de dados
    public static Connection getConexao() throws SQLException {
        try {
            // Identifica qual o tipo de Banco de Dados sera usado na aplicação(MySqL, PostGray..)
            Class.forName("org.postgresql.Driver");
            System.out.println("Conectdado ao Banco");

            // Retorna os dados da conexao: "Tipo de Banco://Endereço do Banco/Nome do banco", "Nome de usuario do Banco", "senha do banco"
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/mobilehealth2", "postgres", "postgres");

            //Retorna as mensagens de Erro, caso a conexao falhi.
        } catch (ClassNotFoundException e) {
            System.out.println("O driver expecificado nao foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao Banco de Dados.");
            return null;
        }



    }
	

}
