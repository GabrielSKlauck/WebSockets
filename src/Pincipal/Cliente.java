package Pincipal;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import javax.swing.JOptionPane;

public class Cliente {

	public static void main(String[] args) {
		try {		
        Socket cliente = new Socket("localhost", 12345);
        
        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
       
        Cartao cartao = new Cartao("4034567890123456", "Gabriel", "12/2025", -7.0);
     
        saida.writeObject(cartao);
        saida.flush();

        String hash = (String) entrada.readObject();
        System.out.println(hash);
        JOptionPane.showMessageDialog(null, "Numero hash: " + hash);
                
        entrada.close();
        saida.close();
        cliente.close();

        System.out.println("Conexao encerrada");
        
    } catch (Exception e) {
        System.out.println(e);
    }

	}

}
