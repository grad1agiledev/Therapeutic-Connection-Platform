import React, { useState, useEffect } from 'react';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import CircularProgress from '@mui/material/CircularProgress';
import Alert from '@mui/material/Alert';
import TherapistFilter from './TherapistFilter';
import TherapistSort from './TherapistSort';
import TherapistDetail from './TherapistDetail';
import TherapistCard from './TherapistCard';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import { auth, db } from '../../firebaseConfig';
import { collection, getDocs, query, where } from 'firebase/firestore';
import axios from 'axios';
import config from '../../config';

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
        const response = await axios.get(`${config.API_URL}/api/therapists`);
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
      if (!newFilters.specialization && !newFilters.location && newFilters.languages.length === 0 &&
          !newFilters.virtualAvailable && newFilters.minRating === 0 && !newFilters.maxCost && !newFilters.availableTime) {
        setFilteredTherapists(sortTherapists(therapists, sortConfig));
        setLoading(false);
        return;
      }
      const params = {};
      if (newFilters.specialization) params.specialization = newFilters.specialization;
      if (newFilters.location) params.location = newFilters.location;
      if (newFilters.languages.length > 0) params.languages = newFilters.languages;
      if (newFilters.virtualAvailable) params.virtualAvailable = newFilters.virtualAvailable;
      if (newFilters.minRating > 0) params.minRating = newFilters.minRating;
      if (newFilters.maxCost) params.maxCost = newFilters.maxCost;
      if (newFilters.availableTime) params.availableTime = newFilters.availableTime;
      const response = await axios.get(`${config.API_URL}/api/therapists/search`, { params });
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
    <Box sx={{ bgcolor: '#FFF8E1', borderRadius: 3, p: 3, boxShadow: 2 }}>
      <Typography variant="h3" color="primary" gutterBottom fontWeight={700}>
        Therapist Profiles
      </Typography>
      <Typography variant="body1" color="text.secondary" mb={2}>
        You can browse therapist profiles without registering. Registration will be required to book appointments.
      </Typography>
      <TherapistFilter onFilterChange={handleFilterChange} />
      <TherapistSort onSortChange={handleSortChange} />
      <Typography variant="subtitle2" color="text.secondary" sx={{ mt: 2, mb: 2 }}>
        {filteredTherapists.length} therapists found
      </Typography>
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
          <CircularProgress color="primary" />
        </Box>
      ) : error ? (
        <Alert severity="error">{error}</Alert>
      ) : (
        <Grid container spacing={3}>
          {filteredTherapists.map(therapist => (
            <Grid item xs={12} sm={6} md={4} key={therapist.id}>
              <TherapistCard therapist={therapist} onViewProfile={handleViewProfile} />
            </Grid>
          ))}
        </Grid>
      )}
      {selectedTherapistId && (
        <TherapistDetail 
          therapistId={selectedTherapistId} 
          onBack={() => setSelectedTherapistId(null)} 
        />
      )}
    </Box>
  );
};

export default TherapistList; 