import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

import styles from "./Notifications.module.css";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;


const Notifications = () => {
    const username = localStorage.getItem("username");
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        if (!username) return;

        const wsUrl = API_BASE_URL.replace(/^http/, "ws") + `/ws-status/websocket`;

        const stompClient = new Client({
            brokerURL: wsUrl,
            reconnectDelay: 50000,
            onConnect: () => {
                stompClient.subscribe(`/topic/alerts/${username}`, (message) => {
                    const payload = JSON.parse(message.body);
                    setNotifications((prev) => [...prev, payload]);

                    const audio1 = new Audio(`/notification.mp3`);
                    const audio2 = new Audio(`/notification.mp3`);
                    const audio3 = new Audio(`/notification.mp3`);


                    audio1.play().catch(() => {
                    });

                    audio1.addEventListener('ended', () => {
                        audio2.play().catch(() => {});
                    });
                    audio2.addEventListener('ended', () => {
                        audio3.play().catch(() => {});
                    });
                });
            },
            onStompError: (frame) => {
                console.error("error STOMP:", frame.headers.message);
            },
        });

        stompClient.activate();

        return () => {
            stompClient.deactivate();
        };
    }, [username]);

    useEffect(() => {
        if (notifications.length === 0) return;

        const timer = setTimeout(() => {
            setNotifications((prev) => prev.slice(1));
        }, 15000);

        return () => clearTimeout(timer);
    }, [notifications]);

    return (
        <div className={styles.container}>
            {notifications.map((notif, idx) => (
                <div key={idx} className={styles.notification}>
                    <b>{notif.agentName}</b>: {notif.message}
                </div>
            ))}
        </div>

    );
};

export default Notifications;
