import React, { createContext, useContext, useState, useEffect } from 'react';
import { auth } from '../firebaseConfig';
import { onAuthStateChanged } from "firebase/auth";
import { signOut } from 'firebase/auth';
import config from '../config';

const UserContext = createContext();



export function useAuth() {
  return useContext(UserContext);
}

//used automatically firebase method for log out
function logOut() {
    return signOut(auth);
}


export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      setCurrentUser(user);

      if (user) {
        try {

          const res = await fetch(`${config.API_URL}/api/users/uid/${user.uid}`);

          if (res.status === 404) {

            await fetch(`${config.API_URL}/api/users/register`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                uid: user.uid,
                name: user.displayName || "Unnamed User",
                email: user.email,
                phone: user.phoneNumber || "",
                role: "client"
              })
            });

            // Fetching again after registering
            const retryRes = await fetch(`${config.API_URL}/api/users/uid/${user.uid}`);
            const userData = await retryRes.json();
            setUserRole(userData.role);
          } else {
            const userData = await res.json();
            setUserRole(userData.role);
          }

        } catch (err) {
          console.error("Error during backend fetch/register:", err);
        } finally {
          setLoading(false);
        }
      } else {
        setUserRole(null);
        setLoading(false);
      }
    });

    return unsubscribe;
  }, []);


  const value = {
    currentUser,
    userRole,
    logOut,
  };

  return (
    <UserContext.Provider value={value}>
      {}
      {!loading && children}
    </UserContext.Provider>
  );
}