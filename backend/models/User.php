<?php

namespace Models;

use Database;

class User {
    private $db;

    public function __construct() {
        $this->db = Database::getInstance()->getConnection();
    }

    public function register(string $username, string $password, string $raspberryId): array {
        // Check if username exists
        $stmt = $this->db->prepare("SELECT username FROM utenti WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();

        // Check if raspberry is already assigned
        $stmt = $this->db->prepare("SELECT id_rasp FROM utenti WHERE id_rasp = ?");
        $stmt->bind_param("s", $raspberryId);
        $stmt->execute();
        $result2 = $stmt->get_result();

        if ($result->num_rows > 0 && $result2->num_rows === 0) {
            return ['success' => false, 'code' => 401, 'message' => 'Username already exists'];
        }

        if ($result->num_rows === 0 && $result2->num_rows > 0) {
            return ['success' => false, 'code' => 409, 'message' => 'Raspberry already assigned'];
        }

        if ($result->num_rows > 0 && $result2->num_rows > 0) {
            return ['success' => false, 'code' => 406, 'message' => 'Both username and raspberry are already in use'];
        }

        // Hash password and create user
        $hashedPassword = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $this->db->prepare("INSERT INTO utenti (username, password, id_rasp) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $username, $hashedPassword, $raspberryId);
        
        if ($stmt->execute()) {
            return ['success' => true, 'code' => 201, 'message' => 'User registered successfully'];
        }

        return ['success' => false, 'code' => 500, 'message' => 'Failed to register user'];
    }

    public function getUnassignedRaspberries(): array {
        $stmt = $this->db->prepare("
            SELECT r.* 
            FROM raspberry r 
            LEFT JOIN utenti u ON r.id_rasp = u.id_rasp 
            WHERE u.id_rasp IS NULL 
            ORDER BY r.date
        ");
        
        $stmt->execute();
        $result = $stmt->get_result();
        
        $raspberries = [];
        while ($row = $result->fetch_assoc()) {
            $raspberries[] = $row;
        }
        
        return $raspberries;
    }
} 