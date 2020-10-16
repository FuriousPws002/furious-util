package com.furious.util.common;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;

class TreeTest {

    @Test
    public void test() {
        Tree tree = Tree.buildTree(list(), entity -> {
            Integer id = entity.getId();
            Integer parentId = entity.getParentId();
            parentId = parentId == 0 ? null : parentId;
            Tree.Node node = new Tree.Node(id, parentId);
            //set attributes
            node.setAttribute("name", entity.getName());
            return node;
        });
//        if (!Objects.isNull(tree)) {
//            tree.foreach(node -> {
////                System.err.println(node.getAttributes());
//            });
//        }

        Assertions.assertNotNull(tree);
    }

    private static List<Entity> list() {
        List<Entity> list = new LinkedList<>();
        list.add(new Entity(1, 0, "name1"));
        list.add(new Entity(2, 0, "name2"));
        list.add(new Entity(3, 1, "name3"));
        list.add(new Entity(4, 2, "name4"));
        list.add(new Entity(5, 4, "name5"));
//        for (int i = 6; i < 100; i++) {
//            list.add(new Entity(i, i - 1, "name" + i));
//        }
        return list;
    }

    @Data
    @AllArgsConstructor
    private static class Entity {
        private Integer id;
        private Integer parentId;
        private String name;
    }
}