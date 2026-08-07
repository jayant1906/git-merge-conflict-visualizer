import { useState } from "react";
import { uploadRepository } from "../services/api";
import BranchSelector from "./BranchSelector/BranchSelector";

function UploadBox() {
    const [selectedFile, setSelectedFile] = useState(null);
    const [repositoryInfo, setRepositoryInfo] = useState(null);
    const [error, setError] = useState("");
    const [isUploading, setIsUploading] = useState(false);

    function handleFileChange(event) {
        const file = event.target.files[0];
        setRepositoryInfo(null);
        setError("");
        setSelectedFile(file || null);
    }

    async function handleUpload(event) {
        event.preventDefault();

        if (!selectedFile) {
            setError("Please choose a ZIP file first.");
            return;
        }

        setIsUploading(true);
        setError("");
        setRepositoryInfo(null);

        try {
            const result = await uploadRepository(selectedFile);
            setRepositoryInfo(result);
        } catch (uploadError) {
            setError(uploadError.message);
        } finally {
            setIsUploading(false);
        }
    }

    return (
        <section className="upload-box">
            <form onSubmit={handleUpload}>
                <label htmlFor="repository-upload">Repository ZIP</label>
                <input
                    id="repository-upload"
                    type="file"
                    accept=".zip,application/zip"
                    onChange={handleFileChange}
                />
                <button type="submit" disabled={isUploading}>
                    {isUploading && <span className="spinner" aria-hidden="true"></span>}
                    {isUploading ? "Uploading repository..." : "Upload"}
                </button>
            </form>

            {selectedFile && <p className="file-name">{selectedFile.name}</p>}
            {error && <p className="error-message">{error}</p>}

            {repositoryInfo && (
                <div className="repository-result">
                    <h2>{repositoryInfo.repoName}</h2>
                    <p>{repositoryInfo.message}</p>
                    <p>ID: {repositoryInfo.uId}</p>
                    <BranchSelector repositoryId={repositoryInfo.uId} />
                </div>
            )}
        </section>
    );
}

export default UploadBox;
