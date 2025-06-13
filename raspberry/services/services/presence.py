import json
import time
import RPi.GPIO as GPIO
from pathlib import Path

PIR_PIN = 13
RULES_FILE = Path(__file__).parent.parent / 'config' / 'presence_rules.json'

class PresenceService:
    def __init__(self, pir_pin=PIR_PIN, rules_file=RULES_FILE, check_interval=3):
        self.pir_pin = pir_pin
        self.rules_file = rules_file
        self.check_interval = check_interval
        self.gpio_targets = []
        self.action = None
        self.notification = None
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)
        GPIO.setup(self.pir_pin, GPIO.IN)
        self._load_rules()

    def _load_rules(self):
        try:
            with open(self.rules_file) as f:
                data = json.load(f)
            self.gpio_targets = [int(pin) for pin in data.get('gpio', [])]
            self.action = data.get('azione', 'Accendi')
            self.notification = data.get('notifica', 'False')
        except Exception as e:
            print(f"Error loading presence rules: {e}")
            self.gpio_targets = []
            self.action = None
            self.notification = None

    def apply_action(self):
        for pin in self.gpio_targets:
            GPIO.setup(pin, GPIO.OUT)
            if self.action == 'Accendi':
                GPIO.output(pin, GPIO.HIGH)
            elif self.action == 'Spegni':
                GPIO.output(pin, GPIO.LOW)

    def run(self):
        print("PresenceService started.")
        while True:
            try:
                if GPIO.input(self.pir_pin):
                    print("Presence detected")
                    self.apply_action()
                    # if self.notification == 'True':
                    #     # send notification logic here
                    time.sleep(self.check_interval)
            except Exception as e:
                print(f"Error in PresenceService loop: {e}")
                time.sleep(self.check_interval) 