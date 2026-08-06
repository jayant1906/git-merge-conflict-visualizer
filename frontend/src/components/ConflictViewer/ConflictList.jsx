import ConflictCard from "./ConflictCard";

function ConflictList({ conflicts = [] }) {
    if (conflicts.length === 0) {
        return <p className="conflict-list-empty">No conflicts found.</p>;
    }

    return (
        <div className="conflict-list">
            {conflicts.map((conflict, index) => (
                <ConflictCard
                    key={`${conflict.filePath}-${index}`}
                    conflict={conflict}
                    conflictNumber={index + 1}
                />
            ))}
        </div>
    );
}

export default ConflictList;
