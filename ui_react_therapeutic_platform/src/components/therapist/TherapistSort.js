import React from 'react';
import './TherapistSort.css';

const TherapistSort = ({ onSortChange }) => {
  const handleSortChange = (event) => {
    const [field, order] = event.target.value.split('-');
    onSortChange({ field, order });
  };

  return (
    <div className="therapist-sort">
      <label htmlFor="sort-select">Sort by: </label>
      <select id="sort-select" onChange={handleSortChange} defaultValue="name-asc">
        <option value="name-asc">Name (A-Z)</option>
        <option value="name-desc">Name (Z-A)</option>
        <option value="experience-asc">Experience (Low to High)</option>
        <option value="experience-desc">Experience (High to Low)</option>
        <option value="rating-asc">Rating (Low to High)</option>
        <option value="rating-desc">Rating (High to Low)</option>
        <option value="price-asc">Price (Low to High)</option>
        <option value="price-desc">Price (High to Low)</option>
      </select>
    </div>
  );
};

export default TherapistSort; 