<?php

// Database configuration
define('DB_HOST', 'localhost');
define('DB_USER', 'S4254186');
define('DB_PASS', 'formaggio18');
define('DB_NAME', 'S4254186');

// Security settings
define('HASH_ALGO', 'sha256');
define('TOKEN_EXPIRY', 3600); // 1 hour in seconds

// Error reporting
if (getenv('ENVIRONMENT') === 'production') {
    error_reporting(0);
    ini_set('display_errors', 0);
} else {
    error_reporting(E_ALL);
    ini_set('display_errors', 1);
}

// Session configuration
ini_set('session.cookie_httponly', 1);
ini_set('session.use_only_cookies', 1);
ini_set('session.cookie_secure', 1);

// Response codes
define('RESPONSE_SUCCESS', 200);
define('RESPONSE_UNAUTHORIZED', 401);
define('RESPONSE_FORBIDDEN', 403);
define('RESPONSE_NOT_FOUND', 404);
define('RESPONSE_SERVER_ERROR', 500); 