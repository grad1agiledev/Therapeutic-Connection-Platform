import React, { useState, useEffect } from 'react';
import axios from 'axios';
import TherapistCard from './TherapistCard';
import TherapistFilter from './TherapistFilter';
import TherapistDetail from './TherapistDetail';
import './TherapistList.css';

const TherapistList = () => {
  const [therapists, setTherapists] = useState([]);
  const [filteredTherapists, setFilteredTherapists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({
    specialization: '',
    location: '',
    languages: [],
    sortBy: 'name',
    sortOrder: 'asc'
  });
  const [selectedTherapistId, setSelectedTherapistId] = useState(null);

  useEffect(() => {
    const fetchTherapists = async () => {
      try {
        setLoading(true);
        const response = await axios.get('http://localhost:8080/api/therapists');
        setTherapists(response.data);
        setFilteredTherapists(response.data);
        setLoading(false);
      } catch (err) {
        setError('An error occurred while loading therapist information. Please try again later.');
        setLoading(false);
        console.error('Therapist loading error:', err);
      }
    };

    fetchTherapists();
  }, []);

  const handleFilterChange = async (newFilters) => {
    setFilters(newFilters);
    
    try {
      setLoading(true);
      
      // Show all therapists if filters are empty
      if (!newFilters.specialization && !newFilters.location && newFilters.languages.length === 0) {
        // Even if no filters, we still want to sort
        const params = {
          sortBy: newFilters.sortBy,
          sortOrder: newFilters.sortOrder
        };
        
        const response = await axios.get('http://localhost:8080/api/therapists/search-and-sort', { params });
        setFilteredTherapists(response.data);
        setLoading(false);
        return;
      }
      
      // Create filter parameters
      const params = {};
      if (newFilters.specialization) params.specialization = newFilters.specialization;
      if (newFilters.location) params.location = newFilters.location;
      if (newFilters.languages.length > 0) params.languages = newFilters.languages;
      
      // Add sort parameters
      params.sortBy = newFilters.sortBy;
      params.sortOrder = newFilters.sortOrder;
      
      const response = await axios.get('http://localhost:8080/api/therapists/search-and-sort', { params });
      setFilteredTherapists(response.data);
      setLoading(false);
    } catch (err) {
      setError('An error occurred while filtering therapists. Please try again later.');
      setLoading(false);
      console.error('Filtering error:', err);
    }
  };

  const handleViewProfile = (therapistId) => {
    setSelectedTherapistId(therapistId);
  };

  const handleBackToList = () => {
    setSelectedTherapistId(null);
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  if (selectedTherapistId) {
    return <TherapistDetail therapistId={selectedTherapistId} onBack={handleBackToList} />;
  }

  return (
    <div className="therapist-list-container">
      <h1>Therapist Profiles</h1>
      <p className="intro-text">
        You can browse therapist profiles without registering. Registration will be required to book appointments.
      </p>
      
      <TherapistFilter onFilterChange={handleFilterChange} />
      
      <div className="therapist-count">
        {filteredTherapists.length} therapists found
      </div>
      
      <div className="therapist-grid">
        {filteredTherapists.length > 0 ? (
          filteredTherapists.map(therapist => (
            <TherapistCard 
              key={therapist.id} 
              therapist={therapist} 
              onViewProfile={handleViewProfile}
            />
          ))
        ) : (
          <div className="no-results">
            No therapists found matching your search criteria. Please adjust your filters.
          </div>
        )}
      </div>
    </div>
  );
};

export default TherapistList; 