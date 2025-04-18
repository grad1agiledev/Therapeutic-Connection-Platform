import React, { useState } from 'react';
import { Form, Row, Col, Button } from 'react-bootstrap';
import './TherapistFilter.css';

const TherapistFilter = ({ onFilter }) => {
  const [filters, setFilters] = useState({
    specialization: '',
    location: '',
    virtualAvailable: false,
    languages: '',
    minRating: 0,
    maxCost: '',
    availableTime: ''
  });

  const specializations = [
    'Anxiety', 'Depression', 'Family Therapy', 'Couples Therapy',
    'Trauma', 'Addiction', 'Child Therapy', 'Stress Management'
  ];

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFilters(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilter(filters);
  };

  return (
    <Form onSubmit={handleSubmit} className="therapist-filter">
      <Row>
        <Col md={4}>
          <Form.Group>
            <Form.Label>Specialization</Form.Label>
            <Form.Select
              name="specialization"
              value={filters.specialization}
              onChange={handleChange}
            >
              <option value="">All Specializations</option>
              {specializations.map(spec => (
                <option key={spec} value={spec}>{spec}</option>
              ))}
            </Form.Select>
          </Form.Group>
        </Col>

        <Col md={4}>
          <Form.Group>
            <Form.Label>Location</Form.Label>
            <Form.Control
              type="text"
              name="location"
              value={filters.location}
              onChange={handleChange}
              placeholder="Enter location"
            />
          </Form.Group>
        </Col>

        <Col md={4}>
          <Form.Group>
            <Form.Label>Virtual Sessions</Form.Label>
            <Form.Check
              type="checkbox"
              name="virtualAvailable"
              checked={filters.virtualAvailable}
              onChange={handleChange}
              label="Available for virtual sessions"
            />
          </Form.Group>
        </Col>
      </Row>

      <Row className="mt-3">
        <Col md={4}>
          <Form.Group>
            <Form.Label>Languages</Form.Label>
            <Form.Control
              type="text"
              name="languages"
              value={filters.languages}
              onChange={handleChange}
              placeholder="Enter languages (comma separated)"
            />
          </Form.Group>
        </Col>

        <Col md={4}>
          <Form.Group>
            <Form.Label>Minimum Rating</Form.Label>
            <Form.Select
              name="minRating"
              value={filters.minRating}
              onChange={handleChange}
            >
              <option value="0">Any Rating</option>
              <option value="4">4+ Stars</option>
              <option value="4.5">4.5+ Stars</option>
              <option value="5">5 Stars</option>
            </Form.Select>
          </Form.Group>
        </Col>

        <Col md={4}>
          <Form.Group>
            <Form.Label>Maximum Cost (per session)</Form.Label>
            <Form.Control
              type="number"
              name="maxCost"
              value={filters.maxCost}
              onChange={handleChange}
              placeholder="Enter maximum cost"
            />
          </Form.Group>
        </Col>
      </Row>

      <Row className="mt-3">
        <Col md={4}>
          <Form.Group>
            <Form.Label>Available Time</Form.Label>
            <Form.Select
              name="availableTime"
              value={filters.availableTime}
              onChange={handleChange}
            >
              <option value="">Any Time</option>
              <option value="morning">Morning (8AM-12PM)</option>
              <option value="afternoon">Afternoon (12PM-5PM)</option>
              <option value="evening">Evening (5PM-9PM)</option>
            </Form.Select>
          </Form.Group>
        </Col>
      </Row>

      <Row className="mt-3">
        <Col>
          <Button variant="primary" type="submit">
            Apply Filters
          </Button>
        </Col>
      </Row>
    </Form>
  );
};

export default TherapistFilter; 