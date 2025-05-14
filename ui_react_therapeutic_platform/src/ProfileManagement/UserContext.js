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
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setCurrentUser(user);
      if (user) {
        fetch(`${config.API_URL}/api/users/${user.uid}`)
          .then(res => res.json())
          .then(data => {
            setUserRole(data.role);
            setLoading(false);
          })
          .catch(err => {
            console.error("Error fetching user data:", err);
            setLoading(false);
          });
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