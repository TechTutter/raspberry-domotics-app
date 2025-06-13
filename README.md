# Raspberry Domotics

## Disclaimer

This project was developed as part of my bachelor thesis and is no longer actively maintained. It was originally created to demonstrate a home automation system using Raspberry Pi, allowing control of GPIO pins to manage connected devices through sensor data or APIs via an Android app. While the code is provided as a reference, it may not work as-is today due to:

- Outdated dependencies and lack of maintenance
- Many endpoints were removed as they are no longer valid, and were replaced with sample urls.

This repository is a reupload of the original project, hence the single commit history.

## Overview

Raspberry Domotics is a comprehensive home automation system leveraging Raspberry Pi to control and monitor various home devices. The system integrates temperature and presence sensors, GPIO control, and a modern Android app for seamless user interaction.

The final project featured a full circuit to connect the raspberry to a relay and multiple devices using a breadboard. I connected a couple of sensors, a small PC fan, and some light bulbs, that could be controlled remotely by the app in multiple ways. It is possible for example to turn on / off lights with voice commands, or to automatically trigger the fan when the temperature is above a specific threshold, or turn off a light at a predefined time.

## Features

- **Hardware Integration**:

  - 8-port Relay control through Raspberry Pi GPIO
  - Support for multiple devices. For my bachelor demo showcase I used some light bulbs and a fans.
  - Breadboard-based circuit design for easy prototyping

- **Device Control**:

  - Voice command integration for hands-free control
  - Remote on/off control of connected devices
  - Programmable timers for automated device scheduling
  - Rule-based automation based on sensor triggers (e.g. turn on the fan when temperature was above a certain threshold).

- **Sensor Integration**:

  - Temperature monitoring with automatic fan control
  - Presence detection for automated lighting
  - Real-time sensor data visualization

- **Smart Automation**:

  - Conditional rules based on temperature thresholds
  - Time-based scheduling for devices
  - Presence-based automation
  - Custom rule creation for device interactions

- **User Interface**:

  - Modern Android app for remote control
  - Real-time device status monitoring
  - Intuitive scheduling interface
  - Voice command support

- **Security**:
  - Token-based authentication
  - Secure API endpoints
  - Protected device access

## Tech Stack

- **Backend**: Python, Flask, RPi.GPIO, Adafruit_DHT
- **Frontend**: Android (Java)
- **Database**: MySQL
- **Authentication**: Token-based
- **Deployment**: Systemd services for reliable operation

## Setup

### Prerequisites

- Raspberry Pi (3B+ or newer recommended)
- Android Studio
- MySQL Server
- Python 3.7+

### Backend Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/raspberry-domotics.git
   cd raspberry-domotics
   ```

2. Install dependencies:

   ```bash
   pip install -r requirements.txt
   ```

3. Configure the database:

   - Import `database/schema.sql` into your MySQL server.
   - Update `config/settings.py` with your database credentials.

4. Start the services:
   ```bash
   sudo systemctl enable temperature.service
   sudo systemctl enable presence.service
   sudo systemctl start temperature.service
   sudo systemctl start presence.service
   ```

### Android App Setup

1. Open the project in Android Studio.
2. Update the API endpoint in `JsonClient.java` to point to your Raspberry Pi's IP address.
3. Build and run the app on your device.

## Project Structure

```
raspberry-domotics/
├── android/                  # Android app
│   └── app/src/main/java/com/mirelorradie/tesi/homemanager/
│       ├── JsonClient.java   # JSON API client
│       ├── HttpClient.java   # HTTP client
│       └── ...               # Other app files
├── raspberry/                # Raspberry Pi backend
│   ├── services/             # Core services
│   │   ├── temperature.py    # Temperature service
│   │   ├── presence.py       # Presence service
│   │   └── main.py           # Main API server
│   ├── config/               # Configuration files
│   └── api/                  # API routes and utilities
├── database/                 # Database schema and migrations
└── README.md                 # Project documentation
```

## API Endpoints

- `POST /gpio`: Toggle GPIO state
- `POST /gpioOn`: Turn GPIO on
- `POST /gpioOff`: Turn GPIO off
- `POST /tuttoOn`: Turn all GPIOs on
- `POST /tuttoOff`: Turn all GPIOs off
- `POST /statoGpio`: Get GPIO status
- `POST /presenza`: Set presence rules
- `POST /temperatura`: Set temperature rules
- `POST /getTemperatura`: Get current temperature and humidity
- `GET /seriale`: Get Raspberry Pi serial number
- `POST /tokenSensore`: Verify sensor token
