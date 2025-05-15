// src/ProfileManagement/NavBar.js
import React, { useState } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import { useAuth } from './ProfileManagement/UserContext';
import Logo from './logo.png';
import Typography from '@mui/material/Typography';
import { signOut } from 'firebase/auth';
import { auth } from './firebaseConfig';

function NavBar() {
  const { user: currentUser, userRole } = useAuth();
  const navigate = useNavigate();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  async function handleLogout() {
    try {
      await signOut(auth);
      navigate('/login');
    } catch (error) {
      console.error('Logout error:', error);
    }
  }

  return (
    <AppBar position="static" sx={{ background: 'linear-gradient(90deg, #FFD54F 0%, #8D6E63 100%)', boxShadow: 3 }}>
      <Toolbar>
        <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1 }}>
          <img src={Logo} alt="SoulThera Logo" style={{ height: 40, marginRight: 12 }} />
          <Typography variant="h6" sx={{ color: '#5D4037', fontWeight: 700, letterSpacing: 1 }}>
            SoulThera
          </Typography>
        </Box>
        <Button color="inherit" component={RouterLink} to="/">Home</Button>
        <Button color="inherit" component={RouterLink} to="/therapists">Therapists</Button>
        <Button color="inherit" component={RouterLink} to="/about">About Us</Button>
        {userRole === 'admin' && (
          <>
            <Button color="inherit" component={RouterLink} to="/admin/reviews">Review Management</Button>
            <Button color="inherit" component={RouterLink} to="/admin/verification">Verification requests</Button>
            <Button color="inherit" component={RouterLink} to="/admin/users">Terapistler</Button>
          </>
        )}
        {currentUser ? (
          <>
            <Button color="inherit" component={RouterLink} to="/profile">Profile</Button>
            <Button color="inherit" onClick={handleLogout} sx={{ ml: 2 }}>Log out</Button>
          </>
        ) : (
          <>
            <Button color="inherit" component={RouterLink} to="/login">Login</Button>
            <Button color="inherit" component={RouterLink} to="/register" sx={{ ml: 1, bgcolor: '#FFD54F', color: '#5D4037', fontWeight: 600, '&:hover': { bgcolor: '#FFECB3' } }}>Register</Button>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
}

export default NavBar;
