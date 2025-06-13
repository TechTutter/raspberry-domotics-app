import RPi.GPIO as GPIO
from ..config.settings import GPIO_PINS

def setup_gpio():
    """Initialize GPIO pins."""
    GPIO.setmode(GPIO.BCM)
    for pin in GPIO_PINS['available']:
        GPIO.setup(pin, GPIO.OUT)
        GPIO.output(pin, GPIO.LOW)

def cleanup_gpio():
    """Cleanup GPIO pins."""
    GPIO.cleanup()

def set_pin(pin: int, state: bool):
    """
    Set a GPIO pin state.
    
    Args:
        pin: GPIO pin number
        state: True for HIGH, False for LOW
    """
    if pin not in GPIO_PINS['available']:
        raise ValueError(f"Pin {pin} is not available")
    
    GPIO.output(pin, GPIO.HIGH if state else GPIO.LOW)

def get_pin_state(pin: int) -> bool:
    """
    Get a GPIO pin state.
    
    Args:
        pin: GPIO pin number
        
    Returns:
        bool: True if HIGH, False if LOW
    """
    if pin not in GPIO_PINS['available']:
        raise ValueError(f"Pin {pin} is not available")
    
    return GPIO.input(pin) == GPIO.HIGH 