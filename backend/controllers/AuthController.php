<?php

namespace Controllers;

use Models\Auth;
use Utils\Response;

class AuthController {
    private $auth;

    public function __construct() {
        $this->auth = new Auth();
    }

    public function login(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            Response::error('Method not allowed', 405);
        }

        $username = $_POST['username'] ?? '';
        $password = $_POST['password'] ?? '';

        if (empty($username) || empty($password)) {
            Response::error('Username and password are required', 400);
        }

        if ($this->auth->login($username, $password)) {
            $token = $this->auth->generateToken($username);
            Response::success(['token' => $token]);
        }

        Response::error('Invalid credentials', 401);
    }

    public function validateToken(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            Response::error('Method not allowed', 405);
        }

        $token = $_POST['token'] ?? '';

        if (empty($token)) {
            Response::error('Token is required', 400);
        }

        $result = $this->auth->validateToken($token);
        
        if (!$result['valid']) {
            Response::error('Invalid or expired token', 401);
        }

        Response::success(['url' => $result['url']]);
    }
} 