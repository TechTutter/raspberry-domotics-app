from flask import Blueprint, request, Response
import json
import RPi.GPIO as GPIO
from ..api.utils.auth import check_token

gpio_bp = Blueprint('gpio', __name__)

# Pins utilizzabili
PINS_USABILI = [17, 18, 27, 22, 23, 5, 6, 12]

@gpio_bp.route('/gpio', methods=['POST'])
def toggle_gpio():
    gpio = request.form['gpio']
    token = request.form.get('token')
    if not check_token(token):
        return "token sbagliato"
    GPIO.setup(int(gpio), GPIO.OUT)
    if GPIO.input(int(gpio)):
        GPIO.output(int(gpio), GPIO.LOW)
        return f"{gpio} spento"
    else:
        GPIO.output(int(gpio), GPIO.HIGH)
        return f"{gpio} acceso"

@gpio_bp.route('/gpioOn', methods=['POST'])
def turn_on_gpio():
    gpio = request.form['gpio']
    token = request.form.get('token')
    if not check_token(token):
        return "token sbagliato"
    GPIO.setup(int(gpio), GPIO.OUT)
    GPIO.output(int(gpio), GPIO.HIGH)
    return f"{gpio} acceso"

@gpio_bp.route('/gpioOff', methods=['POST'])
def turn_off_gpio():
    gpio = request.form['gpio']
    token = request.form.get('token')
    if not check_token(token):
        return "token sbagliato"
    GPIO.setup(int(gpio), GPIO.OUT)
    GPIO.output(int(gpio), GPIO.LOW)
    return f"{gpio} spento"

@gpio_bp.route('/tuttoOn', methods=['POST'])
def turn_all_on():
    token = request.form.get('token')
    if not check_token(token):
        return "token sbagliato"
    for pin in PINS_USABILI:
        GPIO.setup(pin, GPIO.OUT)
        GPIO.output(pin, GPIO.HIGH)
    return "ok"

@gpio_bp.route('/tuttoOff', methods=['POST'])
def turn_all_off():
    token = request.form.get('token')
    if not check_token(token):
        return "token sbagliato"
    for pin in PINS_USABILI:
        GPIO.setup(pin, GPIO.OUT)
        GPIO.output(pin, GPIO.LOW)
    return "ok"

@gpio_bp.route('/statoGpio', methods=['POST'])
def get_gpio_status():
    token = request.form.get('token')
    if not check_token(token):
        return "token errato"
    gpio_accesi = []
    for pin in PINS_USABILI:
        GPIO.setup(pin, GPIO.OUT)
        if GPIO.input(pin):
            gpio_accesi.append(pin)
    data = {'gpioAccesi': gpio_accesi}
    return Response(json.dumps(data), mimetype='application/json') 