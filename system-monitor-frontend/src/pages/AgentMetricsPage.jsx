import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer,
    Brush, ReferenceLine, ReferenceArea
} from "recharts";
import utc from "dayjs/plugin/utc";
import dayjs from "dayjs";
import axios from "axios";

import styles from "./AgentMetricsPage.module.css";

dayjs.extend(utc);

const CHART_OPTIONS = [
    {
        key: "common", lines: [
            {dataKey: "temperature", color: "#ff7300", name: "Temperature (°C)"},
            {dataKey: "cpuUsage", color: "#8884d8", name: "CPU (%)"},
            {dataKey: "ramUsage", color: "#82ca9d", name: "RAM (%)"}
        ]
    },
    {
        key: "temperature", lines: [
            {dataKey: "temperature", color: "#ff7300", name: "Temperature (°C)"}
        ]
    },
    {
        key: "cpu", lines: [
            {dataKey: "cpuUsage", color: "#8884d8", name: "CPU (%)"}
        ]
    },
    {
        key: "ram", lines: [
            {dataKey: "ramUsage", color: "#82ca9d", name: "RAM (%)"}
        ]
    },
    {
        key: "disk", lines: [
            {dataKey: "diskUsage", color: "#3399ff", name: "Disk (%)"}
        ]
    },
    {
        key: "net", lines: [
            {dataKey: "networkIn", color: "#33cc33", name: "Incoming Traffic (KB/s)"},
            {dataKey: "networkOut", color: "#ff3333", name: "Outgoing Traffic (KB/s)"}
        ]
    }
];

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

