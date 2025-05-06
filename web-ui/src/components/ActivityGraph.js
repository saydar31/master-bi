import React from "react";

import ActivityCalendarWidget from 'activity-calendar-widget/react';
import { Card } from "react-bootstrap";

const activities = [
  {
    date: '2024-06-23', 
    count: 2,
    level: 1,
  },
  {
    date: '2024-08-02',
    count: 16,
    level: 4,
  },
  {
    date: '2024-11-29',
    count: 11,
    level: 3,
  },
]

const ActivityGraph = ({ metadata, data }) => {


    return(
        <Card>
            <Card.Header>{metadata.name}</Card.Header>
            <Card.Body>
            <ActivityCalendarWidget
  daysToRender={150}
  data={[
    { date: '2023-04-05', activities: [{}, {}, {}, {}] },
    { date: '2023-04-07', activities: [{}] },
    { date: '2023-04-08', activities: [{}, {}, {}] },
  ]}
/>
            </Card.Body>
        </Card>
    )
}

export default ActivityGraph