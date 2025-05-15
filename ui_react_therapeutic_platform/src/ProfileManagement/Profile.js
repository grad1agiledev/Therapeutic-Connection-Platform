import React, { useEffect, useState } from 'react';
import { useAuth } from './UserContext';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import Chip from '@mui/material/Chip';
import Alert from '@mui/material/Alert';
import CircularProgress from '@mui/material/CircularProgress';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import config from '../config';

// Turkish cities
const TURKISH_CITIES = [
  'Adana', 'Adıyaman', 'Afyonkarahisar', 'Ağrı', 'Amasya', 'Ankara', 'Antalya', 'Artvin', 'Aydın', 'Balıkesir',
  'Bilecik', 'Bingöl', 'Bitlis', 'Bolu', 'Burdur', 'Bursa', 'Çanakkale', 'Çankırı', 'Çorum', 'Denizli',
  'Diyarbakır', 'Edirne', 'Elazığ', 'Erzincan', 'Erzurum', 'Eskişehir', 'Gaziantep', 'Giresun', 'Gümüşhane', 'Hakkari',
  'Hatay', 'Isparta', 'Mersin', 'İstanbul', 'İzmir', 'Kars', 'Kastamonu', 'Kayseri', 'Kırklareli', 'Kırşehir',
  'Kocaeli', 'Konya', 'Kütahya', 'Malatya', 'Manisa', 'Kahramanmaraş', 'Mardin', 'Muğla', 'Muş', 'Nevşehir',
  'Niğde', 'Ordu', 'Rize', 'Sakarya', 'Samsun', 'Siirt', 'Sinop', 'Sivas', 'Tekirdağ', 'Tokat',
  'Trabzon', 'Tunceli', 'Şanlıurfa', 'Uşak', 'Van', 'Yozgat', 'Zonguldak', 'Aksaray', 'Bayburt', 'Karaman',
  'Kırıkkale', 'Batman', 'Şırnak', 'Bartın', 'Ardahan', 'Iğdır', 'Yalova', 'Karabük', 'Kilis', 'Osmaniye', 'Düzce'
];

// Top 20 languages in the world
const TOP_LANGUAGES = [
  { id: 1, langName: 'English' },
  { id: 2, langName: 'Mandarin Chinese' },
  { id: 3, langName: 'Hindi' },
  { id: 4, langName: 'Spanish' },
  { id: 5, langName: 'French' },
  { id: 6, langName: 'Arabic' },
  { id: 7, langName: 'Bengali' },
  { id: 8, langName: 'Portuguese' },
  { id: 9, langName: 'Russian' },
  { id: 10, langName: 'Japanese' },
  { id: 11, langName: 'German' },
  { id: 12, langName: 'Korean' },
  { id: 13, langName: 'Turkish' },
  { id: 14, langName: 'Italian' },
  { id: 15, langName: 'Vietnamese' },
  { id: 16, langName: 'Persian' },
  { id: 17, langName: 'Dutch' },
  { id: 18, langName: 'Polish' },
  { id: 19, langName: 'Ukrainian' },
  { id: 20, langName: 'Romanian' }
];

// Common therapy specializations
const SPECIALIZATIONS = [
  'Anxiety',
  'Depression',
  'Trauma',
  'Relationship Issues',
  'Family Therapy',
  'Addiction',
  'Stress Management',
  'Grief Counseling',
  'Child Psychology',
  'Couples Therapy',
  'Career Counseling',
  'Eating Disorders',
  'PTSD',
  'OCD',
  'ADHD',
  'Autism Spectrum',
  'LGBTQ+ Counseling',
  'Substance Abuse',
  'Anger Management',
  'Self-esteem Issues'
];

