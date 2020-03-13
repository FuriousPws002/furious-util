package com.furious.util.ds;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class TreeNode {

    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }

    @Override
    public String toString() {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(this);
        StringBuilder sb = new StringBuilder();
        while (true) {
            for (int i = 0, len = queue.size(); i < len; i++) {
                TreeNode node = queue.poll();
                sb.append(Objects.isNull(node) ? null : node.val);
                sb.append(" ");
                if (!Objects.isNull(node)) {
                    queue.offer(node.left);
                    queue.offer(node.right);
                }
            }
            if (queue.isEmpty()) {
                return sb.toString();
            }
            sb.append("\n");
        }
    }

    public static TreeNode build(Integer[] nums) {
        int len;
        if (Objects.isNull(nums) || (len = nums.length) == 0) {
            return null;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        TreeNode root = new TreeNode(nums[0]);
        queue.offer(root);
        int start = 1;
        //1 3 7 15
        while (true) {
            int num = 0;
            for (int i = 0, size = queue.size(); i < size; i++) {
                TreeNode node = queue.poll();
                if (!Objects.isNull(node)) {
                    if ((start + num) >= len) {
                        return root;
                    }
                    queue.offer(node.left = nums[start + num] == null ? null : new TreeNode(nums[start + num]));
                    if ((start + num + 1) >= len) {
                        return root;
                    }
                    queue.offer(node.right = nums[start + num + 1] == null ? null : new TreeNode(nums[start + num + 1]));
                }
                num += 2;
            }
            start = (start + 1) * 2 - 1;
        }
    }
}
