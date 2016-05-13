package br.ufg.inf.es.persistencia;

import java.sql.*;

public class Conexao {
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
            Statement statement = connection.createStatement();
            statement.executeUpdate("drop table if exists alunos");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }

        System.exit(0);
    }

    private static boolean CarregarDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
            return true;
        } catch (ClassNotFoundException exp) {
            return false;
        }
    }

    private static Connection obtemConexao(String arquivo) {
        try {
            String url = "jdbc:sqlite:" + arquivo;
            return DriverManager.getConnection(url);
        } catch (SQLException sql) {
            return null;
        }
    }
}
