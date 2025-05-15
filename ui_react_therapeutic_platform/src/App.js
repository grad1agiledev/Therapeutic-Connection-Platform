import React , { useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import TherapistList from './components/therapist/TherapistList';
import Register from './Registration/Register'
import Login from './Login/Login';
import ReviewManagement from './components/admin/ReviewManagement';
import UserList from './components/admin/UserList';
import Profile from './ProfileManagement/Profile';
import NavBar from './NavBar';
import Calendar from './components/calendar/Calendar';
import { AuthProvider } from './ProfileManagement/UserContext';
import VerificationBoard from './components/admin/VerificationBoard';
import { useAuth }        from './ProfileManagement/UserContext';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';

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
          <Typography variant="h5" color="#8D6E63" fontWeight={600} gutterBottom>
            A Modern Therapy Platform That Touches Your Soul
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph>
            SoulThera is a next-generation platform dedicated to making mental health support accessible, reliable, and stigma-free for everyone. We believe that every individual deserves to feel heard, understood, and empowered on their journey to well-being. Our mission is to break down barriers to therapy and create a safe, inclusive space where healing and growth are possible for all.
          </Typography>
          <Typography variant="h6" color="#5D4037" fontWeight={600} sx={{ mt: 3 }}>
            Our Mission
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph>
            To connect people with trusted, compassionate therapists and provide seamless access to quality mental health care—anytime, anywhere. We are committed to leveraging technology to foster genuine human connection and support.
          </Typography>
          <Typography variant="h6" color="#5D4037" fontWeight={600} sx={{ mt: 3 }}>
            Our Vision
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph>
            We envision a world where mental health is prioritized, therapy is normalized, and everyone can thrive emotionally and psychologically. By making therapy approachable and removing the stigma, we aim to inspire hope and resilience in our community.
          </Typography>
          <Typography variant="h6" color="#5D4037" fontWeight={600} sx={{ mt: 3 }}>
            Our Values
          </Typography>
          <ul style={{ color: '#6D4C41', fontSize: '1.1rem', marginLeft: 24 }}>
            <li><b>Privacy & Security:</b> Your confidentiality and safety are our top priorities.</li>
            <li><b>Empathy & Respect:</b> We honor every story and support every journey.</li>
            <li><b>Accessibility & Inclusivity:</b> We strive to make therapy available to all, regardless of background or circumstance.</li>
            <li><b>Innovation:</b> We embrace technology to enhance the therapy experience without losing the human touch.</li>
            <li><b>Quality & Professionalism:</b> Our therapists are carefully selected, experienced, and dedicated to your well-being.</li>
          </ul>
          <Typography variant="h6" color="#5D4037" fontWeight={600} sx={{ mt: 3 }}>
            Why Choose SoulThera?
          </Typography>
          <ul style={{ color: '#6D4C41', fontSize: '1.1rem', marginLeft: 24 }}>
            <li>Easy therapist search and matching based on your needs</li>
            <li>Secure online sessions and flexible appointment scheduling</li>
            <li>Verified reviews and transparent therapist profiles</li>
            <li>Support for both in-person and virtual therapy</li>
            <li>Continuous improvement based on user feedback</li>
          </ul>
          <Typography variant="h6" color="#5D4037" fontWeight={600} sx={{ mt: 3 }}>
            Our Social Impact
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph>
            At SoulThera, we are passionate about raising mental health awareness and supporting our community. We regularly organize free webinars, workshops, and outreach programs to empower individuals and reduce the stigma around seeking help. Together, we are building a more compassionate and resilient society.
          </Typography>
          <Typography variant="body1" color="text.secondary" paragraph sx={{ mt: 2 }}>
            With SoulThera, you can easily find the most suitable therapist for you, book appointments, and take the first step for your mental well-being—either face-to-face or online. We are honored to be by your side on your journey to a healthier, happier you.
          </Typography>
        </CardContent>
      </Card>
    </Container>
  );
}

function AdminRoute({ children }) {
  const { userRole } = useAuth();
  return userRole === 'admin' ? children : <p>403 – admins only</p>;
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <Box className="app-container" sx={{ bgcolor: '#FFF8E1', minHeight: '100vh' }}>
          <header className="app-header">
            <NavBar />
          </header>

          <main className="app-main">
            <Container maxWidth="lg" sx={{ py: 4 }}>
              <Routes>
                <Route path="/" element={<Calendar />} />
                <Route path="/therapists" element={<TherapistList />} />
                <Route path="/about" element={<AboutUs />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/admin/reviews" element={<ReviewManagement />} />
                <Route path="/admin/users" element={<AdminRoute><UserList /></AdminRoute>} />
                <Route path="/profile" element={<Profile />} />
                <Route  path="/admin/verification" element={  <AdminRoute> <VerificationBoard /> </AdminRoute>         }
                />
              </Routes>
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
                  <a href="/" style={{ color: '#FFD54F', textDecoration: 'none', fontWeight: 500 }}>Home</a>
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
      </AuthProvider>
    </Router>
  );
}

export default App;