import React from "react";
import { Card } from "react-bootstrap";
import { TagCloud } from "react-tagcloud";

const TagCloudWiget = ({metadata, data}) => {
    const queryIds = metadata.queries.map(q => q.id);
    console.log(data)
    const tags = queryIds.flatMap(e => data[e].data || [])
        .map(e => ({value: e.key, count: e.value}));
    
    return (
        <Card>
            <Card.Header>
                {metadata.name}
            </Card.Header>
            <Card.Body>
                <TagCloud
                minSize={metadata.minFontSize}
                maxSize={metadata.maxFontSize}
                tags={tags}
                />
            </Card.Body>
        </Card>
    );
}

export default TagCloudWiget;