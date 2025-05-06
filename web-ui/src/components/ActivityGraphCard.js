import React from 'react';
import { Card } from 'react-bootstrap';
import { HeatMapGrid } from "react-grid-heatmap";

const ActivityGraphCard = ({metadata, data}) => {
    const qid = metadata.queries[0].id
    const {numbers, weeksCount} = data[qid].transformedData

    const xLabels = new Array(weeksCount).fill(0).map((_, i) => `${i}`);
    const yLabels = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    return (
        <Card>
            <Card.Header>{metadata.name}</Card.Header>
            <Card.Body>
                <HeatMapGrid
                    data={numbers}
                    xLabels={xLabels}
                    yLabels={yLabels}
                    // Reder cell with tooltip
                    cellRender={(x, y, value) => (
                      <div title={`Pos(${x}, ${y}) = ${value}`}>{value}</div>
                    )}
                    xLabelsStyle={index => ({
                        color: index % 2 ? "transparent" : "#777",
                        fontSize: ".65rem"
                    })}
                    yLabelsStyle={() => ({
                      fontSize: ".65rem",
                      textTransform: "uppercase",
                      color: "#777"
                    })}
                    cellStyle={(_x, _y, ratio) => ({
                      background: `rgb(12, 160, 44, ${ratio})`,
                      fontSize: ".7rem",
                      color: `rgb(0, 0, 0, ${ratio / 2 + 0.4})`
                    })}
                    cellHeight="1.5rem"
                    xLabelsPos="bottom"
                    onClick={(x, y) => alert(`Clicked (${x}, ${y})`)}
                    // yLabelsPos="right"
                    // square
                />
            </Card.Body>
            <Card.Footer>WIP</Card.Footer>
        </Card>
    )
}

export default ActivityGraphCard