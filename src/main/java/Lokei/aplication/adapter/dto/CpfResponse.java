package Lokei.aplication.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CpfResponse {

    private boolean status;

    @JsonProperty("return")
    private String retorno;

    private int consumed;
    private ResultadoCpf result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public int getConsumed() {
        return consumed;
    }

    public void setConsumed(int consumed) {
        this.consumed = consumed;
    }

    public ResultadoCpf getResult() {
        return result;
    }

    public void setResult(ResultadoCpf result) {
        this.result = result;
    }
}

