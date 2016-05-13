package br.ufg.inf.es.persistencia;

import java.sql.*;

/**
 * Ilustra operações básicas de
 * interação com SGBD relacional.
 */
public class Conexao {

    /**
     * Executa algumas operações com SGBD.
     * @param args Arquivo contendo banco SQLite.
     */
    public static void main(String[] args) {

        if (!CarregarDriver()) {
            System.out.println("Verifique classpath!!");
            System.exit(1);
        }

        Connection connection = obtemConexao(args[0]);

        if (connection == null) {
            System.out.println("Falha de conexao...");
            System.exit(1);
        }

        try {
            // De posse de uma Statement, várias operações podem
            // ser executadas, conforme ilustrado abaixo.
            Statement statement = connection.createStatement();

            statement.executeUpdate("drop table if exists colegas");
            statement.executeUpdate("create table colegas (nome string)");
            statement.executeUpdate("insert into colegas values('João')");
            statement.executeUpdate("insert into colegas values('Pedro')");

            // Recupera e exibe todos os registros de 'colegas'
            ResultSet rs = statement.executeQuery("select * from colegas");

            while (rs.next()) {
                System.out.println("Nome = " + rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }

        System.exit(0);
    }

    /**
     * Carrega driver JDBC. Cada SGBD possui
     * um driver JDBC correspondente.
     * Consulte a documentação do seu SGBD para
     * detalhes.
     *
     * Esse exemplo faz uso do driver JDBC para
     * o SQLite.
     *
     * @return {@code true} se foi possível a
     * carga do driver e {@code false}, caso
     * contrário.
     */
    private static boolean CarregarDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
            return true;
        } catch (ClassNotFoundException exp) {
            return false;
        }
    }

    /**
     * Obtém conexão para um dado banco de dados
     * criado pelo SQLite.
     *
     * @param arquivo Nome completo do arquivo contendo
     *                o banco de dados para o qual a
     *                conexão será criada.
     *
     * @return A referência para a conexão, caso tenha sido
     * obtida de forma satisfatória ou o valor {@code null},
     * caso contrário.
     */
    private static Connection obtemConexao(String arquivo) {
        try {
            String url = "jdbc:sqlite:" + arquivo;
            return DriverManager.getConnection(url);
        } catch (SQLException sql) {
            return null;
        }
    }
}
