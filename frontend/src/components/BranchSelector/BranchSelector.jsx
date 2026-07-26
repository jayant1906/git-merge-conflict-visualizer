import { useEffect, useState } from "react";

const API_BASE_URL = "http://localhost:8080/api";

function BranchSelector({ repositoryId }) {
    const [branches, setBranches] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        setBranches([]);
        setError("");

        if (!repositoryId) {
            setIsLoading(false);
            return;
        }

        setIsLoading(true);

        async function loadBranches() {
            try {
                const response = await fetch(
                    `${API_BASE_URL}/repositories/${repositoryId}/branches`
                );

                if (!response.ok) {
                    throw new Error("Failed to load branches");
                }

                const branchData = await response.json();
                setBranches(branchData);
            } catch (loadError) {
                setError(loadError.message);
            } finally {
                setIsLoading(false);
            }
        }

        loadBranches();
    }, [repositoryId]);

    if (isLoading) {
        return <p className="branch-selector-status">Loading branches...</p>;
    }

    if (error) {
        return <p className="error-message">{error}</p>;
    }

    if (branches.length === 0) {
        return <p className="branch-selector-status">No branches found.</p>;
    }

    return (
        <div className="branch-selector">
            <h2>Branches</h2>
            <ul>
                {branches.map((branch) => (
                    <li key={branch.name}>
                        {branch.current ? `${branch.name} (current)` : branch.name}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default BranchSelector;
