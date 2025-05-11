import React, { useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import VerifiedIcon from '@mui/icons-material/Verified';
import StarIcon from '@mui/icons-material/Star';

const TherapistCard = ({ therapist, onViewProfile }) => {
  const [showDetails, setShowDetails] = useState(false);
  const rating = Number(therapist.rating ?? 0);
  const sessionCost = Number(therapist.sessionCost ?? 0);
  const toggleDetails = () => setShowDetails(!showDetails);
  const handleViewProfile = () => onViewProfile && onViewProfile(therapist.id);

  return (
    <Card sx={{ borderRadius: 3, boxShadow: 3, bgcolor: '#FFFDE7', p: 2 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Avatar
          src={therapist.profilePicture}
          alt={therapist.user.fullName}
          sx={{ width: 64, height: 64, mr: 2, bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 700 }}
        >
          {therapist.user.fullName?.[0]}
        </Avatar>
        <Box>
          <Typography variant="h6" fontWeight={700} color="#5D4037">
            {therapist.user.fullName}
          </Typography>
          <Typography variant="subtitle2" color="text.secondary">
            {therapist.specialization}
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', mt: 0.5 }}>
            <StarIcon sx={{ color: '#FFD54F', fontSize: 20, mr: 0.5 }} />
            <Typography variant="body2" color="text.secondary">
              {rating.toFixed(1)}
            </Typography>
            {therapist.verified && (
              <Chip icon={<VerifiedIcon sx={{ color: '#388e3c' }} />} label="Verified" size="small" sx={{ ml: 1, bgcolor: '#E8F5E9', color: '#388e3c' }} />
            )}
          </Box>
        </Box>
      </Box>
      <CardContent sx={{ pt: 0 }}>
        <Typography variant="body2" color="text.secondary">
          <strong>Location:</strong> {therapist.location?.name && therapist.location?.country ? `${therapist.location.name}, ${therapist.location.country}` : 'Not specified'}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          <strong>Languages:</strong> {Array.isArray(therapist.languages) && therapist.languages.length ? therapist.languages.map(l => l.langName).join(', ') : 'Not specified'}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          <strong>Session Cost:</strong> ${sessionCost.toFixed(2)}
        </Typography>
        {showDetails && (
          <Box sx={{ mt: 2 }}>
            <Typography variant="subtitle2" color="#5D4037" fontWeight={600}>About</Typography>
            <Typography variant="body2" color="text.secondary">{therapist.bio}</Typography>
          </Box>
        )}
      </CardContent>
      <CardActions sx={{ justifyContent: 'space-between' }}>
        <Button size="small" onClick={toggleDetails} sx={{ color: '#8D6E63' }}>
          {showDetails ? 'Show Less' : 'Show More'}
        </Button>
        <Button variant="contained" size="small" onClick={handleViewProfile} sx={{ bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600, '&:hover': { bgcolor: '#FFECB3' } }}>
          View Profile
        </Button>
        <Button size="small" disabled sx={{ color: '#BDBDBD' }}>
          Book Appointment (Login Required)
        </Button>
      </CardActions>
    </Card>
  );
};

export default TherapistCard; 