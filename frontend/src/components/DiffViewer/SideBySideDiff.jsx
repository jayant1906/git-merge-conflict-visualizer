function SideBySideDiff({ conflict }) {
    const startLine = conflict.currentStartLine || 1;

    function getLines(content) {
        return content.replace(/\n$/, "").split("\n");
    }

    function renderLines(content, startLine) {
        return getLines(content).map((line, index) => (
            <div className="diff-line" key={`${startLine}-${index}`}>
                <span className="diff-line-number">{startLine + index}</span>
                <code>{line || " "}</code>
            </div>
        ));
    }

    return (
        <div className="side-by-side-diff">
            <div className="diff-column">
                <h4>Current Branch</h4>
                <div className="diff-code">
                    {renderLines(conflict.currentContent || "", startLine)}
                </div>
            </div>

            <div className="diff-column">
                <h4>Incoming Branch</h4>
                <div className="diff-code">
                    {renderLines(conflict.incomingContent || "", startLine)}
                </div>
            </div>
        </div>
    );
}

export default SideBySideDiff;
