import React, { useState } from 'react';
import { auth } from '../firebaseConfig';
import { createUserWithEmailAndPassword } from 'firebase/auth';


function Register() {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');

  const handleRegister = async (e) =>
  {
  try {
        const userInfos = await createUserWithEmailAndPassword(auth, email, password);
        const token = await userInfos.user.getIdToken();

          const response = await fetch('http://localhost:8080/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                  idToken: token,
                  phoneNumber: phone,
                  address: address,
                }),
              });

               if (response.ok) {
                      alert('Registration successful!');
                    } else {
                      const errorMsg = await response.text();
                      alert('Error: ' + errorMsg);
                    }
                  } catch (error) {
                    console.error('Registration error is:', error);
                    alert('Registration failed: ' + error.message);
                  }
                };

   return (
    <div>
      <h2></h2>
      <form onSubmit={handleRegister}>
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
          onChange={(e) => setPassword(e.target.value)}
        /><br/>
        <input
          type="text"
          placeholder="Phone Number"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
        /><br/>
        <input
          type="text"
          placeholder="Address"
          value={address}
          onChange={(e) => setAddress(e.target.value)}
        /><br/>
        <button type="submit">Sign Up</button>
      </form>
    </div>
  );
}

export default Register;