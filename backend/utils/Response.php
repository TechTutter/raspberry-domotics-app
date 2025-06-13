<?php

namespace Utils;

class Response {
    public static function json(array $data, int $statusCode = RESPONSE_SUCCESS): void {
        http_response_code($statusCode);
        header('Content-Type: application/json');
        echo json_encode($data);
        exit;
    }

    public static function error(string $message, int $statusCode = RESPONSE_SERVER_ERROR): void {
        self::json([
            'success' => false,
            'error' => $message
        ], $statusCode);
    }

    public static function success(array $data = []): void {
        self::json([
            'success' => true,
            'data' => $data
        ]);
    }
} 