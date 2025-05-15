import React, { useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TherapistList from './components/therapist/TherapistList';
import Register from './Registration/Register'
import Login from './Login/Login';
import ReviewManagement from './components/admin/ReviewManagement';
import UserList from './components/admin/UserList';
import VideoConference from './components/video_conference/VideoConference';
import Profile from './ProfileManagement/Profile';
import NavBar from './NavBar';
import Calendar from './components/calendar/Calendar';
import { AuthProvider } from './ProfileManagement/UserContext';
import VerificationBoard from './components/admin/VerificationBoard';
import { useAuth } from './ProfileManagement/UserContext';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Welcome from './Welcome/Welcome';

// ... AboutUs component remains the same ...

function AboutUs() {
  return (
    <Container maxWidth="md" sx={{ mt: 6 }}>
      <Card sx={{ boxShadow: 4, borderRadius: 4, bgcolor: '#FFFDE7', p: 3 }}>
        <CardContent>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
            <img src={require('./logo.png')} alt="SoulThera Logo" style={{ height: 60, marginRight: 24 }} />
            <Typography variant="h3" color="#5D4037" fontWeight={700}>
              About SoulThera
            </Typography>
          </Box>
          {/* ... rest of the AboutUs component content ... */}
        </CardContent>
      </Card>
    </Container>
  );
}

function AdminRoute({ children }) {
  const { userRole } = useAuth();
  return userRole === 'admin' ? children : <p>403 – admins only</p>;
}

function MainLayout({ children }) {
  return (
    <Box className="app-container" sx={{ bgcolor: '#FFF8E1', minHeight: '100vh' }}>
      <header className="app-header">
        <NavBar />
      </header>

      <main className="app-main">
        <Container maxWidth="lg" sx={{ py: 4 }}>
          {children}
        </Container>
      </main>

      <footer className="app-footer" style={{ background: '#8D6E63', color: '#FFF8E1' }}>
        <Box sx={{ display: 'flex', flexDirection: { xs: 'column', md: 'row' }, alignItems: 'center', justifyContent: 'space-between', py: 2, px: { xs: 2, md: 6 } }}>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: { xs: 2, md: 0 } }}>
            <img src={require('./logo.png')} alt="SoulThera Logo" style={{ height: 36, marginRight: 12 }} />
            <Typography variant="h6" sx={{ color: '#FFD54F', fontWeight: 700, letterSpacing: 1 }}>
              SoulThera
            </Typography>
          </Box>
          <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: { xs: 'center', md: 'flex-start' }, mb: { xs: 2, md: 0 } }}>
            <Box sx={{ display: 'flex', gap: 2, mb: 1 }}>
              <a href="/home" style={{ color: '#FFD54F', textDecoration: 'none', fontWeight: 500 }}>Home</a>
              <a href="/therapists" style={{ color: '#FFD54F', textDecoration: 'none', fontWeight: 500 }}>Therapists</a>
              <a href="/about" style={{ color: '#FFD54F', textDecoration: 'none', fontWeight: 500 }}>About Us</a>
              <a href="/login" style={{ color: '#FFD54F', textDecoration: 'none', fontWeight: 500 }}>Login</a>
              <a href="/register" style={{ color: '#FFD54F', textDecoration: 'none', fontWeight: 500 }}>Register</a>
            </Box>
            <Typography variant="body2" sx={{ color: '#FFF8E1', fontWeight: 500 }}>
              Rise Like the Sun, Heal Like the Earth.
            </Typography>
            <Typography variant="body2" sx={{ color: '#FFD54F', fontWeight: 400 }}>
              Contact: <a href="mailto:info@soulthera.com" style={{ color: '#FFD54F', textDecoration: 'underline' }}>info@soulthera.com</a>
            </Typography>
            <Box sx={{ mt: 1, display: 'flex', gap: 1, justifyContent: { xs: 'center', md: 'flex-start' } }}>
              <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" style={{ color: '#FFD54F' }}>
                <i className="fab fa-twitter" style={{ fontSize: 20 }}></i>
              </a>
              <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" style={{ color: '#FFD54F' }}>
                <i className="fab fa-facebook" style={{ fontSize: 20 }}></i>
              </a>
              <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" style={{ color: '#FFD54F' }}>
                <i className="fab fa-instagram" style={{ fontSize: 20 }}></i>
              </a>
              <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" style={{ color: '#FFD54F' }}>
                <i className="fab fa-linkedin" style={{ fontSize: 20 }}></i>
              </a>
            </Box>
          </Box>
          <Typography variant="body2" align="center" sx={{ color: '#FFF8E1', mt: { xs: 2, md: 0 } }}>
            &copy; {new Date().getFullYear()} SoulThera. All rights reserved.
          </Typography>
        </Box>
      </footer>
    </Box>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<Welcome />} />
          <Route path="/home" element={
            <MainLayout>
              <Calendar />
            </MainLayout>
          } />
          <Route path="/therapists" element={
            <MainLayout>
              <TherapistList />
            </MainLayout>
          } />
          <Route path="/about" element={
            <MainLayout>
              <AboutUs />
            </MainLayout>
          } />
          <Route path="/login" element={
            <MainLayout>
              <Login />
            </MainLayout>
          } />
          <Route path="/register" element={
            <MainLayout>
              <Register />
            </MainLayout>
          } />
          <Route path="/admin/reviews" element={
            <MainLayout>
              <ReviewManagement />
            </MainLayout>
          } />
          <Route path="/admin/users" element={
            <MainLayout>
              <AdminRoute>
                <UserList />
              </AdminRoute>
            </MainLayout>
          } />
          <Route path="/profile" element={
            <MainLayout>
              <Profile />
            </MainLayout>
          } />
          <Route path="/admin/verification" element={
            <MainLayout>
              <AdminRoute>
                <VerificationBoard />
              </AdminRoute>
            </MainLayout>
          } />
          <Route path="/meeting/:meetingID" element={
            <MainLayout>
              <VideoConference />
            </MainLayout>
          } />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;