import React, { useState } from 'react';
import { auth,db } from '../firebaseConfig';
import { createUserWithEmailAndPassword } from 'firebase/auth';
import { doc, setDoc } from 'firebase/firestore';

function Register() {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  const [role, setRole] = useState('patient');

  const handleRegister = async (e) =>
  {
   e.preventDefault();
  try {
        const userInfos = await createUserWithEmailAndPassword(auth, email, password);
        const token = await userInfos.user.getIdToken();
        const user = userInfos.user;
        const uid = user.uid;

//
//        await setDoc(doc(db,"Users",uid),
//        {
//        email: email,
//        phone : phone,
//        address : address,
//        role: role,
//        createdAt : new Date()
//        });
//
//        alert('Registration is successful!');
//         }
//  catch (error) {
//              console.error('Registration error is:', error);
//              alert('Registration failed bc: ' + error.message);
//            }
//          };

        await fetch('http://localhost:8080/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                  uid :uid,
                  phone: phone,
                  address: address,
                  role: role,
                }),
              });

              alert('Registration is successful!');
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