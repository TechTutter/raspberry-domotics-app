<?php

namespace Models;

use Database;

class Raspberry {
    private $db;

    public function __construct() {
        $this->db = Database::getInstance()->getConnection();
    }

    public function updateUrl(string $idRasp, string $url): array {
        $stmt = $this->db->prepare("SELECT * FROM raspberry WHERE id_rasp = ?");
        $stmt->bind_param("s", $idRasp);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $stmt = $this->db->prepare("UPDATE raspberry SET url = ? WHERE id_rasp = ?");
            $stmt->bind_param("ss", $url, $idRasp);
            
            if ($stmt->execute()) {
                return ['success' => true, 'message' => 'Raspberry URL updated successfully'];
            }
        } else {
            $stmt = $this->db->prepare("INSERT INTO raspberry (url, id_rasp) VALUES (?, ?)");
            $stmt->bind_param("ss", $url, $idRasp);
            
            if ($stmt->execute()) {
                return ['success' => true, 'message' => 'Raspberry added successfully'];
            }
        }

        return ['success' => false, 'message' => 'Failed to update Raspberry URL'];
    }

    public function getAll(): array {
        $stmt = $this->db->prepare("SELECT * FROM raspberry ORDER BY date");
        $stmt->execute();
        $result = $stmt->get_result();
        
        $raspberries = [];
        while ($row = $result->fetch_assoc()) {
            $raspberries[] = $row;
        }
        
        return $raspberries;
    }
} 