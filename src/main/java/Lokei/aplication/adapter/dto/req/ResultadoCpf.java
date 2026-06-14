package Lokei.aplication.adapter.dto.req;

import lombok.Data;

@Data
public class ResultadoCpf {

    private String numero_de_cpf;
    private String nome_da_pf;
    private String data_nascimento;
    private String situacao_cadastral;
    private String data_inscricao;
    private String digito_verificador;
    private String comprovante_emitido;
    private String comprovante_emitido_data;

    public String getNumero_de_cpf() {
        return numero_de_cpf;
    }

    public void setNumero_de_cpf(String numero_de_cpf) {
        this.numero_de_cpf = numero_de_cpf;
    }

    public String getNome_da_pf() {
        return nome_da_pf;
    }

    public void setNome_da_pf(String nome_da_pf) {
        this.nome_da_pf = nome_da_pf;
    }

    public String getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getSituacao_cadastral() {
        return situacao_cadastral;
    }

    public void setSituacao_cadastral(String situacao_cadastral) {
        this.situacao_cadastral = situacao_cadastral;
    }

    public String getData_inscricao() {
        return data_inscricao;
    }

    public void setData_inscricao(String data_inscricao) {
        this.data_inscricao = data_inscricao;
    }

    public String getDigito_verificador() {
        return digito_verificador;
    }

    public void setDigito_verificador(String digito_verificador) {
        this.digito_verificador = digito_verificador;
    }

    public String getComprovante_emitido() {
        return comprovante_emitido;
    }

    public void setComprovante_emitido(String comprovante_emitido) {
        this.comprovante_emitido = comprovante_emitido;
    }

    public String getComprovante_emitido_data() {
        return comprovante_emitido_data;
    }

    public void setComprovante_emitido_data(String comprovante_emitido_data) {
        this.comprovante_emitido_data = comprovante_emitido_data;
    }
}