import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { useEffect, useState } from 'react'

import StartPage from './pages/StartPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import AgentsPage from './pages/AgentsPage'
import AgentMetricsPage from './pages/AgentMetricsPage'
import Notifications from './pages/Notifications'

function App() {
    const [token, setToken] = useState(localStorage.getItem('token'))

    useEffect(() => {
        const handleStorageChange = () => {
            setToken(localStorage.getItem('token'))
        }

        window.addEventListener('storage', handleStorageChange)
        return () => window.removeEventListener('storage', handleStorageChange)
    }, [])

    const ProtectedRoute = ({ children }) => {
        return token ? children : <Navigate to="/" replace />
    }

    return (
        <Router>
            <Notifications />
            <Routes>
                <Route path="/" element={<StartPage />} />
                <Route path="/login" element={<LoginPage onLogin={() => setToken(localStorage.getItem('token'))} />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route
                    path="/agents"
                    element={
                        <ProtectedRoute>
                            <AgentsPage />
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/agents/:id"
                    element={
                        <ProtectedRoute>
                            <AgentMetricsPage />
                        </ProtectedRoute>
                    }
                />
                <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
        </Router>
    )
}

export default App
