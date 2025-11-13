package main.java.com.aula.classes;

import java.util.Comparator;
import java.util.Objects;

public class AVLTree<T> {

    private TreeNode<T> root;
    private final Comparator<? super T> comparator;

    /**
     * Construtor da árvore AVL.
     * Recebe um Comparator para comparar os valores.
     */
    public AVLTree(Comparator<? super T> comparator) {
        this.comparator = Objects.requireNonNull(comparator, "Comparator não pode ser nulo!");
        this.root = null;
    }

    /* Métodos públicos */

    public void insert(T value) {
        root = insert(root, value);
    }

    public void remove(T value) {
        root = remove(root, value);
    }

    public boolean search(T value) {
        return search(root, value);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }

    /** Percurso em ordem (in-order) */
    public void printInOrder() {
        System.out.print("[");
        printInOrder(root);
        System.out.println("]");
    }

    public void printPreOrder() {
        System.out.print("[");
        printPreOrder(root);
        System.out.println("]");
    }

    public void printPostOrder() {
        System.out.print("[");
        printPostOrder(root);
        System.out.println("]");
    }

    public void printTree(){
        printTree(root, 0);
    }

    /*  Métodos privados auxiliares */

    private int height(TreeNode<T> node) {
        return (node == null) ? 0 : node.height;
    }

    private int balanceFactor(TreeNode<T> node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    private void updateHeight(TreeNode<T> node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    /*  Rotações */

    private TreeNode<T> rotateRight(TreeNode<T> y) {
        TreeNode<T> x = y.left;
        TreeNode<T> T2 = x.right;

        // Rotação
        x.right = y;
        y.left = T2;

        // Atualizar alturas
        updateHeight(y);
        updateHeight(x);

        return x; // nova raiz do subárvore
    }

    private TreeNode<T> rotateLeft(TreeNode<T> x) {
        TreeNode<T> y = x.right;
        TreeNode<T> T2 = y.left;

        // Rotação
        y.left = x;
        x.right = T2;

        // Atualizar alturas
        updateHeight(x);
        updateHeight(y);

        return y; // nova raiz da subárvore
    }

    /* Inserção */

    private TreeNode<T> insert(TreeNode<T> node, T value) {
        // Inserção normal de BST
        if (node == null) {
            return new TreeNode<>(value);
        }

        int cmp = comparator.compare(value, node.value);

        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        } else {
            // Valores iguais: aqui podemos decidir não inserir duplicatas
            // ou tratar de alguma forma especial. Aqui, vamos ignorar.
            return node;
        }

        // Atualizar altura
        updateHeight(node);

        // Verificar fator de balanceamento
        int balance = balanceFactor(node);

        // Casos de desbalanceamento:

        // Caso Esquerda-Esquerda: inseriu à esquerda do filho esquerdo
        if (balance > 1 && comparator.compare(value, node.left.value) < 0) {
            return rotateRight(node);
        }

        // Caso Direita-Direita: inseriu à direita do filho direito
        if (balance < -1 && comparator.compare(value, node.right.value) > 0) {
            return rotateLeft(node);
        }

        // Caso Esquerda-Direita: inseriu à direita do filho esquerdo
        if (balance > 1 && comparator.compare(value, node.left.value) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Caso Direita-Esquerda: inseriu à esquerda do filho direito
        if (balance < -1 && comparator.compare(value, node.right.value) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node; // nó já está balanceado
    }

    /* Remoção */

    private TreeNode<T> remove(TreeNode<T> node, T value) {
        if (node == null) {
            return null; // valor não encontrado
        }

        int cmp = comparator.compare(value, node.value);

        if (cmp < 0) {
            node.left = remove(node.left, value);
        } else if (cmp > 0) {
            node.right = remove(node.right, value);
        } else {
            // Encontrou o nó a remover
            // Caso com 0 ou 1 filho
            if (node.left == null || node.right == null) {
                TreeNode<T> temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    // Sem filhos
                    node = null;
                } else {
                    // Um filho: copia o conteúdo
                    node = temp;
                }
            } else {
                // 2 filhos: pegar o menor valor da subárvore direita (sucessor)
                TreeNode<T> sucessor = minValueNode(node.right);
                node.value = sucessor.value;
                // remover o sucessor
                node.right = remove(node.right, sucessor.value);
            }
        }

        // Se a árvore ficou vazia após remoção
        if (node == null) {
            return null;
        }

        // Atualizar altura
        updateHeight(node);

        // Verificar desbalanceamento
        int balance = balanceFactor(node);

        // Casos de desbalanceamento (mesma lógica da inserção, mas usando alturas dos filhos):

        // Caso Esquerda-Esquerda
        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Caso Esquerda-Direita
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Caso Direita-Direita
        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Caso Direita-Esquerda
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private TreeNode<T> minValueNode(TreeNode<T> node) {
        TreeNode<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /* Busca */

    private boolean search(TreeNode<T> node, T value) {
        if (node == null) return false;

        int cmp = comparator.compare(value, node.value);

        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return search(node.left, value);
        } else {
            return search(node.right, value);
        }
    }

    /* Percurso */

    private void printInOrder(TreeNode<T> node) {
        if (node == null) return;
        printInOrder(node.left);
        System.out.print(node.value + " ");
        printInOrder(node.right);
    }

    private void printPreOrder(TreeNode<T> node) {
        if (node == null) return;
        System.out.print(node.value + " ");
        printPreOrder(node.left);
        printPreOrder(node.right);
    }

    private void printPostOrder(TreeNode<T> node) {
        if (node == null) return;
        printPostOrder(node.left);
        printPostOrder(node.right);
        System.out.print(node.value + " ");
    }

    private void printTree(TreeNode<T> A, int nivel){
        if(A==null) return;
        printTree(A.right, nivel+1);
        for(int i=0 ;i<nivel; ++i)
            System.out.print("\t");
        System.out.println(A.value);
        printTree(A.left, nivel+1);
    }

}
