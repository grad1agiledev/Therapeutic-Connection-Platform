import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Badge } from 'react-bootstrap';
import TherapistFilter from './TherapistFilter';
import './TherapistList.css';

const TherapistList = () => {
  const [therapists, setTherapists] = useState([]);
  const [filteredTherapists, setFilteredTherapists] = useState([]);

  useEffect(() => {
    // TODO: Replace with actual API call
    const mockTherapists = [
      {
        id: 1,
        name: 'Dr. Sarah Johnson',
        specialization: 'Anxiety',
        location: 'New York',
        virtualAvailable: true,
        languages: ['English', 'Spanish'],
        rating: 4.8,
        cost: 150,
        availableTimes: ['morning', 'afternoon']
      },
      // Add more mock therapists as needed
    ];
    setTherapists(mockTherapists);
    setFilteredTherapists(mockTherapists);
  }, []);

  const handleFilter = (filters) => {
    let filtered = [...therapists];

    if (filters.specialization) {
      filtered = filtered.filter(t => t.specialization === filters.specialization);
    }

    if (filters.location) {
      filtered = filtered.filter(t => 
        t.location.toLowerCase().includes(filters.location.toLowerCase())
      );
    }

    if (filters.virtualAvailable) {
      filtered = filtered.filter(t => t.virtualAvailable);
    }

    if (filters.languages) {
      const languages = filters.languages.split(',').map(lang => lang.trim());
      filtered = filtered.filter(t => 
        languages.some(lang => t.languages.includes(lang))
      );
    }

    if (filters.minRating > 0) {
      filtered = filtered.filter(t => t.rating >= filters.minRating);
    }

    if (filters.maxCost) {
      filtered = filtered.filter(t => t.cost <= filters.maxCost);
    }

    if (filters.availableTime) {
      filtered = filtered.filter(t => 
        t.availableTimes.includes(filters.availableTime)
      );
    }

    setFilteredTherapists(filtered);
  };

  return (
    <div className="therapist-list-container">
      <TherapistFilter onFilter={handleFilter} />
      
      <Row>
        {filteredTherapists.map(therapist => (
          <Col key={therapist.id} md={4} className="mb-4">
            <Card className="therapist-card">
              <Card.Body>
                <Card.Title>{therapist.name}</Card.Title>
                <Card.Subtitle className="mb-2 text-muted">
                  {therapist.specialization}
                </Card.Subtitle>
                
                <div className="therapist-info">
                  <p><strong>Location:</strong> {therapist.location}</p>
                  <p><strong>Languages:</strong> {therapist.languages.join(', ')}</p>
                  <p><strong>Rating:</strong> {therapist.rating} ⭐</p>
                  <p><strong>Cost:</strong> ${therapist.cost}/session</p>
                </div>

                <div className="therapist-availability">
                  {therapist.virtualAvailable && (
                    <Badge bg="info" className="me-2">Virtual Sessions</Badge>
                  )}
                  {therapist.availableTimes.map(time => (
                    <Badge key={time} bg="secondary" className="me-2">
                      {time.charAt(0).toUpperCase() + time.slice(1)}
                    </Badge>
                  ))}
                </div>

                <button className="btn btn-primary mt-3">
                  Book Session
                </button>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default TherapistList; 