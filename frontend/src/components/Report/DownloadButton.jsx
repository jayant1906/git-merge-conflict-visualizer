import { useState } from "react";

const API_BASE_URL = "http://localhost:8080/api";

function DownloadButton({ repositoryId, sourceBranch, targetBranch, mergeResult }) {
    const [isDownloading, setIsDownloading] = useState(false);
    const [error, setError] = useState("");

    async function handleDownload() {
        if (!repositoryId || !sourceBranch || !targetBranch || !mergeResult) {
            setError("Report data is missing.");
            return;
        }

        setIsDownloading(true);
        setError("");

        try {
            const response = await fetch(`${API_BASE_URL}/report`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    repositoryId,
                    sourceBranch,
                    targetBranch,
                    mergeResult,
                }),
            });

            if (!response.ok) {
                throw new Error("Failed to download report");
            }

            const reportBlob = await response.blob();
            const reportUrl = URL.createObjectURL(reportBlob);
            const downloadLink = document.createElement("a");

            downloadLink.href = reportUrl;
            downloadLink.download = "report.html";
            document.body.appendChild(downloadLink);
            downloadLink.click();
            downloadLink.remove();
            URL.revokeObjectURL(reportUrl);
        } catch (downloadError) {
            setError(downloadError.message);
        } finally {
            setIsDownloading(false);
        }
    }

    return (
        <div className="download-report">
            <button type="button" onClick={handleDownload} disabled={isDownloading}>
                {isDownloading ? "Downloading..." : "Download Report"}
            </button>

            {error && <p className="error-message">{error}</p>}
        </div>
    );
}

export default DownloadButton;
