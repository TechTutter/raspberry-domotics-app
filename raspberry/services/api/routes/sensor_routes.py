from flask import Blueprint, request, Response
import json
import Adafruit_DHT
from pathlib import Path

sensor_bp = Blueprint('sensor', __name__)

# Configurazione sensore temperatura
TEMP_SENSOR_PIN = 26
TEMP_SENSOR_TYPE = Adafruit_DHT.DHT22

# Percorsi file di configurazione
PRESENCE_RULES_FILE = Path(__file__).parent.parent.parent / 'config' / 'presence_rules.json'
TEMP_RULES_FILE = Path(__file__).parent.parent.parent / 'config' / 'temperature_rules.json'

@sensor_bp.route('/presenza', methods=['POST'])
def set_presence_rules():
    data = request.get_json()
    with open(PRESENCE_RULES_FILE, 'w') as f:
        json.dump(data, f)
    return "ok"

@sensor_bp.route('/temperatura', methods=['POST'])
def set_temperature_rules():
    data = request.get_json()
    with open(TEMP_RULES_FILE, 'w') as f:
        json.dump(data, f)
    return "ok"

@sensor_bp.route('/getTemperatura', methods=['POST'])
def get_temperature():
    humidity, temperature = Adafruit_DHT.read_retry(TEMP_SENSOR_TYPE, TEMP_SENSOR_PIN)
    data = {
        'temperatura': round(temperature, 1),
        'umidita': round(humidity, 1)
    }
    return Response(json.dumps(data), mimetype='application/json') 