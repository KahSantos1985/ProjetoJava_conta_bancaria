import controle.Conta;
import controle.ContaCorrente;
import controle.ContaPoupanca;
import modelo.Cliente;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
  public static void main(String[] args) {
    CriarBanco();
  }

  private static void CriarBanco() {
    JFrame f = new JFrame();
    StringBuilder historico = new StringBuilder();

    String nome = JOptionPane.showInputDialog(f, "Entre o Nome do Cliente");
    Cliente cliente = new Cliente();
    cliente.setNome(nome);
    Conta cc = new ContaCorrente(cliente);
    Conta pp = new ContaPoupanca(cliente);
    historico.append("Cliente criado: ").append(cliente.getNome()).append("\n");

    boolean fim = true;
    while (fim) {
      int tipoConta = Integer.parseInt(JOptionPane.showInputDialog(f, "Entre com o tipo da conta: 0 - Sair, 1 - Conta Corrente, 2 - Conta Poupança"));
      switch (tipoConta) {
        case 0:
          fim = false;
          JOptionPane.showMessageDialog(f, "Obrigada!");
          break;
        case 1:
          int movimentoC = Integer.parseInt(JOptionPane.showInputDialog(f, "0 - Sair, 1 - Saldo, 2 - Sacar, 3 - Depósito, 4 - Transferência para Poupança, 5 - Empréstimo"));
          switch (movimentoC) {
            case 0:
              break;
            case 1:
              cc.imprimirExtrato();
              historico.append("Verificado saldo da Conta Corrente\n");
              JOptionPane.showMessageDialog(f, "Saldo atual da Conta Corrente: R$ " + cc.getSaldo());
              break;
            case 2:
              double valorSaqueCC = Double.parseDouble(JOptionPane.showInputDialog(f, "Entre com o valor do saque"));
              if (cc.getSaldo() >= valorSaqueCC) {
                cc.sacar(valorSaqueCC);
                cc.imprimirExtrato();
                historico.append("Saque de ").append(valorSaqueCC).append(" da Conta Corrente\n");
                JOptionPane.showMessageDialog(f, "Saldo atual da Conta Corrente: R$ " + cc.getSaldo());
              } else {
                JOptionPane.showMessageDialog(f, "Saldo insuficiente para realizar o saque.");
              }
              break;
            case 3:
              double valorDepositoCC = Double.parseDouble(JOptionPane.showInputDialog(f, "Entre com o valor do depósito"));
              cc.depositar(valorDepositoCC);
              cc.imprimirExtrato();
              historico.append("Depósito de ").append(valorDepositoCC).append(" na Conta Corrente\n");
              JOptionPane.showMessageDialog(f, "Saldo atual da Conta Corrente: R$ " + cc.getSaldo());
              break;
            case 4:
              double valorTransferenciaCC = Double.parseDouble(JOptionPane.showInputDialog(f, "Entre com o valor da transferência"));
              if (cc.getSaldo() >= valorTransferenciaCC) {
                cc.transferir(valorTransferenciaCC, pp);
                cc.imprimirExtrato();
                historico.append("Transferência de ").append(valorTransferenciaCC).append(" da Conta Corrente para Poupança\n");
                JOptionPane.showMessageDialog(f, "Saldo atual da Conta Corrente: R$ " + cc.getSaldo());
                JOptionPane.showMessageDialog(f, "Saldo atual da Poupança: R$ " + pp.getSaldo());

                // Informar sobre os juros
                double saldoAtualTransferencia = pp.getSaldo();
                double jurosTransferencia = saldoAtualTransferencia * 0.05;
                double totalComJurosTransferencia = saldoAtualTransferencia + jurosTransferencia;

                // Calcular a data de crédito dos juros
                Calendar calTransferencia = Calendar.getInstance();
                calTransferencia.add(Calendar.DAY_OF_MONTH, 30);
                Date dataCreditoTransferencia = calTransferencia.getTime();
                SimpleDateFormat sdfTransferencia = new SimpleDateFormat("dd/MM/yyyy");
                String dataCreditoTransferenciaFormatada = sdfTransferencia.format(dataCreditoTransferencia);

                historico.append("Juros de 5% serão creditados em 30 dias, no valor de R$ ").append(String.format("%.2f", jurosTransferencia)).append(", totalizando R$ ").append(String.format("%.2f", totalComJurosTransferencia)).append(" na data ").append(dataCreditoTransferenciaFormatada).append("\n");
                JOptionPane.showMessageDialog(f, "Juros de 5% serão creditados em 30 dias, no valor de R$ " + String.format("%.2f", jurosTransferencia) + ", totalizando R$ " + String.format("%.2f", totalComJurosTransferencia) + " na data " + dataCreditoTransferenciaFormatada);
              } else {
                JOptionPane.showMessageDialog(f, "Saldo insuficiente para realizar a transferência.");
              }
              break;
            case 5:
              double valorEmprestimo = Double.parseDouble(JOptionPane.showInputDialog(f, "Entre com o valor do empréstimo"));
              JOptionPane.showMessageDialog(f, "Juros de 12% ao mês serão aplicados.");

              StringBuilder opcoesParcelas = new StringBuilder("Escolha a quantidade de parcelas:\n");
              for (int i = 1; i <= 10; i++) {
                double parcela = calcularParcela(valorEmprestimo, 12, i);
                opcoesParcelas.append(i).append(" parcelas de R$ ").append(String.format("%.2f", parcela)).append("\n");
              }

              int numParcelas = Integer.parseInt(JOptionPane.showInputDialog(f, opcoesParcelas.toString()));
              double valorParcela = calcularParcela(valorEmprestimo, 12, numParcelas);

              historico.append("Empréstimo de R$ ").append(valorEmprestimo).append(" com juros de 12% ao mês, parcelado em ").append(numParcelas).append(" vezes de R$ ").append(String.format("%.2f", valorParcela)).append(" cada.\n");

              cc.emprestar(valorEmprestimo, cc);
              cc.imprimirExtrato();
              JOptionPane.showMessageDialog(f, "Saldo atual da Conta Corrente: R$ " + cc.getSaldo());
              break;
          }
          break;
        case 2:
          int movimentoP = Integer.parseInt(JOptionPane.showInputDialog(f, "0 - Sair, 1 - Saldo, 2 - Sacar, 3 - Depósito, 4 - Transferência para Conta Corrente, 5 - Calcular Juros sobre o Saldo da Poupança"));
          switch (movimentoP) {
            case 0:
              JOptionPane.showMessageDialog(f, "Obrigada!");
              break;
            case 1:
              pp.imprimirExtrato();
              historico.append("Verificado saldo da Poupança\n");
              JOptionPane.showMessageDialog(f, "Saldo atual da Poupança: R$ " + pp.getSaldo());
              break;
            case 2:
              double valorSaquePP = Double.parseDouble(JOptionPane.showInputDialog(f, "Entre com o valor do saque"));
              if (pp.getSaldo() >= valorSaquePP) {
                pp.sacar(valorSaquePP);
                pp.imprimirExtrato();
                historico.append("Saque de ").append(valorSaquePP).append(" da Poupança\n");
                JOptionPane.showMessageDialog(f, "Saldo atual da Poupança: R$ " + pp.getSaldo());
              } else {
                JOptionPane.showMessageDialog(f, "Saldo insuficiente para realizar o saque.");
              }
              break;
            case 3:
              double valorDepositoPP = Double.parseDouble(JOptionPane.showInputDialog(f, "Entre com o valor do depósito"));
              pp.depositar(valorDepositoPP);
              pp.imprimirExtrato();
              historico.append("Depósito de ").append(valorDepositoPP).append(" na Poupança\n");
              JOptionPane.showMessageDialog(f, "Saldo atual da Poupança: R$ " + pp.getSaldo());

              // Informar sobre os juros
              double saldoAtualDeposito = pp.getSaldo();
              double jurosDeposito = saldoAtualDeposito * 0.05;
              double totalComJurosDeposito = saldoAtualDeposito + jurosDeposito;

              // Calcular a data de crédito dos juros
              Calendar calDeposito = Calendar.getInstance();
              calDeposito.add(Calendar.DAY_OF_MONTH, 30);
              Date dataCreditoDeposito = calDeposito.getTime();
              SimpleDateFormat sdfDeposito = new SimpleDateFormat("dd/MM/yyyy");
              String dataCreditoDepositoFormatada = sdfDeposito.format(dataCreditoDeposito);

              historico.append("Juros de 5% serão creditados em 30 dias, no valor de R$ ").append(String.format("%.2f", jurosDeposito)).append(", totalizando R$ ").append(String.format("%.2f", totalComJurosDeposito)).append(" na data ").append(dataCreditoDepositoFormatada).append("\n");
              JOptionPane.showMessageDialog(f, "Juros de 5% serão creditados em 30 dias, no valor de R$ " + String.format("%.2f", jurosDeposito) + ", totalizando R$ " + String.format("%.2f", totalComJurosDeposito) + " na data " + dataCreditoDepositoFormatada);
              break;
            case 4:
              double valorTransferenciaPP = Double.parseDouble(JOptionPane.showInputDialog(f, "Entre com o valor da transferência"));
              if (pp.getSaldo() >= valorTransferenciaPP) {
                pp.transferir(valorTransferenciaPP, cc);
                pp.imprimirExtrato();
                historico.append("Transferência de ").append(valorTransferenciaPP).append(" da Poupança para Conta Corrente\n");
                JOptionPane.showMessageDialog(f, "Saldo atual da Poupança: R$ " + pp.getSaldo());
                JOptionPane.showMessageDialog(f, "Saldo atual da Conta Corrente: R$ " + cc.getSaldo());

                // Informar sobre os juros
                double saldoAtualTransferenciaPP = pp.getSaldo();
                double jurosTransferenciaPP = saldoAtualTransferenciaPP * 0.05;
                double totalComJurosTransferenciaPP = saldoAtualTransferenciaPP + jurosTransferenciaPP;

                // Calcular a data de crédito dos juros
                Calendar calTransferenciaPP = Calendar.getInstance();
                calTransferenciaPP.add(Calendar.DAY_OF_MONTH, 30);
                Date dataCreditoTransferenciaPP = calTransferenciaPP.getTime();
                SimpleDateFormat sdfTransferenciaPP = new SimpleDateFormat("dd/MM/yyyy");
                String dataCreditoTransferenciaPPFormatada = sdfTransferenciaPP.format(dataCreditoTransferenciaPP);

                historico.append("Juros de 5% serão creditados em 30 dias, no valor de R$ ").append(String.format("%.2f", jurosTransferenciaPP)).append(", totalizando R$ ").append(String.format("%.2f", totalComJurosTransferenciaPP)).append(" na data ").append(dataCreditoTransferenciaPPFormatada).append("\n");
                JOptionPane.showMessageDialog(f, "Juros de 5% serão creditados em 30 dias, no valor de R$ " + String.format("%.2f", jurosTransferenciaPP) + ", totalizando R$ " + String.format("%.2f", totalComJurosTransferenciaPP) + " na data " + dataCreditoTransferenciaPPFormatada);
              } else {
                JOptionPane.showMessageDialog(f, "Saldo insuficiente para realizar a transferência.");
              }
              break;
            case 5:
              double saldo = pp.getSaldo();
              double jurosPoupanca = saldo * 0.05;
              double totalComJurosPoupanca = saldo + jurosPoupanca;

              // Calcular a data de crédito dos juros
              Calendar calPoupanca = Calendar.getInstance();
              calPoupanca.add(Calendar.DAY_OF_MONTH, 30);
              Date dataCreditoPoupanca = calPoupanca.getTime();
              SimpleDateFormat sdfPoupanca = new SimpleDateFormat("dd/MM/yyyy");
              String dataCreditoPoupancaFormatada = sdfPoupanca.format(dataCreditoPoupanca);

              historico.append("Juros de 5% sobre o saldo da Poupança serão creditados em 30 dias, no valor de R$ ").append(String.format("%.2f", jurosPoupanca)).append(", totalizando R$ ").append(String.format("%.2f", totalComJurosPoupanca)).append(" na data ").append(dataCreditoPoupancaFormatada).append("\n");
              JOptionPane.showMessageDialog(f, "Juros de 5% sobre o saldo da Poupança serão creditados em 30 dias, no valor de R$ " + String.format("%.2f", jurosPoupanca) + ", totalizando R$ " + String.format("%.2f", totalComJurosPoupanca) + " na data " + dataCreditoPoupancaFormatada);
              break;
          }
          break;
      }

      // Mostrar o histórico de operações ao usuário
      JOptionPane.showMessageDialog(f, historico.toString());
    }
  }

  private static double calcularParcela(double valor, double taxaJuros, int numParcelas) {
    double taxaMensal = taxaJuros / 100;
    return (valor * Math.pow(1 + taxaMensal, numParcelas) * taxaMensal) / (Math.pow(1 + taxaMensal, numParcelas) - 1);
  }
}
