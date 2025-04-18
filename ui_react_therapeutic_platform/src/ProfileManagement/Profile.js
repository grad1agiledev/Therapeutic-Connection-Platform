import React, { useEffect, useState } from 'react';
import { useAuth } from './UserContext';

export default function Profile() {
  const { currentUser, userRole } = useAuth();
  const [loading, setLoading]     = useState(true);
  const [locations, setLocations] = useState([]);
  const [locId, setLocId]         = useState('');

  // common fields
  const [fullName, setFullName]   = useState('');
  const [phone, setPhone]         = useState('');

  // patient only
  const [address, setAddress]     = useState('');

  // therapist only
  const [selectedLocation, setSelectedLocation] = useState('');
  const [specialization, setSpecialization]     = useState('');
  const [bio, setBio]                           = useState('');
  const [sessionCost, setSessionCost]           = useState('');
  const [file, setFile]                         = useState(null);
  const [previewUrl, setPreviewUrl]             = useState('');

  const [languages, setLanguages]       = useState([]);
  const [languageIds, setLanguageIds]   = useState([]);

  const [licenceNumber, setLicenceNumber] = useState('');
  const [licenceFile,   setLicenceFile]   = useState(null);
  const [verifyBusy,     setVerifyBusy]     = useState(false);
  const [verificationState, setVerificationState] = useState('UNVERIFIED');

  const [licenceDocUrl, setLicenceDocUrl] = useState('');


  //verification request for therapist
  async function requestVerification() {
    try {
      setVerifyBusy(true);

      let licenceUrl = '';
      if (licenceFile) {
        const fd = new FormData();
        fd.append('file', licenceFile);
        const up = await fetch(
          `http://localhost:8080/api/therapists/${currentUser.uid}/uploadLicence`,
          { method: 'POST', body: fd }
        );
        licenceUrl = (await up.json()).url;
      }

      await fetch(
            `http://localhost:8080/api/therapists/${currentUser.uid}/verify`,
            {
              method : 'POST',
              headers: { 'Content-Type': 'application/json' },
              body   : JSON.stringify({
                licenceDocument : licenceUrl
              })
            }
          );

          alert('Verification request sent!');
          //setIsVerified(th.verificationState === 'VERIFIED');
          setLicenceDocUrl(licenceUrl);
        } catch (e) {
          alert('Failed: ' + e.message);
        } finally {
          setVerifyBusy(false);
        }
      }

  useEffect(() => {
    if (!currentUser) return;

    Promise.all([
        fetch(`http://localhost:8080/api/users/${currentUser.uid}`).then(r => r.json()),
        fetch('http://localhost:8080/api/locations').then(r => r.json()),
        fetch('http://localhost:8080/api/languages').then(r => r.json())
      ])
    .then(([userData, locs,langs]) => {
      setFullName(userData.fullName);
      setPhone(userData.phone);
      setAddress(userData.address || '');

      setLocId(
          locs.find(l => `${l.name}, ${l.country}` === userData.address)?.id || ''
          );

      setLocations(locs);
      setLanguages(langs);

      if (userRole === 'therapist') {
        fetch(`http://localhost:8080/api/therapists/${currentUser.uid}`)
          .then(r => r.json())
          .then(th => {
            setSelectedLocation(th.location?.id || '');
            setLocId(th.location?.id || '');
            setSpecialization(th.specialization || '');
            setBio(th.bio || '');
            setSessionCost(th.sessionCost || 0);
            setPreviewUrl(th.profilePicture || '');
            setLanguageIds(th.languages?.map(l => l.id) || []);
            //setIsVerified(Boolean(th.isVerified));
            setVerificationState(th.verificationState);
            setLicenceDocUrl(th.licenceDocument || '');
          });
      }
    })
    .catch(console.error)
    .finally(() => setLoading(false));
  }, [currentUser, userRole]);

  if (!currentUser) return <p>Please log in to view your profile.</p>;
  if (loading)      return <p>Loading...</p>;

  const handleLocationChange = e => {
      const newId = e.target.value;
        setLocId(newId);
        const loc = locations.find(l => l.id === parseInt(newId, 10));
         if (loc) {
          setAddress(`${loc.name}, ${loc.country}`);
        }
       };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      // updating user
      const userPayload = { fullName, phone, address };
      const res1 = await fetch(
        `http://localhost:8080/api/users/${currentUser.uid}`,
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

      // if therapist, uploading photo then update therapist
      let picUrl = previewUrl;
      if (userRole === 'therapist' && file) {
        const fd = new FormData();
        fd.append('file', file);
        const uploadRes = await fetch(
          `http://localhost:8080/api/therapists/${currentUser.uid}/uploadPhoto`,
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
        const thPayload = {
          specialization,
          bio,
          sessionCost,
          locationId: locId,
          profilePicture: picUrl,
          languageIds
        };
        const res2 = await fetch(
          `http://localhost:8080/api/therapists/${currentUser.uid}`,
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



      alert('Profile updated successfully!');
    } catch (err) {
      console.error('Error in handleSubmit:', err);
      alert(`Update failed: ${err.message}`);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ padding: '1rem' }}>
      <h2>Edit Your Profile</h2>

      {/* Full Name */}
      <label>
        Full Name<br/>
        <input
          type="text"
          value={fullName}
          onChange={e => setFullName(e.target.value)}
        />
      </label>
      <br/><br/>

      {/* Email */}
      <label>
        Email<br/>
        <input type="email" value={currentUser.email} readOnly />
      </label>
      <br/><br/>

      {/* Phone */}
      <label>
        Phone<br/>
        <input
          type="text"
          value={phone}
          onChange={e => setPhone(e.target.value)}
        />
      </label>
      <br/><br/>

   {/* location for everyone */}
         <label>
           Location<br/>
           <select value={locId} onChange={handleLocationChange}>
             <option value="">-- Select --</option>
             {locations.map(loc => (
               <option key={loc.id} value={loc.id}>
                 {loc.name}, {loc.country}
               </option>
             ))}
           </select>
         </label><br/><br/>

         {userRole === 'therapist' && (
           <>

             {/* therapist specific fields */}
                        <label>
                          Specialization<br />
                          <input
                            type="text"
                            value={specialization}
                            onChange={e => setSpecialization(e.target.value)}
                          />
                        </label>
                        <br />
                        <br />

                        <label>
                          Bio<br />
                          <textarea
                            rows={4}
                            value={bio}
                            onChange={e => setBio(e.target.value)}
                          />
                        </label>
                        <br />
                        <br />

                        <label>
                          Languages (Ctrl‑click for multiple)<br />
                          <select
                            multiple
                            value={languageIds}
                            onChange={e =>
                              setLanguageIds(
                                Array.from(e.target.selectedOptions, o => parseInt(o.value, 10))
                              )
                            }
                            style={{ height: 90 }}
                          >
                            {languages.map(lang => (
                              <option key={lang.id} value={lang.id}>
                                {lang.langName}
                              </option>
                            ))}
                          </select>
                        </label>
                        <br />
                        <br />

                        <label>
                          Session Cost (USD)<br />
                          <input
                            type="number"
                            step="10.0"
                            value={sessionCost}
                            onChange={e => setSessionCost(parseFloat(e.target.value))}
                          />
                        </label>
                        <br />
                        <br />

                        <label>
                          Profile Picture<br />
                          {previewUrl && (
                            <img
                              src={previewUrl}
                              alt="preview"
                              width={80}
                              style={{ display: 'block', marginBottom: 8 }}
                            />
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
                        </label>
                        <br />
                        <br />
             {/* status badge */}
             <p
               style={{
                 padding: '6px 10px',
                 borderRadius: 4,
                 fontWeight: 600,
                 background:
                   verificationState === 'VERIFIED'
                     ? '#e0ffe8'
                     : verificationState === 'PENDING'
                     ? '#fff6d9'
                     : '#ffe8e8',
                 color:
                   verificationState === 'VERIFIED'
                     ? 'green'
                     : verificationState === 'PENDING'
                     ? '#b36b00'
                     : 'crimson'
               }}
             >
               {verificationState === 'VERIFIED'
                 ? '✔ Profile verified'
                 : verificationState === 'PENDING'
                 ? '⏳ Verification request pending'
                 : '⚠️Profile not verified'}
             </p>

             {/* if not verified then show only */}
             {verificationState === 'UNVERIFIED'  && (
               <>
                 <hr />
                 <p style={{ color: 'crimson' }}>
                   Your profile is not verified yet. Clients won’t see you in search
                   results until an admin approves it.
                 </p>

                 <label>
                   Upload Licence (PDF/ image)<br />
                   <input
                     type="file"
                     accept=".pdf,image/*"
                     onChange={e => setLicenceFile(e.target.files[0])}
                   />
                 </label>
                 <br />

                 {/* licence document link */}
                 {licenceDocUrl &&
                   (licenceDocUrl.toLowerCase().endsWith('.pdf') ? (
                     <p style={{ marginTop: 6 }}>
                       Current file:&nbsp;
                       <a href={licenceDocUrl} target="_blank" rel="noreferrer">
                         uploaded PDF
                       </a>
                     </p>
                   ) : (
                     <img
                       src={licenceDocUrl}
                       alt="Licence"
                       width={120}
                       style={{ display: 'block', marginTop: 8 }}
                     />
                   ))}

                 <button
                   type="button"
                   disabled={verifyBusy}
                   onClick={requestVerification}
                   style={{ marginTop: 12 }}
                 >
                   {verifyBusy ? 'Sending…' : 'Verify my profile'}
                 </button>
                 <br />
                 <br />
               </>
             )}


           </>
         )}


      <button type="submit">Save Changes</button>
    </form>
  );
}
