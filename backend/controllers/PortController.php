<?php

namespace Controllers;

use Models\Port;
use Utils\Response;

class PortController {
    private $port;

    public function __construct() {
        $this->port = new Port();
    }

    public function getPorts(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            Response::error('Method not allowed', 405);
        }

        $username = $_POST['username'] ?? '';

        if (empty($username)) {
            Response::error('Username is required', 400);
        }

        $ports = $this->port->getPorts($username);
        Response::success($ports);
    }

    public function updatePorts(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            Response::error('Method not allowed', 405);
        }

        $username = $_POST['username'] ?? '';
        $ports = [];

        for ($i = 1; $i <= 8; $i++) {
            $key = "porta$i";
            if (isset($_POST[$key])) {
                $ports[$key] = $_POST[$key];
            }
        }

        if (empty($username) || empty($ports)) {
            Response::error('Username and at least one port are required', 400);
        }

        if ($this->port->updatePorts($username, $ports)) {
            Response::success(['message' => 'Ports updated successfully']);
        }

        Response::error('Failed to update ports', 500);
    }
} 