import { useState } from "react";
import SideBySideDiff from "../DiffViewer/SideBySideDiff";

function ConflictCard({ conflict, conflictNumber }) {
    const [isOpen, setIsOpen] = useState(false);

    return (
        <article className="conflict-card">
            <div className="conflict-card-header">
                <div>
                    <p className="conflict-file-name">File: {conflict.filePath}</p>
                    <p className="conflict-number">Conflict {conflictNumber}</p>
                </div>

                <button type="button" onClick={() => setIsOpen(!isOpen)}>
                    {isOpen ? "Hide Conflict" : "View Conflict"}
                </button>
            </div>

            {isOpen && <SideBySideDiff conflict={conflict} />}
        </article>
    );
}

export default ConflictCard;
