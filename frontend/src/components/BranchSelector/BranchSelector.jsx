import { useEffect, useState } from "react";
import { getBranches, mergeBranches } from "../../services/api";
import ConflictList from "../ConflictViewer/ConflictList";
import DownloadButton from "../Report/DownloadButton";

function BranchSelector({ repositoryId }) {
    const [branches, setBranches] = useState([]);
    const [sourceBranch, setSourceBranch] = useState("");
    const [targetBranch, setTargetBranch] = useState("");
    const [mergeResult, setMergeResult] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isMerging, setIsMerging] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        setBranches([]);
        setSourceBranch("");
        setTargetBranch("");
        setMergeResult(null);
        setError("");

        if (!repositoryId) {
            setIsLoading(false);
            return;
        }

        setIsLoading(true);

        async function loadBranches() {
            try {
                const branchData = await getBranches(repositoryId);
                setBranches(branchData);
                setTargetBranch(branchData.find((branch) => branch.current)?.name || branchData[0]?.name || "");
                setSourceBranch(branchData.find((branch) => !branch.current)?.name || branchData[1]?.name || "");
            } catch (loadError) {
                setError(loadError.message);
            } finally {
                setIsLoading(false);
            }
        }

        loadBranches();
    }, [repositoryId]);

    async function handleMerge(event) {
        event.preventDefault();

        if (!sourceBranch || !targetBranch) {
            setError("Choose both branches before merging.");
            return;
        }

        if (sourceBranch === targetBranch) {
            setError("Choose two different branches.");
            return;
        }

        setIsMerging(true);
        setMergeResult(null);
        setError("");

        try {
            const result = await mergeBranches(repositoryId, sourceBranch, targetBranch);
            setMergeResult(result);
        } catch (mergeError) {
            setError(mergeError.message);
        } finally {
            setIsMerging(false);
        }
    }

    if (isLoading) {
        return (
            <p className="branch-selector-status">
                <span className="spinner spinner-inline" aria-hidden="true"></span>
                Reading branches...
            </p>
        );
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
            <form className="merge-form" onSubmit={handleMerge}>
                <label htmlFor="target-branch">Target branch</label>
                <select
                    id="target-branch"
                    value={targetBranch}
                    onChange={(event) => setTargetBranch(event.target.value)}
                >
                    {branches.map((branch) => (
                        <option key={branch.name} value={branch.name}>
                            {branch.current ? `${branch.name} (current)` : branch.name}
                        </option>
                    ))}
                </select>

                <label htmlFor="source-branch">Source branch</label>
                <select
                    id="source-branch"
                    value={sourceBranch}
                    onChange={(event) => setSourceBranch(event.target.value)}
                >
                    <option value="">Choose a branch</option>
                    {branches.map((branch) => (
                        <option key={branch.name} value={branch.name}>
                            {branch.current ? `${branch.name} (current)` : branch.name}
                        </option>
                    ))}
                </select>

                <button type="submit" disabled={isMerging}>
                    {isMerging && <span className="spinner" aria-hidden="true"></span>}
                    {isMerging ? "Simulating merge..." : "Merge"}
                </button>
            </form>

            {mergeResult && (
                <div className="merge-result">
                    <p className={mergeResult.hasConflicts ? "conflict-message" : "success-message"}>
                        {mergeResult.hasConflicts ? "Conflict Found" : mergeResult.message}
                    </p>
                    {mergeResult.hasConflicts && <ConflictList conflicts={mergeResult.conflicts} />}
                    <DownloadButton
                        repositoryId={repositoryId}
                        sourceBranch={sourceBranch}
                        targetBranch={targetBranch}
                        mergeResult={mergeResult}
                    />
                </div>
            )}
        </div>
    );
}

export default BranchSelector;
