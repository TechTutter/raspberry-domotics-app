import os

# GPIO Configuration
GPIO_PINS = {
    'available': [17, 18, 27, 22, 23, 5, 6, 12],
    'temperature_sensor': 26
}

# API Configuration
API_CONFIG = {
    'host': '127.0.0.1',
    'port': 5000,
    'debug': True,
    'threaded': True
}

# Backend Configuration
BACKEND_URL = os.getenv('BACKEND_URL', 'http://example.com/api')

# Sensor Configuration
SENSOR_CONFIG = {
    'temperature': {
        'type': 'DHT22',
        'pin': GPIO_PINS['temperature_sensor'],
        'check_interval': 4  # seconds
    }
}

# File Paths
DATA_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
TEMP_RULES_FILE = os.path.join(DATA_DIR, 'data', 'temperature_rules.json')
PRESENCE_RULES_FILE = os.path.join(DATA_DIR, 'data', 'presence_rules.json') 