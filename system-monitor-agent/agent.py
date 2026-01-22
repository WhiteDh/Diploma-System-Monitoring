import time
import os
import json
import uuid

from config.config_updater import fetch_config
from core.sender import register_agent, send_metrics
from utils.auth import get_token_and_user_id
from config.settings import SEND_INTERVAL

CONFIG_PATH = "agent_config.json"

def load_or_create_uuid():
    if os.path.exists(CONFIG_PATH):
        try:
            with open(CONFIG_PATH, "r") as f:
                config = json.load(f)
                if "agent_uuid" in config and config["agent_uuid"]:
                    return config["agent_uuid"]
        except (json.JSONDecodeError, IOError):
            pass

    agent_uuid = str(uuid.uuid4())
    with open(CONFIG_PATH, "w") as f:
        json.dump({"agent_uuid": agent_uuid}, f)
    return agent_uuid


def main():
    try:
        print("Getting JWT token...")
        token, user_id = get_token_and_user_id()

        agent_uuid = load_or_create_uuid()
        print("Registering an agent...")
        agent_id = register_agent(token, agent_uuid)
        print(f"Agent registered (ID: {agent_id}). starting to send the metrics...")

        send_interval = SEND_INTERVAL

        while True:
            try:
                config = fetch_config(agent_id, token)
                if config and config.get("sendIntervalSeconds") != send_interval:
                    send_interval = config["sendIntervalSeconds"]
                    print(f"Interval updated: {send_interval} sec")

                send_metrics(token, agent_id)
                print("The metrics have been sent.")
            except Exception as e:
                print(f"Error when sending metrics: {e}")
            time.sleep(send_interval)

    except Exception as e:
        print(f"Agent startup error: {e}")

if __name__ == "__main__":
    main()
