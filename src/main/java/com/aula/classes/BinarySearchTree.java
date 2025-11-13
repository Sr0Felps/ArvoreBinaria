package main.java.com.aula.classes;

import java.util.*;
import java.util.function.Consumer;

public class BinarySearchTree<T> implements Iterable<T> {

    private TreeNode<T> root;
    private int size;
    private final Comparator<? super T> cmp;

    public BinarySearchTree(){
        this(null);
    }
    public BinarySearchTree(Comparator<? super T> cmp){
        this.cmp = cmp;
    }

    public boolean add(T value){
        Objects.requireNonNull(value,"Valor não pode ser nulo!");
        int anterior = size;
        root = insert(root, value);
        return size > anterior;
    }

    public boolean contains(T value){
        Objects.requireNonNull(value,"Valor não pode ser nulo!");
        return findNode(root, value) != null;
    }

    public boolean remove(T value){
        int anterior = size;
        root = delete(root, value);
        return size < anterior;
    }

    public void clear(){
        root = null;
        size = 0;
    }
    public int size() {
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int height() {
        return nodeHeight(root);
    }

    public Optional<T> min(){
        if(root == null)
            return Optional.empty();
        TreeNode<T> n = root;
        while (n.left != null)
            n = n.left;
        return Optional.of(n.value);
    }

    public Optional<T> max(){
        if(root == null)
            return Optional.empty();
        TreeNode<T> n = root;
        while (n.right != null)
            n = n.right;
        return Optional.of(n.value);
    }

    public boolean validate(){
        List<T> in = new ArrayList<>();
        inOrder(v -> in.add(v));
        for(int i =1;i<in.size();i++){
            if(compare(in.get(i-1), in.get(i)) >= 0)
                return false;
        }
        return true;
    }

    public TreeNode<T> findNode(TreeNode<T> n, T value){
        while (n!=null){
            int c = compare(value, n.value);
            if(c==0) return n;
            n= (c < 0) ? n.left : n.right;
        }
        return null;
    }

    private TreeNode<T> insert(TreeNode<T> n, T value){
        if(n == null){
            size++;
            return new TreeNode<>(value);
        }
        int c = compare(value, n.value);
        if(c < 0){
            n.left = insert(n.left,value);
        } else if(c > 0) {
            n.right = insert(n.right,value);
        }
        else {
            return n;
        }
        recompute(n);
        return n;
    }

    private TreeNode<T> delete(TreeNode<T> n, T value){
        if(n==null) return null;

        int c = compare(value, n.value);
        if(c<0){
            n.left = delete(n.left, value);
        }else if(c>0){
            n.right = delete(n.right, value);
        }else{
            //Caso 1: Sem Filhos
            if (n.left == null && n.right == null) {
                size--;
                return null;
            //Caso 2: 1 filho
            }else if(n.left == null || n.right == null){
                size--;
                return n.right!=null ? n.right : n.left;
            //Caso 3: 2 filhos
            }else {
                TreeNode<T> succ = n.right;
                while (succ.left !=null) succ = succ.left;
                n.value = succ.value;
                n.right = delete(n.right, succ.value);
            }
        }
        recompute(n);
        return n;
    }

    @SuppressWarnings("unchecked")
    private int compare(T a, T b) {
        if(cmp != null) return cmp.compare(a,b);
        return ((Comparable<? super T>)a).compareTo(b);
    }

    private void recompute(TreeNode<T> n) {
        n.height = Math.max(nodeHeight(n.left), nodeHeight(n.right))+1;
    }

    private int nodeHeight(TreeNode<T> n) {
        return (n==null) ? 0 : n.height;
    }

    public void preOrder(TreeNode<T> n, Consumer<T> act){
        if(n==null) return;
        act.accept(n.value);
        preOrder(n.left, act);
        preOrder(n.right,act);
    }

    public void preOrder(Consumer<T> act){
        Objects.requireNonNull(act);
        preOrder(root, act);
    }

    public void inOrder(TreeNode<T> n, Consumer<T> act){
        if(n==null) return;
        inOrder(n.left, act);
        act.accept(n.value);
        inOrder(n.right,act);
    }

    public void inOrder(Consumer<T> act){
        Objects.requireNonNull(act);
        inOrder(root, act);
    }

    public void postOrder(TreeNode<T> n, Consumer<T> act){
        if(n==null) return;
        postOrder(n.left, act);
        postOrder(n.right,act);
        act.accept(n.value);
    }

    public void postOrder(Consumer<T> act){
        Objects.requireNonNull(act);
        postOrder(root, act);
    }

    public void levelOrder(Consumer<T> act){
        Objects.requireNonNull(act);
        if(root == null) return;
        Queue<TreeNode<T>> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()){
            TreeNode<T> n = q.remove();
            act.accept(n.value);
            if(n.left != null) q.add(n.left);
            if(n.right != null) q.add(n.right);
        }
    }

    public void printArvore(int nivel){
        imprime_estrutura(root, nivel);
    }

    private void imprime_estrutura(TreeNode<T> A, int nivel){
        if(A==null) return;
        imprime_estrutura(A.right, nivel+1);
        for(int i=0 ;i<nivel; ++i)
            System.out.print("\t");
        System.out.println(A.value);
        imprime_estrutura(A.left, nivel+1);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private final Deque<TreeNode<T>> stack = init();
            private Deque<TreeNode<T>> init(){
                Deque<TreeNode<T>> s = new ArrayDeque<>();
                pushLeft(root,s);
                return s;
            }
            private void pushLeft(TreeNode<T> n, Deque<TreeNode<T>> s){
                while (n != null){
                    s.push(n);
                    n = n.left;
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public T next() {
                if(!hasNext()) throw new NoSuchElementException();
                TreeNode<T> n = stack.pop();
                if(n.right !=null) pushLeft(n.right,stack);
                return n.value;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        boolean[] first = {true};
        inOrder(v-> {
            if(!first[0]) sb.append(", ");
            sb.append(v);
            first[0] = false;
        });
        return sb.append("]").toString();
    }
}
