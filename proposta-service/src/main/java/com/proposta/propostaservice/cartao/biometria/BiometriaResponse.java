package com.proposta.propostaservice.cartao.biometria;

public class BiometriaResponse {
    public String biometria;
    public String cartao;

    public String getBiometria() {
        return biometria;
    }

    public String getCartao() {
        return cartao;
    }

    public BiometriaResponse(Biometria biometria) {
        this.biometria = biometria.getFingerPrint();
        this.cartao = biometria.getCartao().getId();
    }
}
