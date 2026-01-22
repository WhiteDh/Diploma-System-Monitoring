import { useNavigate } from "react-router-dom";

import styles from './StartPage.module.css';

export default function StartPage() {
    const navigate = useNavigate();

    return (
        <div className={styles.container}>
            <h1>Welcome!</h1>
            <p>Please log in or register to continue.</p>

            <div className={styles.buttons}>
                <button
                    className={`${styles.btn} ${styles.loginBtn}`}
                    onClick={() => navigate("/login")}
                >
                    Log In
                </button>

                <button
                    className={`${styles.btn} ${styles.registerBtn}`}
                    onClick={() => navigate("/register")}
                >
                    Register
                </button>
            </div>
        </div>
    );
}
