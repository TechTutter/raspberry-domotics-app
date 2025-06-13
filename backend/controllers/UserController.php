<?php

namespace Controllers;

use Models\User;
use Utils\Response;

class UserController {
    private $user;

    public function __construct() {
        $this->user = new User();
    }

    public function register(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            Response::error('Method not allowed', 405);
        }

        $username = $_POST['username'] ?? '';
        $password = $_POST['password'] ?? '';
        $raspberryId = $_POST['id_rasp'] ?? '';

        if (empty($username) || empty($password) || empty($raspberryId)) {
            Response::error('All fields are required', 400);
        }

        $result = $this->user->register($username, $password, $raspberryId);
        
        if ($result['success']) {
            Response::success(['message' => $result['message']], $result['code']);
        } else {
            Response::error($result['message'], $result['code']);
        }
    }

    public function getUnassignedRaspberries(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
            Response::error('Method not allowed', 405);
        }

        $raspberries = $this->user->getUnassignedRaspberries();
        Response::success(['raspberries' => $raspberries]);
    }
} 