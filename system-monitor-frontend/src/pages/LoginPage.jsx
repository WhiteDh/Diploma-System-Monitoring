import {useNavigate} from "react-router-dom";
import {useState} from "react";
import axios from "axios";

import styles from "./LoginPage.module.css"

function LoginPage({ onLogin }) {
    const navigate = useNavigate();
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    async function handleLogin(e) {
        e.preventDefault();
        setError("");

        if (!username.trim() || !password) {
            setError("Enter login and password");
            return;
        }

        try {
            const res = await axios.post(`${API_BASE_URL}/api/auth/login`, {
                username,
                password,
            });
            localStorage.setItem("token", res.data.token);
            localStorage.setItem("username", username);
            if (onLogin) onLogin();
            navigate("/agents");
        } catch (err) {
            setError(err.response?.data?.message || "Login error");
        }
    }

    return (
        <div className={styles.wrapper}>
            <div className={styles.card}>
                <h2 className={styles.title}>Login</h2>

                <form onSubmit={handleLogin} className={styles.form}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        autoComplete="username"
                        required
                        className={styles.input}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        autoComplete="current-password"
                        required
                        className={styles.input}
                    />

                    {error && <div className={styles.error}>{error}</div>}

                    <button type="submit" className={styles.button}>
                        Log In
                    </button>
                </form>

                <p className={styles.registerText}>
                    Don't have an account?{" "}
                    <span
                        className={styles.registerLink}
                        onClick={() => navigate("/register")}
                    >
                    Register
                </span>
                </p>
            </div>
        </div>
    );
}

export default LoginPage;
