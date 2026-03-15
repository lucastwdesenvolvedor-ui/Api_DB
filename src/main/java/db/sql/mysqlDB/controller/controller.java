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
    String host = System.getenv("MYSQLHOST");
    String port = System.getenv("MYSQLPORT");
    String db = System.getenv("MYSQLDATABASE");
    String user = System.getenv("MYSQLUSER");
    String pass = System.getenv("MYSQLPASSWORD");

    String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&serverTimezone=UTC";

    @RequestMapping("/")
    public ArrayList<doc> bemVindo() {
        doc post = new doc("/post?" , "Requer parâmetros: nome=, &email=, &senha= , &log=");
        ArrayList<doc> dc = new ArrayList<>();
        dc.add(new doc("Bem Vindo" , "/"));
        dc.add(new doc("Pegar dados do DB" , "/get"));
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
    public ArrayList<Usuario> get() throws SQLException {
        ArrayList<Usuario> u = new ArrayList<>();

        Connection conn = Conn(url, user, pass);

        if (conn == null) {
            System.out.println("Conexão falhou");
        }

        String sql = "SELECT * FROM usuarios";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet res = ps.executeQuery();) {

            while (res.next()) {
                Usuario user = new Usuario(res.getInt("id"), res.getString("nome_usuario"), res.getString("email"), res.getString("senha"));
                u.add(user);
            }
            return u;
        } catch (SQLException e) {
            System.out.println("Erro ao executar consulta: " + e.getMessage());
            return u;
        }

    }
    @GetMapping("post")
    public ArrayList<String> post(@RequestParam String nome, @RequestParam String email, @RequestParam String senha , @RequestParam String log) throws SQLException {
if(!(log.equals("1") || log.equals("0"))){

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
            resposta.add("true");
            return resposta;
        }
        catch (Exception e){
            ArrayList<String> resposta = new ArrayList<>();
            resposta.add("Erro ao adicionar usuário: " + e.getMessage());
            return resposta;
        }

    }

}
