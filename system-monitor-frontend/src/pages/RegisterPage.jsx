import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import styles from './RegisterPage.module.css';

export default function RegisterPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
            const res = await axios.post(`${API_BASE_URL}/api/auth/register`, {
                username,
                password,
            });
            const token = res.data.token;
            localStorage.setItem("token", token);
            navigate("/agents");
        } catch (err) {
            setError(err.response?.data?.message || "Registration error");
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.box}>
                <h2>Registration</h2>
                <form onSubmit={handleRegister}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        autoComplete="username"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        autoComplete="new-password"
                    />
                    <button type="submit">Register</button>
                </form>
                {error && <div className={styles.error}>{error}</div>}
            </div>
        </div>
    );
}
