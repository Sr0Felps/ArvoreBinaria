package main.java.com.aula;

import main.java.com.aula.classes.BinarySearchTree;
import main.java.com.aula.classes.ExpressionTree;
import main.java.com.aula.classes.TreeNode;

public class Main {

    public static void main(String[] args) {

        // =====================================================================
        // SETUP: ÁRVORE PRINCIPAL (Testes 1, 2, 3, 4, 7)
        // Valores: 50, 30, 70, 20, 40, 60, 80, 10, 45 (Para ter nós com 1 e 2 filhos)
        // =====================================================================
        System.out.println("=================================================");
        System.out.println("TESTES DA ÁRVORE BINÁRIA DE BUSCA (BST)");
        System.out.println("=================================================");

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.add(50);
        bst.add(30);
        bst.add(70);
        bst.add(20);
        bst.add(40);
        bst.add(60);
        bst.add(80);
        bst.add(10);
        bst.add(45); // Nó com um filho (o 40)

        System.out.println("Árvore Inicial (In-order): " + bst);
        System.out.println("Estrutura (visualmente):");
        bst.printArvore(0);
        System.out.println("-------------------------------------------------");


        // -------------------------------------------------
        // 1. FUNÇÕES ADICIONAIS
        // -------------------------------------------------
        System.out.println("1. FUNÇÕES ADICIONAIS:");

        // 1.1 Exibir nós folhas em ordem crescente
        bst.printLeavesInOrder(); // Esperado: 10 20 45 60 80

        // 1.2 Contar nós com exatamente um filho
        // Nós com 1 filho nesta árvore: 30 (tem 20 e 40, mas 20 e 40 são removidos. 30 tem 2 filhos)
        // 20 (tem 10)
        // 40 (tem 45)
        // 70 (tem 60 e 80, 70 tem 2 filhos)
        // Total esperado: 2 (20 e 40)
        int nodesOneChild = bst.countNodesWithOneChild();
        System.out.println("Contagem de nós com exatamente um filho: " + nodesOneChild);

        // 1.3 Imprimir todos os nós do nível k
        // Nível 0: 50
        // Nível 1: 30, 70
        // Nível 2: 20, 40, 60, 80
        // Nível 3: 10, 45
        bst.printNodesAtLevelK(2); // Esperado: 20 40 60 80
        System.out.println("-------------------------------------------------");


        // -------------------------------------------------
        // 2. ADAPTAÇÕES E VERIFICAÇÕES
        // -------------------------------------------------
        System.out.println("2. ADAPTAÇÕES E VERIFICAÇÕES:");

        // 2.1 Calcular altura da árvore (recursiva, raiz = 0)
        // Altura esperada: 3
        System.out.println("Altura da árvore (maxDepthRecursive): " + bst.maxDepthRecursive());

        // 2.2 Determinar se é estritamente binária
        // A BST de teste não é estritamente binária (20 tem 1 filho, 40 tem 1 filho, 80 tem 0, 70 tem 2, 30 tem 2, 50 tem 2) -> Falso
        System.out.println("A árvore é estritamente binária? " + bst.isStrictlyBinary());

        // 2.3 Determinar se é completa
        // Árvore de teste não é completa (gaps) -> Falso
        System.out.println("A árvore é completa? " + bst.isComplete());
        System.out.println("-------------------------------------------------");


        // -------------------------------------------------
        // 3. ESPELHAMENTO
        // -------------------------------------------------
        System.out.println("3. ESPELHAMENTO:");
        bst.mirror();
        System.out.println("-------------------------------------------------");


        // -------------------------------------------------
        // 4. FUNÇÕES DE SOMA
        // -------------------------------------------------
        System.out.println("4. FUNÇÕES DE SOMA:");
        // Soma esperada: 50+30+70+20+40+60+80+10+45 = 405
        System.out.println("Soma de todos os valores: " + bst.sumAllValues());

        // Soma Folhas: 10+20+45+60+80 (Note que a árvore está espelhada, mas o InOrder permanece. O cálculo é na estrutura atual)
        // Folhas da árvore espelhada: 80, 60, 45, 20, 10
        System.out.println("Soma dos nós folha: " + bst.sumLeafNodes()); // Esperado: 80+60+45+20+10 = 215

        // Diferença subárvores da raiz (Esquerda - Direita na árvore ESPELHADA)
        // Subárvore esquerda da raiz (50) AGORA é a subárvore original da direita (70, 80, 60)
        // Subárvore direita da raiz (50) AGORA é a subárvore original da esquerda (30, 20, 40, 10, 45)
        // Soma Esq (70, 80, 60) = 210
        // Soma Dir (30, 20, 40, 10, 45) = 145
        // Diferença esperada: 210 - 145 = 65
        System.out.println("Diferença Subarvores Raiz (Esq - Dir): " + bst.diffSubtreeSumRoot());
        System.out.println("-------------------------------------------------");


        // -------------------------------------------------
        // 5. RECONSTRUÇÃO (BST<Integer> é obrigatório)
        // -------------------------------------------------
        System.out.println("5. RECONSTRUÇÃO:");
        // Reconstruindo uma árvore simples: 10, 5, 15
        Integer[] preOrder = {10, 5, 1, 8, 15, 12, 20};
        Integer[] inOrder = {1, 5, 8, 10, 12, 15, 20};

        BinarySearchTree<Integer> reconstruida = new BinarySearchTree<>();
        TreeNode<Integer> newRoot = reconstruida.reconstruct(preOrder, inOrder);
        // Caminhamento Posfixado esperado: 1 8 5 12 20 15 10
        System.out.println("-------------------------------------------------");


        // -------------------------------------------------
        // 7. VERIFICAÇÃO DE IDENTIDADE
        // -------------------------------------------------
        System.out.println("7. VERIFICAÇÃO DE IDENTIDADE:");

        BinarySearchTree<Integer> bstA = new BinarySearchTree<>();
        bstA.add(10); bstA.add(5); bstA.add(15);

        BinarySearchTree<Integer> bstB = new BinarySearchTree<>();
        bstB.add(10); bstB.add(5); bstB.add(15);

        BinarySearchTree<Integer> bstC = new BinarySearchTree<>();
        bstC.add(10); bstC.add(5); bstC.add(20); // Valor diferente

        System.out.println("BST A é idêntica à BST B (Idênticas): " + bstA.isIdentical(bstB)); // Esperado: 1
        System.out.println("BST A é idêntica à BST C (Diferentes): " + bstA.isIdentical(bstC)); // Esperado: 0
        System.out.println("-------------------------------------------------");


        // =====================================================================
        // TESTES DA ÁRVORE DE EXPRESSÃO (Instrução 6)
        // =====================================================================
        System.out.println("=================================================");
        System.out.println("6. TESTES DA ÁRVORE DE EXPRESSÃO");
        System.out.println("=================================================");

        // Exemplo A: Expressão com números
        String exprA = "((10 + 5) * (6 - 2))"; // (15 * 4) = 60
        ExpressionTree expTreeA = new ExpressionTree();
        System.out.println("-> Expressão: " + exprA);
        try {
            expTreeA.buildExpressionTree(exprA);

            // 6.1 Exibir a expressão em forma central (infixa)
            expTreeA.printInfix();

            // 6.2 Calcular o resultado
            int resultadoA = expTreeA.evaluateExpression();
            System.out.println("Resultado do Cálculo: " + resultadoA); // Esperado: 60
        } catch (Exception e) {
            System.out.println("Erro ao processar expressão A: " + e.getMessage());
        }

        System.out.println("---");

        // Exemplo B: Expressão com variáveis (para testar tratamento de erro)
        String exprB = "((A + B) / (C * D))";
        ExpressionTree expTreeB = new ExpressionTree();
        System.out.println("-> Expressão: " + exprB);
        try {
            expTreeB.buildExpressionTree(exprB);
            expTreeB.printInfix();

            // 6.2 Calcular o resultado (deve lançar exceção)
            int resultadoB = expTreeB.evaluateExpression();
            System.out.println("Resultado do Cálculo: " + resultadoB);
        } catch (UnsupportedOperationException e) {
            System.out.println("Resultado do Cálculo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao processar expressão B: " + e.getMessage());
        }

        System.out.println("=================================================");
    }
}