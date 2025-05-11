import React, { useEffect, useState } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';

export default function VerificationBoard() {
  const [pending, setPending] = useState([]);
  const [busyId, setBusyId] = useState(null);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    const res = await fetch('http://localhost:8080/api/admin/verification/pending');
    setPending(await res.json());
    setLoading(false);
  }

  useEffect(() => { load(); }, []);

  async function approve(id) {
    setBusyId(id);
    await fetch(
      `http://localhost:8080/api/admin/verification/${id}/approve`,
      { method: 'PUT' }
    );
    await load();
    setBusyId(null);
  }

  if (loading) return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 8 }}><CircularProgress /></Box>;
  if (!pending.length) return <Typography sx={{ mt: 6 }} color="success.main" align="center">No pending requests 🎉</Typography>;

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto', mt: 6 }}>
      <Typography variant="h4" color="#5D4037" fontWeight={700} gutterBottom>Doğrulama Talepleri</Typography>
      <TableContainer component={Paper} sx={{ borderRadius: 2, boxShadow: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell><b>Name</b></TableCell>
              <TableCell><b>Licence Document</b></TableCell>
              <TableCell><b>Action</b></TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {pending.map(t => (
              <TableRow key={t.id}>
                <TableCell>{t.user.fullName}</TableCell>
                <TableCell>
                  <a href={t.licenceDocument} target="_blank" rel="noreferrer">
                    View
                  </a>
                </TableCell>
                <TableCell>
                  <Button
                    variant="contained"
                    color="success"
                    disabled={busyId === t.id}
                    onClick={() => approve(t.id)}
                    sx={{ fontWeight: 600 }}
                  >
                    {busyId === t.id ? 'Approving…' : 'Approve'}
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}
