package pl.edu.wat;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;

class Tree {

    private TreeNode<Move> root = node(null);

    void addToNode(TreeNode<Move> node, Move move) {
        node.add(node(move));
    }

    void addToRoot(Move move) {
        addToNode(root, move);
    }

    //TODO: refactor
    Set<TreeNode<Move>> getNodesInPath(TreeNode<Move> node) {
        Set<TreeNode<Move>> nodes = new HashSet<>();
        TreeNode<Move> temp = node;
        nodes.add(node);
        while (temp.parent() != null) {
            nodes.add(temp);
            temp = temp.parent();
        }
        return nodes;
    }

    Set<TreeNode<Move>> getLeaves() {
        return getNodes(root).stream()
                .filter(TreeNode::isLeaf)
                .collect(Collectors.toSet());
    }

    //TODO: refactor
    private Set<TreeNode<Move>> getNodes(TreeNode<Move> treeNode) {
        Set<TreeNode<Move>> nodes = new HashSet<>();
        Collection<? extends TreeNode<Move>> subtrees = treeNode.subtrees();
        if (!subtrees.isEmpty()) {
            for (TreeNode<Move> subtree : subtrees) {
                nodes.addAll(getNodes(subtree));
            }
            return nodes;
        } else {
            return Collections.singleton(treeNode);
        }
    }

    private ArrayMultiTreeNode<Move> node(Move move) {
        return new ArrayMultiTreeNode<>(move);
    }
}
