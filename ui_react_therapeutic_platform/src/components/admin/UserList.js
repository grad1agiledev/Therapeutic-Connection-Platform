import React, { useState, useEffect } from 'react';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import CircularProgress from '@mui/material/CircularProgress';
import Alert from '@mui/material/Alert';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Button from '@mui/material/Button';
import InputBase from '@mui/material/InputBase';
import Paper from '@mui/material/Paper';
import SearchIcon from '@mui/icons-material/Search';
import axios from 'axios';
import './UserList.css';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        setLoading(true);
        // Azure veritabanından sadece terapist rolündeki kullanıcıları çekmek için API çağrısı
        const response = await axios.get('http://localhost:8080/api/users/role/therapist');
        setUsers(response.data);
        setFilteredUsers(response.data);
        setLoading(false);
      } catch (err) {
        setError('Kullanıcı bilgileri yüklenirken bir hata oluştu. Lütfen daha sonra tekrar deneyin.');
        setLoading(false);
        console.error('Kullanıcı yükleme hatası:', err);
      }
    };
    fetchUsers();
  }, []);

  useEffect(() => {
    if (searchTerm.trim() === '') {
      setFilteredUsers(users);
    } else {
      const filtered = users.filter(user => 
        user.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredUsers(filtered);
    }
  }, [searchTerm, users]);

  const handleSearch = (e) => {
    setSearchTerm(e.target.value);
  };

  const UserCard = ({ user }) => {
    return (
      <Card className="user-card">
        <CardContent>
          <Typography variant="h6" component="div" gutterBottom>
            {user.fullName}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Email: {user.email}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Rol: {user.role}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Kayıt Tarihi: {new Date(user.dateCreated).toLocaleDateString()}
          </Typography>
        </CardContent>
        <CardActions>
          <Button size="small" color="primary">Detayları Görüntüle</Button>
        </CardActions>
      </Card>
    );
  };

  return (
    <Box sx={{ bgcolor: '#f5f5f5', borderRadius: 3, p: 3, boxShadow: 2 }}>
      <Typography variant="h3" color="primary" gutterBottom fontWeight={700}>
        Terapist Listesi
      </Typography>
      
      <Paper
        component="form"
        sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: 400, mb: 3 }}
      >
        <SearchIcon sx={{ color: 'action.active', mr: 1, ml: 1 }} />
        <InputBase
          sx={{ ml: 1, flex: 1 }}
          placeholder="Terapist ara..."
          inputProps={{ 'aria-label': 'terapist ara' }}
          value={searchTerm}
          onChange={handleSearch}
        />
      </Paper>
      
      <Typography variant="subtitle2" color="text.secondary" sx={{ mt: 2, mb: 2 }}>
        {filteredUsers.length} terapist bulundu
      </Typography>
      
      {loading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', my: 4 }}>
          <CircularProgress color="primary" />
        </Box>
      ) : error ? (
        <Alert severity="error">{error}</Alert>
      ) : (
        <Grid container spacing={3}>
          {filteredUsers.map(user => (
            <Grid item xs={12} sm={6} md={4} key={user.id}>
              <UserCard user={user} />
            </Grid>
          ))}
        </Grid>
      )}
    </Box>
  );
};

export default UserList; 