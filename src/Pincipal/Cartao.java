package Pincipal;

import java.io.Serializable;

public class Cartao implements Serializable{
	
	public String NCartao;
	public String Nome;
	public String DataExpiracao;
	public Double ValorTransacao;
	
	public Cartao(String nCartao, String nome, String dataExpiracao, Double valorTransacao) {
		super();
		
		NCartao = nCartao;
		Nome = nome;
		DataExpiracao = dataExpiracao;
		ValorTransacao = valorTransacao;
	}
	public String getNCartao() {
		return NCartao;
	}
	public void setNCartao(String nCartao) {
		NCartao = nCartao;
	}
	public String getNome() {
		return Nome;
	}
	public void setNome(String nome) {
		Nome = nome;
	}
	public String getDataExpiracao() {
		return DataExpiracao;
	}
	public void setDataExpiracao(String dataExpiracao) {
		DataExpiracao = dataExpiracao;
	}
	public Double getValorTransacao() {
		return ValorTransacao;
	}
	public void setValorTransacao(Double valorTransacao) {
		ValorTransacao = valorTransacao;
	}
	
	
}
