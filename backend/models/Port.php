<?php

namespace Models;

use Database;

class Port {
    private $db;

    public function __construct() {
        $this->db = Database::getInstance()->getConnection();
    }

    public function getPorts(string $username): array {
        $stmt = $this->db->prepare("SELECT * FROM porte WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();
        
        if ($result->num_rows === 0) {
            return [];
        }

        return $result->fetch_assoc();
    }

    public function updatePorts(string $username, array $ports): bool {
        $stmt = $this->db->prepare("SELECT * FROM porte WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows === 0) {
            return $this->insertPorts($username, $ports);
        }

        $query = "UPDATE porte SET ";
        $types = "";
        $params = [];

        foreach ($ports as $key => $value) {
            $query .= "$key = ?, ";
            $types .= "s";
            $params[] = $value;
        }

        $query = rtrim($query, ", ");
        $query .= " WHERE username = ?";
        $types .= "s";
        $params[] = $username;

        $stmt = $this->db->prepare($query);
        $stmt->bind_param($types, ...$params);
        
        return $stmt->execute();
    }

    private function insertPorts(string $username, array $ports): bool {
        $columns = implode(", ", array_keys($ports));
        $values = implode(", ", array_fill(0, count($ports), "?"));
        
        $query = "INSERT INTO porte (username, $columns) VALUES (?, $values)";
        
        $types = "s" . str_repeat("s", count($ports));
        $params = array_merge([$username], array_values($ports));
        
        $stmt = $this->db->prepare($query);
        $stmt->bind_param($types, ...$params);
        
        return $stmt->execute();
    }
} 