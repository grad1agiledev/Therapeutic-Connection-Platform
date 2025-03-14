import React, { useState } from 'react';
import './TherapistFilter.css';

const TherapistFilter = ({ onFilterChange }) => {
  const [specialization, setSpecialization] = useState('');
  const [location, setLocation] = useState('');
  const [languages, setLanguages] = useState([]);
  const [isExpanded, setIsExpanded] = useState(false);

  // Language options
  const languageOptions = [
    { value: 'Turkish', label: 'Turkish' },
    { value: 'English', label: 'English' },
    { value: 'German', label: 'German' },
    { value: 'French', label: 'French' }
  ];

  // Location options
  const locationOptions = [
    { value: '', label: 'All Locations' },
    { value: 'Istanbul', label: 'Istanbul' },
    { value: 'Ankara', label: 'Ankara' },
    { value: 'Izmir', label: 'Izmir' },
    { value: 'Bursa', label: 'Bursa' },
    { value: 'Antalya', label: 'Antalya' }
  ];

  // Specialization options
  const specializationOptions = [
    { value: '', label: 'All Specializations' },
    { value: 'Anxiety', label: 'Anxiety' },
    { value: 'Depression', label: 'Depression' },
    { value: 'Family Therapy', label: 'Family Therapy' },
    { value: 'Couples Therapy', label: 'Couples Therapy' },
    { value: 'Trauma', label: 'Trauma' },
    { value: 'EMDR', label: 'EMDR' },
    { value: 'Child and Adolescent Psychology', label: 'Child and Adolescent Psychology' },
    { value: 'Cognitive Behavioral Therapy', label: 'Cognitive Behavioral Therapy' }
  ];

  const handleLanguageChange = (e) => {
    const value = e.target.value;
    setLanguages(
      languages.includes(value)
        ? languages.filter(lang => lang !== value)
        : [...languages, value]
    );
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilterChange({
      specialization,
      location,
      languages
    });
  };

  const handleReset = () => {
    setSpecialization('');
    setLocation('');
    setLanguages([]);
    onFilterChange({
      specialization: '',
      location: '',
      languages: []
    });
  };

  const toggleExpand = () => {
    setIsExpanded(!isExpanded);
  };

  return (
    <div className={`filter-container ${isExpanded ? 'expanded' : ''}`}>
      <div className="filter-header" onClick={toggleExpand}>
        <h2>Filter Therapists</h2>
        <span className="expand-icon">{isExpanded ? '▲' : '▼'}</span>
      </div>
      
      {isExpanded && (
        <form onSubmit={handleSubmit} className="filter-form">
          <div className="filter-section">
            <label htmlFor="specialization">Specialization:</label>
            <select
              id="specialization"
              value={specialization}
              onChange={(e) => setSpecialization(e.target.value)}
            >
              {specializationOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <div className="filter-section">
            <label htmlFor="location">Location:</label>
            <select
              id="location"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
            >
              {locationOptions.map(option => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </div>

          <div className="filter-section">
            <label>Languages:</label>
            <div className="language-options">
              {languageOptions.map(option => (
                <div key={option.value} className="language-option">
                  <input
                    type="checkbox"
                    id={`lang-${option.value}`}
                    value={option.value}
                    checked={languages.includes(option.value)}
                    onChange={handleLanguageChange}
                  />
                  <label htmlFor={`lang-${option.value}`}>{option.label}</label>
                </div>
              ))}
            </div>
          </div>

          <div className="filter-actions">
            <button type="submit" className="filter-button apply">Apply</button>
            <button type="button" className="filter-button reset" onClick={handleReset}>Reset</button>
          </div>
        </form>
      )}
    </div>
  );
};

export default TherapistFilter; 