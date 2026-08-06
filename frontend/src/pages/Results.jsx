import ConflictList from "../components/ConflictViewer/ConflictList";

function Results({ mergeResult }) {
    if (!mergeResult) {
        return (
            <section className="results-page">
                <h2>Merge Results</h2>
                <p>No merge result available.</p>
            </section>
        );
    }

    const conflicts = mergeResult.conflicts || [];

    return (
        <section className="results-page">
            <h2>Merge Results</h2>

            <p className={mergeResult.hasConflicts ? "conflict-message" : "success-message"}>
                {mergeResult.message}
            </p>

            {mergeResult.hasConflicts && conflicts.length > 0 && (
                <div className="results-conflicts">
                    <h3>Conflict List</h3>
                    <ConflictList conflicts={conflicts} />
                </div>
            )}
        </section>
    );
}

export default Results;