export default function Profile() {
  const { currentUser, userRole } = useAuth();
  const [loading, setLoading] = useState(true);
  const [locations, setLocations] = useState([]);
  const [locId, setLocId] = useState('');
  const [fullName, setFullName] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  const [selectedLocation, setSelectedLocation] = useState('');
  const [specializations, setSpecializations] = useState([]);
  const [bio, setBio] = useState('');
  const [sessionCost, setSessionCost] = useState('');
  const [file, setFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState('');
  const [languageIds, setLanguageIds] = useState([]);
  const [licenceNumber, setLicenceNumber] = useState('');
  const [licenceFile, setLicenceFile] = useState(null);
  const [verifyBusy, setVerifyBusy] = useState(false);
  const [verificationState, setVerificationState] = useState('UNVERIFIED');
  const [licenceDocUrl, setLicenceDocUrl] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isVirtual, setIsVirtual] = useState(false);

  async function requestVerification() {
    try {
      setVerifyBusy(true);
      let licenceUrl = '';
      if (licenceFile) {
        const fd = new FormData();
        fd.append('file', licenceFile);
        const up = await fetch(
          `${config.API_URL}/api/therapists/${currentUser.uid}/uploadLicence`,
          { method: 'POST', body: fd }
        );
        licenceUrl = (await up.json()).url;
      }
      await fetch(
        `${config.API_URL}/api/therapists/${currentUser.uid}/verify`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ licenceDocument: licenceUrl })
        }
      );
      setSuccess('Verification request sent!');
      setLicenceDocUrl(licenceUrl);
    } catch (e) {
      setError('Failed: ' + e.message);
    } finally {
      setVerifyBusy(false);
    }
  }

  useEffect(() => {
    if (!currentUser) return;
    Promise.all([
      fetch(`${config.API_URL}/api/users/${currentUser.uid}`).then(r => r.json()),
      fetch(`${config.API_URL}/api/locations`).then(r => r.json()),
      fetch(`${config.API_URL}/api/languages`).then(r => r.json())
    ])
      .then(([userData, locs, langs]) => {
        setFullName(userData.fullName);
        setPhone(userData.phone);
        setAddress(userData.address || '');
        setLocId(
          locs.find(l => `${l.name}, ${l.country}` === userData.address)?.id || ''
        );
        setLocations(locs);
        if (userRole === 'therapist') {
          fetch(`${config.API_URL}/api/therapists/${currentUser.uid}`)
            .then(r => r.json())
            .then(th => {
              setSelectedLocation(th.location?.id || '');
              setLocId(th.location?.id || '');
              setSpecializations(th.specializations || []);
              setBio(th.bio || '');
              setSessionCost(th.sessionCost || 0);
              setPreviewUrl(th.profilePicture || '');
              setLanguageIds(th.languages?.map(l => l.id) || []);
              setVerificationState(th.verificationState);
              setLicenceDocUrl(th.licenceDocument || '');
              setIsVirtual(th.isVirtual || false);
            });
        }
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [currentUser, userRole]);

  if (!currentUser) return <Alert severity="warning">Please log in to view your profile.</Alert>;
  if (loading) return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}><CircularProgress /></Box>;

  const handleLocationChange = e => {
    const newId = e.target.value;
    setLocId(newId);
    
    // Check if the value is a number (location id) or a string (city name)
    if (!isNaN(parseInt(newId, 10))) {
      const loc = locations.find(l => l.id === parseInt(newId, 10));
      if (loc) {
        setAddress(`${loc.name}, ${loc.country}`);
      }
    } else {
      // Handle Turkey cities directly
      setAddress(`${newId}, Turkey`);
    }
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      const userPayload = { fullName, phone, address };
      const res1 = await fetch(
        `${config.API_URL}/api/users/${currentUser.uid}`,
        {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(userPayload)
        }
      );
      if (!res1.ok) {
        const text = await res1.text();
        throw new Error(`User update failed: ${res1.status} ${text}`);
      }
      let picUrl = previewUrl;
      if (userRole === 'therapist' && file) {
        const fd = new FormData();
        fd.append('file', file);
        const uploadRes = await fetch(
          `${config.API_URL}/api/therapists/${currentUser.uid}/uploadPhoto`,
          { method: 'POST', body: fd }
        );
        if (!uploadRes.ok) {
          const text = await uploadRes.text();
          throw new Error(`Photo upload failed: ${uploadRes.status} ${text}`);
        }
        const { url } = await uploadRes.json();
        picUrl = url;
      }
      if (userRole === 'therapist') {
        // Check if locId is a Turkish city
        const isTurkishCity = TURKISH_CITIES.includes(locId);
        
        const thPayload = {
          specializations,
          bio,
          sessionCost,
          locationId: isTurkishCity ? null : locId, // Send null if it's a Turkish city
          profilePicture: picUrl,
          languageIds,
          isVirtual
        };
        const res2 = await fetch(
          `${config.API_URL}/api/therapists/${currentUser.uid}`,
          {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(thPayload)
          }
        );
        if (!res2.ok) {
          const text = await res2.text();
          throw new Error(`Therapist update failed: ${res2.status} ${text}`);
        }
      }
      setSuccess('Profile updated successfully!');
    } catch (err) {
      setError(`Update failed: ${err.message}`);
    }
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mt: 6, p: 4, bgcolor: '#FFFDE7', borderRadius: 3, boxShadow: 2 }}>
        <Typography variant="h4" color="#5D4037" fontWeight={700} gutterBottom>Edit Your Profile</Typography>
        {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
        {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}
        <Box component="form" onSubmit={handleSubmit}>
          <TextField
            label="Full Name"
            value={fullName}
            onChange={e => setFullName(e.target.value)}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Email"
            value={currentUser.email}
            fullWidth
            margin="normal"
            InputProps={{ readOnly: true }}
          />
          <TextField
            label="Phone"
            value={phone}
            onChange={e => setPhone(e.target.value)}
            fullWidth
            margin="normal"
          />
          <FormControl fullWidth margin="normal">
            <InputLabel id="location-label">Location</InputLabel>
            <Select
              labelId="location-label"
              value={locId}
              label="Location"
              onChange={handleLocationChange}
              required
            >
              <MenuItem value="">-- Select --</MenuItem>
              {locations.map(loc => (
                <MenuItem key={loc.id} value={loc.id}>{loc.name}, {loc.country}</MenuItem>
              ))}
              <MenuItem disabled sx={{ opacity: 0.6, mt: 1, fontWeight: 'bold' }}>
                --- Turkey Cities ---
              </MenuItem>
              {TURKISH_CITIES.map((city) => (
                <MenuItem key={`city-${city}`} value={city}>
                  {city}, Turkey
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          {userRole === 'therapist' && (
            <>
              <FormControl fullWidth margin="normal">
                <InputLabel id="specializations-label">Specializations</InputLabel>
                <Select
                  labelId="specializations-label"
                  multiple
                  value={specializations}
                  onChange={e => setSpecializations(typeof e.target.value === 'string' ? e.target.value.split(',') : e.target.value)}
                  renderValue={selected => (
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                      {selected.map(value => (
                        <Chip key={value} label={value} />
                      ))}
                    </Box>
                  )}
                  label="Specializations"
                >
                  {SPECIALIZATIONS.map(spec => (
                    <MenuItem key={spec} value={spec}>
                      {spec}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="Bio"
                value={bio}
                onChange={e => setBio(e.target.value)}
                fullWidth
                margin="normal"
                multiline
                rows={4}
              />
              <FormControl fullWidth margin="normal">
                <InputLabel id="languages-label">Languages</InputLabel>
                <Select
                  labelId="languages-label"
                  multiple
                  value={languageIds}
                  onChange={e => setLanguageIds(typeof e.target.value === 'string' ? e.target.value.split(',') : e.target.value)}
                  renderValue={selected => (
                    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                      {selected.map(id => {
                        const lang = TOP_LANGUAGES.find(l => l.id === id);
                        return lang ? <Chip key={id} label={lang.langName} /> : null;
                      })}
                    </Box>
                  )}
                  label="Languages"
                >
                  {TOP_LANGUAGES.map(lang => (
                    <MenuItem key={lang.id} value={lang.id}>
                      {lang.langName}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="Session Cost (USD)"
                type="number"
                value={sessionCost}
                onChange={e => setSessionCost(parseFloat(e.target.value))}
                fullWidth
                margin="normal"
                InputProps={{
                  inputProps: { min: 0, step: 0.01 }
                }}
              />
              <FormControlLabel
                control={
                  <Checkbox
                    checked={isVirtual}
                    onChange={e => setIsVirtual(e.target.checked)}
                    color="primary"
                  />
                }
                label="Offer Virtual Sessions"
                sx={{ mt: 2 }}
              />
              <Box sx={{ my: 2 }}>
                <Typography variant="subtitle2">Profile Picture</Typography>
                {previewUrl && (
                  <img src={previewUrl} alt="preview" width={80} style={{ display: 'block', marginBottom: 8 }} />
                )}
                <input
                  type="file"
                  accept="image/*"
                  onChange={e => {
                    const f = e.target.files[0];
                    setFile(f);
                    setPreviewUrl(URL.createObjectURL(f));
                  }}
                />
              </Box>
              <Box sx={{ my: 2 }}>
                <Chip
                  label={
                    verificationState === 'VERIFIED'
                      ? '✔ Profile verified'
                      : verificationState === 'PENDING'
                      ? '⏳ Verification request pending'
                      : '⚠️ Profile not verified'
                  }
                  color={
                    verificationState === 'VERIFIED'
                      ? 'success'
                      : verificationState === 'PENDING'
                      ? 'warning'
                      : 'error'
                  }
                  sx={{ fontWeight: 600, mb: 1 }}
                />
                {verificationState === 'UNVERIFIED' && (
                  <Box sx={{ mt: 2 }}>
                    <Alert severity="warning" sx={{ mb: 2 }}>
                      Your profile is not verified yet. Clients won't see you in search results until an admin approves it.
                    </Alert>
                    <Typography variant="subtitle2">Upload Licence (PDF/ image)</Typography>
                    <input
                      type="file"
                      accept=".pdf,image/*"
                      onChange={e => setLicenceFile(e.target.files[0])}
                    />
                    {licenceDocUrl &&
                      (licenceDocUrl.toLowerCase().endsWith('.pdf') ? (
                        <Typography sx={{ mt: 1 }}>
                          Current file:&nbsp;
                          <a href={licenceDocUrl} target="_blank" rel="noreferrer">
                            uploaded PDF
                          </a>
                        </Typography>
                      ) : (
                        <img
                          src={licenceDocUrl}
                          alt="Licence"
                          width={120}
                          style={{ display: 'block', marginTop: 8 }}
                        />
                      ))}
                    <Button
                      type="button"
                      disabled={verifyBusy}
                      onClick={requestVerification}
                      sx={{ mt: 2, bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600, '&:hover': { bgcolor: '#FFECB3' } }}
                    >
                      {verifyBusy ? 'Sending…' : 'Verify my profile'}
                    </Button>
                  </Box>
                )}
              </Box>
            </>
          )}
          <Button type="submit" variant="contained" sx={{ mt: 2, bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600, '&:hover': { bgcolor: '#FFECB3' } }}>
            Save Changes
          </Button>
        </Box>
      </Box>
    </Container>
  );
}
