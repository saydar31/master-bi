import React, { useEffect, useRef } from 'react';
import * as d3 from 'd3';
import { generateDummyData } from '../utils/generateDummyData';
import { Card } from 'react-bootstrap';

const Histogram = ({ title }) => {
  const ref = useRef();

  useEffect(() => {
    const data = generateDummyData('HISTOGRAM');
    const svg = d3.select(ref.current)
      .attr('width', 500)
      .attr('height', 300)
      .style('background', '#f0f0f0')
      .style('margin-top', '50')
      .style('overflow', 'visible');

    const xScale = d3.scaleLinear().domain([0, 50]).range([0, 480]);
    const yScale = d3.scaleLinear().domain([0, 100]).range([300, 0]);

    const xAxis = d3.axisBottom(xScale).ticks(5);
    const yAxis = d3.axisLeft(yScale).ticks(5);

    svg.append('g').call(xAxis).attr('transform', 'translate(0,300)');
    svg.append('g').call(yAxis);

    svg.selectAll('.bar')
      .data(data)
      .enter()
      .append('rect')
      .attr('x', (d) => xScale(d.x))
      .attr('y', (d) => yScale(d.y))
      .attr('width', 10)
      .attr('height', (d) => 300 - yScale(d.y))
      .attr('fill', 'orange');
  }, []);

  return (
    <Card>
      <Card.Header>{title}</Card.Header>
      <Card.Body>
        <svg ref={ref}></svg>
      </Card.Body>
    </Card>
  );
};

export default Histogram;