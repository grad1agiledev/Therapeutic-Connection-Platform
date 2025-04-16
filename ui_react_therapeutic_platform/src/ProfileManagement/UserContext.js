import React, { createContext, useContext, useState, useEffect } from 'react';
import { auth } from '../firebaseConfig';
import { onAuthStateChanged } from "firebase/auth";

const UserContext = createContext();

export function useAuth() {
  return useContext(UserContext);
}

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setCurrentUser(user);
      if (user) {

        fetch(`http://localhost:8080/api/users/${user.uid}`)
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
  };

  return (
    <UserContext.Provider value={value}>
      {}
      {!loading && children}
    </UserContext.Provider>
  );
}