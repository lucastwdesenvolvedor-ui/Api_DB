package db.sql.mysqlDB.controller;

import db.sql.mysqlDB.Documentacao.doc;
import db.sql.mysqlDB.usuario.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class controller {
    String API_KEY = System.getenv("key");
    String host = System.getenv("MYSQLHOST");
    String port = System.getenv("MYSQLPORT");
    String db = System.getenv("MYSQLDATABASE");
    String user = System.getenv("MYSQLUSER");
    String pass = System.getenv("MYSQLPASSWORD");

    String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&serverTimezone=UTC";

    @RequestMapping("/")
    public ArrayList<doc> bemVindo() {
        doc post = new doc("/post?" , "Requer parâmetros: (KEY) nome=, &email=, &senha= , &log=");
        ArrayList<doc> dc = new ArrayList<>();
        dc.add(new doc("Bem Vindo" , "/"));
        dc.add(new doc("Pegar dados do DB" , "/get?(KEY)&"));
        dc.add(new doc("Inserir dados no DB" , post +" ||log = logado(1) / não loado(0) "));
        return dc;
    }
    public Connection Conn(String url, String user, String password) {
        Connection conn = null;
        try {
            conn = java.sql.DriverManager.getConnection(url, user, password);
            return conn;
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return conn;
        }

    }

    @GetMapping("/get")
    public ArrayList<Usuario> get(@RequestParam String key) throws SQLException {
        ArrayList<Usuario> u = new ArrayList<>();
        boolean iguals = key.equals(API_KEY);
        if(!iguals){
        Usuario err = new Usuario(401, String.valueOf(iguals), "erro" ,"erro");
            u.add(err);
            return u;
        }


        Connection conn = Conn(url, user, pass);
        if (conn == null) {
            Usuario err = new Usuario(500, "connect", "server","error" );
            u.add(err);
            return u;
        }

        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet res = ps.executeQuery())
        {
            while (res.next()) {
                Usuario user = new Usuario(res.getInt("id"), res.getString("nome"), res.getString("email"), res.getString("senha"));
                u.add(user);
            }
            return u;
        } catch (SQLException e) {
            Usuario err = new Usuario(401, String.valueOf(iguals), "erro" ,"erro");
            u.add(err);
            return u;
        }

    }
    @GetMapping("/post")
    public ArrayList<String> post(@RequestParam String key, @RequestParam String nome, @RequestParam String email, @RequestParam String senha , @RequestParam String log) throws SQLException {
        boolean iguals = key.equals(API_KEY);
        if(!iguals){
            ArrayList<String> u = new ArrayList<>();
            u.add("401 erro unautorized");
            u.add(String.valueOf(key.length()));
            u.add(String.valueOf(API_KEY.length()));
            return u;
        }

        if(!(log.equals("1") || log.equals("0"))){
            ArrayList<String> err = new ArrayList<>() ;
            err.add("erro");
            return err;
        }
        String sql = "INSERT INTO usuarios (nome, email, pass, logado) VALUES (?, ?, ?,?)";
        try (Connection conn = Conn(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha);
            ps.setString(4, log);
            ps.executeUpdate();
            ArrayList<String> resposta = new ArrayList<>();
            resposta.add("True");
            return resposta;
        }
        catch (Exception e){
            ArrayList<String> resposta = new ArrayList<>();
            resposta.add("Erro ao adicionar usuário: " + e.getMessage());
            return resposta;
        }

    }

}
