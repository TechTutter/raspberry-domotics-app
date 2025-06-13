import os

# GPIO Configuration
GPIO_PINS = {
    'porta1': 17,
    'porta2': 18,
    'porta3': 27,
    'porta4': 22,
    'porta5': 23,
    'porta6': 24,
    'porta7': 25,
    'porta8': 4
}

# API Configuration
API_URL = os.getenv('API_URL', 'http://localhost:80')
API_TOKEN = os.getenv('API_TOKEN', '')

# Service Configuration
SERVICE_INTERVAL = 60  # seconds
TEMPERATURE_INTERVAL = 300  # seconds
PRESENCE_INTERVAL = 60  # seconds

# Ngrok Configuration
NGROK_AUTH_TOKEN = os.getenv('NGROK_AUTH_TOKEN', '')
NGROK_REGION = os.getenv('NGROK_REGION', 'eu') 