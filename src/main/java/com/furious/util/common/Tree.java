package com.furious.util.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Function;

import lombok.Getter;

public class Tree {

    private final Node root;

    private Tree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public static <T> Tree buildTree(Collection<T> c, Function<T, Node> func) {
        if (Objects.isNull(c) || c.isEmpty()) {
            return null;
        }
        Collection<Node> nodes = new ArrayList<>(c.size());
        c.forEach(t -> nodes.add(func.apply(t)));

        Collection<Node> fakeRoots = new LinkedList<>();
        for (Node node : nodes) {
            if (Objects.isNull(node.parentId)) {
                fakeRoots.add(findChild(node, nodes));
            }
        }

        Tree tree = new Tree(new Node(Integer.MIN_VALUE, null));
        tree.getRoot().addChildren(fakeRoots);

        fakeRoots.forEach(fakeRoot -> {
            fakeRoot.parentId = tree.getRoot().id;
            fakeRoot.parent = tree.getRoot();
        });
        return tree;
    }

    private static Node findChild(Node parent, Collection<Node> nodes) {
        for (Node node : nodes) {
            if (Objects.equals(node.parentId, parent.id)) {
                node.parent = parent;
                parent.addChild(findChild(node, nodes));
            }
        }
        return parent;
    }

    public void foreach(Consumer<Node> consumer) {
        if (Objects.isNull(root)) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            consumer.accept(n);
            if (n.hasChildren()) {
                queue.addAll(n.children);
            }
        }
    }

    @Getter
    public static class Node {

        private final Serializable id;
        private Serializable parentId;
        //jdk *** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding" with message transform method call failed at JPLISAgent.c line: 844
        private Node parent;
        private List<Node> children;
        private Map<String, Object> attributes;

        public Node(Serializable id, Serializable parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        public void addChild(Node child) {
            ensureChildrenInit();
            this.children.add(child);
        }

        public void addChildren(Collection<Node> children) {
            ensureChildrenInit();
            this.children.addAll(children);
        }

        public boolean hasChildren() {
            return !Objects.isNull(children);
        }

        public void setAttribute(String name, Object value) {
            ensureAttributesInit();
            this.attributes.put(name, value);
        }

        public void setAttributes(Map<String, Object> attributes) {
            ensureAttributesInit();
            this.attributes.putAll(attributes);
        }

        private void ensureAttributesInit() {
            if (Objects.isNull(attributes)) {
                attributes = new LinkedHashMap<>();
            }
        }

        private void ensureChildrenInit() {
            if (Objects.isNull(children)) {
                children = new LinkedList<>();
            }
        }
    }
}
