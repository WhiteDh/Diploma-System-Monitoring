import {useEffect, useState} from "react";
import {Client} from "@stomp/stompjs";
import {useNavigate} from "react-router-dom";

import styles from "./AgentsPage.module.css";

function AgentsPage() {
    const [agents, setAgents] = useState([]);
    const [statuses, setStatuses] = useState({});
    const [editIntervals, setEditIntervals] = useState({});
    const [loadingStates, setLoadingStates] = useState({});
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

        fetch(`${API_BASE_URL}/api/agents`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
            .then((res) => res.json())
            .then(async (data) => {
                console.log("Agents data:", data);
                setAgents(data);

                // Загружаем конфиги для каждого агента
                const initialEditValues = {};
                await Promise.all(
                    data.map(async (agent) => {
                        try {
                            const res = await fetch(`${API_BASE_URL}/api/agents/${agent.id}/config`, {
                                headers: {
                                    Authorization: `Bearer ${token}`,
                                },
                            });
                            if (res.ok) {
                                const configData = await res.json();
                                initialEditValues[agent.id] = configData.sendIntervalSeconds;
                            } else {
                                console.error(`Error loading config for agent ${agent.id}`);
                                initialEditValues[agent.id] = 60;
                            }
                        } catch (error) {
                            console.error(`Error loading config for agent ${agent.id}`, error);
                            initialEditValues[agent.id] = 60;
                        }
                    })
                );

                setEditIntervals(initialEditValues);
            })
            .catch(console.error);

        const stompClient = new Client({
            brokerURL: `${API_BASE_URL.replace(/^http/, "ws")}/ws-status/websocket`,
            reconnectDelay: 5000,
            onConnect: () => {
                stompClient.subscribe("/topic/status", (message) => {
                    const {agentId, online} = JSON.parse(message.body);
                    setStatuses((prev) => ({...prev, [agentId]: online}));
                });
            },
            onStompError: (frame) => {
                console.error("Broker reported error: " + frame.headers["message"]);
                console.error("Additional details: " + frame.body);
            },
        });

        stompClient.activate();

        return () => {
            stompClient.deactivate();
        };
    }, []);

    const handleIntervalChange = (agentId, value) => {
        const numValue = parseInt(value);
        if (!isNaN(numValue)) {
            setEditIntervals(prev => ({
                ...prev,
                [agentId]: Math.max(10, numValue) // Не меньше 10 секунд
            }));
        }
    };

    const saveInterval = async (agentId) => {
        const token = localStorage.getItem("token");
        const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
        const newInterval = editIntervals[agentId];

        if (newInterval < 10) {
            alert("Interval cannot be less than 10 seconds");
            return;
        }

        setLoadingStates(prev => ({...prev, [agentId]: true}));

        try {
            const response = await fetch(`${API_BASE_URL}/api/agents/${agentId}/config`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({sendIntervalSeconds: newInterval}),
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }

            const updatedConfig = await response.json();

            setAgents(prevAgents =>
                prevAgents.map(agent =>
                    agent.id === agentId
                        ? {...agent, configuration: updatedConfig}
                        : agent
                )
            );

        } catch (error) {
            console.error("Update error details:", error);
            alert(`Update failed: ${error.message}`);

            // Reset to previous value
            setEditIntervals(prev => ({
                ...prev,
                [agentId]: agents.find(a => a.id === agentId)?.configuration?.sendIntervalSeconds || 60
            }));
        } finally {
            setLoadingStates(prev => ({...prev, [agentId]: false}));
        }
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.heading}>Agents</h2>
            <ul className={styles.agentList}>
                {agents.map((agent) => (
                    <li
                        key={agent.id}
                        onClick={() => navigate(`/agents/${agent.id}`)}
                        className={styles.agentItem}
                    >
                        <div className={styles.agentInfo}>
                        <span
                            className={`${styles.statusDot} ${
                                statuses[agent.id] ? styles.statusOnline : styles.statusOffline
                            }`}
                        ></span>
                            <span>
                            🖥 {agent.name || `Agent #${agent.id}`} —{" "}
                                {statuses[agent.id] === undefined
                                    ? "..."
                                    : statuses[agent.id]
                                        ? "Online"
                                        : "Offline"}
                        </span>
                        </div>

                        <div
                            className={styles.controls}
                            onClick={(e) => e.stopPropagation()}
                        >
                            <input
                                type="number"
                                min="10"
                                value={editIntervals[agent.id] || 60}
                                onChange={(e) => handleIntervalChange(agent.id, e.target.value)}
                                className={styles.input}
                            />
                            <button
                                onClick={() => saveInterval(agent.id, agent.uuid)}
                                disabled={loadingStates[agent.id]}
                                className={styles.saveButton}
                            >
                                {loadingStates[agent.id] ? "Saving..." : "Save"}
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default AgentsPage;