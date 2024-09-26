package Pincipal;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MetodoCobranca extends Remote{
	double calcularValorComComisao(double valor) throws RemoteException;
}
