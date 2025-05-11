import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Badge } from 'react-bootstrap';
import TherapistFilter from './TherapistFilter';
import TherapistSort from './TherapistSort';
import TherapistDetail from './TherapistDetail';
import './TherapistList.css';
import { auth, db } from '../../firebaseConfig';
import { collection, getDocs, query, where } from 'firebase/firestore';
import axios from 'axios';

const TherapistList = () => {
  const [therapists, setTherapists] = useState([]);
  const [filteredTherapists, setFilteredTherapists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    specialization: '',
    location: '',
    languages: [],
    virtualAvailable: false,
    minRating: 0,
    maxCost: null,
    availableTime: ''
  });
  const [sortConfig, setSortConfig] = useState({ field: 'name', order: 'asc' });
  const [selectedTherapistId, setSelectedTherapistId] = useState(null);

  useEffect(() => {
    const fetchTherapists = async () => {
      try {
        setLoading(true);
        const response = await axios.get('http://localhost:8080/api/therapists');
        setTherapists(response.data);
        setFilteredTherapists(sortTherapists(response.data, sortConfig));
        setLoading(false);
      } catch (err) {
        setError('An error occurred while loading therapist information. Please try again later.');
        setLoading(false);
        console.error('Therapist loading error:', err);
      }
    };

    fetchTherapists();
  }, []);

  const sortTherapists = (therapistList, { field, order }) => {
    return [...therapistList].sort((a, b) => {
      let comparison = 0;

      switch (field) {
        case 'name':
          comparison = a.user.fullName.localeCompare(b.user.fullName);
          break;
//        case 'experience':
//          comparison = a.yearsOfExperience - b.yearsOfExperience;
//          break;
        case 'rating':
          comparison = a.rating - b.rating;
          break;
        case 'price':
          comparison = a.sessionCost - b.sessionCost;
          break;
        default:
          comparison = 0;
      }

      return order === 'asc' ? comparison : -comparison;
    });
  };

  const handleFilterChange = async (newFilters) => {
    setFilters(newFilters);
    
    try {
      setLoading(true);
      
      // Show all therapists if filters are empty
      if (!newFilters.specialization && !newFilters.location && newFilters.languages.length === 0 &&
          !newFilters.virtualAvailable && newFilters.minRating === 0 && !newFilters.maxCost && !newFilters.availableTime) {
        setFilteredTherapists(sortTherapists(therapists, sortConfig));
        setLoading(false);
        return;
      }
      
      // Create filter parameters
      const params = {};
      if (newFilters.specialization) params.specialization = newFilters.specialization;
      if (newFilters.location) params.location = newFilters.location;
      if (newFilters.languages.length > 0) params.languages = newFilters.languages;
      if (newFilters.virtualAvailable) params.virtualAvailable = newFilters.virtualAvailable;
      if (newFilters.minRating > 0) params.minRating = newFilters.minRating;
      if (newFilters.maxCost) params.maxCost = newFilters.maxCost;
      if (newFilters.availableTime) params.availableTime = newFilters.availableTime;
      
      const response = await axios.get('http://localhost:8080/api/therapists/search', { params });
      setFilteredTherapists(sortTherapists(response.data, sortConfig));
      setLoading(false);
    } catch (err) {
      setError('An error occurred while filtering therapists. Please try again later.');
      setLoading(false);
      console.error('Filtering error:', err);
    }
  };

  const handleSortChange = (newSortConfig) => {
    setSortConfig(newSortConfig);
    setFilteredTherapists(sortTherapists(filteredTherapists, newSortConfig));
  };

  const handleViewProfile = (therapistId) => {
    setSelectedTherapistId(therapistId);
  };

  return (
    <div className="therapist-list-container">
      <h1>Therapist Profiles</h1>
      <p className="intro-text">
        You can browse therapist profiles without registering. Registration will be required to book appointments.
      </p>
      
      <TherapistFilter onFilterChange={handleFilterChange} />
      <TherapistSort onSortChange={handleSortChange} />
      
      <div className="therapist-count">
        {filteredTherapists.length} therapists found
      </div>
      
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
                  {/* {therapist.availableTimes.map(time => (
                    <Badge key={time} bg="secondary" className="me-2">
                      {time.charAt(0).toUpperCase() + time.slice(1)}
                    </Badge>
                  ))} */}
                </div>

                <button className="btn btn-primary mt-3" onClick={() => handleViewProfile(therapist.id)}>
                  View Profile
                </button>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
      
      {selectedTherapistId && (
        <TherapistDetail 
          therapistId={selectedTherapistId} 
          onBack={() => setSelectedTherapistId(null)} 
        />
      )}
    </div>
  );
};

export default TherapistList; 