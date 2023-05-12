package com.example.routernodes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dijkstra<T> {

    public void calculateShortestPath(Node<T> source) {
        source.setDistance(0);
        Set<Node<T>> settledNodes = new HashSet<>();
        Queue<Node<T>> unsettledNodes = new PriorityQueue<>(Collections.singleton(source));
        while (!unsettledNodes.isEmpty()) {
            Node<T> currentNode = unsettledNodes.poll();
            currentNode.getAdjacentNodes()
                    .entrySet().stream()
                    .filter(entry -> !settledNodes.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentNode);
                        unsettledNodes.add(entry.getKey());
                    });
            settledNodes.add(currentNode);
        }
    }


    private void evaluateDistanceAndPath(Node<T> adjacentNode, Integer edgeWeight, Node<T> sourceNode) {
        Integer newDistance = sourceNode.getDistance() + edgeWeight;
        if (newDistance < adjacentNode.getDistance()) {
            adjacentNode.setDistance(newDistance);
            adjacentNode.setShortestPath(Stream.concat(sourceNode.getShortestPath().stream(), Stream.of(sourceNode)).toList());
        }
    }

    public String printPaths(List<Node<T>> nodes) {

        StringBuilder returnString = new StringBuilder();

        nodes.forEach(node -> {
            if(node.getDistance() < Integer.MAX_VALUE)
            {
                String path = node.getShortestPath().stream()
                        .map(Node::getIp).map(Objects::toString)
                        .collect(Collectors.joining(" -> "));
                returnString.append((path.isBlank()
                        ? "%s : %s\n".formatted(node.getIp(), node.getDistance())
                        : "%s -> %s : %s\n".formatted(path, node.getIp(), node.getDistance()))
                );
            }
        });

        return returnString.toString();
    }

}