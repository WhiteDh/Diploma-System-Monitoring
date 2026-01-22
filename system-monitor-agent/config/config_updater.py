import requests

from config.settings import SERVER_URL


def fetch_config(agent_id, token):
    try:
        response = requests.get(
            f"{SERVER_URL}/api/agents/{agent_id}/config",
            headers={"Authorization": f"Bearer {token}"}
        )
        response.raise_for_status()
        return response.json()
    except Exception as e:
        print(f"Failed to get the configuration: {e}")
        return None
