import React, { useEffect, useState } from 'react';
import { auth } from '../firebaseConfig';
import { createUserWithEmailAndPassword } from 'firebase/auth';
import { useNavigate } from 'react-router-dom';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Alert from '@mui/material/Alert';

function Register() {
  const navigate = useNavigate();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [locations, setLocations] = useState([]);
  const [selectedLocation, setSelectedLocation] = useState('');
  const [role, setRole] = useState('patient');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/locations')
      .then(res => res.json())
      .then(data => {
        setLocations(data);
        if (data.length > 0) {
          setSelectedLocation(data[0].id);
        }
      })
      .catch(error => console.error('Error fetching locations:', error));
  }, []);

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    if (password.length < 8) {
      setError('Password must be at least 8 characters long');
      return;
    }
    if (!/[A-Z]/.test(password)) {
      setError('Password must contain at least one uppercase letter');
      return;
    }
    if (!/[a-z]/.test(password)) {
      setError('Password must contain at least one lowercase letter');
      return;
    }
    if (!/[0-9]/.test(password)) {
      setError('Password must contain at least one number');
      return;
    }
    if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
      setError('Password must contain at least one special character');
      return;
    }
    try {
      const userInfos = await createUserWithEmailAndPassword(auth, email, password);
      const uid = userInfos.user.uid;
      await fetch('http://localhost:8080/api/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          uid: uid,
          name: name,
          phone: phone,
          locationId: selectedLocation,
          role: role,
          email: email,
        }),
      });
      setSuccess('Registration is successful!');
      setTimeout(() => navigate('/profile'), 1500);
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 8, p: 4, bgcolor: '#FFFDE7', borderRadius: 3, boxShadow: 2 }}>
        <Typography variant="h4" align="center" color="#5D4037" fontWeight={700} gutterBottom>Register</Typography>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}
        <Box component="form" onSubmit={handleRegister}>
          <TextField
            type="text"
            label="Full Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            type="email"
            label="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            type="password"
            label="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            fullWidth
            margin="normal"
            required
            inputProps={{ minLength: 8 }}
          />
          <TextField
            type="text"
            label="Phone Number"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            fullWidth
            margin="normal"
          />
          <FormControl fullWidth margin="normal">
            <InputLabel id="location-label">Location</InputLabel>
            <Select
              labelId="location-label"
              id="locationSelect"
              value={selectedLocation}
              label="Location"
              onChange={(e) => setSelectedLocation(e.target.value)}
              required
            >
              {locations.map(loc => (
                <MenuItem key={loc.id} value={loc.id}>
                  {loc.name}, {loc.country}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl component="fieldset" margin="normal">
            <RadioGroup row value={role} onChange={(e) => setRole(e.target.value)}>
              <FormControlLabel value="patient" control={<Radio />} label="Patient" />
              <FormControlLabel value="therapist" control={<Radio />} label="Therapist" />
            </RadioGroup>
          </FormControl>
          <Button type="submit" variant="contained" fullWidth sx={{ mt: 2, bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600, '&:hover': { bgcolor: '#FFECB3' } }}>
            Sign Up
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default Register;