<?php

namespace Controllers;

use Models\Raspberry;
use Utils\Response;

class RaspberryController {
    private $raspberry;

    public function __construct() {
        $this->raspberry = new Raspberry();
    }

    public function updateUrl(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
            Response::error('Method not allowed', 405);
        }

        $idRasp = $_POST['id_rasp'] ?? '';
        $url = $_POST['url'] ?? '';

        if (empty($idRasp) || empty($url)) {
            Response::error('Raspberry ID and URL are required', 400);
        }

        $result = $this->raspberry->updateUrl($idRasp, $url);
        
        if ($result['success']) {
            Response::success(['message' => $result['message']]);
        } else {
            Response::error($result['message'], 500);
        }
    }

    public function getAll(): void {
        if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
            Response::error('Method not allowed', 405);
        }

        $raspberries = $this->raspberry->getAll();
        Response::success(['raspberries' => $raspberries]);
    }
} 