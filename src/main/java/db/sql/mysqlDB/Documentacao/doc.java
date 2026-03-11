package db.sql.mysqlDB.Documentacao;

public class doc {
    private String mensagem;
    private String rota;
    public doc(String mensagem, String rota) {
        this.mensagem = mensagem;
        this.rota = rota;
    }
    public doc(String mensagem , doc rota) {
        this.mensagem = mensagem;
        this.rota = rota.getMensagem()+" "+rota.getRota();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getRota() {
        return rota;
    }

    public void setRota(String rota) {
        this.rota = rota;
    }



}

