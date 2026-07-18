const API_BASE_URL = "http://localhost:8080/api";

export async function getHealthStatus() {
    const response = await fetch(`${API_BASE_URL}/health`);

    if (!response.ok) {
        throw new Error("Failed to reach backend");
    }

    return response.text();
}
