package pl.edu.wat;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.scalified.tree.TreeNode;

import lombok.Getter;

@Getter
public class Algorithm {

    public static final int SIZE = 8;
    private long iterations = 1;
    private Tree tree = new Tree();
    private Set<TreeNode<Move>> usedNodes = new HashSet<>();

    void run() {
        firstStep();

        TreeNode<Move> node = bestNodeOrSolution();
        while (!getMove(node).isLastQueen()) {
            expandNode(node);
            usedNodes.add(node);
            node = bestNodeOrSolution();
            iterations++;
        }
        solution(node);
        System.out.println(String.format("Solution found in %s iterations", iterations));
    }

    private void firstStep() {
        IntStream.range(0, Algorithm.SIZE)
                .forEach(i -> tree.addToRoot(
                        Move.createMove(Collections.emptyList(), 0, i)
                                .orElseThrow(() -> new RuntimeException("Cannot create move on first step!"))));

    }

    private void expandNode(TreeNode<Move> node) {
        Set<TreeNode<Move>> nodes = findNodesInPath(node);
        IntStream.range(0, SIZE)
                .mapToObj(i -> Move.createMove(getMoveList(nodes), getMove(node).getRow() + 1, i))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(addToNode(node));
    }

    private Consumer<Move> addToNode(TreeNode<Move> node) {
        return move -> tree.addToNode(node, move);
    }

    private TreeNode<Move> bestNodeOrSolution() {
        Set<TreeNode<Move>> nodes = findLeaves();
        Optional<TreeNode<Move>> solution = findSolution(nodes);
        return solution
                .orElseGet(findBestNode(nodes));
    }

    private Optional<TreeNode<Move>> findSolution(Set<TreeNode<Move>> nodes) {
        return nodes.stream()
                .filter(node -> getMove(node).isLastQueen())
                .findAny();
    }

    private Supplier<TreeNode<Move>> findBestNode(Set<TreeNode<Move>> nodes) {
        return () -> nodes.stream()
                .filter(node -> !usedNodes.contains(node))
                .max(Comparator.comparingLong(n -> getMove(n).getScore()))
                .orElseThrow(() -> new RuntimeException("Cannot find best node!"));
    }

    private Set<TreeNode<Move>> findLeaves() {
        return tree.getLeaves();
    }

    private Set<TreeNode<Move>> findNodesInPath(TreeNode<Move> node) {
        return tree.getNodesInPath(node);
    }

    private void solution(TreeNode<Move> node) {
        Set<TreeNode<Move>> nodes = tree.getNodesInPath(node);
        Board.getInstance().applyMoves(getMoveList(nodes));
        Board.getInstance().print();
    }

    private List<Move> getMoveList(Set<TreeNode<Move>> nodes) {
        return nodes.stream()
                .map(TreeNode::data)
                .collect(Collectors.toList());
    }

    private Move getMove(TreeNode<Move> node) {
        return node.data();
    }
}