function AgentMetricsPage() {
    const {id} = useParams();
    const [metrics, setMetrics] = useState([]);
    const [selectedDate, setSelectedDate] = useState(dayjs().format("YYYY-MM-DD"));
    const [activeChart, setActiveChart] = useState("common");
    const [loading, setLoading] = useState(false);

    // Threshold values
    const [thresholds, setThresholds] = useState({
        temperature: 50,
        cpuUsage: 80,
        ramUsage: 80,
        diskUsage: 70,
        networkIn: 1000,
        networkOut: 1000,
    });


    // Fetch metrics
    useEffect(() => {
        const fetchMetrics = async () => {
            setLoading(true);
            const token = localStorage.getItem("token");
            const from = dayjs(selectedDate).startOf("day").toISOString();
            const to = dayjs(selectedDate).endOf("day").toISOString();

            try {
                const res = await axios.get(`${API_BASE_URL}/api/metrics/history`, {
                    params: {agentId: id, from, to},
                    headers: {Authorization: `Bearer ${token}`},
                });

                const data = res.data.map(item => ({
                    time: dayjs.utc(item.createdAt).local().format("HH:mm:ss"),
                    cpuUsage: item.cpuUsage,
                    ramUsage: item.ramUsage,
                    temperature: item.temperature,
                    diskUsage: item.diskUsage,
                    networkIn: item.networkIn,
                    networkOut: item.networkOut
                }));

                setMetrics(data);
            } catch (err) {
                console.error("Failed to fetch metrics:", err);
                setMetrics([]);
            } finally {
                setLoading(false);
            }
        };

        fetchMetrics();
    }, [id, selectedDate]);

    // Fetch thresholds
    useEffect(() => {
        const fetchThresholds = async () => {
            try {
                const token = localStorage.getItem("token");
                const res = await axios.get(`${API_BASE_URL}/api/thresholds`, {
                    params: {agentId: id},
                    headers: {Authorization: `Bearer ${token}`}
                });
                const data = res.data;

                setThresholds({
                    temperature: data.tempMax ?? 50,
                    cpuUsage: data.cpuMax ?? 80,
                    ramUsage: data.ramMax ?? 80,
                    diskUsage: data.diskMax ?? 70,
                    networkIn: data.networkInMax ?? 1000,
                    networkOut: data.networkOutMax ?? 1000,
                });
            } catch (err) {
                console.error("Failed to fetch thresholds:", err);
            }
        };

        if (id) fetchThresholds();
    }, [id]);

    // Update threshold on server
    const updateThresholdOnServer = async (newThresholds) => {
        try {
            const token = localStorage.getItem("token");
            const body = {
                cpuMax: newThresholds.cpuUsage,
                ramMax: newThresholds.ramUsage,
                tempMax: newThresholds.temperature,
                diskMax: newThresholds.diskUsage,
                networkInMax: newThresholds.networkIn,
                networkOutMax: newThresholds.networkOut,
            };

            await axios.put(`${API_BASE_URL}/api/thresholds`, body, {
                params: {agentId: id},
                headers: {Authorization: `Bearer ${token}`}
            });
        } catch (err) {
            console.error("Failed to update threshold:", err);
        }
    };

    // Handle threshold change
    const handleThresholdChange = (value) => {
        if (!activeChart) return;

        const activeChartObj = CHART_OPTIONS.find(c => c.key === activeChart);
        if (!activeChartObj || activeChartObj.lines.length !== 1) return;

        const key = activeChartObj.lines[0].dataKey;
        const numValue = Number(value);

        setThresholds(prev => {
            const newThresholds = {...prev, [key]: numValue};
            updateThresholdOnServer(newThresholds);
            return newThresholds;
        });
    };

    // Current threshold key and value
    const activeChartObj = CHART_OPTIONS.find(c => c.key === activeChart);
    const currentThresholdKey = (activeChartObj && activeChartObj.lines.length === 1) ?
        activeChartObj.lines[0].dataKey : null;
    const currentThresholdValue = currentThresholdKey ? thresholds[currentThresholdKey] : null;

    // Handle day selection
    const handleDayButton = (offset) => {
        setSelectedDate(dayjs().subtract(offset, "day").format("YYYY-MM-DD"));
    };

    // Get max range for slider
    const getMaxRange = () => {
        if (currentThresholdKey === "temperature") return 100;
        if (currentThresholdKey === "cpuUsage") return 100;
        if (currentThresholdKey === "ramUsage") return 100;
        if (currentThresholdKey === "diskUsage") return 100;
        return 5000; // For network values
    };

    return (
        <div className={styles['page-container']}>
            <div className={styles['card-container']}>
                <h2 className={styles.header}>Agent #{id} Metrics</h2>

                {/* Date selection panel */}
                <div className={styles['date-panel']}>
                    <div className={styles['date-buttons']}>
                        <button
                            className={styles['date-button']}
                            onClick={() => handleDayButton(0)}
                        >
                            Today
                        </button>
                        <button
                            className={styles['date-button']}
                            onClick={() => handleDayButton(1)}
                        >
                            Yesterday
                        </button>
                        <button
                            className={styles['date-button']}
                            onClick={() => handleDayButton(2)}
                        >
                            2 Days Ago
                        </button>
                    </div>
                    <input
                        type="date"
                        value={selectedDate}
                        onChange={e => setSelectedDate(e.target.value)}
                        className={styles['date-input']}
                    />
                </div>

                {/* Chart selection */}
                <div className={styles['chart-buttons']}>
                    {CHART_OPTIONS.map(({key}) => (
                        <button
                            key={key}
                            onClick={() => setActiveChart(key)}
                            className={`${styles['chart-button']} ${activeChart === key ? styles['active-chart-button'] : ''}`}
                        >
                            {key === "common" ? "Overview" :
                                key === "temperature" ? "Temperature" :
                                    key === "cpu" ? "CPU" :
                                        key === "ram" ? "RAM" :
                                            key === "disk" ? "Disk" :
                                                key === "net" ? "Network" : key}
                        </button>
                    ))}
                </div>

                {/* Loading indicator */}
                {loading && (
                    <div className={styles['loading-container']}>
                        <div className={styles['loading-spinner']}></div>
                        <p>Loading data...</p>
                    </div>
                )}

                {/* Chart */}
                {!loading && metrics.length > 0 && activeChartObj && (
                    <div className={styles['chart-container']}>
                        <ResponsiveContainer width="100%" height={400}>
                            <LineChart data={metrics} margin={{top: 20, right: 30, left: 20, bottom: 5}}>
                                <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0"/>
                                <XAxis dataKey="time" stroke="#777"/>
                                <YAxis stroke="#777"/>
                                <Tooltip
                                    contentStyle={{
                                        backgroundColor: "#fff",
                                        border: "1px solid #e0e0e0",
                                        borderRadius: "6px",
                                        boxShadow: "0 3px 10px rgba(0, 0, 0, 0.1)",
                                        padding: "12px"
                                    }}
                                    labelStyle={{fontWeight: 'bold'}}
                                />
                                <Legend wrapperStyle={{padding: "10px 0", fontSize: "14px"}}/>

                                {/* Threshold background */}
                                {activeChart !== "common" && currentThresholdKey && thresholds[currentThresholdKey] !== undefined && (
                                    <>
                                        <ReferenceArea
                                            y1="auto" y2={thresholds[currentThresholdKey]}
                                            x1="dataMin" x2="dataMax"
                                            fill="#d4f8d4"
                                            fillOpacity={0.3}
                                            ifOverflow="extendDomain"
                                        />
                                        <ReferenceArea
                                            y1={thresholds[currentThresholdKey]} y2="auto"
                                            x1="dataMin" x2="dataMax"
                                            fill="#f8d4d4"
                                            fillOpacity={0.3}
                                            ifOverflow="extendDomain"
                                        />
                                    </>
                                )}

                                {/* Data lines */}
                                {activeChartObj.lines.map(({dataKey, color, name}) => (
                                    <Line
                                        key={dataKey}
                                        type="monotone"
                                        dataKey={dataKey}
                                        stroke={color}
                                        name={name}
                                        strokeWidth={2}
                                        dot={{r: 0}}
                                        activeDot={{r: 5}}
                                    />
                                ))}

                                {/* Threshold lines */}
                                {activeChartObj.lines.map(({dataKey, color}) => (
                                    thresholds[dataKey] !== undefined && (
                                        <ReferenceLine
                                            key={"ref_" + dataKey}
                                            y={thresholds[dataKey]}
                                            stroke={color}
                                            strokeDasharray="3 3"
                                            label={{
                                                position: "right",
                                                value: `Threshold ${thresholds[dataKey]}`,
                                                fill: color,
                                                fontSize: 12
                                            }}
                                        />
                                    )
                                ))}

                                <Brush dataKey="time" height={30} stroke="#8884d8"/>
                            </LineChart>
                        </ResponsiveContainer>
                    </div>
                )}

                {/* No data message */}
                {!loading && metrics.length === 0 && (
                    <div className={styles['no-data']}>
                        <p>No data available for selected date</p>
                    </div>
                )}

                {/* Threshold slider */}
                {activeChart !== "common" && currentThresholdKey && (
                    <div className={styles['threshold-container']}>
                        <h3 className={styles['threshold-header']}>
                            Threshold for {currentThresholdKey === "temperature" ? "Temperature (°C)" :
                            currentThresholdKey === "cpuUsage" ? "CPU (%)" :
                                currentThresholdKey === "ramUsage" ? "RAM (%)" :
                                    currentThresholdKey === "diskUsage" ? "Disk (%)" :
                                        currentThresholdKey === "networkIn" ? "Incoming Traffic (KB/s)" :
                                            currentThresholdKey === "networkOut" ? "Outgoing Traffic (KB/s)" : currentThresholdKey}:
                        </h3>

                        <div className={styles['slider-container']}>
                            <input
                                type="range"
                                min={0}
                                max={getMaxRange()}
                                value={currentThresholdValue}
                                onChange={e => handleThresholdChange(e.target.value)}
                                className={styles.slider}
                            />
                            <span className={styles['slider-value']}>{currentThresholdValue}</span>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
export default AgentMetricsPage;