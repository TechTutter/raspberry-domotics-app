import requests
from ..config.settings import BACKEND_URL

def validate_token(token: str) -> bool:
    """
    Validate a token against the backend server.
    
    Args:
        token: The token to validate
        
    Returns:
        bool: True if token is valid, False otherwise
    """
    try:
        response = requests.post(
            f'{BACKEND_URL}/tokenIsValid.php',
            data={'token': token}
        )
        data = response.json()
        return data.get('code') == 0
    except Exception as e:
        print(f"Error validating token: {e}")
        return False 