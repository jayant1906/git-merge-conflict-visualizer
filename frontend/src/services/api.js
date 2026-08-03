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

export async function getBranches(repositoryId) {
    const response = await fetch(`${API_BASE_URL}/repositories/${repositoryId}/branches`);

    if (!response.ok) {
        throw new Error("Failed to load branches");
    }

    return response.json();
}

export async function mergeBranches(repositoryId, sourceBranch, targetBranch) {
    const response = await fetch(`${API_BASE_URL}/merge`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            repositoryId,
            sourceBranch,
            targetBranch,
        }),
    });

    if (!response.ok) {
        throw new Error("Failed to simulate merge");
    }

    return response.json();
}
