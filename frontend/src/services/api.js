const API_BASE_URL = "http://localhost:8080/api";

export async function getHealthStatus() {
    const response = await fetch(`${API_BASE_URL}/health`);

    if (!response.ok) {
        throw new Error("Failed to reach backend");
    }

    return response.text();
}

export async function uploadRepository(file) {
    const formData = new FormData();
    formData.append("file", file);

    const response = await fetch(`${API_BASE_URL}/upload`, {
        method: "POST",
        body: formData,
    });

    if (!response.ok) {
        throw new Error("Failed to upload repository");
    }

    return response.json();
}
