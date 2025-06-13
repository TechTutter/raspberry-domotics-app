<?php

require_once __DIR__ . '/bootstrap.php';

use Controllers\AuthController;
use Controllers\PortController;
use Controllers\UserController;
use Controllers\RaspberryController;
use Utils\Response;

$action = $_GET['action'] ?? '';

try {
    switch ($action) {
        case 'login':
            $controller = new AuthController();
            $controller->login();
            break;

        case 'validate-token':
            $controller = new AuthController();
            $controller->validateToken();
            break;

        case 'get-ports':
            $controller = new PortController();
            $controller->getPorts();
            break;

        case 'update-ports':
            $controller = new PortController();
            $controller->updatePorts();
            break;

        case 'register':
            $controller = new UserController();
            $controller->register();
            break;

        case 'get-unassigned-raspberries':
            $controller = new UserController();
            $controller->getUnassignedRaspberries();
            break;

        case 'update-raspberry-url':
            $controller = new RaspberryController();
            $controller->updateUrl();
            break;

        case 'get-all-raspberries':
            $controller = new RaspberryController();
            $controller->getAll();
            break;

        default:
            Response::error('Invalid action', 404);
    }
} catch (Exception $e) {
    Response::error($e->getMessage(), 500);
} 