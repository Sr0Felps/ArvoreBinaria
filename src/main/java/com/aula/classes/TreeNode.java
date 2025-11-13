package main.java.com.aula.classes;

import java.util.Objects;

public class TreeNode<T> {
    T value;
    TreeNode<T> left;
    TreeNode<T> right;
    int height;

    TreeNode(T value) {
        this.value = Objects.requireNonNull(value, "Valor não pode ser nulo!");
        height = 1;
        left = null;
        right = null;
    }
    @Override
    public String toString(){
        return String.valueOf(value);
    }
}
