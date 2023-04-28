package pl.tomasz.smutek;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.cj.xdevapi.Client;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {

    public static void main(String[] args) {

        sqlConnection();

        internetConnection();
    }

    private static void internetConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/posts")
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
//            System.out.println(response.body().string());
            Gson gson = new Gson();
            List<Entry> entries = gson.fromJson(response.body().string(), new TypeToken<ArrayList<Entry>>(){}.getType());
            System.out.println(entries.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }

    private static void sqlConnection() {
        String jdbcSqliteUrl = "jdbc:sqlite:/C:\\dev\\sql\\ms.sqlite";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(jdbcSqliteUrl);

            statement = connection.createStatement();

            String sql = "select count(*) from tbMslProducts where mspName like '%a%'";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String result = resultSet.getString(1);
                System.out.println(result);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//        Class.forName("com.mysql.cj.jdbc.Driver");

        // JDBC URL for the MySQL database
        String jdbcMySqlUrl = "jdbc:mysql://localhost:3306/testdb";
        // Database user name and password
        String username = "development";
        String password = "development";

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(jdbcMySqlUrl, username, password);

            // Create a statement object for executing SQL queries
            statement = connection.createStatement();

            // Execute a query and get the results
            resultSet = statement.executeQuery("SELECT * FROM firsttable");

            // Iterate over the results and print them
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
            }

            // Close the database connection and release resources
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            // Handle any errors that occur during the connection or query execution
            e.printStackTrace();
        }
    }
}

