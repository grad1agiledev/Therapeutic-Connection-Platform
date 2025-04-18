import React, { useEffect, useState } from 'react';
import './VerificationBoard.css';

export default function VerificationBoard() {
  const [pending, setPending]  = useState([]);
  const [busyId,  setBusyId]   = useState(null);

  async function load() {
    const res = await fetch('http://localhost:8080/api/admin/verification/pending');
    setPending(await res.json());
  }

  useEffect(() => { load(); }, []);

  async function approve(id) {
    setBusyId(id);
    await fetch(
      `http://localhost:8080/api/admin/verification/${id}/approve`,
      { method:'PUT' }
    );
    await load();
    setBusyId(null);
  }

  if (!pending.length) return <p>No pending requests 🎉</p>;

  return (
      <table className="verif-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Licence Document</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {pending.map(t => (
            <tr key={t.id}>
              <td>{t.user.fullName}</td>
              <td>
                <a href={t.licenceDocument} target="_blank" rel="noreferrer">
                  View
                </a>
              </td>
              <td>
                <button
                  disabled={busyId === t.id}
                  onClick={() => approve(t.id)}
                >
                  {busyId === t.id ? 'Approving…' : 'Approve'}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  }
