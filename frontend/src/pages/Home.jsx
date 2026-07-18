import { useEffect, useState } from "react";
import { getHealthStatus } from "../services/api";

function Home() {
    const [healthStatus, setHealthStatus] = useState("Checking backend...");

    useEffect(() => {
        getHealthStatus()
            .then(setHealthStatus)
            .catch(() => setHealthStatus("Backend is not connected"));
    }, []);

    return (
        <main>
            <h1>Git Merge Conflict Visualizer</h1>
            <p>{healthStatus}</p>
        </main>
    );
}

export default Home;
