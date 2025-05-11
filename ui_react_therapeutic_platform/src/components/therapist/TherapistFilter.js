import React, { useState } from 'react';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import Button from '@mui/material/Button';

const TherapistFilter = ({ onFilterChange }) => {
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
    onFilterChange(filters);
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ mb: 3, p: 2, bgcolor: '#FFFDE7', borderRadius: 2, boxShadow: 1 }}>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6} md={3}>
          <FormControl fullWidth>
            <InputLabel>Specialization</InputLabel>
            <Select
              name="specialization"
              value={filters.specialization}
              label="Specialization"
              onChange={handleChange}
            >
              <MenuItem value="">All Specializations</MenuItem>
              {specializations.map(spec => (
                <MenuItem key={spec} value={spec}>{spec}</MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <TextField
            fullWidth
            label="Location"
            name="location"
            value={filters.location}
            onChange={handleChange}
            placeholder="Enter location"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <TextField
            fullWidth
            label="Languages"
            name="languages"
            value={filters.languages}
            onChange={handleChange}
            placeholder="Enter languages (comma separated)"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <FormControlLabel
            control={<Checkbox name="virtualAvailable" checked={filters.virtualAvailable} onChange={handleChange} />}
            label="Virtual Sessions"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <FormControl fullWidth>
            <InputLabel>Minimum Rating</InputLabel>
            <Select
              name="minRating"
              value={filters.minRating}
              label="Minimum Rating"
              onChange={handleChange}
            >
              <MenuItem value={0}>Any Rating</MenuItem>
              <MenuItem value={4}>4+ Stars</MenuItem>
              <MenuItem value={4.5}>4.5+ Stars</MenuItem>
              <MenuItem value={5}>5 Stars</MenuItem>
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <TextField
            fullWidth
            label="Maximum Cost (per session)"
            name="maxCost"
            type="number"
            value={filters.maxCost}
            onChange={handleChange}
            placeholder="Enter maximum cost"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <FormControl fullWidth>
            <InputLabel>Available Time</InputLabel>
            <Select
              name="availableTime"
              value={filters.availableTime}
              label="Available Time"
              onChange={handleChange}
            >
              <MenuItem value="">Any Time</MenuItem>
              <MenuItem value="morning">Morning (8AM-12PM)</MenuItem>
              <MenuItem value="afternoon">Afternoon (12PM-5PM)</MenuItem>
              <MenuItem value="evening">Evening (5PM-9PM)</MenuItem>
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12} sm={6} md={3} sx={{ display: 'flex', alignItems: 'center' }}>
          <Button type="submit" variant="contained" sx={{ bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600, '&:hover': { bgcolor: '#FFECB3' } }} fullWidth>
            Apply Filters
          </Button>
        </Grid>
      </Grid>
    </Box>
  );
};

export default TherapistFilter; 