import requests
from config.settings import SERVER_URL, AGENT_NAME
from utils.auth import get_token_and_user_id
from core.metrics_collector import collect_metrics

def register_agent(token, agent_uuid):
    print("Send the token:", token)
    print("Headers:", {"Authorization": f"Bearer {token}"})
    print("Data:", {"name": AGENT_NAME, "uuid": agent_uuid})
    response = requests.post(
        f"{SERVER_URL}/api/agents/register",
        headers={"Authorization": f"Bearer {token}"},
        json={"name": AGENT_NAME, "uuid": agent_uuid}
    )
    response.raise_for_status()
    return response.json()["id"]

def send_metrics(token, agent_id):
    metrics = collect_metrics(agent_id)
    print(metrics)
    metrics["agentId"] = agent_id
    response = requests.post(
        f"{SERVER_URL}/api/metrics",
        headers={"Authorization": f"Bearer {token}"},
        json=metrics
    )
    response.raise_for_status()

