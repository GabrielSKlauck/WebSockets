package Pincipal;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Date;


public class Servidor implements MetodoCobranca{

	public static void main(String[] args) {
		try {
			ServerSocket servidor = new ServerSocket(12345);
            System.out.println("Servidor iniciado na porta " + servidor.getLocalPort());
            
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                
                try (ObjectInputStream entradaDadosCartao = new ObjectInputStream(cliente.getInputStream());
                        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream())) {
                                             
                       Cartao cartao = (Cartao) entradaDadosCartao.readObject();
                                             
                       String hash = dadosTransacao(cartao);
                       
                       saida.writeObject(hash);
                       saida.flush();
                   } catch (Exception e) {
                       System.out.println(e.getMessage());
                       e.printStackTrace();
                   } finally {
                       cliente.close();
                   }
            }
		}catch(Exception e) {
			System.out.println(e);
		}

	}

	public static String dadosTransacao(Cartao cartao) {
		String hashCompleto = geraHash(cartao);
		String tipoCartao = defineTipoCartao(cartao);
		if(validacoes(cartao)) {
			if(tipoCartao.equals("visa")){
				hashCompleto += ":" + cartao.getNome() + ":" + cartao.getNCartao().substring(10,16) +
						":" + cartao.getDataExpiracao() + ":" + cartao.getValorTransacao() + ":" + (cartao.getValorTransacao() - 12.59);
				return hashCompleto;
			}else if(tipoCartao.equals("mastercard")){
				hashCompleto += ":" +  cartao.getNCartao().substring(10,16) + ":" + cartao.getValorTransacao() +
						":" + cartao.getValorTransacao() + ":" + (cartao.getValorTransacao() - 7.57);
				return hashCompleto;
			}

		};
		return "";
	}

	private static String geraHash(Cartao cartao){
		String hashCompleto = "";
		//Gera hash
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigest[] = algorithm.digest(cartao.getNCartao().getBytes("UTF-8"));
			hashCompleto = messageDigest.toString();
			return hashCompleto;
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Erro 3006");
		}
	}
	private static String defineTipoCartao(Cartao cartao){
		if(cartao.getNCartao().startsWith("4")) {
			return "visa";
		}
		int doisPrimeiros = Integer.parseInt(cartao.getNCartao().substring(0,2));
		if(doisPrimeiros >= 51 && doisPrimeiros <= 55){
			return "mastercard";
		}
		throw new IllegalArgumentException("Erro 3000");
	}
	private static boolean validacoes(Cartao cartao) {
		String tipoCartao = defineTipoCartao(cartao);
		String codigoErro = "";
		if(tipoCartao.equals("visa")){
			codigoErro = "300";
		} else if (tipoCartao.equals("mastercard")) {
			codigoErro = "400";
		}
		if(!(cartao.getNome().length() != 16)) {
			throw new IllegalArgumentException("Erro " + codigoErro + "1");
		}
		if(cartao.getNome().isBlank() || cartao.getNome().isEmpty()) {
			throw new IllegalArgumentException("Erro " + codigoErro + "3");
		}
		
		int mesAtual = LocalDate.now().getMonthValue();
		int anoAtual = LocalDate.now().getYear();
		String mesAnoCartao[] = cartao.getDataExpiracao().split("/");
		int mesCartao = Integer.parseInt(mesAnoCartao[0]);
		int anoCartao = Integer.parseInt(mesAnoCartao[1]);
		
		if(anoCartao < anoAtual) {
			throw new IllegalArgumentException("Erro " + codigoErro + "4");
		}else {
			if(mesCartao < mesAtual) {	
				throw new IllegalArgumentException("Erro " + codigoErro + "4");
			}
		}
		if(cartao.getValorTransacao() < 0) {
			throw new IllegalArgumentException("Erro " + codigoErro + "5");
		}
		return true;
	}

	@Override
	public double calcularValorComComisao(double valor) throws RemoteException {		
		return (valor * 5)/100;
	}
}







