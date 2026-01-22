import requests
from config.settings import SERVER_URL, USERNAME, PASSWORD

def get_token_and_user_id():
    response = requests.post(
        f"{SERVER_URL}/api/auth/login",
        json={"username": USERNAME, "password": PASSWORD}
    )
    response.raise_for_status()
    data = response.json()
    print("Response from the server when logging in:", data)

    token = data["token"]
    user_id = data["userId"]
    return token, user_id

