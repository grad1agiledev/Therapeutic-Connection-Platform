import React, { useEffect,useState } from 'react';
import { auth,db } from '../firebaseConfig';
import { createUserWithEmailAndPassword } from 'firebase/auth';
import { doc, setDoc } from 'firebase/firestore';
import { useNavigate } from 'react-router-dom';

function Register() {

  const navigate = useNavigate();
  const [name,setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');

  const [locations, setLocations] = useState([]);
  const [selectedLocation, setSelectedLocation] = useState('');
  const [role, setRole] = useState('patient');

  useEffect(() => {
    fetch('http://localhost:8080/api/locations')
      .then(res => res.json())
      .then(data => {
        setLocations(data);
        if(data.length > 0) {
          setSelectedLocation(data[0].id);
        }
      })
      .catch(error => console.error("Error fetching locations:", error));
  }, []);

  const handleRegister = async (e) =>
  {
   e.preventDefault();
   
   if (password.length < 8) {
    alert('Password must be at least 8 characters long');
    return;
   }
   
   if (!/[A-Z]/.test(password)) {
    alert('Password must contain at least one uppercase letter');
    return;
   }
   
   if (!/[a-z]/.test(password)) {
    alert('Password must contain at least one lowercase letter');
    return;
   }
   
   if (!/[0-9]/.test(password)) {
    alert('Password must contain at least one number');
    return;
   }
   
   if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
    alert('Password must contain at least one special character');
    return;
   }
   
   try {
        const userInfos = await createUserWithEmailAndPassword(auth, email, password);
        const token = await userInfos.user.getIdToken();
        const user = userInfos.user;
        const uid = userInfos.user.uid;

        await fetch('http://localhost:8080/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                  uid :uid,
                  name: name,
                  phone: phone,
                  locationId: selectedLocation,
                  role: role,
                  email:email,
                }),
              });

              alert('Registration is successful!');

               if (role === "therapist") {
                      navigate('/profile');
                    } else {
                      navigate('/profile');
                    }
         } catch (error) {
                    console.error('Registration error is: ', error);
                    alert('Registration failed bc: ' + error.message);
                  }
                };
//
//               if (response.ok) {
//                      alert('Registration successful!');
//                    } else {
//                      const errorMsg = await response.text();
//                      alert('Error: ' + errorMsg);
//                    }
//                  } catch (error) {
//                    console.error('Registration error is:', error);
//                    alert('Registration failed: ' + error.message);
                 // }
          //    };

   return (
    <div>
      <h2>Register</h2>
      <form onSubmit={handleRegister}>
       <input
                type="text"
                placeholder="Full Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
              /><br/>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        /><br/>
        <input
          type="password"
          placeholder="Password"
          value={password}
          minLength={8}
          onChange={(e) => setPassword(e.target.value)}
        /><br/>
        <input
          type="text"
          placeholder="Phone Number"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
        /><br/>
         {/* <input
                  type="text"
                  placeholder="Address"
                  value={address}
                  onChange={(e) => setAddress(e.target.value)}
                /><br/> */}


        <label htmlFor="locationSelect">Location:</label>
         <select
           id="locationSelect"
           value={selectedLocation}
           onChange={(e) => setSelectedLocation(e.target.value)}
         >
           {locations.map(loc => (
             <option key={loc.id} value={loc.id}>
               {loc.name}, {loc.country}
             </option>
           ))}
         </select>
         <br/>

        <label>
                  <input type="radio" value="patient" checked={role === "patient"} onChange={(e) => setRole(e.target.value)} />
                  Patient
                </label>
                <label>
                  <input type="radio" value="therapist" checked={role === "therapist"} onChange={(e) => setRole(e.target.value)} />
                  Therapist
                </label>
                <br/>


        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
}

export default Register;