import React, { useEffect, useState } from 'react';
import { auth } from '../firebaseConfig';

const TherapistProfile = () => {
  const [therapist, setTherapist] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
      const fetchTherapist = async () => {
        try {
          const user = auth.currentUser;
          if (!user) return;

          const uid = user.uid;

          const response = await fetch(`http://localhost:8080/api/therapists/by-uid/${uid}`);
          if (!response.ok) throw new Error('Failed to fetch therapist info');

          const data = await response.json();
          setTherapist(data);
        } catch (error) {
          console.error('Error fetching therapist data:', error);
        } finally {
          setLoading(false);
        }
      };
      fetchTherapist();
        }, []);

        if (loading) return <p>Loading...</p>;
        if (!therapist) return <p>No therapist info found.</p>;

        return (
            <div>
              <h2>{therapist.user.fullName}'s Profile</h2>
              <img src={therapist.profilePicture} alt="Profile" />
              <p><strong>Specialization:</strong> {therapist.specialization}</p>
              <p><strong>Bio:</strong> {therapist.bio}</p>
              <p><strong>Cost:</strong> ${therapist.sessionCost}</p>
            </div>
          );
        };

        export default TherapistProfile;