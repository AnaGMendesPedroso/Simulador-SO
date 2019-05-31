Trabalho disciplina Sistemas Operacionais

Universidade Federal de Mato Grosso do Sul - UFMS

Faculdade de Computação - FACOM

Implementado por Ana Gabrielly Mendes Pedroso

Estrutura: Todo o trabalho foi desenvoldido em Java. Todos os códigos necessários para a execução estão na pasta Simulador-SO/Code/. 

Para execução: 
   
-Entre na pasta Code:
	cd Simulador-SO/Code

-Compile os arquivos .java e execute a classe Executor :
		javac *.java && java Executor

A saída obtida para a entrada:
     1000 3 2 1 500 5 15 0 700 1 5 2 600 2 10
  está no arquivo saida.txt na pasta Code/

Em Code/ temos as seguintes classes:
    Executor: Responsável pela criação das demais threads e encerramento da aplicação.

    CriadorDeProcessos: Responsável pela criação de um processo de acordo com seu tempo de chegada, colocando-o na FilaDeEntradaCompartilhada.

    EscalonadorFirstComeFirstServed: Escalonador de longo prazo responsável por alocar um processo na FilaDeProntosCompartilhada quando este se encontra alocado na Memória.

    EscalonadorRoundRobin: Responsável por disparar um processo para sua execução de acordo com o TIMEQUANTUM indicado pelo usuário em Executor.

    Despachante: Responsável por alocar o processo e seu tempo de execução, de acordo com EscalonadorRoundRobin, na CPU. Aguarda o término do processamento para retirá-lo da CPU e avisa EscalonadorRoundRobin quando isso ocorre.

    Swapper: Gerencia a alocação e remoção de processos da Memória.

    FilaEntradaCompartilhada: Estrutura compartilhada entre as threads. Guarda os processos que serão executados.

    FilaProntosCompartilhada: Estrutura compartilhada entre as threads. Guarda os processos prontos para execução.

    Memoria: Alocação de armazenamento dinâmico com partições de tamanho variável e algoritmo first-fit.

    Processo: Objeto processo.
