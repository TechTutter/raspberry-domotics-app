<?php

namespace Models;

use Database;

class Auth {
    private $db;

    public function __construct() {
        $this->db = Database::getInstance()->getConnection();
    }

    public function login(string $username, string $password): bool {
        $stmt = $this->db->prepare("SELECT username, password FROM utenti WHERE username = ?");
        $stmt->bind_param("s", $username);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows === 0) {
            return false;
        }

        $user = $result->fetch_assoc();
        return password_verify($password, $user['password']);
    }

    public function validateToken(string $token): array {
        $stmt = $this->db->prepare("
            SELECT t.expireDate, r.url 
            FROM token t
            JOIN utenti u ON t.username = u.username
            JOIN raspberry r ON u.id_rasp = r.id_rasp
            WHERE t.token = ?
        ");
        
        $stmt->bind_param("s", $token);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows === 0) {
            return ['valid' => false, 'url' => ''];
        }

        $row = $result->fetch_assoc();
        $isExpired = strtotime($row['expireDate']) - time();

        return [
            'valid' => $isExpired > 0,
            'url' => $isExpired > 0 ? $row['url'] : ''
        ];
    }

    public function generateToken(string $username): string {
        $token = bin2hex(random_bytes(32));
        $expiry = date('Y-m-d H:i:s', time() + TOKEN_EXPIRY);

        $stmt = $this->db->prepare("
            INSERT INTO token (token, username, expireDate) 
            VALUES (?, ?, ?)
        ");
        
        $stmt->bind_param("sss", $token, $username, $expiry);
        $stmt->execute();

        return $token;
    }
} 