import json
import time
import Adafruit_DHT
from ..config.settings import SENSOR_CONFIG, TEMP_RULES_FILE
from ..api.utils.gpio import set_pin

class TemperatureService:
    def __init__(self):
        self.sensor = Adafruit_DHT.DHT22
        self.pin = SENSOR_CONFIG['temperature']['pin']
        self.check_interval = SENSOR_CONFIG['temperature']['check_interval']
        self.rules = self._load_rules()

    def _load_rules(self) -> dict:
        """Load temperature rules from JSON file."""
        try:
            with open(TEMP_RULES_FILE, 'r') as f:
                return json.load(f)
        except FileNotFoundError:
            print(f"Rules file not found: {TEMP_RULES_FILE}")
            return {}

    def read_temperature(self) -> float:
        """Read temperature from DHT22 sensor."""
        humidity, temperature = Adafruit_DHT.read_retry(self.sensor, self.pin)
        if temperature is not None:
            return temperature
        raise RuntimeError("Failed to read temperature")

    def apply_rules(self, temperature: float):
        """Apply temperature rules to control GPIO pins."""
        for rule in self.rules.get('rules', []):
            if rule['min_temp'] <= temperature <= rule['max_temp']:
                for action in rule['actions']:
                    set_pin(action['pin'], action['state'])

    def run(self):
        """Main service loop."""
        while True:
            try:
                temperature = self.read_temperature()
                self.apply_rules(temperature)
                time.sleep(self.check_interval)
            except Exception as e:
                print(f"Error in temperature service: {e}")
                time.sleep(self.check_interval) 