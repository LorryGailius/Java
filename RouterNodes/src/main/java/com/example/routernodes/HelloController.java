package com.example.routernodes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    ObservableList<Node> nodes = FXCollections.observableArrayList();
    Dijkstra dijkstra = new Dijkstra();
    @FXML
    private TextField ipField;
    @FXML
    private TextField weightField;
    @FXML
    private TextArea outputArea;
    @FXML
    private ComboBox sourceNode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sourceNode.setItems(nodes);
        PopulateNodes();
    }

    public void PopulateNodes() {

        Node node1 = new Node("74.88.204.223");
        Node node2 = new Node("197.29.42.67");
        Node node3 = new Node("39.225.125.21");
        Node node4 = new Node("70.17.160.73");
        Node node5 = new Node("50.173.234.194");
        Node node6 = new Node("198.28.29.95");

        node1.addAdjacentNode(node2, 2);
        node1.addAdjacentNode(node3, 4);

        node2.addAdjacentNode(node4, 5);
        node2.addAdjacentNode(node5, 1);
        node2.addAdjacentNode(node3, 3);

        node3.addAdjacentNode(node5, 2);

        node4.addAdjacentNode(node6, 2);

        node5.addAdjacentNode(node6, 4);
        node5.addAdjacentNode(node4, 1);

        nodes.addAll(node1, node2, node3, node4, node5, node6);
    }

    public void changeSourceNode() {

        Node node = (Node) sourceNode.getSelectionModel().getSelectedItem();

        if (node == null) {
            return;
        }

        ResetNodes();

        dijkstra.calculateShortestPath(node);
        outputArea.setText(dijkstra.printPaths(nodes));
    }

    public void RemoveNode() {
        Node source = (Node) sourceNode.getSelectionModel().getSelectedItem();

        if (source == null) {
            return;
        }

        if (ipField.getText().isEmpty()) {
            return;
        }

        String ip = ipField.getText().trim();
        Node nodeToRemove = null;

        // Check if node with the given ip exists
        for (Node node : nodes) {
            if (Objects.equals(node.getIp(), ip)) {
                nodeToRemove = node;
            }
        }

        if (nodeToRemove == null || nodeToRemove == source) {
            return;
        }

        nodes.remove(nodeToRemove);

        for (Node node : nodes) {
            Map<Node, Integer> adjacentNodes = node.getAdjacentNodes();
            adjacentNodes.remove(nodeToRemove);
        }

        ResetNodes();

        dijkstra.calculateShortestPath(source);
        outputArea.setText(dijkstra.printPaths(nodes));
    }

    public void AddNode() {
        Node source = (Node) sourceNode.getSelectionModel().getSelectedItem();

        if (ipField.getText().isEmpty() || weightField.getText().isEmpty() || source == null) {
            return;
        }

        String ip = ipField.getText().trim();
        int weight = Integer.parseInt(weightField.getText());
        Node node = new Node(ip);

        if (node == null) {
            return;
        }

        // Add node to the list of nodes if there is no node with the same ip
        boolean nodeExists = false;
        for (Node n : nodes) {
            if (Objects.equals(n.getIp(), ip)) {
                nodeExists = true;
            }
        }

        if (!nodeExists) {
            // Add node to the list of nodes
            nodes.add(node);
            source.addAdjacentNode(node, weight);
        } else {
            // Find the node with the given ip
            for(Node n : nodes) {
                if(Objects.equals(n.getIp(), ip)) {
                    node = n;
                }
            }
            source.addAdjacentNode(node, weight);
        }

        ResetNodes();
        dijkstra.calculateShortestPath(source);
        outputArea.setText(dijkstra.printPaths(nodes));
    }

    public void ResetNodes() {
        for (Node node : nodes) {
            node.setDistance(Integer.MAX_VALUE);
            node.setShortestPath(new LinkedList<>());
        }
        outputArea.clear();
    }


}