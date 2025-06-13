from flask import Blueprint, request
import os

system_bp = Blueprint('system', __name__)

@system_bp.route('/seriale')
def get_serial():
    cpuserial = "0000000000000000"
    try:
        with open('/proc/cpuinfo', 'r') as f:
            for line in f:
                if line.startswith('Serial'):
                    cpuserial = line[10:26]
    except:
        cpuserial = "ERROR000000000"
    return cpuserial

@system_bp.route('/tokenSensore', methods=['POST'])
def check_sensor_token():
    token = request.form.get('token')
    from ..api.utils.auth import check_token
    return "true" if check_token(token) else "false" 